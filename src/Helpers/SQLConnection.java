package src.Helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
This is a singleton class which stores connections for the SQL and allows the objects to create a connection to the database
 */
public class SQLConnection {
    static Connection connection = null;

    public void makeConnection() {
        if(connection == null) {
            try {
                // below two lines are used for connectivity.
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/bank_project",
                        "bankaccess", "testpassword123");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
