/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import util.DB;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList
;import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import operations.CourierOperations;

public class ga140489_CourierOperations implements CourierOperations {

    @Override
    public boolean insertCourier(String string, String string1) {
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean deleteCourier(String courierUserName) {
        
        try {
            int KorisnikID = -1;
            String sql  = "SELECT KorisnikID FROM Korisnik WHERE KorisnickoIme = " + DB.addQuotes(courierUserName);
            Statement stmt = DB.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if(!rs.next())
                return false;
            KorisnikID = rs.getInt("KorisnikID");
            
            sql = "DELETE FROM ? WHERE KorisnikID = ?";
            
            PreparedStatement pstmt = DB.getConnection().prepareStatement(sql);
            pstmt.setString(1, "Kurir");
            pstmt.setInt(2, KorisnikID);
            if(pstmt.executeUpdate()<=0){
                return false;
            }
            pstmt.clearParameters();
            pstmt.setString(1, "Korisnik");
            pstmt.setInt(2, KorisnikID);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException ex) {
            Logger.getLogger(ga140489_CourierOperations.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return false;
    }

    @Override
    public List<String> getCouriersWithStatus(int statusOfCourier) {
        
        List<String> courierList = new ArrayList<>();
        
        try {
            String sql = "SELECT kor.KorisnickoIme FROM Kurir k INNER JOIN Korisnik kor ON kor.KorisnikID=k.KorisnikID WHERE k.Status=?";
            PreparedStatement pst = DB.getConnection().prepareStatement(sql);
            pst.setInt(1, statusOfCourier);
            ResultSet rs = pst.executeQuery();
            
            while(rs.next()){
                courierList.add(rs.getString("KorisnickoIme"));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ga140489_CourierOperations.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return courierList;
    }

    @Override
    public List<String> getAllCouriers() {
        List<String> courierList = new ArrayList<>();
        
        try {
            Statement st = DB.getConnection().createStatement();
            
            ResultSet rs = st.executeQuery("SELECT kor.KorisnickoIme FROM Kurir k INNER JOIN Korisnik kor ON kor.KorisnikID=k.KorisnikID");
            
            while(rs.next()){
                courierList.add(rs.getString("KorisnickoIme"));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ga140489_CourierOperations.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return courierList;
    }

    @Override
    public BigDecimal getAverageCourierProfit(int numberOfDeliveries) {
        BigDecimal avgProfit = BigDecimal.ZERO;
        try {
            String sql = "SELECT AVG(Profit) AS Profit FROM dbo.Kurir WHERE BrojPaketa>=?";
            PreparedStatement pst = DB.getConnection().prepareStatement(sql);
            
            pst.setInt(1, numberOfDeliveries);
            
            ResultSet rs = pst.executeQuery();
            
            if(rs.next()){
                avgProfit = rs.getBigDecimal("Profit");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ga140489_CourierOperations.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return avgProfit;
        
    }
    
}
