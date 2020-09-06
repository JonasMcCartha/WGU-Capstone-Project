package c868.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import c868.Main.MainApp;
import c868.Models.Appointment;

public class SQLAppointment implements InterfaceAppointment {
    private final static Connection conn = MainApp.conn;
    
    
    // Constructor
    public SQLAppointment() {
    }
    
    
    // Retrieve highest primary key value from table.
    @Override
    public int getMaxId() {
        int maxID = 0;
        String maxIdQuery = "SELECT MAX(appointmentId) FROM appointment";
        
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
    
    
    // Add new appointment.
    @Override
    public int addAppointment(Appointment appointment) {
        String addAppointmentQuery;
        addAppointmentQuery = String.join(" ",
                "INSERT INTO appointment (appointmentId, patientId, userId, title, description, location, phone, email, start, end, createDate, createdBy, lastUpdate, lastUpdateBy)",
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
        );
    
        int appointmentId = getMaxId();
        try {
            PreparedStatement statement = conn.prepareStatement(addAppointmentQuery);
            
            ZoneId zone = ZoneId.systemDefault();
            LocalDateTime startUTC = appointment.getStart().atZone(zone).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
            LocalDateTime endUTC = appointment.getEnd().atZone(zone).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
            
            statement.setInt(1, appointmentId);
            statement.setInt(2, appointment.getPatient().getPatientId());
            statement.setInt(3, appointment.getUser().getUserId());
            statement.setString(4, appointment.getTitle());
            statement.setString(5, appointment.getDesc());
            statement.setString(6, appointment.getLocation());
            statement.setString(7, appointment.getPhone());
            statement.setString(8, appointment.getEmail());
            statement.setTimestamp(9, Timestamp.valueOf(startUTC));
            statement.setTimestamp(10, Timestamp.valueOf(endUTC));
            statement.setTimestamp(11, Timestamp.valueOf(startUTC));
            statement.setString(12, MainApp.user.username);
            statement.setTimestamp(13, Timestamp.valueOf(startUTC));
            statement.setString(14, MainApp.user.username);
            
            statement.executeUpdate();
        } catch(SQLException ex) {
            System.out.println(ex.getMessage());
        }
        
        return appointmentId;
    }

    
    // Retrieve list of appointments.
    @Override
    public ObservableList<Appointment> getAllAppointments() {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        String getAppointmentQuery = String.join(" ",
            "SELECT * FROM appointment AS a",
            "JOIN patient AS c",
            "ON a.patientId = c.patientId",
            "WHERE c.active = 1"
        ); 

        try{
            PreparedStatement statement = conn.prepareStatement(getAppointmentQuery);
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                Appointment fetchedAppointment = new Appointment();
                ZoneId zone = ZoneId.systemDefault();
                InterfacePatient patient = new SQLPatient();
                LocalDateTime startUTC = resultSet.getTimestamp("start").toLocalDateTime();
                LocalDateTime endUTC = resultSet.getTimestamp("end").toLocalDateTime();
                LocalDateTime startLocal = startUTC.atZone(ZoneOffset.UTC).withZoneSameInstant(zone).toLocalDateTime();
                LocalDateTime endLocal = endUTC.atZone(ZoneOffset.UTC).withZoneSameInstant(zone).toLocalDateTime();
                
                fetchedAppointment.setAppointmentId(resultSet.getInt("appointmentId"));
                fetchedAppointment.setTitle(resultSet.getString("title"));
                fetchedAppointment.setDesc(resultSet.getString("description"));
                fetchedAppointment.setLocation(resultSet.getString("location"));
                fetchedAppointment.setPhone(resultSet.getString("phone"));
                fetchedAppointment.setEmail(resultSet.getString("email"));
                fetchedAppointment.setStart(startLocal);
                fetchedAppointment.setEnd(endLocal);
                fetchedAppointment.setPatient(patient.getPatient(resultSet.getInt("patientId")));
                
                appointments.add(fetchedAppointment);
            }
        } catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        return appointments;
    }
    

    // Retrieve list of appointments in date range.
    @Override
    public ObservableList<Appointment> getAppointmentsInRange(LocalDateTime start, LocalDateTime end) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        
        String getAppointmentQuery = String.join(" ",
            "SELECT * FROM appointment AS a",
            "JOIN patient AS c",
            "ON a.patientId = c.patientId",
            "WHERE a.start >= ? AND a.end <= ?",
            "AND c.active = 1"
        );

        try {
            PreparedStatement statement = conn.prepareStatement(getAppointmentQuery);
            
            ZoneId zone = ZoneId.systemDefault();
            LocalDateTime startDatetimeParam = start.atZone(zone).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
            LocalDateTime endDatetimeParam = end.atZone(zone).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
            statement.setTimestamp(1, Timestamp.valueOf(startDatetimeParam));
            statement.setTimestamp(2, Timestamp.valueOf(endDatetimeParam));
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                Appointment fetchedAppointment = new Appointment();
                InterfacePatient patient = new SQLPatient();
                LocalDateTime startUTC = resultSet.getTimestamp("start").toLocalDateTime();
                LocalDateTime endUTC = resultSet.getTimestamp("end").toLocalDateTime();
                LocalDateTime startLocal = startUTC.atZone(ZoneOffset.UTC).withZoneSameInstant(zone).toLocalDateTime();
                LocalDateTime endLocal = endUTC.atZone(ZoneOffset.UTC).withZoneSameInstant(zone).toLocalDateTime();
                
                fetchedAppointment.setAppointmentId(resultSet.getInt("appointmentId"));
                fetchedAppointment.setTitle(resultSet.getString("title"));
                fetchedAppointment.setDesc(resultSet.getString("description"));
                fetchedAppointment.setLocation(resultSet.getString("location"));
                fetchedAppointment.setPhone(resultSet.getString("phone"));
                fetchedAppointment.setEmail(resultSet.getString("email"));
                fetchedAppointment.setStart(startLocal);
                fetchedAppointment.setEnd(endLocal);
                fetchedAppointment.setPatient(patient.getPatient(resultSet.getInt("patientId")));
                
                appointments.add(fetchedAppointment);
            }
        } catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        
        return appointments;
    }
   
    
    // Get overlapping appointments.
    @Override
    public ObservableList<Appointment> getOverlappingAppointments(LocalDateTime start, LocalDateTime end) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        
        String getAppointmentQuery = String.join(" ",
            "SELECT * FROM appointment AS a",
            "JOIN patient AS c",
            "ON a.patientId = c.patientId",
            "WHERE (a.start >= ? AND a.end <= ?)",
            "OR (a.start <= ? AND a.end >= ?)",
            "OR (a.start BETWEEN ? AND ? OR a.end BETWEEN ? AND ?)",
            "AND c.active = 1"
        ); 

        try{
            PreparedStatement statement = conn.prepareStatement(getAppointmentQuery);
            
            ZoneId zone = ZoneId.systemDefault();
            LocalDateTime startDatetimeParam = start.atZone(zone).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
            LocalDateTime endDatetimeParam = end.atZone(zone).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
            statement.setTimestamp(1, Timestamp.valueOf(startDatetimeParam));
            statement.setTimestamp(2, Timestamp.valueOf(endDatetimeParam));
            statement.setTimestamp(3, Timestamp.valueOf(startDatetimeParam));
            statement.setTimestamp(4, Timestamp.valueOf(endDatetimeParam));
            statement.setTimestamp(5, Timestamp.valueOf(startDatetimeParam));
            statement.setTimestamp(6, Timestamp.valueOf(endDatetimeParam));
            statement.setTimestamp(7, Timestamp.valueOf(startDatetimeParam));
            statement.setTimestamp(8, Timestamp.valueOf(endDatetimeParam));
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                Appointment fetchedAppointment = new Appointment();
                InterfacePatient patient = new SQLPatient();
                LocalDateTime startUTC = resultSet.getTimestamp("start").toLocalDateTime();
                LocalDateTime endUTC = resultSet.getTimestamp("end").toLocalDateTime();
                LocalDateTime startLocal = startUTC.atZone(ZoneOffset.UTC).withZoneSameInstant(zone).toLocalDateTime();
                LocalDateTime endLocal = endUTC.atZone(ZoneOffset.UTC).withZoneSameInstant(zone).toLocalDateTime();
                
                fetchedAppointment.setAppointmentId(resultSet.getInt("appointmentId"));
                fetchedAppointment.setTitle(resultSet.getString("title"));
                fetchedAppointment.setDesc(resultSet.getString("description"));
                fetchedAppointment.setLocation(resultSet.getString("location"));
                fetchedAppointment.setPhone(resultSet.getString("phone"));
                fetchedAppointment.setEmail(resultSet.getString("email"));
                fetchedAppointment.setStart(startLocal);
                fetchedAppointment.setEnd(endLocal);
                fetchedAppointment.setPatient(patient.getPatient(resultSet.getInt("patientId")));
                
                appointments.add(fetchedAppointment);
            }
        } catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        
        return appointments;
    }
    
    
    // Remove an appointment.
    @Override
    public void removeAppointment(Appointment appointment) {
        String removeAppointmentQuery = "DELETE FROM appointment WHERE appointmentId = ?";
    
        try {
            PreparedStatement statement = conn.prepareStatement(removeAppointmentQuery);
            statement.setInt(1, appointment.getAppointmentId());
            statement.executeUpdate();
        } catch(SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    
    // Update an appointment.
    @Override
    public void updateAppointment(Appointment appointment) {
        String updateAppointmentQuery = String.join(" ",
            "UPDATE appointment",
            "SET patientId=?, title=?, description=?, location=?, phone=?, email=?, start=?, end=?, lastUpdate=NOW(), lastUpdateBy=?",
            "WHERE appointmentId = ?"
        );
    
        try {
            PreparedStatement statement = conn.prepareStatement(updateAppointmentQuery);
            ZoneId zone = ZoneId.systemDefault();
            LocalDateTime startUTC = appointment.getStart().atZone(zone).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
            LocalDateTime endUTC = appointment.getEnd().atZone(zone).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
            
            statement.setInt(1, appointment.getPatient().getPatientId());
            statement.setString(2, appointment.getTitle());
            statement.setString(3, appointment.getDesc());
            statement.setString(4, appointment.getLocation());
            statement.setString(5, appointment.getPhone());
            statement.setString(6, appointment.getEmail());
            statement.setTimestamp(7, Timestamp.valueOf(startUTC));
            statement.setTimestamp(8, Timestamp.valueOf(endUTC));
            statement.setString(9, MainApp.user.username);
            statement.setInt(10, appointment.getAppointmentId());
            statement.executeUpdate();
            
        } catch(SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
