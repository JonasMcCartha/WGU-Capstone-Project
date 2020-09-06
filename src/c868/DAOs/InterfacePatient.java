package c868.DAOs;

import c868.Models.Patient;
import javafx.collections.ObservableList;


public interface InterfacePatient {
    public int addPatient(Patient patient);
    public int getMaxId();
    public ObservableList<Patient> getAllPatients();
    public Patient getPatient(int patientId);
    public void removePatient(Patient patient);
    public void updatePatient(Patient patient);
}

