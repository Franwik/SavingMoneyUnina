ALTER TABLE smu.wallet RENAME COLUMN name TO walletName;

CREATE OR REPLACE FUNCTION smu.connect_transaction_to_wallet() RETURNS trigger
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
        WHERE iban = OLD.cardiban;

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