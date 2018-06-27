/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DB {

    private static String serverName = "localhost";
    private static String instanceName = "DESKTOP-NQTM9QP";
    private static int portNumber = 1434;
    private static String database = "ga140489";
    private static String username = "sa";
    private static String password = "12345678";
    private static String connectionString = "jdbc:sqlserver://" + serverName + 
                            "\\" + instanceName + ":" + portNumber + ";databaseName=" + database 
                            + ";username=" + username + ";password=" + password;

    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(connectionString);
            } catch (SQLException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        return connection;
    }
    
        public static String addQuotes(String str){
        return "'" + str + "'";
    }
}
