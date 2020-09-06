package c868.DAOs;

import c868.Exceptions.UserException;
import c868.Main.MainApp;
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
public class SQLCityTest {
    
    MainApp testMain = new MainApp();
        
    
    public SQLCityTest() {
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
     * Test of addCity method, of class SQLCity.
     */
    @Test
    public void testAddCity() {
        State state = new State();
        state.setStateId(1);
        state.setStateName("testState");
        
        City city = new City();
        city.setCityName("junitTest");
        city.setState(state);
        
        InterfaceCity SQLCity = new SQLCity();
        int cityId = SQLCity.addCity(city);
        
        assertEquals(SQLCity.getCity(cityId).cityName, "junitTest");
    }

    
    /**
     * Test of getCity method, of class SQLCity.
     */
    @Test
    public void testGetCity() {
        int cityId = 1;
        InterfaceCity SQLCity = new SQLCity();
        City result = SQLCity.getCity(cityId);
        assertEquals(1, result.getCityId());
    }


    /**
     * Test of updateCity method, of class SQLCity.
     */
    @Test
    public void testUpdateCity() {
        State state = new State();
        state.setStateId(1);
        state.setStateName("testState");
        
        City city = new City();
        city.setCityId(1);
        city.setCityName("junitTest");
        city.setState(state);
        
        InterfaceCity SQLCity = new SQLCity();
        SQLCity.updateCity(city);
        
        assertEquals(SQLCity.getCity(1).cityName, "junitTest");
        
        //set test city name back to initial value
        city.setCityName("testCity");
        SQLCity.updateCity(city);
    }
    
}
