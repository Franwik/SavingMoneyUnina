package smu.DAO;

import smu.DTO.BankAccount;
import java.util.*;
import java.sql.*;

public interface BankAccountDAO {

    List<BankAccount> getByEmail(String email) throws SQLException;

    List<BankAccount> getByCF(String CF) throws SQLException;

}
