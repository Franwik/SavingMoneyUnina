--
-- PostgreSQL database dump
--

-- Dumped from database version 16.2
-- Dumped by pg_dump version 16.2

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
    wallet_id smu.wallet.id_wallet%TYPE;
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
			SET balance = balance + NEW.amount
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
			SET balance = balance + NEW.amount
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
        SET balance = balance - OLD.amount
        WHERE accountnumber = old_ba_row.accountnumber;

        -- Aggiorno il saldo del nuovo conto corrente
        UPDATE smu.bankaccount
        SET balance = balance + NEW.amount
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
-- Name: remove_transaction(); Type: FUNCTION; Schema: smu; Owner: postgres
--

CREATE FUNCTION smu.remove_transaction() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
    wallet_id smu.wallet.id_wallet%TYPE;
    card_row smu.card%ROWTYPE;
    ba_row smu.bankaccount%ROWTYPE;
BEGIN

    -- Recupero il conto corrente associato alla transazione cancellata
    SELECT *
    INTO card_row
    FROM smu.card
    WHERE cardnumber = OLD.cardnumber;

    SELECT *
    INTO ba_row
    FROM smu.bankaccount
    WHERE accountnumber = card_row.ba_number;

    -- Aggiorno il saldo del conto corrente
    UPDATE smu.bankaccount
    SET balance = balance - OLD.amount
    WHERE accountnumber = ba_row.accountnumber;

    -- Seleziona tutti i portafogli a cui deve essere cancellata la transazione
    FOR wallet_id IN
        SELECT W.id_wallet
        FROM smu.wallet AS W
        NATURAL JOIN smu.transactioninwallet AS TIW 
        WHERE TIW.id_transaction = OLD.id_transaction
    LOOP
        -- Aggiorna il campo totalamount del wallet
        UPDATE smu.wallet
        SET totalamount = totalamount - OLD.amount
        WHERE id_wallet = wallet_id;

    END LOOP;

    RETURN OLD;
	
END;
$$;


ALTER FUNCTION smu.remove_transaction() OWNER TO postgres;

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
        WHERE T.category = NEW.walletcategory AND T.cardnumber IN (SELECT cardnumber FROM smu.card WHERE owneremail = NEW.owneremail
                                                            UNION
                                                            SELECT C.cardnumber
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
12500	3	Banca Popolare	\N	lucabianchi88@example.com
8200	4	UniCredit	\N	lucabianchi88@example.com
9400	9	Banca Nazionale del Lavoro	BNCMLU85T20Z123U	\N
15795	5	Sanpaolo	BNCMLU78T10Z123S	\N
13035	12	Banca Sella	BNCMLU90T15Z123V	\N
6825	11	Banca Carige	BNCMLU90T15Z123V	\N
7670	8	Banco di Sicilia	BNCMLU80T05Z123T	\N
11295	10	Credito Emiliano	BNCMLU85T20Z123U	\N
10335	7	Banco di Napoli	BNCMLU80T05Z123T	\N
6065	6	Monte dei Paschi di Siena	BNCMLU78T10Z123S	\N
12500	15	Banca Popolare	\N	sofiarossi95@example.com
8200	16	UniCredit	\N	sofiarossi95@example.com
7670	21	Banco di Sicilia	RNCMLU80T05Z123T	\N
9495	17	Banca Nazionale del Lavoro	RNCMLU85T20Z123U	\N
6100	24	Monte dei Paschi di Siena	RNCMLU78T10Z123S	\N
10360	23	Banco di Napoli	RNCMLU80T05Z123T	\N
6895	20	Banca Carige	RNCMLU90T15Z123V	\N
11390	22	Credito Emiliano	RNCMLU85T20Z123U	\N
13070	19	Banca Sella	RNCMLU90T15Z123V	\N
15960	18	Sanpaolo	RNCMLU78T10Z123S	\N
\.


--
-- Data for Name: card; Type: TABLE DATA; Schema: smu; Owner: postgres
--

COPY smu.card (cardnumber, iban, cvv, expiredata, cardtype, ba_number, ownercf, owneremail) FROM stdin;
4532957216031773	CE4532957216031773456392716	856	2026-09-30	Prepagata	10	BNCMLU85T20Z123U	\N
5480701378439248	BN5480701378439248548070137	712	2025-11-30	Debito	7	BNCMLU90T15Z123V	\N
4024007128253241	SA4024007128253241402400712	593	2024-07-31	Credito	5	BNCMLU78T10Z123S	\N
4916480174807685	BS4916480174807685491648017	402	2023-05-31	Prepagata	8	\N	lucabianchi88@example.com
4716830916578062	MP4716830916578062471683091	148	2028-03-28	Debito	6	BNCMLU85T20Z123U	\N
5363478001927136	SA5363478001927136536347800	637	2027-08-31	Credito	5	\N	lucabianchi88@example.com
4556620100361261	BS4556620100361261455662010	980	2025-06-30	Prepagata	12	BNCMLU78T10Z123S	\N
4024007158297436	MP4024007158297436402400715	259	2026-12-31	Debito	6	BNCMLU90T15Z123V	\N
5250772138246597	CE5250772138246597525077213	716	2027-10-31	Credito	10	BNCMLU80T05Z123T	\N
4916740256859631	BC4916740256859631491674025	409	2024-02-28	Prepagata	11	BNCMLU80T05Z123T	\N
4916480174807686	BS4916480174807685491648018	403	2023-05-31	Prepagata	20	\N	sofiarossi95@example.com
5363478001927137	SA5363478001927136536347801	638	2027-08-31	Credito	17	\N	sofiarossi95@example.com
4532957216031774	CE4532957216031773456392717	857	2026-09-30	Prepagata	22	RNCMLU85T20Z123U	\N
5480701378439249	BN5480701378439248548070138	713	2025-11-30	Debito	19	RNCMLU90T15Z123V	\N
4024007128253242	SA4024007128253241402400713	594	2024-07-31	Credito	17	RNCMLU78T10Z123S	\N
4716830916578063	MP4716830916578062471683092	149	2028-03-28	Debito	18	RNCMLU85T20Z123U	\N
4556620100361262	BS4556620100361261455662011	981	2025-06-30	Prepagata	24	RNCMLU78T10Z123S	\N
4024007158297437	MP4024007158297436402400716	260	2026-12-31	Debito	18	RNCMLU90T15Z123V	\N
5250772138246598	CE5250772138246597525077214	717	2027-10-31	Credito	22	RNCMLU80T05Z123T	\N
4916740256859632	BC4916740256859631491674026	410	2024-02-28	Prepagata	23	RNCMLU80T05Z123T	\N
\.


--
-- Data for Name: familiar; Type: TABLE DATA; Schema: smu; Owner: postgres
--

COPY smu.familiar (name, surname, cf, dateofbirth, familiaremail) FROM stdin;
Marta	Bianchi	BNCMLU78T10Z123S	1978-03-10	lucabianchi88@example.com
Paolo	Bianchi	BNCMLU80T05Z123T	1980-11-05	lucabianchi88@example.com
Elena	Bianchi	BNCMLU85T20Z123U	1985-08-20	lucabianchi88@example.com
Andrea	Bianchi	BNCMLU90T15Z123V	1990-06-15	lucabianchi88@example.com
Marta	Rossi	RNCMLU78T10Z123S	1978-03-10	sofiarossi95@example.com
Paolo	Rossi	RNCMLU80T05Z123T	1980-11-05	sofiarossi95@example.com
Elena	Rossi	RNCMLU85T20Z123U	1985-08-20	sofiarossi95@example.com
Andrea	Rossi	RNCMLU90T15Z123V	1990-06-15	sofiarossi95@example.com
\.


--
-- Data for Name: transaction; Type: TABLE DATA; Schema: smu; Owner: postgres
--

COPY smu.transaction (id_transaction, amount, date, category, cardnumber, walletname) FROM stdin;
52	-20	2022-04-15	Risparmi	4916480174807685	Fondi di Emergenza
53	100	2024-01-03	Risparmi	5363478001927136	Fondi di Emergenza
55	-35	2023-06-04	Risparmi	5363478001927136	Fondi di Emergenza
57	75	2023-05-05	Risparmi	4024007128253241	Fondi di Emergenza
58	-45	2024-03-06	Risparmi	4024007128253241	Fondi di Emergenza
60	60	2023-11-07	Investimenti	4556620100361261	Azioni Tecnologiche
62	-25	2023-12-08	Spese Mensili	4556620100361261	Affitto e Bollette
64	80	2023-09-09	Intrattenimento	5250772138246597	Intrattenimento
66	-15	2023-08-10	Viaggi	5250772138246597	Viaggio Estivo
67	55	2023-06-11	Risparmi	4916740256859631	Fondi di Emergenza
68	-30	2023-10-12	Investimenti	4916740256859631	Azioni Tecnologiche
71	90	2023-05-13	Spese Mensili	4916480174807685	Affitto e Bollette
72	-40	2023-08-14	Intrattenimento	4532957216031773	Intrattenimento
73	70	2024-04-15	Viaggi	4532957216031773	Viaggio Estivo
75	-10	2023-04-16	Investimenti	4716830916578062	Azioni Tecnologiche
76	85	2024-03-17	Spese Mensili	4716830916578062	Affitto e Bollette
77	85	2024-03-17	Intrattenimento	5480701378439248	Intrattenimento
78	-50	2024-02-18	Spese Mensili	5480701378439248	Affitto e Bollette
79	110	2024-01-19	Risparmi	4024007158297436	Fondi di Emergenza
81	-20	2023-07-20	Viaggi	4024007158297436	Viaggio Estivo
82	-20	2022-04-15	Risparmi	4916480174807686	Fondi di Emergenza
83	100	2024-01-03	Risparmi	5363478001927137	Fondi di Emergenza
84	-35	2023-06-04	Risparmi	5363478001927137	Fondi di Emergenza
85	75	2023-05-05	Risparmi	4024007128253242	Fondi di Emergenza
86	-45	2024-03-06	Risparmi	4024007128253242	Fondi di Emergenza
87	60	2023-11-07	Investimenti	4556620100361262	Azioni Tecnologiche
88	-25	2023-12-08	Spese Mensili	4556620100361262	Affitto e Bollette
89	80	2023-09-09	Intrattenimento	5250772138246598	Intrattenimento
90	-15	2023-08-10	Viaggi	5250772138246598	Viaggio Estivo
91	55	2023-06-11	Risparmi	4916740256859632	Fondi di Emergenza
92	-30	2023-10-12	Investimenti	4916740256859632	Azioni Tecnologiche
93	90	2023-05-13	Spese Mensili	4916480174807686	Affitto e Bollette
94	-40	2023-08-14	Intrattenimento	4532957216031774	Intrattenimento
95	70	2024-04-15	Viaggi	4532957216031774	Viaggio Estivo
96	-10	2023-04-16	Investimenti	4716830916578063	Azioni Tecnologiche
97	85	2024-03-17	Spese Mensili	4716830916578063	Affitto e Bollette
98	85	2024-03-17	Intrattenimento	5480701378439249	Intrattenimento
99	-50	2024-02-18	Spese Mensili	5480701378439249	Affitto e Bollette
100	110	2024-01-19	Risparmi	4024007158297437	Fondi di Emergenza
101	-20	2023-07-20	Viaggi	4024007158297437	Viaggio Estivo
\.


--
-- Data for Name: transactioninwallet; Type: TABLE DATA; Schema: smu; Owner: postgres
--

COPY smu.transactioninwallet (id_transaction, id_wallet) FROM stdin;
52	37
53	37
55	37
57	37
58	37
60	38
62	39
64	40
66	36
67	37
68	38
71	39
72	40
73	36
75	38
76	39
77	40
78	39
79	37
81	36
82	45
83	45
84	45
85	45
86	45
87	42
88	44
89	43
90	46
91	45
92	42
93	44
94	43
95	46
96	42
97	44
98	43
99	44
100	45
101	46
\.


--
-- Data for Name: user; Type: TABLE DATA; Schema: smu; Owner: postgres
--

COPY smu."user" (email, username, password, address, name, surname, cf, dateofbirth) FROM stdin;
lucabianchi88@example.com	lucabianchi88	P@ssw0rd123	Via Roma, 123	Luca	Bianchi	BNCLCU88E10H501J	1988-05-10
sofiarossi95@example.com	sofiarossi95	SecurePass123	Via Milano, 456	Sofia	Rossi	RSSSFI95P55F839F	1995-09-15
\.


--
-- Data for Name: wallet; Type: TABLE DATA; Schema: smu; Owner: postgres
--

COPY smu.wallet (id_wallet, walletname, walletcategory, totalamount, owneremail) FROM stdin;
38	Azioni Tecnologiche	Investimenti	20	lucabianchi88@example.com
40	Intrattenimento	Intrattenimento	125	lucabianchi88@example.com
39	Affitto e Bollette	Spese Mensili	100	lucabianchi88@example.com
37	Fondi di Emergenza	Risparmi	240	lucabianchi88@example.com
36	Viaggio Estivo	Viaggi	35	lucabianchi88@example.com
42	Azioni Tecnologiche	Investimenti	20	sofiarossi95@example.com
43	Intrattenimento	Intrattenimento	125	sofiarossi95@example.com
44	Affitto e Bollette	Spese Mensili	100	sofiarossi95@example.com
45	Fondi di Emergenza	Risparmi	240	sofiarossi95@example.com
46	Viaggio Estivo	Viaggi	35	sofiarossi95@example.com
\.


--
-- Name: bankaccount_accountnumber_seq; Type: SEQUENCE SET; Schema: smu; Owner: postgres
--

SELECT pg_catalog.setval('smu.bankaccount_accountnumber_seq', 24, true);


--
-- Name: card_ba_number_seq; Type: SEQUENCE SET; Schema: smu; Owner: postgres
--

SELECT pg_catalog.setval('smu.card_ba_number_seq', 1, false);


--
-- Name: transaction_id_transaction_seq; Type: SEQUENCE SET; Schema: smu; Owner: postgres
--

SELECT pg_catalog.setval('smu.transaction_id_transaction_seq', 101, true);


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

SELECT pg_catalog.setval('smu.wallet_id_wallet_seq', 46, true);


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
-- Name: transaction remove_transaction_trigger; Type: TRIGGER; Schema: smu; Owner: postgres
--

CREATE TRIGGER remove_transaction_trigger BEFORE DELETE ON smu.transaction FOR EACH ROW EXECUTE FUNCTION smu.remove_transaction();


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

