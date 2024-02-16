package smu;

import java.sql.*;

public class Database {

    public static Connection DBlink;

    public static Connection getConnection() throws SQLException{
 
        try {

            Class.forName("org.postgresql.Driver");
            DBlink = DriverManager.getConnection("jdbc:postgresql://localhost:5432/saving_money_unina", "postgres", "postgres");
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        return DBlink;
    }

}
