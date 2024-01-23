--
-- PostgreSQL database dump
--

-- Dumped from database version 16.1
-- Dumped by pg_dump version 16.1

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
    ownerusername character varying(20)
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
    iban character varying(27) NOT NULL,
    cvv character varying(3) NOT NULL,
    expiredata date NOT NULL,
    cardtype character varying(11),
    ba_number integer NOT NULL,
    ownercf character varying(16) NOT NULL,
    ownerusername character varying(20) NOT NULL
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
    familiarusername character varying(20) NOT NULL
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
    cardiban character varying(27) NOT NULL
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
-- Name: transactionwallet; Type: TABLE; Schema: smu; Owner: postgres
--

CREATE TABLE smu.transactionwallet (
    id_transaction integer NOT NULL,
    id_wallet integer NOT NULL
);


ALTER TABLE smu.transactionwallet OWNER TO postgres;

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

ALTER SEQUENCE smu.transactionwallet_id_transaction_seq OWNED BY smu.transactionwallet.id_transaction;


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

ALTER SEQUENCE smu.transactionwallet_id_wallet_seq OWNED BY smu.transactionwallet.id_wallet;


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
    dateofbirth date NOT NULL
);


ALTER TABLE smu."user" OWNER TO postgres;

--
-- Name: wallet; Type: TABLE; Schema: smu; Owner: postgres
--

CREATE TABLE smu.wallet (
    id_wallet integer NOT NULL,
    name character varying(35) NOT NULL,
    walletcategory character varying(35) NOT NULL,
    totalamount double precision NOT NULL
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
-- Name: transactionwallet id_transaction; Type: DEFAULT; Schema: smu; Owner: postgres
--

ALTER TABLE ONLY smu.transactionwallet ALTER COLUMN id_transaction SET DEFAULT nextval('smu.transactionwallet_id_transaction_seq'::regclass);


--
-- Name: transactionwallet id_wallet; Type: DEFAULT; Schema: smu; Owner: postgres
--

ALTER TABLE ONLY smu.transactionwallet ALTER COLUMN id_wallet SET DEFAULT nextval('smu.transactionwallet_id_wallet_seq'::regclass);


--
-- Name: wallet id_wallet; Type: DEFAULT; Schema: smu; Owner: postgres
--

ALTER TABLE ONLY smu.wallet ALTER COLUMN id_wallet SET DEFAULT nextval('smu.wallet_id_wallet_seq'::regclass);


--
-- Data for Name: bankaccount; Type: TABLE DATA; Schema: smu; Owner: postgres
--

COPY smu.bankaccount (balance, accountnumber, bank, ownercf, ownerusername) FROM stdin;
\.


--
-- Data for Name: card; Type: TABLE DATA; Schema: smu; Owner: postgres
--

COPY smu.card (iban, cvv, expiredata, cardtype, ba_number, ownercf, ownerusername) FROM stdin;
\.


--
-- Data for Name: familiar; Type: TABLE DATA; Schema: smu; Owner: postgres
--

COPY smu.familiar (name, surname, cf, dateofbirth, familiarusername) FROM stdin;
\.


--
-- Data for Name: transaction; Type: TABLE DATA; Schema: smu; Owner: postgres
--

COPY smu.transaction (id_transaction, amount, date, category, cardiban) FROM stdin;
\.


--
-- Data for Name: transactionwallet; Type: TABLE DATA; Schema: smu; Owner: postgres
--

COPY smu.transactionwallet (id_transaction, id_wallet) FROM stdin;
\.


--
-- Data for Name: user; Type: TABLE DATA; Schema: smu; Owner: postgres
--

COPY smu."user" (email, username, password, address, name, surname, cf, dateofbirth) FROM stdin;
arturodonnarumma01@gmail.com	thankyousomaz	maradona	via stazione 23	Arturo	Donnarumma	DNNRTR01R30L245F	2001-10-30
\.


--
-- Data for Name: wallet; Type: TABLE DATA; Schema: smu; Owner: postgres
--

COPY smu.wallet (id_wallet, name, walletcategory, totalamount) FROM stdin;
\.


--
-- Name: bankaccount_accountnumber_seq; Type: SEQUENCE SET; Schema: smu; Owner: postgres
--

SELECT pg_catalog.setval('smu.bankaccount_accountnumber_seq', 1, false);


--
-- Name: card_ba_number_seq; Type: SEQUENCE SET; Schema: smu; Owner: postgres
--

SELECT pg_catalog.setval('smu.card_ba_number_seq', 1, false);


--
-- Name: transaction_id_transaction_seq; Type: SEQUENCE SET; Schema: smu; Owner: postgres
--

SELECT pg_catalog.setval('smu.transaction_id_transaction_seq', 1, false);


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

SELECT pg_catalog.setval('smu.wallet_id_wallet_seq', 1, false);


--
-- Name: bankaccount pk_bankaccount; Type: CONSTRAINT; Schema: smu; Owner: postgres
--

ALTER TABLE ONLY smu.bankaccount
    ADD CONSTRAINT pk_bankaccount PRIMARY KEY (accountnumber);


--
-- Name: card pk_card; Type: CONSTRAINT; Schema: smu; Owner: postgres
--

ALTER TABLE ONLY smu.card
    ADD CONSTRAINT pk_card PRIMARY KEY (iban);


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
-- Name: user pk_user; Type: CONSTRAINT; Schema: smu; Owner: postgres
--

ALTER TABLE ONLY smu."user"
    ADD CONSTRAINT pk_user PRIMARY KEY (username);


--
-- Name: wallet pk_wallet; Type: CONSTRAINT; Schema: smu; Owner: postgres
--

ALTER TABLE ONLY smu.wallet
    ADD CONSTRAINT pk_wallet PRIMARY KEY (id_wallet);


--
-- Name: bankaccount fk_bankaccount; Type: FK CONSTRAINT; Schema: smu; Owner: postgres
--

ALTER TABLE ONLY smu.bankaccount
    ADD CONSTRAINT fk_bankaccount FOREIGN KEY (ownercf) REFERENCES smu.familiar(cf);


--
-- Name: bankaccount fk_bankaccount2; Type: FK CONSTRAINT; Schema: smu; Owner: postgres
--

ALTER TABLE ONLY smu.bankaccount
    ADD CONSTRAINT fk_bankaccount2 FOREIGN KEY (ownerusername) REFERENCES smu."user"(username);


--
-- Name: card fk_card; Type: FK CONSTRAINT; Schema: smu; Owner: postgres
--

ALTER TABLE ONLY smu.card
    ADD CONSTRAINT fk_card FOREIGN KEY (ba_number) REFERENCES smu.bankaccount(accountnumber);


--
-- Name: card fk_card2; Type: FK CONSTRAINT; Schema: smu; Owner: postgres
--

ALTER TABLE ONLY smu.card
    ADD CONSTRAINT fk_card2 FOREIGN KEY (ownercf) REFERENCES smu.familiar(cf);


--
-- Name: card fk_card3; Type: FK CONSTRAINT; Schema: smu; Owner: postgres
--

ALTER TABLE ONLY smu.card
    ADD CONSTRAINT fk_card3 FOREIGN KEY (ownerusername) REFERENCES smu."user"(username);


--
-- Name: familiar fk_familiar; Type: FK CONSTRAINT; Schema: smu; Owner: postgres
--

ALTER TABLE ONLY smu.familiar
    ADD CONSTRAINT fk_familiar FOREIGN KEY (familiarusername) REFERENCES smu."user"(username);


--
-- Name: transaction fk_transaction; Type: FK CONSTRAINT; Schema: smu; Owner: postgres
--

ALTER TABLE ONLY smu.transaction
    ADD CONSTRAINT fk_transaction FOREIGN KEY (cardiban) REFERENCES smu.card(iban);


--
-- Name: transactionwallet fk_transactionwallet; Type: FK CONSTRAINT; Schema: smu; Owner: postgres
--

ALTER TABLE ONLY smu.transactionwallet
    ADD CONSTRAINT fk_transactionwallet FOREIGN KEY (id_transaction) REFERENCES smu.transaction(id_transaction);


--
-- Name: transactionwallet fk_transactionwallet2; Type: FK CONSTRAINT; Schema: smu; Owner: postgres
--

ALTER TABLE ONLY smu.transactionwallet
    ADD CONSTRAINT fk_transactionwallet2 FOREIGN KEY (id_wallet) REFERENCES smu.wallet(id_wallet);


--
-- PostgreSQL database dump complete
--

