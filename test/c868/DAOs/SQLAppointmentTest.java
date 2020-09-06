
package c868.DAOs;

import c868.Exceptions.UserException;
import c868.Main.MainApp;
import c868.Models.Appointment;
import c868.Models.Patient;
import c868.Models.User;
import java.io.IOException;
import java.time.LocalDateTime;
import javafx.collections.ObservableList;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jonas McCartha
 */
public class SQLAppointmentTest {
    
    MainApp testMain = new MainApp();
        
    
    public SQLAppointmentTest() {
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
     * Test of addAppointment method, of class SQLAppointment.
     */
    @Test
    public void testAddAppointment() {
        Patient patient = new Patient();
        patient.setPatientId(1);
        patient.setPatientName("testAddAppointmentPatient");
        
        User user = new User();
        user.setUserId(1);
        user.setUsername("testAddAppointmentUser");
        
        Appointment appointment = new Appointment();
        appointment.setTitle("junitTest");
        appointment.setPatient(patient);
        appointment.setUser(user);
        appointment.setDesc("junitTest");
        
        // add the test appointment
        InterfaceAppointment SQLAppointment = new SQLAppointment();
        int appointmentId = SQLAppointment.addAppointment(appointment);
        
        assertEquals(appointmentId, SQLAppointment.getMaxId() - 1);
        
        // clean up the added test appointment after testing
        appointment.setAppointmentId(SQLAppointment.getMaxId() - 1);
        SQLAppointment.removeAppointment(appointment);
    }

    /**
     * Test of getAllAppointments method, of class SQLAppointment.
     */
    @Test
    public void testGetAllAppointments() {
        InterfaceAppointment SQLAppointment = new SQLAppointment();

        ObservableList<Appointment> appointments = SQLAppointment.getAllAppointments();
        assertEquals(SQLAppointment.getMaxId() - 1, appointments.size());
    }

    /**
     * Test of getAppointmentsInRange method, of class SQLAppointment.
     */
    @Test
    public void testGetAppointmentsInRange() {
        InterfaceAppointment SQLAppointment = new SQLAppointment();
        
        LocalDateTime start = LocalDateTime.of(2020, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2020, 12, 31, 0, 0);
        
        ObservableList<Appointment> appointments = SQLAppointment.getAppointmentsInRange(start, end);
        ObservableList<Appointment> control = SQLAppointment.getAllAppointments();
        // appointments variable won't include test appointments for non-2020 dates
        assertEquals(appointments.size(), control.size() - 1);
    }

    /**
     * Test of getOverlappingAppointments method, of class SQLAppointment.
     */
    @Test
    public void testGetOverlappingAppointments() {
        InterfaceAppointment SQLAppointment = new SQLAppointment();
        
        LocalDateTime start = LocalDateTime.of(2020, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2020, 12, 31, 0, 0);
        
        ObservableList<Appointment> appointments = SQLAppointment.getOverlappingAppointments(start, end);
        ObservableList<Appointment> control = SQLAppointment.getAllAppointments();
        // appointments variable won't include test appointments for non-2020 dates
        assertEquals(appointments.size(), control.size() - 1);
    }

    /**
     * Test of removeAppointment method, of class SQLAppointment.
     */
    @Test
    public void testRemoveAppointment() {
        Patient patient = new Patient();
        patient.setPatientId(1);
        patient.setPatientName("testRemoveAppointmentPatient");
        
        User user = new User();
        user.setUserId(1);
        user.setUsername("testRemoveAppointmentUser");
        
        Appointment appointment = new Appointment();
        appointment.setTitle("junitTest");
        appointment.setPatient(patient);
        appointment.setUser(user);
        appointment.setDesc("junitTest");
        
        // add a test appointment to remove and put size of appointment table in variable
        InterfaceAppointment SQLAppointment = new SQLAppointment();
        SQLAppointment.addAppointment(appointment);
        int appointmentsPlusOne = SQLAppointment.getAllAppointments().size();

        // remove the added appointment and put size of appointment table in second variable
        appointment.setAppointmentId(SQLAppointment.getMaxId() - 1);
        SQLAppointment.removeAppointment(appointment);
        int appointmentsControl = SQLAppointment.getAllAppointments().size();
        
        assertEquals(appointmentsPlusOne, appointmentsControl + 1);
    }

    /**
     * Test of updateAppointment method, of class SQLAppointment.
     */
    @Test
    public void testUpdateAppointment() {
        Patient patient = new Patient();
        patient.setPatientId(1);
        patient.setPatientName("testUpdateAppointmentPatient");
        
        User user = new User();
        user.setUserId(1);
        user.setUsername("testUpdateAppointmentUser");
        
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(1);
        appointment.setTitle("junitUpdateAppointmentTest");
        appointment.setPatient(patient);
        appointment.setUser(user);
        
        InterfaceAppointment SQLAppointment = new SQLAppointment();
        SQLAppointment.updateAppointment(appointment);
        
        assertEquals(appointment.getTitle(), "junitUpdateAppointmentTest");
        
        appointment.setTitle("Test Appointment");
        SQLAppointment.updateAppointment(appointment);
    }    
}
