package c868.Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

import c868.Main.MainApp;
import c868.DAOs.InterfaceLogin;
import c868.DAOs.SQLLogin;
import c868.Exceptions.UserException;
import c868.Models.User;


public class LoginScreenController implements Initializable {
    Locale userLocale;
    ResourceBundle rb;
    private MainApp mainApp;
    @FXML
    private TextField LoginUsernameField;
    @FXML
    private PasswordField LoginPasswordField;
    @FXML
    private Label LoginUsernameLabel;
    @FXML
    private Label LoginPasswordLabel;
    @FXML
    private DialogPane MessageBox;
    
    
    // Constructor
    public LoginScreenController(){
    }
        
    
    public void bind(MainApp app) {
        this.mainApp = app;
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.userLocale = Locale.getDefault();
        this.rb = ResourceBundle.getBundle("LoginFields", this.userLocale);
        LoginUsernameLabel.setText(this.rb.getString("username"));
        LoginPasswordLabel.setText(this.rb.getString("password"));
    }

    
    //Handle cancel and exit application.
    @FXML
    void handleCancel(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle(this.rb.getString("confirm_title"));
        alert.setHeaderText(this.rb.getString("confirm_title"));
        alert.setContentText(this.rb.getString("confirm_text"));
        alert.showAndWait()
            .filter(response -> response == ButtonType.OK)
            .ifPresent((ButtonType response) -> {
                System.exit(0);
        });
    }
    
    
    // Handle login attempt.
    @FXML
    void handleLogin(ActionEvent event) throws IOException {
        String loginUsername = LoginUsernameField.getText();
        String loginPassword = LoginPasswordField.getText();
        
        try {        
            User loginUser = new User();
            loginUser.setUsername(loginUsername);
            loginUser.setPassword(loginPassword);
            loginUser.validate();
            InterfaceLogin userDAO = new SQLLogin();
            User user = userDAO.login(loginUsername, loginPassword);
            if (user == null) {
                throw new UserException("Incorrect username or password.");
            }
            
            this.mainApp.setUser(user);
        } catch (UserException | AssertionError e) {
            MessageBox.setContentText(e.getMessage());
        }
    }
}
