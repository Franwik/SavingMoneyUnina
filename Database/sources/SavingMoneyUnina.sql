--
-- PostgreSQL database dump
--

-- Dumped from database version 16.2 (Postgres.app)
-- Dumped by pg_dump version 16.2 (Postgres.app)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: smu; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA smu;


ALTER SCHEMA smu OWNER TO postgres;

--
-- Name: check_card_owner(); Type: FUNCTION; Schema: smu; Owner: postgres
--

CREATE FUNCTION smu.check_card_owner() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
    BA_used smu.bankaccount%ROWTYPE;
    familiar_email smu.familiar.familiaremail%TYPE;
BEGIN

    familiar_email := NULL;

    -- Recupera le informazioni relative al conto corrente
    -- al quale si sta associando la carta
    SELECT *
    INTO BA_used
    FROM smu.bankaccount
    WHERE accountnumber = NEW.ba_number;

    -- Recupera l'email dell'account al quale è associato
    -- il familiare, proprietario della carta
    IF NEW.ownercf IS NOT NULL THEN
        SELECT familiaremail
        INTO familiar_email
        FROM smu.familiar
        WHERE cf = NEW.ownercf;
    END IF;

    -- Se tutte le condizioni sono vere, viene inserita la carta, altrimenti viene restituita una exception
    IF (BA_used.owneremail = NEW.owneremail OR BA_used.ownercf = NEW.ownercf) OR
    (familiar_email = BA_used.owneremail) OR BA_used.ownercf IN (SELECT CF FROM smu.familiar WHERE familiaremail = NEW.owneremail)
    THEN
        -- L'IF è stato strutturato in questo modo perché non basta negare le condizioni per avere
        -- solo un IF THEN. Questo perché nel caso in cui alcuni attributi sono NULL il sistema
        -- non è in grado di fornire una valutazione sulla condizione, quindi gli AND da sostituire
        -- agli attuali OR sarebbero sempre falsi.
    ELSE
        RAISE EXCEPTION 'Il proprietario della carta deve essere anche il proprietario del conto corrente, o al massimo un suo familiare.';
    END IF;

    -- Nell'ultima porzione della condizione dell'IF viene controllato se il codice fiscale
    -- del proprietario del conto corrente è presente nell'elenco dei familiari associati
    -- all'email del proprietario della carta

    RETURN NEW;
END;
$$;


ALTER FUNCTION smu.check_card_owner() OWNER TO postgres;

--
-- Name: connect_transaction_to_wallet(); Type: FUNCTION; Schema: smu; Owner: postgres
--

CREATE FUNCTION smu.connect_transaction_to_wallet() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
    wallet_row smu.wallet%ROWTYPE;
    card_row smu.card%ROWTYPE;
    old_card_row smu.card%ROWTYPE;
    account_email smu.user.email%TYPE;
    ba_row smu.bankaccount%ROWTYPE;
    old_ba_row smu.bankaccount%ROWTYPE;
BEGIN

    -- Seleziono la carta con la quale è stata effettuata la transazione
    SELECT *
    INTO card_row
    FROM smu.card
    WHERE cardnumber = NEW.cardnumber;

    -- Controlla se la carta è scaduta o meno al momento della transazione
    IF smu.expired_card(card_row.expiredata, NEW.date) THEN
        RAISE EXCEPTION 'La carta risultava scaduta al momento della transazione';
    END IF;

    -- Recupero il conto corrente al quale è associato la carta
    SELECT *
    INTO ba_row
    FROM smu.bankaccount
    WHERE accountnumber = card_row.ba_number;
    
    -- Controllo se la transazione può essere effettuata o meno
    IF TG_OP = 'INSERT' AND ba_row.balance < NEW.amount THEN
        RAISE EXCEPTION 'Saldo sul conto corrente insufficiente';
    ELSIF TG_OP = 'UPDATE' AND (ba_row.balance + OLD.amount) < NEW.amount THEN
        RAISE EXCEPTION 'Saldo sul conto corrente insufficiente al momento della transazione';
    END IF;

    -- Recupero l'email dell'account al quale saranno associati i portafogli
    IF card_row.owneremail IS NOT NULL THEN
        -- Se la carta appartiene ad un utente, mi salvo l'email nella variabile account_email
        account_email := card_row.owneremail;
    ELSE
        -- Altrimenti, appartiene sicuramente ad un familiare e vado a
        -- recuperare l'email dell'utente al quale è associato
        SELECT familiaremail
        INTO account_email
        FROM smu.familiar
        WHERE cf = card_row.ownercf;
    END IF;

    IF TG_OP = 'INSERT' THEN
        -- Trova i wallet con la stessa categoria della transazione
        -- appena inserita che appartengono all'utente corretto
		IF NEW.walletName IS NULL THEN
			FOR wallet_row IN
				SELECT *
				FROM smu.wallet
				WHERE walletcategory = NEW.category AND owneremail = account_email
			LOOP

				-- Collega la transazione al wallet trovato
				INSERT INTO smu.transactioninwallet (id_transaction, id_wallet)
				VALUES (NEW.id_transaction, wallet_row.id_wallet);

				-- Aggiorna il campo totalamount del wallet
				UPDATE smu.wallet
				SET totalamount = totalamount + NEW.amount
				WHERE id_wallet = wallet_row.id_wallet;

			END LOOP;

			-- Aggiorna il saldo del conto corrente
			UPDATE smu.bankaccount
			SET balance = balance - NEW.amount
			WHERE accountnumber = ba_row.accountnumber;
		ELSE
			FOR wallet_row IN
				SELECT *
				FROM smu.wallet
				WHERE walletcategory = NEW.category AND owneremail = account_email AND walletName = NEW.walletName
			LOOP

				-- Collega la transazione al wallet trovato
				INSERT INTO smu.transactioninwallet (id_transaction, id_wallet)
				VALUES (NEW.id_transaction, wallet_row.id_wallet);

				-- Aggiorna il campo totalamount del wallet
				UPDATE smu.wallet
				SET totalamount = totalamount + NEW.amount
				WHERE id_wallet = wallet_row.id_wallet;

			END LOOP;

			-- Aggiorna il saldo del conto corrente
			UPDATE smu.bankaccount
			SET balance = balance - NEW.amount
			WHERE accountnumber = ba_row.accountnumber;
		END IF;
    END IF;

    IF TG_OP = 'UPDATE' THEN

        -- Se è stata modificata la categoria, la transazione viene collegata ai nuovi portafogli
        -- altrimenti viene ricollegata agli stessi

        -- Scollego la transazione da tutti i portafogli
        DELETE FROM smu.transactioninwallet WHERE id_transaction = OLD.id_transaction;
        
        -- Seleziona tutti i portafogli a cui era collegata la transazione
        FOR wallet_row IN
            SELECT *
            FROM smu.wallet
            WHERE walletcategory = OLD.category AND owneremail = account_email
        LOOP

            -- Aggiorna il campo totalamount del wallet
            UPDATE smu.wallet
            SET totalamount = totalamount - OLD.amount
            WHERE id_wallet = wallet_row.id_wallet;

        END LOOP;

        -- Seleziona tutti i portafogli a cui deve essere collegata la transazione
        FOR wallet_row IN
            SELECT *
            FROM smu.wallet
            WHERE walletcategory = NEW.category AND owneremail = account_email
        LOOP

            -- Collega la transazione al wallet trovato
            INSERT INTO smu.transactioninwallet (id_transaction, id_wallet)
            VALUES (NEW.id_transaction, wallet_row.id_wallet);

            -- Aggiorna il campo totalamount del wallet
            UPDATE smu.wallet
            SET totalamount = totalamount + NEW.amount
            WHERE id_wallet = wallet_row.id_wallet;

        END LOOP;        

        -- Seleziono la carta con la quale è stata effettuata inizialmente la transazione
        SELECT *
        INTO old_card_row
        FROM smu.card
        WHERE cardnumber = OLD.cardnumber;

        -- Recupero il conto corrente al quale è associato la vecchia carta
        SELECT *
        INTO old_ba_row
        FROM smu.bankaccount
        WHERE accountnumber = old_card_row.ba_number;

        -- Aggiorno il saldo del vecchio conto corrente
        UPDATE smu.bankaccount
        SET balance = balance + OLD.amount
        WHERE accountnumber = old_ba_row.accountnumber;

        -- Aggiorno il saldo del nuovo conto corrente
        UPDATE smu.bankaccount
        SET balance = balance - NEW.amount
        WHERE accountnumber = ba_row.accountnumber;

    END IF;

    RETURN NEW;
END;
$$;


ALTER FUNCTION smu.connect_transaction_to_wallet() OWNER TO postgres;

--
-- Name: expired_card(date, date); Type: FUNCTION; Schema: smu; Owner: postgres
--

CREATE FUNCTION smu.expired_card(card_expire_date date, transaction_date date) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
BEGIN
    -- Questa funzione controlla se la carta data in input era scaduta
    -- al momento della transazione data in input. La funzione viene usata
    -- all'interno della funzione "connect_transaction_to_wallet" la quale viene
    -- eseguita ad ogni inserimento di una nuova transazione.
    -- Questa funzione viene utilizzata per garantire il vincolo "check_expire_date".
    IF card_expire_date < transaction_date THEN
        RETURN TRUE;
    ELSE
        RETURN FALSE;
    END IF;
END;
$$;


ALTER FUNCTION smu.expired_card(card_expire_date date, transaction_date date) OWNER TO postgres;

--
-- Name: update_wallet_category(); Type: FUNCTION; Schema: smu; Owner: postgres
--

CREATE FUNCTION smu.update_wallet_category() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
    transaction_row smu.transaction%ROWTYPE;
BEGIN

    -- Vengono scollegate tutte le transazioni dal wallet
    DELETE FROM smu.transactioninwallet WHERE id_wallet = OLD.id_wallet;

    -- Reimposta a 0 la somma degli importi del portafoglio
    UPDATE smu.wallet
    SET totalamount = 0
    WHERE id_wallet = NEW.id_wallet;

    FOR transaction_row IN
        SELECT *
        FROM smu.transaction AS T
        WHERE T.category = NEW.walletcategory AND T.cardiban IN (SELECT iban FROM smu.card WHERE owneremail = NEW.owneremail
                                                            UNION
                                                            SELECT C.iban
                                                            FROM smu.familiar AS F
                                                            JOIN smu.card AS C ON F.cf = C.ownercf
                                                            WHERE familiaremail = NEW.owneremail)
    LOOP

        -- Collega la transazione trovata
        INSERT INTO smu.transactioninwallet (id_transaction, id_wallet)
        VALUES (transaction_row.id_transaction, NEW.id_wallet);

        -- Aggiorna il campo totalamount del wallet
        UPDATE smu.wallet
        SET totalamount = totalamount + transaction_row.amount
        WHERE id_wallet = NEW.id_wallet;

    END LOOP;

    RETURN NEW;
    
END;
$$;


ALTER FUNCTION smu.update_wallet_category() OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: bankaccount; Type: TABLE; Schema: smu; Owner: postgres
--

CREATE TABLE smu.bankaccount (
    balance integer NOT NULL,
    accountnumber integer NOT NULL,
    bank character varying(40),
    ownercf character varying(16),
    owneremail character varying(100),
    CONSTRAINT ownership_check_ba CHECK (((ownercf IS NULL) <> (owneremail IS NULL)))
);


ALTER TABLE smu.bankaccount OWNER TO postgres;

--
-- Name: bankaccount_accountnumber_seq; Type: SEQUENCE; Schema: smu; Owner: postgres
--

CREATE SEQUENCE smu.bankaccount_accountnumber_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE smu.bankaccount_accountnumber_seq OWNER TO postgres;

--
-- Name: bankaccount_accountnumber_seq; Type: SEQUENCE OWNED BY; Schema: smu; Owner: postgres
--

ALTER SEQUENCE smu.bankaccount_accountnumber_seq OWNED BY smu.bankaccount.accountnumber;


--
-- Name: card; Type: TABLE; Schema: smu; Owner: postgres
--

CREATE TABLE smu.card (
    cardnumber character varying(16) NOT NULL,
    iban character varying(27) NOT NULL,
    cvv character varying(3) NOT NULL,
    expiredata date NOT NULL,
    cardtype character varying(11),
    ba_number integer NOT NULL,
    ownercf character varying(16),
    owneremail character varying(100),
    CONSTRAINT cardtype_check CHECK (((cardtype)::text = ANY (ARRAY[('Prepagata'::character varying)::text, ('Debito'::character varying)::text, ('Credito'::character varying)::text]))),
    CONSTRAINT ownership_check_card CHECK (((ownercf IS NULL) <> (owneremail IS NULL)))
);


ALTER TABLE smu.card OWNER TO postgres;

--
-- Name: card_ba_number_seq; Type: SEQUENCE; Schema: smu; Owner: postgres
--

CREATE SEQUENCE smu.card_ba_number_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE smu.card_ba_number_seq OWNER TO postgres;

--
-- Name: card_ba_number_seq; Type: SEQUENCE OWNED BY; Schema: smu; Owner: postgres
--

ALTER SEQUENCE smu.card_ba_number_seq OWNED BY smu.card.ba_number;


--
-- Name: familiar; Type: TABLE; Schema: smu; Owner: postgres
--

CREATE TABLE smu.familiar (
    name character varying(20) NOT NULL,
    surname character varying(30) NOT NULL,
    cf character varying(16) NOT NULL,
    dateofbirth date NOT NULL,
    familiaremail character varying(100) NOT NULL,
    CONSTRAINT check_birthdate_familiar CHECK ((dateofbirth < CURRENT_DATE))
);


ALTER TABLE smu.familiar OWNER TO postgres;

--
-- Name: transaction; Type: TABLE; Schema: smu; Owner: postgres
--

CREATE TABLE smu.transaction (
    id_transaction integer NOT NULL,
    amount double precision NOT NULL,
    date date NOT NULL,
    category character varying(35),
    cardnumber character varying(16) NOT NULL,
    walletname character varying(35),
    CONSTRAINT check_transaction_date CHECK ((date <= CURRENT_DATE))
);


ALTER TABLE smu.transaction OWNER TO postgres;

--
-- Name: transaction_id_transaction_seq; Type: SEQUENCE; Schema: smu; Owner: postgres
--

CREATE SEQUENCE smu.transaction_id_transaction_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE smu.transaction_id_transaction_seq OWNER TO postgres;

--
-- Name: transaction_id_transaction_seq; Type: SEQUENCE OWNED BY; Schema: smu; Owner: postgres
--

ALTER SEQUENCE smu.transaction_id_transaction_seq OWNED BY smu.transaction.id_transaction;


--
-- Name: transactioninwallet; Type: TABLE; Schema: smu; Owner: postgres
--

CREATE TABLE smu.transactioninwallet (
    id_transaction integer NOT NULL,
    id_wallet integer NOT NULL
);


ALTER TABLE smu.transactioninwallet OWNER TO postgres;

--
-- Name: transactionwallet_id_transaction_seq; Type: SEQUENCE; Schema: smu; Owner: postgres
--

CREATE SEQUENCE smu.transactionwallet_id_transaction_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE smu.transactionwallet_id_transaction_seq OWNER TO postgres;

--
-- Name: transactionwallet_id_transaction_seq; Type: SEQUENCE OWNED BY; Schema: smu; Owner: postgres
--

ALTER SEQUENCE smu.transactionwallet_id_transaction_seq OWNED BY smu.transactioninwallet.id_transaction;


--
-- Name: transactionwallet_id_wallet_seq; Type: SEQUENCE; Schema: smu; Owner: postgres
--

CREATE SEQUENCE smu.transactionwallet_id_wallet_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE smu.transactionwallet_id_wallet_seq OWNER TO postgres;

--
-- Name: transactionwallet_id_wallet_seq; Type: SEQUENCE OWNED BY; Schema: smu; Owner: postgres
--

ALTER SEQUENCE smu.transactionwallet_id_wallet_seq OWNED BY smu.transactioninwallet.id_wallet;


--
-- Name: user; Type: TABLE; Schema: smu; Owner: postgres
--

CREATE TABLE smu."user" (
    email character varying(100) NOT NULL,
    username character varying(20) NOT NULL,
    password character varying(30) NOT NULL,
    address character varying(100),
    name character varying(20) NOT NULL,
    surname character varying(30) NOT NULL,
    cf character varying(16) NOT NULL,
    dateofbirth date NOT NULL,
    CONSTRAINT check_birthdate_user CHECK ((dateofbirth < CURRENT_DATE))
);


ALTER TABLE smu."user" OWNER TO postgres;

--
-- Name: wallet; Type: TABLE; Schema: smu; Owner: postgres
--

CREATE TABLE smu.wallet (
    id_wallet integer NOT NULL,
    walletname character varying(35) NOT NULL,
    walletcategory character varying(35) NOT NULL,
    totalamount double precision NOT NULL,
    owneremail character varying(100) NOT NULL
);


ALTER TABLE smu.wallet OWNER TO postgres;

--
-- Name: wallet_id_wallet_seq; Type: SEQUENCE; Schema: smu; Owner: postgres
--

CREATE SEQUENCE smu.wallet_id_wallet_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE smu.wallet_id_wallet_seq OWNER TO postgres;

--
-- Name: wallet_id_wallet_seq; Type: SEQUENCE OWNED BY; Schema: smu; Owner: postgres
--

ALTER SEQUENCE smu.wallet_id_wallet_seq OWNED BY smu.wallet.id_wallet;


--
-- Name: bankaccount accountnumber; Type: DEFAULT; Schema: smu; Owner: postgres
--

ALTER TABLE ONLY smu.bankaccount ALTER COLUMN accountnumber SET DEFAULT nextval('smu.bankaccount_accountnumber_seq'::regclass);


--
-- Name: card ba_number; Type: DEFAULT; Schema: smu; Owner: postgres
--

ALTER TABLE ONLY smu.card ALTER COLUMN ba_number SET DEFAULT nextval('smu.card_ba_number_seq'::regclass);


--
-- Name: transaction id_transaction; Type: DEFAULT; Schema: smu; Owner: postgres
--

ALTER TABLE ONLY smu.transaction ALTER COLUMN id_transaction SET DEFAULT nextval('smu.transaction_id_transaction_seq'::regclass);


--
-- Name: transactioninwallet id_transaction; Type: DEFAULT; Schema: smu; Owner: postgres
--

ALTER TABLE ONLY smu.transactioninwallet ALTER COLUMN id_transaction SET DEFAULT nextval('smu.transactionwallet_id_transaction_seq'::regclass);


--
-- Name: transactioninwallet id_wallet; Type: DEFAULT; Schema: smu; Owner: postgres
--

ALTER TABLE ONLY smu.transactioninwallet ALTER COLUMN id_wallet SET DEFAULT nextval('smu.transactionwallet_id_wallet_seq'::regclass);


--
-- Name: wallet id_wallet; Type: DEFAULT; Schema: smu; Owner: postgres
--

ALTER TABLE ONLY smu.wallet ALTER COLUMN id_wallet SET DEFAULT nextval('smu.wallet_id_wallet_seq'::regclass);


--
-- Data for Name: bankaccount; Type: TABLE DATA; Schema: smu; Owner: postgres
--

COPY smu.bankaccount (balance, accountnumber, bank, ownercf, owneremail) FROM stdin;
99987	2	Poste Italiane	\N	franwik_@outlook.com
\.


--
-- Data for Name: card; Type: TABLE DATA; Schema: smu; Owner: postgres
--

COPY smu.card (cardnumber, iban, cvv, expiredata, cardtype, ba_number, ownercf, owneremail) FROM stdin;
1234567890123456	PI0123456789012345678901234	123	2025-10-13	Prepagata	2	\N	franwik_@outlook.com
\.


--
-- Data for Name: familiar; Type: TABLE DATA; Schema: smu; Owner: postgres
--

COPY smu.familiar (name, surname, cf, dateofbirth, familiaremail) FROM stdin;
\.


--
-- Data for Name: transaction; Type: TABLE DATA; Schema: smu; Owner: postgres
--

COPY smu.transaction (id_transaction, amount, date, category, cardnumber, walletname) FROM stdin;
30	13	2024-03-11	\N	1234567890123456	\N
\.


--
-- Data for Name: transactioninwallet; Type: TABLE DATA; Schema: smu; Owner: postgres
--

COPY smu.transactioninwallet (id_transaction, id_wallet) FROM stdin;
\.


--
-- Data for Name: user; Type: TABLE DATA; Schema: smu; Owner: postgres
--

COPY smu."user" (email, username, password, address, name, surname, cf, dateofbirth) FROM stdin;
franwik_@outlook.com	Franwik	Ifs4ppic	Via Napoli 281	Francesco	Donnarumma	DNNFNC03A22C129A	2003-01-22
\.


--
-- Data for Name: wallet; Type: TABLE DATA; Schema: smu; Owner: postgres
--

COPY smu.wallet (id_wallet, walletname, walletcategory, totalamount, owneremail) FROM stdin;
\.


--
-- Name: bankaccount_accountnumber_seq; Type: SEQUENCE SET; Schema: smu; Owner: postgres
--

SELECT pg_catalog.setval('smu.bankaccount_accountnumber_seq', 2, true);


--
-- Name: card_ba_number_seq; Type: SEQUENCE SET; Schema: smu; Owner: postgres
--

SELECT pg_catalog.setval('smu.card_ba_number_seq', 1, false);


--
-- Name: transaction_id_transaction_seq; Type: SEQUENCE SET; Schema: smu; Owner: postgres
--

SELECT pg_catalog.setval('smu.transaction_id_transaction_seq', 30, true);


--
-- Name: transactionwallet_id_transaction_seq; Type: SEQUENCE SET; Schema: smu; Owner: postgres
--

SELECT pg_catalog.setval('smu.transactionwallet_id_transaction_seq', 1, false);


--
-- Name: transactionwallet_id_wallet_seq; Type: SEQUENCE SET; Schema: smu; Owner: postgres
--

SELECT pg_catalog.setval('smu.transactionwallet_id_wallet_seq', 1, false);


--
-- Name: wallet_id_wallet_seq; Type: SEQUENCE SET; Schema: smu; Owner: postgres
--

SELECT pg_catalog.setval('smu.wallet_id_wallet_seq', 5, true);


--
-- Name: bankaccount pk_bankaccount; Type: CONSTRAINT; Schema: smu; Owner: postgres
--

ALTER TABLE ONLY smu.bankaccount
    ADD CONSTRAINT pk_bankaccount PRIMARY KEY (accountnumber);


--
-- Name: card pk_card; Type: CONSTRAINT; Schema: smu; Owner: postgres
--

ALTER TABLE ONLY smu.card
    ADD CONSTRAINT pk_card PRIMARY KEY (cardnumber);


--
-- Name: user pk_email; Type: CONSTRAINT; Schema: smu; Owner: postgres
--

ALTER TABLE ONLY smu."user"
    ADD CONSTRAINT pk_email PRIMARY KEY (email);


--
-- Name: familiar pk_familiar; Type: CONSTRAINT; Schema: smu; Owner: postgres
--

ALTER TABLE ONLY smu.familiar
    ADD CONSTRAINT pk_familiar PRIMARY KEY (cf);


--
-- Name: transaction pk_transaction; Type: CONSTRAINT; Schema: smu; Owner: postgres
--

ALTER TABLE ONLY smu.transaction
    ADD CONSTRAINT pk_transaction PRIMARY KEY (id_transaction);


--
-- Name: wallet pk_wallet; Type: CONSTRAINT; Schema: smu; Owner: postgres
--

ALTER TABLE ONLY smu.wallet
    ADD CONSTRAINT pk_wallet PRIMARY KEY (id_wallet);


--
-- Name: user unique_cf; Type: CONSTRAINT; Schema: smu; Owner: postgres
--

ALTER TABLE ONLY smu."user"
    ADD CONSTRAINT unique_cf UNIQUE (cf);


--
-- Name: card unique_cnumber; Type: CONSTRAINT; Schema: smu; Owner: postgres
--

ALTER TABLE ONLY smu.card
    ADD CONSTRAINT unique_cnumber UNIQUE (cardnumber);


--
-- Name: card unique_iban; Type: CONSTRAINT; Schema: smu; Owner: postgres
--

ALTER TABLE ONLY smu.card
    ADD CONSTRAINT unique_iban UNIQUE (iban);


--
-- Name: user unique_username; Type: CONSTRAINT; Schema: smu; Owner: postgres
--

ALTER TABLE ONLY smu."user"
    ADD CONSTRAINT unique_username UNIQUE (username);


--
-- Name: transaction connect_transaction_to_wallet_trigger; Type: TRIGGER; Schema: smu; Owner: postgres
--

CREATE TRIGGER connect_transaction_to_wallet_trigger AFTER INSERT OR UPDATE ON smu.transaction FOR EACH ROW EXECUTE FUNCTION smu.connect_transaction_to_wallet();


--
-- Name: wallet update_wallet_category_trigger; Type: TRIGGER; Schema: smu; Owner: postgres
--

CREATE TRIGGER update_wallet_category_trigger AFTER UPDATE OF walletcategory ON smu.wallet FOR EACH ROW EXECUTE FUNCTION smu.update_wallet_category();


--
-- Name: bankaccount fk_bankaccount; Type: FK CONSTRAINT; Schema: smu; Owner: postgres
--

ALTER TABLE ONLY smu.bankaccount
    ADD CONSTRAINT fk_bankaccount FOREIGN KEY (ownercf) REFERENCES smu.familiar(cf) ON DELETE CASCADE;


--
-- Name: bankaccount fk_bankaccount2; Type: FK CONSTRAINT; Schema: smu; Owner: postgres
--

ALTER TABLE ONLY smu.bankaccount
    ADD CONSTRAINT fk_bankaccount2 FOREIGN KEY (owneremail) REFERENCES smu."user"(email) ON DELETE CASCADE;


--
-- Name: card fk_card; Type: FK CONSTRAINT; Schema: smu; Owner: postgres
--

ALTER TABLE ONLY smu.card
    ADD CONSTRAINT fk_card FOREIGN KEY (ba_number) REFERENCES smu.bankaccount(accountnumber) ON DELETE CASCADE;


--
-- Name: card fk_card2; Type: FK CONSTRAINT; Schema: smu; Owner: postgres
--

ALTER TABLE ONLY smu.card
    ADD CONSTRAINT fk_card2 FOREIGN KEY (ownercf) REFERENCES smu.familiar(cf) ON DELETE CASCADE;


--
-- Name: card fk_card3; Type: FK CONSTRAINT; Schema: smu; Owner: postgres
--

ALTER TABLE ONLY smu.card
    ADD CONSTRAINT fk_card3 FOREIGN KEY (owneremail) REFERENCES smu."user"(email) ON DELETE CASCADE;


--
-- Name: familiar fk_familiar; Type: FK CONSTRAINT; Schema: smu; Owner: postgres
--

ALTER TABLE ONLY smu.familiar
    ADD CONSTRAINT fk_familiar FOREIGN KEY (familiaremail) REFERENCES smu."user"(email) ON DELETE CASCADE;


--
-- Name: transaction fk_transaction; Type: FK CONSTRAINT; Schema: smu; Owner: postgres
--

ALTER TABLE ONLY smu.transaction
    ADD CONSTRAINT fk_transaction FOREIGN KEY (cardnumber) REFERENCES smu.card(cardnumber) ON DELETE CASCADE;


--
-- Name: transactioninwallet fk_transactionwallet; Type: FK CONSTRAINT; Schema: smu; Owner: postgres
--

ALTER TABLE ONLY smu.transactioninwallet
    ADD CONSTRAINT fk_transactionwallet FOREIGN KEY (id_transaction) REFERENCES smu.transaction(id_transaction) ON DELETE CASCADE;


--
-- Name: transactioninwallet fk_transactionwallet2; Type: FK CONSTRAINT; Schema: smu; Owner: postgres
--

ALTER TABLE ONLY smu.transactioninwallet
    ADD CONSTRAINT fk_transactionwallet2 FOREIGN KEY (id_wallet) REFERENCES smu.wallet(id_wallet) ON DELETE CASCADE;


--
-- Name: wallet fk_wallet; Type: FK CONSTRAINT; Schema: smu; Owner: postgres
--

ALTER TABLE ONLY smu.wallet
    ADD CONSTRAINT fk_wallet FOREIGN KEY (owneremail) REFERENCES smu."user"(email) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

