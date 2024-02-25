package smu.DAOImplementation;

import java.sql.*;
import java.util.*;

import smu.Database;
import smu.DAO.WalletDAO;
import smu.DTO.Wallet;

public class WalletDAOimp implements WalletDAO {

    @Override
    public Wallet getById(int id) throws SQLException {

        Connection con = Database.getConnection();

        Wallet wallet = null;

        String sql = "SELECT * FROM wallet WHERE id_wallet = ?";

        PreparedStatement ps = con.prepareStatement(sql) ;
            
		ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();
		
        if (rs.next()) 
            wallet = new Wallet(rs.getInt("id_wallet"), rs.getString("name"), rs.getString("walletCategory"), rs.getInt("totalAmount"));            
		

		return wallet;
	}

	



    @Override
    public List<Wallet> getAllByEmail(String email) throws SQLException {

        Connection con = Database.getConnection();

        List<Wallet> wallets = new ArrayList<>();

        String sql = "SELECT * FROM wallet WHERE owneremail = ?";

        PreparedStatement ps = con.prepareStatement(sql);

		ps.setString(1, email);
            
		ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            Wallet wallet = new Wallet(rs.getInt("id_wallet"), rs.getString("name"), rs.getString("walletCategory"), rs.getInt("totalAmount"));
            wallets.add(wallet);

        }

		return wallets;
    }


}

