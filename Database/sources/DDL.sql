CREATE SCHEMA IF NOT EXISTS SavingMoneyUnina;

CREATE TABLE IF NOT EXISTS User(
    email VARCHAR(100) NOT NULL,
    username VARCHAR(20),
    password VARCHAR(30) NOT NULL,
    address VARCHAR(100),
    name VARCHAR(20) NOT NULL,
    surname VARCHAR(30) NOT NULL,
    CF VARCHAR(16) NOT NULL,
    dateOfBirth DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS BankAccount(
    balance INTEGER NOT NULL,
    accountNumber SERIAL,
    bank VARCHAR(40) NOT NULL
);

CREATE TYPE CardCategory AS ENUM ('debit', 'prepaid', 'credit');

CREATE TABLE IF NOT EXISTS Card(
    IBAN VARCHAR(27),
    CVV VARCHAR(3) NOT NULL,
    expireData DATE NOT NULL,
    cardType CardCategory NOT NULL
);

CREATE TABLE IF NOT EXISTS Familiar(
    name VARCHAR(20) NOT NULL,
    surname VARCHAR(30) NOT NULL,
    CF VARCHAR(16),
    dateOfBirth DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS Transaction(
    ID_Transaction SERIAL,
    amount FLOAT NOT NULL,
    Date DATE NOT NULL,
    category VARCHAR(35)
);

CREATE TABLE IF NOT EXISTS Wallet(
    ID_Wallet SERIAL,
    name VARCHAR(35) NOT NULL,
    walletCategory VARCHAR(35) NOT NULL,
    totalAmount INTEGER NOT NULL
);



