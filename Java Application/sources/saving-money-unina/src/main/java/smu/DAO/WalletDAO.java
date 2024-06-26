package smu.DAO;

import smu.DTO.Wallet;
import java.sql.SQLException;
import java.util.*;

public interface WalletDAO {

    Wallet getById(int id) throws SQLException;

    Wallet getByName(String walletName) throws SQLException;

    float getTotalAmountById(int id) throws SQLException;

    List<Wallet> getAllByEmail(String email) throws SQLException;

    List<Wallet> getAllByEmailAndCategory(String email, String category) throws SQLException;

    int insert(Wallet wallet) throws SQLException;

    int update(Wallet wallet) throws SQLException;

    int delete(int id_wallet) throws SQLException;

}




