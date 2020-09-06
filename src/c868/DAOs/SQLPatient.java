package c868.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import c868.Main.MainApp;
import c868.Models.Patient;


public class SQLPatient implements InterfacePatient {
    private final static Connection conn = MainApp.conn;
    
    
    // Constructor
    public SQLPatient() {
    }
    
    
    // Retrieve highest primary key value from table.
    @Override
    public int getMaxId() {
        int maxID = 0;
        String maxIdQuery = "SELECT MAX(patientId) FROM patient";
        
        try {
            Statement statement = conn.createStatement(); 
            ResultSet result = statement.executeQuery(maxIdQuery);
            
            if(result.next()) {
                maxID = result.getInt(1);
            }
        } catch(SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return maxID + 1;
    }
    
    
    // Add a new patient.
    @Override
    public int addPatient(Patient patient) {
        int patientId = getMaxId();
        String addPatientQuery = String.join(" ",
            "INSERT INTO patient (patientId, patientName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy)",
            "VALUES (?, ?, ?, 1, NOW(), ?, NOW(), ?)"
        );
        
        try {
            PreparedStatement statement = conn.prepareStatement(addPatientQuery);
            
            statement.setInt(1, patientId);
            statement.setString(2, patient.getPatientName());
            statement.setInt(3, patient.getAddress().getAddressId());
            statement.setString(4, MainApp.user.username);
            statement.setString(5, MainApp.user.username);
            statement.executeUpdate();
            
        } catch(SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return patientId;
    }
    
    
    // Retrieve list of patients.
    @Override
    public ObservableList<Patient> getAllPatients() {
        ObservableList<Patient> patients = FXCollections.observableArrayList();
        String getPatientQuery = "SELECT * FROM patient WHERE active = 1"; 

        try {
            PreparedStatement statement = conn.prepareStatement(getPatientQuery);
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                Patient fetchedPatient = new Patient();
                InterfaceAddress address = new SQLAddress();
                
                fetchedPatient.setPatientName(resultSet.getString("patientName"));
                fetchedPatient.setPatientId(resultSet.getInt("patientId"));
                fetchedPatient.setIsActive(resultSet.getInt("active"));
                fetchedPatient.setAddress(address.getAddress (resultSet.getInt("addressId")));
                
                patients.add(fetchedPatient);
            }
        } catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        return patients;
    }
 
    
    // Look up patient by ID.
    @Override
    public Patient getPatient(int patientId) {
        Patient fetchedPatient = new Patient();
        String getPatientQuery = "SELECT * FROM patient WHERE patientId = ?"; 
        
        try {
            PreparedStatement statement = conn.prepareStatement(getPatientQuery);
            statement.setInt(1, patientId);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()){
                InterfaceAddress address = new SQLAddress();
                
                fetchedPatient.setPatientName(resultSet.getString("patientName"));
                fetchedPatient.setPatientId(resultSet.getInt("patientId"));
                fetchedPatient.setIsActive(resultSet.getInt("active"));
                fetchedPatient.setAddress(address.getAddress (resultSet.getInt("addressId")));
            } else {
                return null;
            }
        } catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        return fetchedPatient;
    }
    
    
    // Remove a patient.
    @Override
    public void removePatient(Patient patient) {
        String removePatientQuery = "UPDATE patient SET active=0 WHERE patientId = ?";
    
        try {
            PreparedStatement statement = conn.prepareStatement(removePatientQuery);
            
            statement.setInt(1, patient.getPatientId());
            statement.executeUpdate();
            
        } catch(SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    
    // Update a patient.
    @Override
    public void updatePatient(Patient patient) {
        String updatePatientQuery = String.join(" ",
            "UPDATE patient",
            "SET patientName=?, addressId=?, lastUpdate=NOW(), lastUpdateBy=?",
            "WHERE patientId = ?"
        );
    
        try {
            PreparedStatement statement = conn.prepareStatement(updatePatientQuery);
            
            statement.setString(1, patient.getPatientName());
            statement.setInt(2, patient.getAddress().getAddressId());
            statement.setString(3, MainApp.user.username);
            statement.setInt(4, patient.getPatientId());
            statement.executeUpdate();
            
        } catch(SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
