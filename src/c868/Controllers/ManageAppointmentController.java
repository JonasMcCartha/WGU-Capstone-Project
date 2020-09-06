package c868.Controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import c868.Main.MainApp;
import c868.DAOs.*;
import c868.Exceptions.*;
import c868.Models.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import static c868.Controllers.MonthlyCalendarController.getModifiedAppointment;

public class ManageAppointmentController implements Initializable {
    private Stage stage;
    private final ObservableList<Patient> patients;
    private final ObservableList<String> dropdownTimes;
    private final DateTimeFormatter timeFormat = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
    private final Appointment modifiedAppointment;

    @FXML
    private DatePicker AppointmentDateField;
    @FXML
    private ComboBox<Patient> AppointmentPatientField;
    @FXML
    private ComboBox<String> AppointmentStartField;
    @FXML
    private ComboBox<String> AppointmentEndField;
    @FXML
    private TextField AppointmentTitleField;
    @FXML
    private TextField AppointmentDescriptionField;
    @FXML
    private TextField AppointmentLocationField;
    @FXML
    private TextField AppointmentPhoneField;
    @FXML
    private TextField AppointmentEmailField;

    
    // Constructor
    public ManageAppointmentController() {
        modifiedAppointment = getModifiedAppointment();
                
        // Make an array of times divided by 30 minutes.
        dropdownTimes = FXCollections.observableArrayList();
        LocalTime time = LocalTime.MIDNIGHT.plusHours(9);
        for (int i = 0; i < 17; i++) {
            dropdownTimes.add(time.format(timeFormat));
            time = time.plusMinutes(30);
        }
        
        // Retrieve and sort patient list.
        InterfacePatient SQLPatient = new SQLPatient();
        patients = SQLPatient.getAllPatients();
        patients.sort((a, b) -> a.getPatientName().compareTo(b.getPatientName()));
    }
    
    
    public void bind(Stage stage) {
        this.stage = stage;
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AppointmentDateField.setValue(LocalDate.now());
        AppointmentStartField.setItems(dropdownTimes);
        AppointmentEndField.setItems(dropdownTimes);
        AppointmentPatientField.setItems(patients);
        if (modifiedAppointment != null) {
            AppointmentDateField.setValue(modifiedAppointment.getStart().toLocalDate());
            AppointmentStartField.getSelectionModel().select(modifiedAppointment.getStart().format(timeFormat));
            AppointmentEndField.getSelectionModel().select(modifiedAppointment.getEnd().format(timeFormat));
            AppointmentTitleField.setText(modifiedAppointment.getTitle());
            AppointmentDescriptionField.setText(modifiedAppointment.getDesc());
            AppointmentLocationField.setText(modifiedAppointment.getLocation());
            AppointmentPhoneField.setText(modifiedAppointment.getPhone());
            AppointmentEmailField.setText(modifiedAppointment.getEmail());
            AppointmentPatientField.getSelectionModel().select(modifiedAppointment.getPatient());
        }
    }
    
    
    // Cancel edit.
    @FXML
    void handleCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Cancel Modification");
        alert.setHeaderText("Cancel Modification");
        alert.setContentText("Are you sure you want to cancel?");
        alert.showAndWait()
            .filter(response -> response == ButtonType.OK)
            .ifPresent(response -> showCalendarScreen());
    }
    
    
    // Handle save when modifying or adding an appointment.
    @FXML
    void handleSave(ActionEvent event) throws IOException {
        User appointmentUser = c868.Main.MainApp.user;
        LocalDate appointmentDate = AppointmentDateField.getValue();
        Patient appointmentPatient = AppointmentPatientField.getValue();
        
        String appointmentStart = AppointmentStartField.getValue();
        String appointmentEnd = AppointmentEndField.getValue();
        String appointmentLocation = AppointmentLocationField.getText();
        String appointmentTitle = AppointmentTitleField.getText();
        String appointmentDescription = AppointmentDescriptionField.getText();
        String appointmentPhone = AppointmentPhoneField.getText();
        String appointmentEmail = AppointmentEmailField.getText();
        try {
            
            if (appointmentDate == null || appointmentStart == null || appointmentEnd == null) {
                throw new AppointmentException("Dates and times are required.");
            }
            
            LocalDateTime startDateTime = LocalDateTime.of(appointmentDate, LocalTime.parse(appointmentStart, timeFormat));
            LocalDateTime endDateTime = LocalDateTime.of(appointmentDate, LocalTime.parse(appointmentEnd, timeFormat));
            InterfaceAppointment SQLAppointment = new SQLAppointment();
            
            Appointment appointment = new Appointment();
            appointment.setPatient(appointmentPatient);
            appointment.setUser(appointmentUser);
            appointment.setTitle(appointmentTitle);
            appointment.setDesc(appointmentDescription);
            appointment.setLocation(appointmentLocation);
            appointment.setPhone(appointmentPhone);
            appointment.setEmail(appointmentEmail);
            appointment.setEnd(endDateTime);
            appointment.setStart(startDateTime);
            appointment.validate();
                      
            if (modifiedAppointment == null) {
                try {
                    appointment.validateAvailability();
                    int newId = SQLAppointment.addAppointment(appointment);
                    appointment.setAppointmentId(newId);
                }
                catch (AppointmentException e) {
                    throw new AppointmentException(e.getMessage());
                }
                
            } else {
                appointment.setAppointmentId(modifiedAppointment.getAppointmentId());
                SQLAppointment.updateAppointment(appointment);
            }
            showCalendarScreen();
        }
        catch (AppointmentException e) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Invalid Appointment");
            alert.setContentText(e.getMessage());
            alert.showAndWait()
                .filter(response -> response == ButtonType.OK);
        }
    }
    
        
    // Show monthly calender screen.
    @FXML
    private void showCalendarScreen() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/c868/Views/MonthlyCalendar.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            MonthlyCalendarController controller = loader.getController();
            controller.bind(stage);
            stage.show();
        } catch (IOException e) {
        }
    }
}