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
import javafx.stage.Stage;

import c868.Main.MainApp;

public class ReportController implements Initializable {
    private Stage stage;
    
    
    // Constructor
    public ReportController() {
    }
       
    public void bind(Stage stage) {
        this.stage = stage;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    
    // Go to main screen.
    @FXML
    void handleBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("/c868/Views/Main.fxml"));
        AnchorPane root = (AnchorPane) loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        MainController controller = loader.getController();
        controller.bind(stage);
        stage.show();
    }
    
    
    // Go to appointments by description report.
    @FXML
    void handleAppointmentsByDescription(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("/c868/Views/AppointmentsByDescriptionReport.fxml"));
        AnchorPane root = (AnchorPane) loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        AppointmentsByDescriptionReportController controller = loader.getController();
        controller.bind(stage);
        stage.show();
    }
    
    
    // Go to appointments by day of week report.    
    @FXML
    void handleAppointmentsByDayOfWeek(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("/c868/Views/AppointmentsByDayOfWeekReport.fxml"));
        AnchorPane root = (AnchorPane) loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        AppointmentsByDayOfWeekReportController controller = loader.getController();
        controller.bind(stage);
        stage.show();
    } 
    
    
    // Go to today's appointments report.
    @FXML
    void handleDailyAppointments(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("/c868/Views/DailyAppointmentsReport.fxml"));
        AnchorPane root = (AnchorPane) loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        DailyAppointmentsReportController controller = loader.getController();
        controller.bind(stage);
        stage.show();
    } 
}

