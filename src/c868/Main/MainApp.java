package c868.Main;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.*;

import c868.DAOs.*;
import c868.Models.*;
import c868.Exceptions.UserException;
import c868.Controllers.LoginScreenController;
import c868.Controllers.MainController;
import java.io.FileNotFoundException;
import static javafx.application.Application.launch;


/**
 *
 * @author Jonas McCartha
 */

public class MainApp extends Application {
    private Stage stage;
    private AnchorPane root;
    private static String DRIVER;
    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;
    public static Connection conn = null;
    public static User user;
    static private InterfaceLogin SQLLogin;
    
   
    // Get database connection.
    private static void getDbConnection() throws IOException {
        getDbCredentials();
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } 
        catch(ClassNotFoundException | SQLException ex){
            System.out.println(ex.getMessage());
        } 
    }
    
        
    // Get database credentials.
    private static void getDbCredentials() throws FileNotFoundException, IOException {
        Properties prop = new Properties();
	InputStream propFile = new FileInputStream("db.properties");
        prop.load(propFile);
        DRIVER = prop.getProperty("dbdriver");
        URL = prop.getProperty("dburl");
        USERNAME = prop.getProperty("dbuser");
        PASSWORD = prop.getProperty("dbpassword");

    }
    
    
    // Establish logged-in user.
    public void setUser(User loggedInUser) throws IOException, UserException {
        user = loggedInUser;
        if (user != null) {
            showMainScreen();
        } else {
            showLoginScreen();
        }
    }
    
    
    // Main method
    public static void main(String[] args) throws IOException {
        getDbConnection();
        SQLLogin = new SQLLogin();
        launch(args);
    }
    
    /**
     *
     * @throws IOException
     * @throws c868.Exceptions.UserException
     */
    public void DbConnectionForTesting() throws IOException, UserException {
        getDbConnection();
        InterfaceLogin userDAO = new SQLLogin();
        User testUser = userDAO.login("admin", "password");
        user = testUser;
    }
    
    
    // Load login screen.
    public void showLoginScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("/c868/Views/LoginScreen.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        LoginScreenController controller = loader.getController();     
        controller.bind(this);
        stage.show();  
    }
    
    
    // Load main screen.
    private void showMainScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("/c868/Views/Main.fxml"));
        root = (AnchorPane)loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        MainController controller = loader.getController();
        controller.bind(stage);
        stage.show();
    }
    
        
    // Start FXML.
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        showLoginScreen();
    }
}