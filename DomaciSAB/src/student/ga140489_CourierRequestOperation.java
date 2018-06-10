/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import DB.DB;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import operations.CourierRequestOperation;

public class ga140489_CourierRequestOperation implements CourierRequestOperation {

    @Override
    public boolean insertCourierRequest(String userName, String licencePlateNumber) {
        
        try {
            
            int VoziloID,KorisnikID;
            
            //get userID
            String sql = "SELECT KorisnikID FROM Korisnik WHERE KorisnickoIme=?";
            PreparedStatement pst = DB.getConnection().prepareStatement(sql);
            
            pst.setString(1, userName);
            
            ResultSet rs = pst.executeQuery();
            
            if(rs.next()){
                KorisnikID = rs.getInt("KorisnikID");
            }else{
                return false;
            }
            
            
            //get vehicle
            
            sql = "SELECT VoziloID FROM Vozilo WHERE RegBroj=?";
            pst = DB.getConnection().prepareStatement(sql);
            
            pst.setString(1, licencePlateNumber);
            
            rs = pst.executeQuery();
            
            if(rs.next()){
                VoziloID = rs.getInt("VoziloID");
            }else{
                return false;
            }
            
            //insert request
            sql = "INSERT INTO ZahtevZaKurira(KorisnikID,VoziloID) VALUES (?,?)";
            pst = DB.getConnection().prepareStatement(sql);
            
            pst.setInt(1, KorisnikID);
            pst.setInt(2, VoziloID);
            
            return pst.executeUpdate()>0;
            
            
        } catch (SQLException ex) {
            Logger.getLogger(ga140489_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
        
    }

    @Override
    public boolean deleteCourierRequest(String userName) {
        
        try {
            //get userID
            int KorisnikID;
            String sql = "SELECT KorisnikID FROM Korisnik WHERE KorisnickoIme=?";
            PreparedStatement pst = DB.getConnection().prepareStatement(sql);
            
            pst.setString(1, userName);
            
            ResultSet rs = pst.executeQuery();
            
            if(rs.next()){
                KorisnikID = rs.getInt("KorisnikID");
            }else{
                return false;
            }
            
            
            //delete
            sql = "DELETE FROM ZahtevZaKurira WHERE KorisnikID=?";
            pst = DB.getConnection().prepareStatement(sql);
            pst.setInt(1, KorisnikID);
            
            return pst.executeUpdate()>0;
            
            
        } catch (SQLException ex) {
            Logger.getLogger(ga140489_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean changeVehicleInCourierRequest(String userName, String licencePlateNumber) {
        
        
        try {
            //get userID
            int KorisnikID;
            String sql = "SELECT KorisnikID FROM Korisnik WHERE KorisnickoIme=?";
            PreparedStatement pst = DB.getConnection().prepareStatement(sql);
            
            pst.setString(1, userName);
            
            ResultSet rs = pst.executeQuery();
            
            if(rs.next()){
                KorisnikID = rs.getInt("KorisnikID");
            }else{
                return false;
            }
            
            //get vehicle
            
            sql = "SELECT VoziloID FROM Vozilo WHERE RegBroj=?";
            pst = DB.getConnection().prepareStatement(sql);
            
            pst.setString(1, userName);
            
            rs = pst.executeQuery();
            
            if(rs.next()){
                KorisnikID = rs.getInt("KorisnikID");
            }else{
                return false;
            }
            
            
            
            
            
            
            
            sql = "UPDATE ZahtevZaKurira SET RegBroj=? WHERE KorisnikID=?";
            pst = DB.getConnection().prepareStatement(sql);
            pst.setString(1, licencePlateNumber);
            pst.setInt(2, KorisnikID);
            
            return pst.executeUpdate()>0;
            
            
        } catch (SQLException ex) {
            Logger.getLogger(ga140489_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }

    @Override
    public List<String> getAllCourierRequests() {
        
        List<String> result = new ArrayList<String>();
        
        try {
            String sql = "SELECT k.KorisnickoIme  FROM ZahtevZaKurira zzk INNER JOIN Korisnik k ON k.KorisnikID=zzk.KorisnikID";
            Statement st = DB.getConnection().createStatement();
            
            ResultSet rs = st.executeQuery(sql);
            
            while(rs.next()){
                String rb = rs.getString("KorisnickoIme");
                result.add(rb);
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(ga140489_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
    }

    @Override
    public boolean grantRequest(String username) {
        
        try {
            //get userID
            int KorisnikID,VoziloID;
            String sql = "SELECT zzk.KorisnikID, zzk.VoziloID FROM Korisnik k INNER JOIN ZahtevZaKurira zzk ON zzk.KorisnikID=k.KorisnikID WHERE KorisnickoIme=?";
            PreparedStatement pst = DB.getConnection().prepareStatement(sql);
            
            pst.setString(1, username);
            
            ResultSet rs = pst.executeQuery();
            
            if(rs.next()){
                KorisnikID = rs.getInt("KorisnikID");
                VoziloID = rs.getInt("VoziloID");
            }else{
                return false;
            }
            
            if(!deleteCourierRequest(username)){
                return false;
            }
            
            sql="INSERT INTO Kurir(Profit, Status, BrojPaketa, KorisnikID, VoziloID) VALUES(?,?,?,?,?)";
            pst = DB.getConnection().prepareStatement(sql);
            pst.setBigDecimal(1, BigDecimal.ZERO);
            pst.setInt(2, 0);
            pst.setInt(3, 0);
            pst.setInt(4, KorisnikID);
            pst.setInt(5, VoziloID);
            
            
            if(pst.executeUpdate()<=0){
                return false;
            }
            
            sql = "UPDATE Korisnik SET TipKorisnika=3 WHERE KorisnikID=?";
            PreparedStatement st = DB.getConnection().prepareStatement(sql);
            st.setInt(1, KorisnikID);
            
            return st.executeUpdate()>0;
            
            
        } catch (SQLException ex) {
            Logger.getLogger(ga140489_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
}
