package c868.DAOs;

import c868.Models.Appointment;
import java.time.LocalDateTime;
import javafx.collections.ObservableList;


public interface InterfaceAppointment {
    public int addAppointment(Appointment appointment);
    public int getMaxId();
    public ObservableList<Appointment> getAllAppointments();
    public ObservableList<Appointment> getAppointmentsInRange(LocalDateTime start, LocalDateTime end);
    public ObservableList<Appointment> getOverlappingAppointments(LocalDateTime start, LocalDateTime end);
    public void removeAppointment(Appointment appointment);
    public void updateAppointment(Appointment appointment);
}