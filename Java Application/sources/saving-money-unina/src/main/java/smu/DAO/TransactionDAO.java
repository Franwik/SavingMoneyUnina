package smu.DAO;

import java.sql.SQLException;
import java.util.*;
import smu.DTO.Transaction;

public interface TransactionDAO {

    Transaction getById(int id) throws SQLException;

    List<Transaction> getAllByEmail(String email) throws SQLException;

}
