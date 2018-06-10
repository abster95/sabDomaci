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
import operations.DistrictOperations;

public class ga140489_DistrictOperations implements DistrictOperations {

    @Override
    public int insertDistrict(String name, int cityId, int xCord, int yCord) {
        int key = -1;
        
        try {
            String sql = "INSERT INTO Opstina(Naziv,XKord,YKord,GradID) VALUES (?,?,?,?)";
            PreparedStatement pst = DB.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, name);
            pst.setInt(2, cityId);
            pst.setBigDecimal(3, BigDecimal.valueOf(xCord));
            pst.setBigDecimal(4, BigDecimal.valueOf(yCord));
            
            
            int rowsChanged = pst.executeUpdate();
            
            if(rowsChanged>0){
                ResultSet keys = pst.getGeneratedKeys();
                
                while(keys.next()){
                    key = keys.getInt(1);
                }
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(ga140489_DistrictOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return key;
    }

    @Override
    public int deleteDistricts(String... strings) {
        
        int result = -1;
        
        if(strings!=null && strings.length>0){
            
            StringBuilder sb = new StringBuilder();
            sb.append("DELETE FROM Opstina WHERE Naziv IN(");
            for(int i=0; i<strings.length ; i++){
                if(i!=0)sb.append(",");
                
                sb.append("'").append(strings[i]).append("'");
            }
            sb.append(")");
        
            
            try {
                Statement st = DB.getConnection().createStatement();
                result = st.executeUpdate(sb.toString());
            } catch (SQLException ex) {
                Logger.getLogger(ga140489_DistrictOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
        return result;
    }

    @Override
    public boolean deleteDistrict(int i) {
        boolean result= false;
        
        try {
            String sql = "DELETE FROM Opstina WHERE OpstinaID=?";
            PreparedStatement pst = DB.getConnection().prepareStatement(sql);
            pst.setInt(1, i);
            
            result = pst.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(ga140489_DistrictOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
    }

    @Override
    public int deleteAllDistrictsFromCity(String string) {
        int result=0;
        try {
            String sql = "DELETE o FROM Opstina o INNER JOIN Grad g on g.GradID=o.GradID WHERE g.Naziv=?";
            PreparedStatement pst = DB.getConnection().prepareStatement(sql);
            pst.setString(1, string);
            
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ga140489_DistrictOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
         
        return result;
    }

    @Override
    public List<Integer> getAllDistrictsFromCity(int i) {
        List<Integer> result = new ArrayList<Integer>();
        
        
        try {
            String sql = "SELECT OpstinaID, GradID FROM Opstina WHERE GradID=?";
            PreparedStatement pst = DB.getConnection().prepareStatement(sql);
            pst.setString(1, Integer.toString(i));
            ResultSet rs = pst.executeQuery();
            
            while(rs.next()){
                int id = rs.getInt("OpstinaID");
                result.add(id);
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(ga140489_DistrictOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return result;
        
    }

    @Override
    public List<Integer> getAllDistricts() {
        List<Integer> result = new ArrayList<Integer>();
        
        
        try {
            String sql = "SELECT OpstinaID FROM Opstina";
            Statement st = DB.getConnection().createStatement();
            
            ResultSet rs = st.executeQuery(sql);
            
            while(rs.next()){
                int id = rs.getInt("OpstinaID");
                result.add(id);
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(ga140489_DistrictOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return result;
    }
    
}
