/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import util.DB;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import operations.UserOperations;

public class ga140489_UserOperations implements UserOperations{

    @Override
    public boolean insertUser(String userName, String firstName, String lastName, String password) {
        try {
            
            // can't have two same usernames
            String sql = "SELECT * FROM Korisnik WHERE KorisnickoIme = " + DB.addQuotes(userName);
            Statement stmt = DB.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next())
                return false;
           
            // it's ok to put it in
            sql = "INSERT INTO Korisnik(Ime, Prezime, KorisnickoIme, Lozinka, TipKorisnika, PoslatiPaketi) VALUES (?,?,?,?,?,?)";
            PreparedStatement pstmt = DB.getConnection().prepareStatement(sql);
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, userName);
            pstmt.setString(4, password);
            pstmt.setInt(5,1);
            pstmt.setInt(6, 0);
            return (pstmt.executeUpdate()>0);
        } catch (SQLException ex) {
            Logger.getLogger(ga140489_UserOperations.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        
        return false;
    }

    @Override
    public int declareAdmin(String userName) {
        try {
            // Check if username exists
            String sql = "SELECT * FROM Korisnik WHERE KorisnickoIme = " + DB.addQuotes(userName);
            Statement stmt = DB.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if(!rs.next())
                return 2;
            
            // check if they're already admin
            sql = "SELECT * FROM Korisnik WHERE KorisnickoIme = ? AND TipKorisnika = 2";
            
            PreparedStatement pstmt = DB.getConnection().prepareStatement(sql);
            pstmt.setString(1, userName);
            rs = pstmt.executeQuery();
            if(rs.next()){
                // user already an admin
                return 1;
            }
            // promote to admin         
            sql = "UPDATE Korisnik SET TipKorisnika=2 WHERE KorisnickoIme=?";
            
            pstmt = DB.getConnection().prepareStatement(sql);
            pstmt.setString(1, userName);
            return (pstmt.executeUpdate()>0) ? 0:2;
        } catch (SQLException ex) {
            Logger.getLogger(ga140489_UserOperations.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        
        return 2;
    }

    @Override
    public Integer getSentPackages(String... userNames) {
        int result = 0;
        
        if(userNames!=null && userNames.length>0){
            
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT ? FROM Korisnik WHERE KorisnickoIme IN(");
            for(int i=0; i<userNames.length ; i++){
                if(i!=0)sb.append(",");
                
                sb.append(DB.addQuotes(userNames[i]));
            }
            sb.append(")");
            String sql = sb.toString();
            try {
                PreparedStatement pstmt = DB.getConnection().prepareStatement(sql);
                pstmt.setString(1, "*");
                ResultSet rs = pstmt.executeQuery();
                if(!rs.next())
                    return null;
                
                PreparedStatement pstmt1 = DB.getConnection().prepareStatement(sql);
                pstmt1.setString(1, "SUM(PoslatiPaketi) AS BrojPaketa");
                ResultSet rs1 = pstmt1.executeQuery();
                if(rs.next()){
                    result = rs.getInt("BrojPaketa");
                }
            } catch (SQLException ex) {
                Logger.getLogger(ga140489_CityOperations.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
            
        }
        
        return result;
    }

    @Override
    public int deleteUsers(String... strings) {
        int ret = -1;
        
        if(strings!=null && strings.length>0){
            
            StringBuilder sb = new StringBuilder();
            sb.append("DELETE FROM Korisnik WHERE KorisnickoIme IN(");
            for(int i=0; i<strings.length ; i++){
                if(i!=0)sb.append(",");
                
                sb.append(DB.addQuotes(strings[i]));
            }
            sb.append(")");

            try {
                Statement stmt = DB.getConnection().createStatement();
                ret = stmt.executeUpdate(sb.toString());
            } catch (SQLException ex) {
                Logger.getLogger(ga140489_UserOperations.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
            
        }
        
        return ret;
    }

    @Override
    public List<String> getAllUsers() {
        List<String> result = new ArrayList<>();
        
        
        try {
            String sql = "SELECT KorisnickoIme FROM Korisnik";
            Statement stmt = DB.getConnection().createStatement();
            
            ResultSet rs = stmt.executeQuery(sql);
            
            while(rs.next()){
                result.add(rs.getString("KorisnickoIme"));
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(ga140489_UserOperations.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        
        
        return result;
    }
    
}
