package c868.Controllers;

import java.io.IOException;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.net.URL;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

import c868.Main.MainApp;
import c868.DAOs.*;
import c868.Models.*;
import java.util.Calendar;


public class AppointmentsByDayOfWeekReportController implements Initializable {
        
    @FXML
    private TableView<ReportCount> ReportTable;
    @FXML
    private TableColumn<ReportCount, String> ReportDayCol;
    @FXML
    private TableColumn<ReportCount, Integer> ReportCountCol;
    private Stage stage;
    private Date selectedDayOfWeek;
    private ReportList counts;
           
    
    // Constructor
    public AppointmentsByDayOfWeekReportController() {
    }

    public void bind(Stage stage) {
        this.stage = stage;
    }
    
     @Override
    public void initialize(URL url, ResourceBundle rb) {
        selectedDayOfWeek = Calendar.getInstance().getTime();
        ReportDayCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        ReportCountCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCount()).asObject());
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
       
        if (selectedDayOfWeek != null) {
            counts = new ReportList();
            ObservableList<Appointment> appointments = SQLappointment.getAllAppointments();
            

                appointments.stream().map((appointment) -> appointment.getStart().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH)).forEachOrdered((type) -> {
                    int index = counts.indexOf(type);
                    if (index == -1) {
                        ReportCount item = new ReportCount(type);
                        counts.add(item);
                    }
                    else {
                        ReportCount item;
                        item = counts.get(index);
                        item.autoIncrement(); 
                    } 
                }
            );
            ReportTable.setItems(counts.list());
        }
    }
}
