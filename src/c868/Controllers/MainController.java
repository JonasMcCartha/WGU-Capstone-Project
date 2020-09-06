package c868.Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

import c868.Main.MainApp;


public class MainController implements Initializable {
    private Stage stage;
    
    
    // Constructor
    public MainController() {
    }
        
    public void bind(Stage stage) {
        this.stage = stage;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    
    // Exit app
    @FXML
    void handleCancel(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Exit Application");
        alert.setContentText("Are you sure you want to close the application?");
        
        //Lambda expressions make for clear code when handling exiting the app.
        alert.showAndWait()
            .filter(response -> response == ButtonType.OK)
            .ifPresent(response -> System.exit(0));
    }
    
    
    // Go to Appointments screen.
    @FXML
    void handleManageAppointments(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("/c868/Views/MonthlyCalendar.fxml"));
        AnchorPane root = (AnchorPane) loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        MonthlyCalendarController controller = loader.getController();
        controller.bind(stage);
        stage.show();
    } 

    
    // Go to Patients screen.
    @FXML
    void handleManagePatients(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("/c868/Views/Patient.fxml"));
        AnchorPane root = (AnchorPane) loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        PatientController controller = loader.getController();
        controller.bind(stage);
        stage.show();
    }
    
    
    // Go to Reports screen.
    @FXML
    void handleReports(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("/c868/Views/Report.fxml"));
        AnchorPane root = (AnchorPane) loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        ReportController controller = loader.getController();
        controller.bind(stage);
        stage.show();
    }
    
    
    // Go to Change Password screen.
    @FXML
    void handleChangeUserPassword(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("/c868/Views/ChangeUserPassword.fxml"));
        AnchorPane root = (AnchorPane) loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        ChangeUserPasswordController controller = loader.getController();
        controller.bind(stage);
        stage.show();
    }
}
