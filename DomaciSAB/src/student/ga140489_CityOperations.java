/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import operations.*;
import DB.DB;

public class ga140489_CityOperations implements CityOperations  {

    @Override
    public int insertCity(String name, String postalCode) {
        int key = -1;
        try {
            String sql = "INSERT INTO Grad(PostanskiBroj, Naziv) VALUES(?,?)";
            PreparedStatement pst = DB.getConnection().prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            
            pst.setString(1, postalCode);
            pst.setString(2, name);
            
            
            
            int affectedRows = pst.executeUpdate();
            ResultSet keys = pst.getGeneratedKeys();
            
            
            while(keys.next()){
                key = keys.getInt(1);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ga140489_CityOperations.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        
        
        return key;
    }

    @Override
    public int deleteCity(String... strings) {
        int result = -1;
        
        if(strings!=null && strings.length>0){
            
            StringBuilder sb = new StringBuilder();
            sb.append("DELETE FROM Grad WHERE Naziv IN(");
            for(int i=0; i<strings.length ; i++){
                if(i!=0)sb.append(",");
                
                sb.append("'").append(strings[i]).append("'");
            }
            sb.append(")");
        
            
            try {
                Statement st = DB.getConnection().createStatement();
                result = st.executeUpdate(sb.toString());
            } catch (SQLException ex) {
                Logger.getLogger(ga140489_CityOperations.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
            
        }
        
        return result;
        
    }

    @Override
    public boolean deleteCity(int i) {
        boolean result= false;
        
        try {
            Statement st = DB.getConnection().createStatement();
            result = st.executeUpdate("DELETE FROM Grad WHERE GradID=" + i) > 0;
        } catch (SQLException ex) {
            Logger.getLogger(ga140489_CityOperations.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        
        return result;
    }

    @Override
    public List<Integer> getAllCities() {
        List<Integer> cityList = new ArrayList<>();
        
        try {
            Statement st = DB.getConnection().createStatement();
            
            ResultSet rs = st.executeQuery("SELECT GradID FROM Grad");
            
            while(rs.next()){
                int id = rs.getInt("GradID");
                cityList.add(id);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ga140489_CityOperations.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return cityList;
    }
    
}
