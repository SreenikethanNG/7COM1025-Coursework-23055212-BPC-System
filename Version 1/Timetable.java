public class Timetable {
    String id;
    String day;
    String time;
    String status;
    String patientName;
    String physiotherapistName;
    String therapy;

    public Timetable(String id, String day, String time, String status, String patientName, String physiotherapistName, String therapy) {
        this.id = id;
        this.day = day;
        this.time = time;
        this.status = status;
        this.patientName = patientName;
        this.physiotherapistName = physiotherapistName;
        this.therapy = therapy;
    }
}
