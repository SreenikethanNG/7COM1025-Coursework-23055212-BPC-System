//Timetable Class
import java.time.*;
import java.time.format.*;
public class Timetable {
    public String day;
    String id;
    LocalDate date;
    String time;
    String status;
    String patientName;
    String physiotherapistName;
    String therapy;

    public Timetable(String id, LocalDate date, String time, String status,
                     String patientName, String physiotherapistName, String therapy) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.status = status;
        this.patientName = patientName;
        this.physiotherapistName = physiotherapistName;
        this.therapy = therapy;
    }

    public String getDayName() {
        return date.getDayOfWeek().toString();
    }

}