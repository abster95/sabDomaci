package student;

//import Test.operations.*;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import tests.TestHandler;
import tests.TestRunner;


public class StudentMain {

    public static void main(String[] args) {
        
       
        ga140489_UserOperations uo = new ga140489_UserOperations();
        ga140489_CourierRequestOperation cr = new ga140489_CourierRequestOperation();
        ga140489_VehicleOperations vo = new ga140489_VehicleOperations();
        
        /*
        System.out.println(uo.insertUser("ikac", "Ivan", "Milenkovic", "pass"));
        
        System.out.println(uo.declareAdmin("ikac"));
        System.out.println(uo.insertUser("mikica", "Milana", "Djordjevic", "pass"));
        srintln(vo.insertVehicle("1234A", 1, new BigDecimal(13.5)));
        
        System.out.println(cr.insertCourierRequest("mikica", "1234A"));
        
        System.out.println(cr.grantRequest("mikica"));
        */
        
        System.out.println(cr.changeVehicleInCourierRequest("mikica", "AB"));
        
        
    }
}
