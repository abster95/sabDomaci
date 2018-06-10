/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DB {

    private static String serverName = "localhost";
    private static String instanceName = "DESKTOP-L5V19S9";
    private static int portNumber = 1433;
    private static String database = "mi140392";
    private static String username = "sa";
    private static String password = "ikacikacikac";
    private static String connectionString = "jdbc:sqlserver://" + serverName + "\\" + instanceName + ":" + portNumber + ";databaseName=" + database + ";username=" + username + ";password=" + password;

    private static Connection connection;

    static {
        try {
            connection = DriverManager.getConnection(connectionString);
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(connectionString);
            } catch (SQLException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return connection;
    }
}
