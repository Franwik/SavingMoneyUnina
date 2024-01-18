DROP SCHEMA IF EXISTS smu CASCADE;
CREATE SCHEMA IF NOT EXISTS smu;

CREATE TABLE IF NOT EXISTS smu.User(
    email VARCHAR(100) NOT NULL,
    username VARCHAR(20),
    password VARCHAR(30) NOT NULL,
    address VARCHAR(100),
    name VARCHAR(20) NOT NULL,
    surname VARCHAR(30) NOT NULL,
    CF VARCHAR(16) NOT NULL,
    dateOfBirth DATE NOT NULL,
    --primary key
    CONSTRAINT pk_user PRIMARY KEY (username)
);

CREATE TABLE IF NOT EXISTS smu.Familiar(
    name VARCHAR(20) NOT NULL,
    surname VARCHAR(30) NOT NULL,
    CF VARCHAR(16),
    dateOfBirth DATE NOT NULL,
    familiarUsername VARCHAR(20) NOT NULL,
    --primary key
    CONSTRAINT pk_familiar PRIMARY KEY (CF),
    --foreign key
    CONSTRAINT fk_familiar FOREIGN KEY (familiarUsername) REFERENCES smu.User(username)
);

CREATE TABLE IF NOT EXISTS smu.BankAccount(
    balance INTEGER NOT NULL,
    accountNumber SERIAL unique,
    bank VARCHAR(40),
    ownerCF VARCHAR(16),
    ownerUsername VARCHAR(20),
    --primary key
    CONSTRAINT pk_bankaccount PRIMARY KEY (accountNumber),
    --foreign key
    CONSTRAINT fk_bankaccount FOREIGN KEY (ownerCF) REFERENCES smu.Familiar(CF),
    CONSTRAINT fk_bankaccount2 FOREIGN KEY (ownerUsername) REFERENCES smu.User(username)
);

CREATE TABLE IF NOT EXISTS smu.Card(
    IBAN VARCHAR(27),
    CVV VARCHAR(3) NOT NULL,
    expireData DATE NOT NULL,
    cardType VARCHAR(11),
    BA_Number SERIAL NOT NULL,
    ownerCF VARCHAR(16) NOT NULL,
    ownerUsername VARCHAR(20) NOT NULL,
    --primary key
    CONSTRAINT pk_card PRIMARY KEY (IBAN),
    --foreign key
    CONSTRAINT fk_card FOREIGN KEY (BA_Number) REFERENCES smu.BankAccount(accountNumber),
    CONSTRAINT fk_card2 FOREIGN KEY (ownerCF) REFERENCES smu.Familiar(CF),
    CONSTRAINT fk_card3 FOREIGN KEY (ownerUsername) REFERENCES smu.User(username)
);

CREATE TABLE IF NOT EXISTS smu.Wallet(
    ID_Wallet SERIAL,
    name VARCHAR(35) NOT NULL,
    walletCategory VARCHAR(35) NOT NULL,
    totalAmount FLOAT NOT NULL,
    --primary key
    CONSTRAINT pk_wallet PRIMARY KEY (ID_Wallet)
);

CREATE TABLE IF NOT EXISTS smu.Transaction(
    ID_Transaction SERIAL,
    amount FLOAT NOT NULL,
    date DATE NOT NULL,
    category VARCHAR(35),
    cardIBAN VARCHAR(27) NOT NULL,
    --primary key
    CONSTRAINT pk_transaction PRIMARY KEY (ID_Transaction),
    --foreign key
    CONSTRAINT fk_transaction FOREIGN KEY (CardIBAN) REFERENCES smu.Card(IBAN)
);

CREATE TABLE IF NOT EXISTS smu.TransactionWallet(
    ID_Transaction SERIAL,
    ID_Wallet SERIAL,
    --foreign key
    CONSTRAINT fk_transactionWallet FOREIGN KEY (ID_Transaction) REFERENCES smu.Transaction(ID_Transaction),
    CONSTRAINT fk_transactionWallet2 FOREIGN KEY (ID_Wallet) REFERENCES smu.Wallet(ID_Wallet)
);

--dichiarazione dei vincoli

