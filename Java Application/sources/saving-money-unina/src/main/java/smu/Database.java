package smu;

import java.sql.*;

public class Database {

    //Instance
    private static Connection instance;

    public static Connection getConnection() throws SQLException{

        if(instance == null){
            try {

                Class.forName("org.postgresql.Driver");
                instance = DriverManager.getConnection("jdbc:postgresql://localhost:5432/saving_money_unina", "postgres", "postgres");
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return instance;
    }

}
