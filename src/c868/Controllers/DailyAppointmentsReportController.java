package c868.Controllers;

import java.io.IOException;
import java.util.ResourceBundle;
import java.net.URL;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import c868.Main.MainApp;
import c868.DAOs.*;
import c868.Models.*;


public class DailyAppointmentsReportController implements Initializable {
        
    @FXML
    private TableView<Appointment> ReportTable;
    @FXML
    private TableColumn<Appointment, String> ReportTitleCol;
    @FXML
    private TableColumn<Appointment, String> ReportNameCol;
    @FXML
    private TableColumn<Appointment, LocalDateTime> ReportStartTimeCol;
    @FXML
    private TableColumn<Appointment, LocalDateTime> ReportEndTimeCol;
    private Stage stage;
    private LocalDate today;
           
    
    // Constructor
    public DailyAppointmentsReportController() {
    }

    public void bind(Stage stage) {
        this.stage = stage;
    }
    
     @Override
    public void initialize(URL url, ResourceBundle rb) {
        today = LocalDate.now();
        ReportTitleCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        ReportNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDesc()));
        ReportStartTimeCol.setCellValueFactory(cellData -> new SimpleObjectProperty(cellData.getValue().getStart().toLocalTime()));
        ReportEndTimeCol.setCellValueFactory(cellData -> new SimpleObjectProperty(cellData.getValue().getEnd().toLocalTime()));
        populateTable();
    }
    
    
    // Go to general report screen.
    @FXML
    void handleBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("/c868/Views/Report.fxml"));
        AnchorPane root = (AnchorPane) loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        ReportController controller = loader.getController();
        controller.bind(stage);
        stage.show();
    }
    
    
    // Handle update
    @FXML
    void handleUpdate(ActionEvent event) throws IOException {
        populateTable();
    } 
          
    
    // Populate table
    public void populateTable() {
        InterfaceAppointment SQLappointment = new SQLAppointment();
        ObservableList<Appointment> appointments = SQLappointment.getAppointmentsInRange(LocalDateTime.of(today, LocalTime.MIDNIGHT), (LocalDateTime.of(today, LocalTime.MAX)));
        
        ReportTable.setItems(appointments);        
    }
}
