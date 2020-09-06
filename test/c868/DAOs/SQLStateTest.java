
package c868.DAOs;

import c868.Exceptions.UserException;
import c868.Models.State;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import c868.Main.MainApp;
import java.io.IOException;

/**
 *
 * @author Jonas McCartha
 */
public class SQLStateTest {
    
    MainApp testMain = new MainApp();
        
    
    public SQLStateTest() {
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
     * Test of addState method, of class SQLState.
     */
    @Test
    public void testAddState() {
        State state = new State();
        state.setStateName("junitTest");
        
        InterfaceState SQLState = new SQLState();
        int stateId = SQLState.addState(state);
        
        assertEquals(SQLState.getState(stateId).stateName, "junitTest");
    }

    /**
     * Test of getState method, of class SQLState.
     */
    @Test
    public void testGetState() {
        int stateId = 1;
        InterfaceState SQLState = new SQLState();
        State result = SQLState.getState(stateId);
        assertEquals(1, result.getStateId());
    }

    /**
     * Test of updateState method, of class SQLState.
     */
    @Test
    public void testUpdateState() {
        State state = new State();

        state.setStateId(1);
        state.setStateName("junitTest");
        InterfaceState SQLState = new SQLState();
        SQLState.updateState(state);
        
        assertEquals(SQLState.getState(1).stateName, "junitTest");
        
        //set state name back to initial value
        state.setStateName("test");
        SQLState.updateState(state);
    }    
}
