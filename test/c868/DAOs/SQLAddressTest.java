package c868.DAOs;

import c868.Exceptions.UserException;
import c868.Main.MainApp;
import c868.Models.Address;
import c868.Models.City;
import c868.Models.State;
import java.io.IOException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jonas McCartha
 */
public class SQLAddressTest {
    
    MainApp testMain = new MainApp();
        
    
    public SQLAddressTest() {
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
     * Test of addAddress method, of class SQLAddress.
     */
    @Test
    public void testAddAddress() {
        State state = new State();
        state.setStateId(1);
        state.setStateName("testState");
        
        City city = new City();
        city.setCityId(1);
        city.setCityName("testCity");
        city.setState(state);
        
        Address address = new Address();
        address.setAddress1("junitTest");
        address.setCity(city);
        
        InterfaceAddress SQLAddress = new SQLAddress();
        int addressId = SQLAddress.addAddress(address);
        
        assertEquals(SQLAddress.getAddress(addressId).address1, "junitTest");
    }

    /**
     * Test of getAddress method, of class SQLAddress.
     */
    @Test
    public void testGetAddress() {
        int addressId = 1;
        InterfaceAddress SQLAddress = new SQLAddress();
        Address result = SQLAddress.getAddress(addressId);
        assertEquals(1, result.getAddressId());
    }

    /**
     * Test of updateAddress method, of class SQLAddress.
     */
    @Test
    public void testUpdateAddress() {        
        City city = new City();
        city.setCityId(1);
        city.setCityName("testCity");
        
        Address address = new Address();
        address.setAddressId(1);
        address.setAddress1("junitTest");
        address.setCity(city);
                
        InterfaceAddress SQLAddress = new SQLAddress();
        SQLAddress.updateAddress(address);
        
        assertEquals(SQLAddress.getAddress(1).address1, "junitTest");
        
        //set test address name back to initial value
        address.setAddress1("testAddress");
        SQLAddress.updateAddress(address);
    }
    
}
