// Timetable class
public class Timetable {
    String id;
    String day;
    String time;
    String status;
    String patientName;
    String physiotherapistName;

    public Timetable(String id, String day, String time, String status, String patientName, String physiotherapistName) {
        this.id = id;
        this.day = day;
        this.time = time;
        this.status = status;
        this.patientName = patientName;
        this.physiotherapistName = physiotherapistName;
    }
}
