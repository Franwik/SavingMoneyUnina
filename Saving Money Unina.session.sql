ALTER TABLE ONLY smu.familiar
    ADD CONSTRAINT fk_familiar FOREIGN KEY (familiaremail) REFERENCES smu."user"(email);

ALTER TABLE smu.familiar DROP COLUMN familiarusername;

ALTER TABLE smu.familiar ADD COLUMN familiaremail character varying(100) NOT NULL;