package smu.DAOImplementation;

import java.sql.*;
import java.util.*;

import smu.Database;
import smu.DAO.BankAccountDAO;
import smu.DTO.BankAccount;

public class BankAccountDAOimp implements BankAccountDAO{

    @Override
    public List<BankAccount> getByEmail(String email) throws SQLException {
        Connection con = Database.getConnection();
        List<BankAccount> result = new ArrayList<>();

        String sql = "SELECT * FROM smu.bankaccount WHERE owneremail = ?";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, email);
        
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            BankAccount bankAccount = new BankAccount(rs.getInt("balance"), rs.getInt("accountnumber"), rs.getString("bank"), rs.getString("ownercf"), rs.getString("owneremail"));
            result.add(bankAccount);
        }

        return result;
    }

    @Override
    public List<BankAccount> getByCF(String CF) throws SQLException {
        Connection con = Database.getConnection();
        List<BankAccount> result = new ArrayList<>();

        String sql = "SELECT * FROM smu.bankaccount WHERE ownercf = ?";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, CF);
        
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            BankAccount bankAccount = new BankAccount(rs.getInt("balance"), rs.getInt("accountnumber"), rs.getString("bank"), rs.getString("ownercf"), rs.getString("owneremail"));
            result.add(bankAccount);
        }

        return result;
    }

}
