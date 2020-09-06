package c868.Controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ResourceBundle;
import java.net.URL;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;

import c868.Main.MainApp;
import c868.DAOs.*;
import c868.Models.*;


public class AppointmentsByDescriptionReportController implements Initializable {
 
    @FXML
    private ComboBox<YearMonth> ReportMonthField;
    @FXML
    private TableView<ReportCount> ReportTable;
    @FXML
    private TableColumn<ReportCount, String> ReportDescriptionCol;
    @FXML
    private TableColumn<ReportCount, Integer> ReportCountCol;
    private Stage stage;
    private final ObservableList<YearMonth> months = FXCollections.observableArrayList();
    private YearMonth selectedMonth;
    private ReportList counts;
        
    
    // Constructor
    public AppointmentsByDescriptionReportController() {
        YearMonth currentYearMonth = YearMonth.now();
        int currentYear = currentYearMonth.getYear();
        for (int i = 1; i <= 12; i++) {
            months.add(YearMonth.of(currentYear, i));
        }
    }

    public void bind(Stage stage) {
        this.stage = stage;
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        selectedMonth = YearMonth.now();
        ReportMonthField.setItems(months);
        ReportMonthField.getSelectionModel().select(selectedMonth);
        ReportDescriptionCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
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
        YearMonth chosenMonth = ReportMonthField.getValue();
        if (chosenMonth != null) {
            selectedMonth = chosenMonth;
            populateTable();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("No Month Selected");
            alert.setContentText("Please select a month.");
            alert.showAndWait()
                .filter(response -> response == ButtonType.OK);
        }
    }
          
    
    // Populate table
    public void populateTable() {
        InterfaceAppointment SQLAppointment = new SQLAppointment();
       
        if (selectedMonth != null) {
            LocalDate firstDate = selectedMonth.atDay(1);
            LocalDate lastDate = selectedMonth.atEndOfMonth();
            LocalDateTime startDatetime = LocalDateTime.of(firstDate, LocalTime.MIDNIGHT);
            LocalDateTime endDatetime = LocalDateTime.of(lastDate, LocalTime.MIDNIGHT);
            ObservableList<Appointment> appointments = SQLAppointment.getAppointmentsInRange(startDatetime, endDatetime);
            counts = new ReportList();
            
            appointments.stream().map((appointment) -> appointment.getDesc()).forEachOrdered((type) -> {
                int index = counts.indexOf(type);
                if (index == -1) {
                    ReportCount item = new ReportCount(type);
                    counts.add(item);
                }
                else {
                    ReportCount item = counts.get(index);
                    item.autoIncrement();
                }
            });

            ReportTable.setItems(counts.list());
        }
    }
}