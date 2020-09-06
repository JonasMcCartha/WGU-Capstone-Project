package c868.Controllers;

import static c868.Controllers.AbstractCalendarController.modifiedAppointment;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import java.net.URL;
import javafx.geometry.Pos;

import c868.Models.Appointment;


public class MonthlyCalendarController extends AbstractCalendarController implements Initializable {
    private YearMonth selectedMonth;
    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    DateTimeFormatter yearMonthFormat = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault());
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        selectedMonth = YearMonth.now();
        setCalendarView();
    }

    
    // Show date screen.
    private BorderPane getDatePane(int dayOfMonth, LocalDate currentDate) {
        BorderPane datePane = new BorderPane();
        Label dateLabel = new Label();
        dateLabel.setText(Integer.toString(dayOfMonth));
        datePane.setTop(dateLabel);
        BorderPane.setAlignment(dateLabel, Pos.TOP_RIGHT);
        ObservableList<Appointment> filteredAppointments = appointments.stream()
            .filter(a -> a.getStart().toLocalDate().equals(currentDate))
            .collect(Collectors.toCollection(FXCollections::observableArrayList));
        ListView<Appointment> listView = new ListView<>(filteredAppointments); 
        listView.getSelectionModel().selectedItemProperty()
            .addListener((ObservableValue<? extends Appointment> obs, Appointment oldValue, Appointment newValue) -> {
                modifiedAppointment = newValue;
        });
        datePane.setCenter(listView);
        return datePane;
    }

    
    // Get span between first day of month and Sunday.
    private int getMonthOffset() {
        LocalDate firstDay = selectedMonth.atDay(1);
        DayOfWeek day = firstDay.getDayOfWeek();
        int offset = (day.getValue() - 1) % 7;
        LocalDate firstSunday = firstDay.minusDays(offset);
        Period span = Period.between(firstDay, firstSunday);
        return span.getDays();
    }
    
    
    // Show next month on calendar.
    @FXML
    void handleNextMonth() {
        selectedMonth = selectedMonth.plusMonths(1);
        setCalendarView();
    }
  
    
    // Show previous month on calendar.
    @FXML
    void handlePreviousMonth() {
        selectedMonth = selectedMonth.minusMonths(1);
        setCalendarView();
    }
    
    
    // Show calendar screen.
    @Override
    public void setCalendarView() {
        resetCalendar();
        CalendarLabel.setText(selectedMonth.format(yearMonthFormat));
        LocalDate firstDate = selectedMonth.atDay(1);
        LocalDate lastDate = selectedMonth.atEndOfMonth();
        LocalDateTime startDatetime = LocalDateTime.of(firstDate, LocalTime.MIDNIGHT);
        LocalDateTime endDatetime = LocalDateTime.of(lastDate, LocalTime.MIDNIGHT);
        appointments = SQLAppointment.getAppointmentsInRange(startDatetime, endDatetime);
        int monthlyOffset = getMonthOffset();
        for (LocalDate date = firstDate; date.isBefore(lastDate.plusDays(1)); date = date.plusDays(1)) {
            int dayOfMonth = date.getDayOfMonth();
            int dayOfMonthMinusOffset = dayOfMonth - monthlyOffset;
            BorderPane datePane = getDatePane(dayOfMonth, date);
            CalendarPane.add(datePane, dayOfMonthMinusOffset % 7, dayOfMonthMinusOffset / 7);
        }
    }
}
