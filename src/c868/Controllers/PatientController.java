package c868.Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.event.ActionEvent;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;


import c868.DAOs.*;
import c868.Models.*;
import c868.Main.MainApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;


public class PatientController implements Initializable {

    private Stage stage;
    @FXML
    private TableView<Patient> PatientTable;
    @FXML
    private TableColumn<Patient, String> PatientNameCol;
    @FXML
    private TableColumn<Patient, String> PatientAddress1Col;
    @FXML
    private TableColumn<Patient, String> PatientAddress2Col;
    @FXML
    private TableColumn<Patient, String> PatientPostalCodeCol;
    @FXML
    private TableColumn<Patient, String> PatientPhoneCol;
    @FXML
    private TableColumn<Patient, String> PatientCityCol;
    @FXML
    private TableColumn<Patient, String> PatientStateCol;
    @FXML
    private TextField PatientSearchField;
    
    
    private static Patient modifiedPatient;
    public static Patient getModifiedPatient() {
        return modifiedPatient;
    }
    
    
    // Constructor
    public PatientController() {
    }

    public void bind(Stage stage) {
        this.stage = stage;
    }
            
    @FXML
    void handleAddPatient(ActionEvent event) throws IOException {        
        showManageScreen(event);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        PatientNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPatientName()));
        PatientAddress1Col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress().getAddress1()));
        PatientAddress2Col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress().getAddress2()));
        PatientPostalCodeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress().getPostalCode()));
        PatientPhoneCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress().getPhone()));
        PatientCityCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress().getCity().getCityName()));
        PatientStateCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress().getCity().getState().getStateName()));
        populatePatientTable();
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
    
    
    // Delete a patient.
    @FXML
    void handleDeletePatient(ActionEvent event) throws IOException {
        Patient patient = PatientTable.getSelectionModel().getSelectedItem();
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Delete Patient");
        alert.setContentText("Are you sure you want to delete " + patient.getPatientName() + "?");
        alert.showAndWait()
            .filter(response -> response == ButtonType.OK)
            .ifPresent((ButtonType response) -> {
                InterfacePatient SQLPatient = new SQLPatient();
                SQLPatient.removePatient(patient);
                populatePatientTable();
        });
    }
    
    
    // Go to manage patient screen.
    @FXML
    void handleModifyPatient(ActionEvent event) throws IOException {
        modifiedPatient = PatientTable.getSelectionModel().getSelectedItem();
        showManageScreen(event);
    }
      
    
    // Populate table.
    public void populatePatientTable() {
        InterfacePatient patientDAO = new SQLPatient();
        PatientTable.setItems(patientDAO.getAllPatients());
    }
    
    
    // Display manage patient screen.
    @FXML
    private void showManageScreen(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("/c868/Views/ManagePatient.fxml"));
        AnchorPane root = (AnchorPane) loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        ManagePatientController controller = loader.getController();
        controller.bind(stage);
        stage.show();
    }
    
    
    // Search for patient.
    @FXML
    void handleSearchPatient(ActionEvent event) throws IOException {
        String patientSearchIdString = PatientSearchField.getText();
        Patient searchedPatient = Patient.lookupPatient(patientSearchIdString);
        if (searchedPatient != null) {
            ObservableList<Patient> patientsList = FXCollections.observableArrayList();
            patientsList.add(searchedPatient);
            PatientTable.setItems(patientsList);
        // Return alert if patient not found
        } 
            else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Alert!");
                alert.setHeaderText("Patient not found");
                alert.setContentText("No patient matching search terms found.");
                alert.showAndWait();
        }
    }
}
