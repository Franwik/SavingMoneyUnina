package smu.DAO;

import java.sql.SQLException;
import java.util.*;
import smu.DTO.Transaction;

public interface TransactionDAO {

    Transaction getById(int id) throws SQLException;

    List<Transaction> getByCardNumber(String choosenCard) throws SQLException;

    List<Transaction> getByWalletCategory(String choosenWallet) throws SQLException;

    List<Transaction> getByWalletName(String choosenWalletName, String category) throws SQLException;

    int insert(Transaction transaction) throws SQLException;

    int update(Transaction transaction) throws SQLException;

    int delete(int id_transaction) throws SQLException;

}
