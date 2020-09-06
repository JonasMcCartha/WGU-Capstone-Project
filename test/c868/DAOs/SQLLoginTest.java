
package c868.DAOs;

import c868.Exceptions.UserException;
import c868.Main.MainApp;
import c868.Models.User;
import java.io.IOException;
import javafx.collections.ObservableList;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jonas McCartha
 */
public class SQLLoginTest {
    
    MainApp testMain = new MainApp();
    
    public SQLLoginTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
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
     * Test of getAllUsers method, of class SQLLogin.
     */
    @Test
    public void testGetAllUsers() {
        InterfaceLogin SQLLogin = new SQLLogin();

        ObservableList<User> users = SQLLogin.getAllUsers();
        assertEquals(2, users.size());
    }

    /**
     * Test of login method, of class SQLLogin.
     */
    @Test
    public void testLogin() {
        String username = "junitTester";
        String password = "junitTester";
        
        User user = new User();
        user.setUsername("junitTester");
        user.setPassword("junitTester");
        
        InterfaceLogin SQLLogin = new SQLLogin();
        User result = SQLLogin.login(username, password);
        
        assertEquals(user.username, result.username);
    }

    /**
     * Test of updateUser method, of class SQLLogin.
     */
    @Test
    public void testUpdateUser() {
        User user = new User();

        user.setUserId(2);
        user.setPassword("junitUpdateUnitTest");
        InterfaceLogin SQLLogin = new SQLLogin();
        SQLLogin.updateUser(user);
        
        assertEquals(SQLLogin.login("junitTester", "junitUpdateUnitTest").username, "junitTester");
        
        user.setPassword("junitTester");
        SQLLogin.updateUser(user);
    }
    
}
