CREATE SCHEMA IF NOT EXISTS SavingMoneyUnina;

CREATE TABLE IF NOT EXISTS "User"(
    email VARCHAR(100) NOT NULL,
    username VARCHAR(20),
    password VARCHAR(30) NOT NULL,
    address VARCHAR(100),
    name VARCHAR(20) NOT NULL,
    surname VARCHAR(30) NOT NULL,
    CF VARCHAR(16) NOT NULL,
    dateOfBirth DATE NOT NULL
);

ALTER TABLE "User" DROP CONSTRAINT IF EXISTS pk_user;
--primary key
ALTER TABLE "User" ADD CONSTRAINT pk_user PRIMARY KEY (username);

CREATE TABLE IF NOT EXISTS Familiar(
    name VARCHAR(20) NOT NULL,
    surname VARCHAR(30) NOT NULL,
    CF VARCHAR(16),
    dateOfBirth DATE NOT NULL,
    familiarUsername VARCHAR(20) NOT NULL
);

ALTER TABLE Familiar DROP CONSTRAINT IF EXISTS pk_familiar;
--primary key
ALTER TABLE Familiar ADD CONSTRAINT pk_familiar PRIMARY KEY (CF);

ALTER TABLE Familiar DROP CONSTRAINT IF EXISTS fk_familiar;
--foreign key
ALTER TABLE Familiar ADD CONSTRAINT fk_familiar FOREIGN KEY (familiarUsername) REFERENCES "User"(username);

CREATE TABLE IF NOT EXISTS BankAccount(
    balance INTEGER NOT NULL,
    accountNumber SERIAL,
    bank VARCHAR(40) NOT NULL,
    ownerCF VARCHAR(16) NOT NULL,
    ownerUsername VARCHAR(20) NOT NULL
);

ALTER TABLE BankAccount DROP CONSTRAINT IF EXISTS pk_bankaccount;
--primary key
ALTER TABLE BankAccount ADD CONSTRAINT pk_bankaccount PRIMARY KEY (accountNumber);

ALTER TABLE BankAccount DROP CONSTRAINT IF EXISTS fk_bankaccount;
ALTER TABLE BankAccount DROP CONSTRAINT IF EXISTS fk_bankaccount2;
--foreign key
ALTER TABLE BankAccount ADD CONSTRAINT fk_bankaccount FOREIGN KEY (ownerCF) REFERENCES Familiar(CF);
ALTER TABLE BankAccount ADD CONSTRAINT fk_bankaccount2 FOREIGN KEY (ownerUsername) REFERENCES "User"(username);

CREATE TYPE CardCategory AS ENUM ('debit', 'prepaid', 'credit');

CREATE TABLE IF NOT EXISTS Card(
    IBAN VARCHAR(27),
    CVV VARCHAR(3) NOT NULL,
    expireData DATE NOT NULL,
    cardType CardCategory NOT NULL,
    BA_Number SERIAL NOT NULL,
    ownerCF VARCHAR(16) NOT NULL,
    ownerUsername VARCHAR(20) NOT NULL
);

ALTER TABLE Card DROP CONSTRAINT IF EXISTS pk_card;
--primary key
ALTER TABLE Card ADD CONSTRAINT pk_card PRIMARY KEY (IBAN);

ALTER TABLE Card DROP CONSTRAINT IF EXISTS fk_card;
ALTER TABLE Card DROP CONSTRAINT IF EXISTS fk_card2;
ALTER TABLE Card DROP CONSTRAINT IF EXISTS fk_card3;
--foreign key
ALTER TABLE Card ADD CONSTRAINT fk_card FOREIGN KEY (BA_Number) REFERENCES BankAccount(accountNumber);
ALTER TABLE Card ADD CONSTRAINT fk_card2 FOREIGN KEY (ownerCF) REFERENCES Familiar(CF);
ALTER TABLE Card ADD CONSTRAINT fk_card3 FOREIGN KEY (ownerUsername) REFERENCES "User"(username);

CREATE TABLE IF NOT EXISTS Wallet(
    ID_Wallet SERIAL,
    name VARCHAR(35) NOT NULL,
    walletCategory VARCHAR(35) NOT NULL,
    totalAmount FLOAT NOT NULL
);

ALTER TABLE Wallet DROP CONSTRAINT IF EXISTS pk_wallet;
--primary key
ALTER TABLE Wallet ADD CONSTRAINT pk_wallet PRIMARY KEY (ID_Wallet);

CREATE TABLE IF NOT EXISTS Transaction(
    ID_Transaction SERIAL,
    amount FLOAT NOT NULL,
    date DATE NOT NULL,
    category VARCHAR(35),
    cardIBAN VARCHAR(27) NOT NULL
);

ALTER TABLE Transaction DROP CONSTRAINT IF EXISTS pk_transaction;
--primary key
ALTER TABLE Transaction ADD CONSTRAINT pk_transaction PRIMARY KEY (ID_Transaction);

ALTER TABLE Transaction DROP CONSTRAINT IF EXISTS fk_transaction;
ALTER TABLE Transaction DROP CONSTRAINT IF EXISTS fk_transaction2;
--foreign key
ALTER TABLE Transaction ADD CONSTRAINT fk_transaction FOREIGN KEY (CardIBAN) REFERENCES Card(IBAN);

CREATE TABLE IF NOT EXISTS TransactionWallet(
    ID_Transaction SERIAL,
    ID_Wallet SERIAL
)

ALTER TABLE TransactionInWallet DROP CONSTRAINT IF EXISTS fk_transactionWallet;
ALTER TABLE TransactionInWallet DROP CONSTRAINT IF EXISTS fk_transactionWallet2;
--foreign key
ALTER TABLE TransactionInWallet ADD CONSTRAINT fk_transactionWallet FOREIGN KEY (ID_Transaction) REFERENCES Transaction(ID_Transaction);
ALTER TABLE TransactionInWallet ADD CONSTRAINT fk_transactionWallet2 FOREIGN KEY (ID_Wallet) REFERENCES Wallet(ID_Wallet);

--ALTER TABLE TransactionWallet RENAME TO TransactionInWallet;


