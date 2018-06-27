package student;

//import Test.operations.*;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import operations.CityOperations;
import operations.CourierOperations;
import operations.CourierRequestOperation;
import operations.DistrictOperations;
import operations.GeneralOperations;
import operations.PackageOperations;
import operations.UserOperations;
import operations.VehicleOperations;
import tests.TestHandler;
import tests.TestRunner;


public class StudentMain {

    public static void main(String[] args) {
        
       
        CityOperations cityOperations = new ga140489_CityOperations(); // Change this to your implementation.
        DistrictOperations districtOperations = new ga140489_DistrictOperations(); // Do it for all classes.
        CourierOperations courierOperations = new ga140489_CourierOperations(); // e.g. = new MyDistrictOperations();
        CourierRequestOperation courierRequestOperation = new ga140489_CourierRequestOperation();
        GeneralOperations generalOperations = new ga140489_GeneralOperations();
        UserOperations userOperations = new ga140489_UserOperations();
        VehicleOperations vehicleOperations = new ga140489_VehicleOperations();
        PackageOperations packageOperations = new ga140489_PackageOperations();

        tests.TestHandler.createInstance(
                cityOperations,
                courierOperations,
                courierRequestOperation,
                districtOperations,
                generalOperations,
                userOperations,
                vehicleOperations,
                null);

        TestRunner.runTests();     
    }
}
