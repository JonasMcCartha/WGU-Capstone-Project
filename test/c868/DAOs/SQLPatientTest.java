package c868.DAOs;

import c868.Exceptions.UserException;
import c868.Main.MainApp;
import c868.Models.Address;
import c868.Models.Patient;
import java.io.IOException;
import javafx.collections.ObservableList;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jonas McCartha
 */
public class SQLPatientTest {
    
    MainApp testMain = new MainApp();        
    
    public SQLPatientTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
        
    @Before
    public void setUp() throws UserException {
        try {
            testMain.DbConnectionForTesting();
        }
        catch(IOException e) {
        }        
    }
    

    /**
     * Test of addPatient method, of class SQLPatient.
     */
    @Test
    public void testAddPatient() {
        Address address = new Address();
        address.setAddress1("junitTestAddPatient");
        address.setAddressId(1);
        
        Patient patient = new Patient();
        patient.setPatientName("junitTest");
        patient.setAddress(address);
        
        InterfacePatient SQLPatient = new SQLPatient();
        int patientId = SQLPatient.addPatient(patient);
        
        assertEquals(SQLPatient.getPatient(patientId).patientName, "junitTest");
        
        patient.setPatientId(SQLPatient.getMaxId() - 1);
        SQLPatient.removePatient(patient);
    }

    /**
     * Test of getAllPatients method, of class SQLPatient.
     */
    @Test
    public void testGetAllPatients() {
        InterfacePatient SQLPatient = new SQLPatient();

        ObservableList<Patient> patients = SQLPatient.getAllPatients();
        assertEquals(1, patients.size());
    }

    /**
     * Test of getPatient method, of class SQLPatient.
     */
    @Test
    public void testGetPatient() {
        int patientId = 1;
        InterfacePatient SQLPatient = new SQLPatient();
        Patient result = SQLPatient.getPatient(patientId);
        assertEquals(1, result.getPatientId());
    }

    /**
     * Test of removePatient method, of class SQLPatient.
     */
    @Test
    public void testRemovePatient() {
        Address address = new Address();
        address.setAddressId(1);
        address.setAddress1("junitTestRemovePatient");
        
        Patient patient = new Patient();
        patient.setPatientName("junitTest");
        patient.setAddress(address);
        
        // add a test patient to remove and put size of patient table in variable
        InterfacePatient SQLPatient = new SQLPatient();
        SQLPatient.addPatient(patient);
        int patientsPlusOne = SQLPatient.getAllPatients().size();

        // remove the added patient and put size of patient table in second variable
        patient.setPatientId(SQLPatient.getMaxId() - 1);
        SQLPatient.removePatient(patient);
        int patientsControl = SQLPatient.getAllPatients().size();
        
        assertEquals(patientsPlusOne, patientsControl + 1);
    }

    /**
     * Test of updatePatient method, of class SQLPatient.
     */
    @Test
    public void testUpdatePatient() {
        Address address = new Address();
        address.setAddress1("junitTestUpdatePatient");
        address.setAddressId(1);
        
        Patient patient = new Patient();
        patient.setPatientId(1);
        patient.setPatientName("junitTest");
        patient.setAddress(address);
        
        InterfacePatient SQLPatient = new SQLPatient();
        SQLPatient.updatePatient(patient);
        
        assertEquals(SQLPatient.getPatient(1).patientName, "junitTest");
        
        //set patient name back to initial value
        patient.setPatientName("test");
        SQLPatient.updatePatient(patient);
    }    
}
