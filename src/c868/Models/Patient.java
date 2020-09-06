package c868.Models;

import c868.DAOs.InterfacePatient;
import c868.DAOs.SQLPatient;
import c868.Exceptions.AddressException;
import c868.Exceptions.PatientException;
import javafx.collections.ObservableList;

public class Patient {
    public Address patientAddress;
    public String patientName;
    public int patientId;
    public int isActive;
    
    private final static InterfacePatient patientDAO = new SQLPatient();
    private final static ObservableList<Patient> allPatients = patientDAO.getAllPatients();
    
            
    // Constructor
    public Patient() {
        seedPatient();
    }
    
    
    /* Getters */
    public Address getAddress() {
        return this.patientAddress;
    }
    public int getPatientId() {
        return this.patientId;
    }
    public String getPatientName() {
        return this.patientName;
    }
    public int getIsActive() {
        return this.isActive;
    }
    
    
    /* Setters */
    public void setAddress(Address address) {
        this.patientAddress = address;
    }
    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }
    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }
    public void setIsActive (int isActive) {
        this.isActive = isActive;
    }
    
    
    @Override
    public String toString() {
        return getPatientName();
    }
    
    
    private void seedPatient() {
        setPatientName("");
    }
       
    // Return list of patients
    public static ObservableList<Patient> getPatients() {
        return allPatients;
    }
    
    
    // Search for a patient.  Return null if nothing found.
    public static Patient lookupPatient(String name) {
        for (Patient p : allPatients) {
            if (p.getPatientName().equals(name) || (Integer.toString(p.getPatientId()) == null ? name == null : Integer.toString(p.getPatientId()).equals(name))) {
                return p;
            }
        }
        return null;
    }
        
    
    // Validation
    public boolean validate() throws PatientException {
        if (this.patientName.equals("")) {
            throw new PatientException("Please enter a patient name.");
        }
        try {
            this.patientAddress.validate();
        }
        catch (AddressException e) {
            throw new PatientException(e.getMessage());
        }
        return true;
    }
}
