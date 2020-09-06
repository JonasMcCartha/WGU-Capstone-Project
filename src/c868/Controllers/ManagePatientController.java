package c868.Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;

import static c868.Controllers.PatientController.getModifiedPatient;
import c868.Main.MainApp;
import c868.DAOs.*;
import c868.Models.*;
import c868.Exceptions.*;


public class ManagePatientController implements Initializable {

    private Stage stage;

    @FXML
    private TextField PatientNameField;
    @FXML
    private TextField PatientAddress1Field;
    @FXML
    private TextField PatientAddress2Field;
    @FXML
    private TextField PatientAddressPhoneField;
    @FXML
    private TextField PatientAddressPostalCodeField;
    @FXML
    private TextField PatientAddressCityField;
    @FXML
    private TextField PatientAddressStateField;
    private final Patient modifiedPatient;

    
    // Constructor
    public ManagePatientController() {
        this.modifiedPatient = getModifiedPatient();
    }

    public void bind(Stage stage) {
        this.stage = stage;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (modifiedPatient != null) {
            PatientNameField.setText(modifiedPatient.getPatientName());
            PatientAddress1Field.setText(modifiedPatient.getAddress().getAddress1());
            PatientAddress2Field.setText(modifiedPatient.getAddress().getAddress2());
            PatientAddressPhoneField.setText(modifiedPatient.getAddress().getPhone());
            PatientAddressPostalCodeField.setText(modifiedPatient.getAddress().getPostalCode());
            PatientAddressCityField.setText(modifiedPatient.getAddress().getCity().getCityName());
            PatientAddressStateField.setText(modifiedPatient.getAddress().getCity().getState().getStateName());
        }
    }
    
    
    // Add an address.
    private Address handleAddAddress(String address1, String address2, String postalCode, String phone, City city) {
        Address address = new Address();
        address.setAddress1(address1);
        address.setAddress2(address2);
        address.setPostalCode(postalCode);
        address.setPhone(phone);
        address.setCity(city);
        InterfaceAddress SQLAddress = new SQLAddress();
        int newId = SQLAddress.addAddress(address);
        address.setAddressId(newId);
        return address;
    }

    
    // Add a city.
    private City handleAddCity(String cityName, State state) {
        City city = new City();
        city.setCityName(cityName);
        city.setState(state);
        InterfaceCity SQLCity = new SQLCity();
        int newId = SQLCity.addCity(city);
        city.setCityId(newId);
        return city;
    }
    
    
    // Add a state.
    private State handleAddState(String stateName) {
        State state = new State();
        state.setStateName(stateName);
        InterfaceState SQLState = new SQLState();
        int newId = SQLState.addState(state);
        state.setStateId(newId);
        return state;
    }
    
    
    // Add a patient.
    private Patient handleAddPatient(String patientName, Address address) {
        Patient patient = new Patient();
        patient.setPatientName(patientName);
        patient.setAddress(address);
        InterfacePatient SQLPatient = new SQLPatient();
        int newId = SQLPatient.addPatient(patient);
        patient.setPatientId(newId);
        return patient;
    }
    
    
    // Cancel change.
    @FXML
    void handleCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Cancel Modification");
        alert.setContentText("Are you sure you want to cancel?");
        alert.showAndWait()
            .filter(response -> response == ButtonType.OK)
            .ifPresent(response -> showPatientScreen());
    }
    
    
    // Edit an address.
    private Address handleEditAddress(int addressId, String address1, String address2, String postalCode, String phone, City city) {
        Address address = new Address();
        address.setAddressId(addressId);
        address.setAddress1(address1);
        address.setAddress2(address2);
        address.setPostalCode(postalCode);
        address.setPhone(phone);
        address.setCity(city);
        InterfaceAddress addressDAO = new SQLAddress();
        addressDAO.updateAddress(address);
        return address;
    }

    
    // Edit a city.
    private City handleEditCity(int cityId, String cityName, State state) {
        City city = new City();
        city.setCityId(cityId);
        city.setCityName(cityName);
        city.setState(state);
        InterfaceCity SQLCity = new SQLCity();
        SQLCity.updateCity(city);
        return city;
    }
    
    
    // Edit a state.
    private State handleEditState(int stateId, String stateName) {
        State state = new State();
        state.setStateId(stateId);
        state.setStateName(stateName);
        InterfaceState SQLState = new SQLState();
        SQLState.updateState(state);
        return state;
    }
    
    
    // Edit a patient.
    private Patient handleEditPatient(int patientId, String patientName, Address address) {
        Patient patient = new Patient();
        patient.setPatientId(patientId);
        patient.setPatientName(patientName);
        patient.setAddress(address);
        InterfacePatient SQLPatient = new SQLPatient();
        SQLPatient.updatePatient(patient);
        return patient;
    }
    
    
    // Handle save when modifying or adding a patient.
    @FXML
    void handleSave(ActionEvent event) throws IOException, PatientException {
        String patientName = PatientNameField.getText();
        String address1 = PatientAddress1Field.getText();
        String address2 = PatientAddress2Field.getText();
        String addressPhone = PatientAddressPhoneField.getText();
        String addressPostalCode = PatientAddressPostalCodeField.getText();
        String addressCity = PatientAddressCityField.getText();
        String addressState = PatientAddressStateField.getText();
        try {
            Patient patient = new Patient();
            Address address = new Address();
            City city = new City();
            State state = new State();
            
            patient.setPatientName(patientName);
            address.setAddress1(address1);
            address.setAddress2(address2);
            address.setPhone(addressPhone);
            address.setPostalCode(addressPostalCode);
            patient.setAddress(address);
            
            city.setCityName(addressCity);
            address.setCity(city);
            
            state.setStateName(addressState);
            city.setState(state);
            
            patient.validate();
            
             if (modifiedPatient == null) {
                state = handleAddState(addressState);
                city = handleAddCity(addressCity, state);
                address = handleAddAddress(address1, address2, addressPostalCode, addressPhone, city);
                patient.validate();
                handleAddPatient(patientName, address);
                
            } else {
                Address modifiedPatientAddress = modifiedPatient.getAddress();
                City modifiedPatientCity = modifiedPatientAddress.getCity();
                State modifiedPatientState = modifiedPatientCity.getState();
                state = handleEditState(modifiedPatientState.getStateId(), addressState);
                city = handleEditCity(modifiedPatientCity.getCityId(), addressCity, state);
                address = handleEditAddress(modifiedPatientAddress.getAddressId(), address1, address2, addressPostalCode, addressPhone, city);
                patient.validate();
                handleEditPatient(modifiedPatient.getPatientId(), patientName, address);
            }
            showPatientScreen();
        }    
        catch (PatientException e) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Invalid Patient or Address Information");
            alert.setContentText(e.getMessage());
            alert.showAndWait()
                .filter(response -> response == ButtonType.OK); 
        }
    }    
       
    
    // Go to main patient screen.
    @FXML
    private void showPatientScreen() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/c868/Views/Patient.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            PatientController controller = loader.getController();
            controller.bind(stage);
            stage.show();
        } catch (IOException e) {
        }
    }
}

