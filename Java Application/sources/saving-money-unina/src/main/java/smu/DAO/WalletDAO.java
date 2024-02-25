package smu.DAO;

import smu.DTO.Wallet;
import java.sql.SQLException;
import java.util.*;

public interface WalletDAO {

    Wallet getById(int id) throws SQLException;

    List<Wallet> getAllByEmail(String email) throws SQLException;

}




