package c868.Controllers;

import c868.DAOs.InterfaceLogin;
import c868.DAOs.SQLLogin;
import c868.Exceptions.UserException;
import c868.Main.MainApp;
import c868.Models.User;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class ChangeUserPasswordController {
    
    private Stage stage;
    
    @FXML
    private TextField OldPasswordField;
    @FXML
    private TextField RetypeOldPasswordField;
    @FXML
    private TextField NewPasswordField;
    @FXML
    private TextField RetypeNewPasswordField;
    
    
    // Constructor
    public ChangeUserPasswordController() {
    }
        
    public void bind(Stage stage) {
        this.stage = stage;
    }
    
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    
    // Change a password.
    private User handleEditUser(int userId, String password) {
        User user = new User();
        user.setUserId(userId);
        user.setPassword(password);
        InterfaceLogin SQLLogin = new SQLLogin();
        SQLLogin.updateUser(user);
        return user;
    }
    
    
    // Handle save.
    @FXML
    void handleSave(ActionEvent event) throws IOException, UserException {
        String oldPassword = OldPasswordField.getText();
        String retypeOldPassword = RetypeOldPasswordField.getText();
        String newPassword = NewPasswordField.getText();
        String retypeNewPassword = RetypeNewPasswordField.getText();
        User currentUser = c868.Main.MainApp.user;
        
        try {
            if (oldPassword == null ? retypeOldPassword != null : !oldPassword.equals(retypeOldPassword)) {
                throw new UserException("Old passwords do not match.");
            }
            if (newPassword == null ? retypeNewPassword != null : !newPassword.equals(retypeNewPassword)) {
                throw new UserException("New passwords do not match.");                
            }
            
            else {
                handleEditUser(currentUser.getUserId(), newPassword);
            }
            showMainScreen();
        }
        
        catch (UserException e) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Invalid Password Information");
            alert.setContentText(e.getMessage());
            alert.showAndWait()
                .filter(response -> response == ButtonType.OK);              
        }        
    }
    
    
    // Cancel change.
    @FXML
    void handleCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Cancel Password Change");
        alert.setContentText("Are you sure you want to cancel?");
        alert.showAndWait()
            .filter(response -> response == ButtonType.OK)
            .ifPresent(response -> showMainScreen());
    }
    
    
    // Go to main patient screen.
    @FXML
    private void showMainScreen() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/c868/Views/Main.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            MainController controller = loader.getController();
            controller.bind(stage);
            stage.show();
        } catch (IOException e) {
        }
    }
}
