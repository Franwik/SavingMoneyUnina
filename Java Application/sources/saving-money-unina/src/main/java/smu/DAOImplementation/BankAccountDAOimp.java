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
            BankAccount bankAccount = new BankAccount(rs.getInt("balance"), rs.getInt("accountnumber"), rs.getString("bank"), rs.getString("owneremail"), rs.getString("ownercf"));
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
            BankAccount bankAccount = new BankAccount(rs.getInt("balance"), rs.getInt("accountnumber"), rs.getString("bank"), rs.getString("owneremail"), rs.getString("ownercf"));
            result.add(bankAccount);
        }

        return result;
    }

    @Override
    public int insert(BankAccount bankAccount) throws SQLException {
        Connection con = Database.getConnection();

        String sql = "INSERT INTO smu.bankaccount (balance, accountnumber, bank, ownercf, owneremail) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, bankAccount.getBalance());
        ps.setInt(2, bankAccount.getAccountNumber());
        ps.setString(3, bankAccount.getBank());
        ps.setString(4, bankAccount.getOwnerCF());
        ps.setString(5, bankAccount.getOwnerEmail());

        int result = ps.executeUpdate();

        return result;
    }

    @Override
    public int update(BankAccount bankAccount) throws SQLException {
        Connection con = Database.getConnection();

        String sql = "UPDATE smu.bankaccount SET balance = ? WHERE accountnumber = ?";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, bankAccount.getBalance());
        ps.setInt(2, bankAccount.getAccountNumber());

        int result = ps.executeUpdate();

        return result;
    }

    @Override
    public int delete(int accountNumber) throws SQLException {
        Connection con = Database.getConnection();

        String sql = "DELETE FROM smu.bankaccount WHERE accountnumber = ?";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, accountNumber);

        int result = ps.executeUpdate();

        return result;
    }
}
