/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import DB.DB;
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
            String sql = "INSERT INTO Korisnik(Ime, Prezime, KorisnickoIme, Lozinka, Jmbg, TipKorisnika, BrojPoslatihPaketa) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement pst = DB.getConnection().prepareStatement(sql);
            pst.setString(1, firstName);
            pst.setString(2, lastName);
            pst.setString(3, userName);
            pst.setString(4, password);
            pst.setString(5, "");
            pst.setInt(6,1);
            pst.setInt(7, 0);
            return (pst.executeUpdate()>0);
        } catch (SQLException ex) {
            Logger.getLogger(ga140489_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }

    @Override
    public int declareAdmin(String userName) {
        try {
            //Check if it exist
            String sql = "SELECT * FROM Korisnik WHERE KorisnickoIme=?";
            
            PreparedStatement pst = DB.getConnection().prepareStatement(sql);
            
            pst.setString(1, userName);
            
            ResultSet rs = pst.executeQuery();
            
            
            if(!rs.next()){
                //username doesn't exist
                return 2;
            }
            
            //check if its already admin
            
            sql = "SELECT * FROM Korisnik WHERE KorisnickoIme=? AND TipKorisnika=2";
            
            pst = DB.getConnection().prepareStatement(sql);
            
            pst.setString(1, userName);
            
            rs = pst.executeQuery();
            
            if(rs.next()){
                //already exist
                return 1;
            }
            
            //update to admin
            
            sql = "UPDATE Korisnik SET TipKorisnika=2 WHERE KorisnickoIme=?";
            
            pst = DB.getConnection().prepareStatement(sql);
            pst.setString(1, userName);
            return (pst.executeUpdate()>0) ? 0:2;
        } catch (SQLException ex) {
            Logger.getLogger(ga140489_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return 2;
    }

    @Override
    public Integer getSentPackages(String... userNames) {
        int result = 0;
        
        if(userNames!=null && userNames.length>0){
            
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT SUM(BrojPaketa) AS BrojPaketa FROM Korisnik WHERE KorisnickoIme IN(");
            for(int i=0; i<userNames.length ; i++){
                if(i!=0)sb.append(",");
                
                sb.append("'").append(userNames[i]).append("'");
            }
            sb.append(")");
        
            
            try {
                Statement st = DB.getConnection().createStatement();
                ResultSet rs = st.executeQuery(sb.toString());
                
                if(rs.next()){
                    result = rs.getInt("BrojPaketa");
                }
            } catch (SQLException ex) {
                Logger.getLogger(ga140489_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
        return result;
    }

    @Override
    public int deleteUsers(String... strings) {
        int result = -1;
        
        if(strings!=null && strings.length>0){
            
            StringBuilder sb = new StringBuilder();
            sb.append("DELETE FROM Korisnik WHERE KorisnickoIme IN(");
            for(int i=0; i<strings.length ; i++){
                if(i!=0)sb.append(",");
                
                sb.append("'").append(strings[i]).append("'");
            }
            sb.append(")");
        
            
            try {
                Statement st = DB.getConnection().createStatement();
                result = st.executeUpdate(sb.toString());
            } catch (SQLException ex) {
                Logger.getLogger(ga140489_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
        return result;
    }

    @Override
    public List<String> getAllUsers() {
        List<String> result = new ArrayList<String>();
        
        
        try {
            String sql = "SELECT KorisnickoIme FROM Korisnik";
            Statement st = DB.getConnection().createStatement();
            
            ResultSet rs = st.executeQuery(sql);
            
            while(rs.next()){
                String rb = rs.getString("KorisnickoIme");
                result.add(rb);
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(ga140489_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return result;
    }
    
}
