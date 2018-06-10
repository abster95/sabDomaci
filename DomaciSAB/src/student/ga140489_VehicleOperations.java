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
import operations.VehicleOperations;

public class ga140489_VehicleOperations implements VehicleOperations{

    @Override
    public boolean insertVehicle(String licencePlateNumber, int fuelType, BigDecimal fuelConsumtion) {
        
        try {
            String sql = "INSERT INTO Vozilo(RegBroj, TipGoriva, Potrosnja) VALUES (?,?,?)";
            PreparedStatement pst = DB.getConnection().prepareStatement(sql);
            pst.setString(1, licencePlateNumber);
            pst.setInt(2, fuelType);
            pst.setBigDecimal(3, fuelConsumtion);
            
            return (pst.executeUpdate()>0);
        } catch (SQLException ex) {
            Logger.getLogger(ga140489_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }

    @Override
    public int deleteVehicles(String... strings) {
        int result = -1;
        
        if(strings!=null && strings.length>0){
            
            StringBuilder sb = new StringBuilder();
            sb.append("DELETE FROM Vozilo WHERE RegBroj IN(");
            for(int i=0; i<strings.length ; i++){
                if(i!=0)sb.append(",");
                
                sb.append("'").append(strings[i]).append("'");
            }
            sb.append(")");
        
            
            try {
                Statement st = DB.getConnection().createStatement();
                result = st.executeUpdate(sb.toString());
            } catch (SQLException ex) {
                Logger.getLogger(ga140489_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
        return result;
    }

    @Override
    public List<String> getAllVehichles() {
        List<String> result = new ArrayList<String>();
        
        
        try {
            String sql = "SELECT RegBroj FROM Vozilo";
            Statement st = DB.getConnection().createStatement();
            
            ResultSet rs = st.executeQuery(sql);
            
            while(rs.next()){
                String rb = rs.getString("RegBroj");
                result.add(rb);
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(ga140489_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return result;
    }

    @Override
    public boolean changeFuelType(String licensePlateNumber, int fuelType) {
        
        try {
            String sql = "UPDATE Vozilo SET TipGoriva=? WHERE RegBroj=?";
            
            PreparedStatement pst = DB.getConnection().prepareStatement(sql);
            pst.setInt(1, fuelType);
            pst.setString(2, licensePlateNumber);
            
            return pst.executeUpdate()>0;
        } catch (SQLException ex) {
            Logger.getLogger(ga140489_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
        
    }

    @Override
    public boolean changeConsumption(String licensePlateNumber, BigDecimal fuelConsumption) {
        try {
            String sql = "UPDATE Vozilo SET Potrosnja=? WHERE RegBroj=?";
            
            PreparedStatement pst = DB.getConnection().prepareStatement(sql);
            pst.setBigDecimal(1, fuelConsumption);
            pst.setString(2, licensePlateNumber);
            
            return pst.executeUpdate()>0;
        } catch (SQLException ex) {
            Logger.getLogger(ga140489_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
}
