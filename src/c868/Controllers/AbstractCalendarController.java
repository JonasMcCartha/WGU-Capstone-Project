package c868.Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import c868.Main.MainApp;
import c868.DAOs.*;
import c868.Models.Appointment;

public abstract class AbstractCalendarController implements Initializable {
    
    public Stage stage;
    @FXML
    public Label CalendarLabel;
    @FXML
    
    public GridPane CalendarPane;
    public ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    public final InterfaceAppointment SQLAppointment;
    public static Appointment modifiedAppointment;

    
    // Constructor
    public AbstractCalendarController() {
        SQLAppointment = new SQLAppointment();
    }

    public void bind(Stage stage) {
        this.stage = stage;
    }

    public static Appointment getModifiedAppointment() {
        return modifiedAppointment;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setCalendarView();
    }

    
    // Return to main screen.
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
    
    
    // Show manage appointments screen.
    @FXML
    private void showManageScreen(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("/c868/Views/ManageAppointment.fxml"));
        AnchorPane root = (AnchorPane) loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        ManageAppointmentController controller = loader.getController();
        controller.bind(stage);
        stage.show();
    }

    
    // Add an appointment.
    @FXML
    void handleAddAppointment(ActionEvent event) throws IOException {  
        modifiedAppointment = null;
        showManageScreen(event);
    }
    
    
    // Delete an appointment.
    @FXML
    void handleDeleteAppointment(ActionEvent event) throws IOException {
        if (modifiedAppointment == null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("No Appointment Selected");
            alert.setContentText("You must select an appointment to delete.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Delete Appointment");
            alert.setContentText("Are you sure you want to delete " + modifiedAppointment.getTitle() + "?");
            alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> {
                    SQLAppointment.removeAppointment(modifiedAppointment);
                    setCalendarView();
                });
        }
    }

    
    // Edit an appointment.
    @FXML
    void handleModifyAppointment(ActionEvent event) throws IOException {
        if (modifiedAppointment == null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("No Appointment Selected");
            alert.setContentText("You must select an appointment to modify.");
            alert.showAndWait();
        } else {
            showManageScreen(event);
        }
    }

    
    // Reset calendar screen.
    public void resetCalendar() {
        modifiedAppointment = null;
        CalendarPane.getChildren().clear();
    }

    
    // Abstract calendar view.
    public void setCalendarView() {
    }
}
