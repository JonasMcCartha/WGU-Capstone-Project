package c868.Models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javafx.collections.ObservableList;

import c868.DAOs.InterfaceAppointment;
import c868.DAOs.SQLAppointment;
import c868.Exceptions.AppointmentException;
import c868.Exceptions.PatientException;


public class Appointment {
    public int appointmentId;    
    private String description;
    private String location;
    private String phone;
    private String email;
    private LocalDateTime start;
    private LocalDateTime end;
    private final LocalDateTime midnightToday;
    public String title;
    public Patient patient;
    public User user;
    
    
    // Constructor
    public Appointment() {
        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalDate today = LocalDate.now();
        midnightToday = LocalDateTime.of(today, midnight);
        seedAppointment();
    }
    
    
    /* Getters */
    public int getAppointmentId() {
        return this.appointmentId;
    }
    public String getPhone() {
        return this.phone;
    }
    public Patient getPatient() {
        return this.patient;
    }
    public User getUser() {
        return this.user;
    }
    public String getDesc() {
        return this.description;
    }
    public LocalDateTime getEnd() {
        return this.end;
    }
    public String getLocation() {
        return this.location;
    }
    public LocalDateTime getStart() {
        return this.start;
    }
    public String getTitle() {
        return this.title;
    }
    public String getEmail() {
        return this.email;
    }
    
    
    /* Setters */
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setPatient(Patient patient) {
        this.patient = patient;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public void setDesc(String description) {
        this.description = description;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public void setStart(LocalDateTime start) {
        this.start = start;
    }
    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    
    
    @Override
    public String toString() {
        return getTitle();
    }
    
    
    // Validations
    private void seedAppointment() {
        setTitle("");
        setLocation("");
        setPhone("");
        setEmail("");
        setStart(midnightToday);
        setEnd(midnightToday.plusMinutes(30));
    }
    
    public boolean validateAvailability() throws AppointmentException {
        InterfaceAppointment SQLAppointment = new SQLAppointment();
        ObservableList<Appointment> appointments = SQLAppointment.getOverlappingAppointments(this.start, this.end);
        if (appointments.size() > 0) {
            throw new AppointmentException("The appointment time conflicts with an existing appointment.");
        }
        return true;
    } 
    
    public boolean validateTime() throws AppointmentException {
        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalDate startDate = this.start.toLocalDate();
        LocalDate endDate = this.end.toLocalDate();
        LocalTime startTime = this.start.toLocalTime();
        LocalTime endTime = this.end.toLocalTime();
        
        if (!startDate.isEqual(endDate)) {
            throw new AppointmentException("Appointments cannot be scheduled over multiple days.");
        }
        if (startTime.isBefore(midnight.plusHours(9)) || (endTime.isAfter(midnight.plusHours(17)))) {
            throw new AppointmentException("Appointment time extends past business hours.");
        }
        return true;
    } 
           
    public boolean validate() throws AppointmentException {        
        if (this.patient == null) {
            throw new AppointmentException("Please choose a patient.");
        }
        if (this.title.equals("")) {
            throw new AppointmentException("Please enter a title.");
        }
        if (this.phone.equals("")) {
            throw new AppointmentException("Please enter a contact.");
        }
        if (this.location.equals("")) {
            throw new AppointmentException("Please enter a location.");
        }
        if (this.start.isEqual(midnightToday)) {
            throw new AppointmentException("Please choose a start time.");
        }
        if (this.end.isEqual(midnightToday.plusMinutes(30))) {
            throw new AppointmentException("Please choose an end time.");
        }
        
        try {
            validateTime();
            this.patient.validate();
        }
        catch (AppointmentException | PatientException e) {
            throw new AppointmentException(e.getMessage());
        }
        return true;
    } 
}