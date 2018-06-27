/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import operations.GeneralOperations;
import util.DB;

/**
 *
 * @author Abi
 */
public class ga140489_GeneralOperations  implements GeneralOperations{

    @Override
    public void eraseAll() {
        
        try {
            String sql = "DELETE FROM Ponuda";
            Statement pst = DB.getConnection().createStatement();
            pst.executeUpdate(sql);
            
            sql = "DELETE FROM Paket";
            pst = DB.getConnection().createStatement();
            pst.executeUpdate(sql);
            
            
            sql = "DELETE FROM ZahtevZaPrevoz";
            pst = DB.getConnection().createStatement();
            pst.executeUpdate(sql);
            
            
            sql = "DELETE FROM Opstina";
            pst = DB.getConnection().createStatement();
            pst.executeUpdate(sql);
            
            sql = "DELETE FROM Grad";
            pst = DB.getConnection().createStatement();
            pst.executeUpdate(sql);
            
            sql = "DELETE FROM ZahtevZaKurira";
            pst = DB.getConnection().createStatement();
            pst.executeUpdate(sql);
            
            sql = "DELETE FROM Kurir";
            pst = DB.getConnection().createStatement();
            pst.executeUpdate(sql);
            
            sql = "DELETE FROM Vozilo";
            pst = DB.getConnection().createStatement();
            pst.executeUpdate(sql);
            
            sql = "DELETE FROM Korisnik";
            pst = DB.getConnection().createStatement();
            pst.executeUpdate(sql);
            
            
        } catch (SQLException ex) {
            Logger.getLogger(ga140489_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}