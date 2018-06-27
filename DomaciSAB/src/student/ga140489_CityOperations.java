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
import util.DB;

public class ga140489_CityOperations implements CityOperations  {

    @Override
    public int insertCity(String name, String postalCode) {
        int key = -1;
        try {
            String sql = "SELECT * FROM Grad WHERE Naziv = " + DB.addQuotes(name) 
                    + "OR PostanskiBroj = " + DB.addQuotes(postalCode);
            Statement stmt = DB.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next())
                return key;
            
            sql = "INSERT INTO Grad(Naziv, PostanskiBroj) VALUES(?,?)";
            PreparedStatement pstmt = DB.getConnection().prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setString(2, postalCode);
            pstmt.setString(1, name);
            
            pstmt.executeUpdate();

            ResultSet keys = pstmt.getGeneratedKeys();
            
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
                
                sb.append(DB.addQuotes(strings[i]));
            }
            sb.append(")");
 
            try {
                Statement stmt = DB.getConnection().createStatement();
                result = stmt.executeUpdate(sb.toString());
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
            Statement stmt = DB.getConnection().createStatement();
            result = stmt.executeUpdate("DELETE FROM Grad WHERE GradID = " + i) > 0;
        } catch (SQLException ex) {
            Logger.getLogger(ga140489_CityOperations.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        
        return result;
    }

    @Override
    public List<Integer> getAllCities() {
        List<Integer> cityList = new ArrayList<>();
        
        try {
            Statement stmt = DB.getConnection().createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT GradID FROM Grad");
            
            while(rs.next()){
                cityList.add(rs.getInt("GradID"));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ga140489_CityOperations.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return cityList;
    }
    
}
