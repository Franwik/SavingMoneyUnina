package smu.DAOImplementation;

import java.sql.*;
import java.util.*;

import smu.Database;
import smu.LoggedUser;
import smu.DAO.WalletDAO;
import smu.DTO.Wallet;

public class WalletDAOimp implements WalletDAO {

    @Override
    public Wallet getById(int id) throws SQLException {

        Connection con = Database.getConnection();

        Wallet wallet = null;

        String sql = "SELECT * FROM smu.wallet WHERE ID_Wallet = ?";

        PreparedStatement ps = con.prepareStatement(sql) ;
            
		ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();
		
        if (rs.next()){
            wallet = new Wallet(rs.getInt("id_wallet"), rs.getString("walletname"), rs.getString("walletcategory"));            
        }

		return wallet;
	}

    @Override
    public List<Wallet> getAllByEmail(String email) throws SQLException {

        Connection con = Database.getConnection();

        List<Wallet> wallets = new ArrayList<>();

        String sql = "SELECT * FROM smu.wallet WHERE owneremail = ?";

        PreparedStatement ps = con.prepareStatement(sql);

		ps.setString(1, email);
            
		ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            Wallet wallet = new Wallet(rs.getInt("id_wallet"), rs.getString("walletname"), rs.getString("walletcategory"));
            wallets.add(wallet);

        }

		return wallets;
    }

    @Override
    public List<Wallet> getAllByEmailAndCategory(String email, String category) throws SQLException {

        Connection con = Database.getConnection();

        List<Wallet> wallets = new ArrayList<>();

        String sql = "SELECT * FROM smu.wallet WHERE owneremail = ? AND walletcategory = ?";

        PreparedStatement ps = con.prepareStatement(sql);

		ps.setString(1, email);
        ps.setString(2, category);

		ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            Wallet wallet = new Wallet(rs.getInt("id_wallet"), rs.getString("walletname"), rs.getString("walletcategory"));
            wallets.add(wallet);

        }

		return wallets;
    }

    @Override
    public int insert(Wallet wallet) throws SQLException {
        Connection con = Database.getConnection();

        LoggedUser loggedUser = LoggedUser.getInstance();

        String sql = "INSERT INTO smu.wallet (walletname, walletcategory, totalamount, owneremail) VALUES (?, ?, 0, ?)";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, wallet.getWalletName());
        ps.setString(2, wallet.getWalletCategory());
        ps.setString(3, loggedUser.getEmail());

        int result = ps.executeUpdate();

        return result;
    }

    @Override
    public int update(Wallet wallet) throws SQLException {
        Connection con = Database.getConnection();

        String sql = "UPDATE smu.wallet SET walletname = ?, walletcategory = ? WHERE id_wallet = ?";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, wallet.getWalletName());
        ps.setString(2, wallet.getWalletCategory());
        ps.setInt(3, wallet.getId_wallet());

        int result = ps.executeUpdate();

        return result;
    }

    @Override
    public int delete(int id) throws SQLException {
        Connection con = Database.getConnection();

        String sql = "DELETE FROM smu.wallet WHERE ID_Wallet = ?";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, id);

        int result = ps.executeUpdate();

        return result;
    }

}

