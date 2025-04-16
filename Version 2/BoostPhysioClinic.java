// Main class (simplified)
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BoostPhysioClinic {
    static List<Patient> patients = new ArrayList<>();
    static List<Physiotherapist> physiotherapists = new ArrayList<>();
    static List<Timetable> timetable = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initializeSampleData();

        while (true) {
            System.out.println("\n--- Boost Physio Clinic (BPC) ---");
            System.out.println("1. Book Appointment");
            System.out.println("2. Cancel Appointment");
            System.out.println("3. Add Patient");
            System.out.println("4. View Timetable");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    bookAppointment();
                    break;
                case 2:
                    cancelAppointment();
                    break;
                case 3:
                    addPatient();
                    break;
                case 4:
                    viewTimetable();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    static void initializeSampleData() {
        // Sample Patients (3 entries)
        patients.add(new Patient("P1", "John Doe", "123-456-7890"));
        patients.add(new Patient("P2", "Jane Smith", "987-654-3210"));
        patients.add(new Patient("P3", "Alice Johnson", "555-123-4567"));

        // Sample Physiotherapists (2 entries)
        physiotherapists.add(new Physiotherapist("PT1", "Dr. Alice", "555-123-4567"));
        physiotherapists.add(new Physiotherapist("PT2", "Dr. Bob", "555-987-6543"));

        // Sample Timetable (4 slots)
        timetable.add(new Timetable("T1", "Monday", "10:00 AM", "Available", "", "Dr. Alice"));
        timetable.add(new Timetable("T2", "Monday", "11:00 AM", "Booked", "John Doe", "Dr. Alice"));
        timetable.add(new Timetable("T3", "Tuesday", "10:00 AM", "Available", "", "Dr. Bob"));
        timetable.add(new Timetable("T4", "Tuesday", "11:00 AM", "Available", "", "Dr. Bob"));
    }

    static void bookAppointment() {
        System.out.println("\nAvailable Appointments:");
        for (Timetable entry : timetable) {
            if (entry.status.equals("Available")) {
                System.out.println("ID: " + entry.id + " | " + entry.day + " " + entry.time + " | Physio: " + entry.physiotherapistName);
            }
        }

        System.out.print("Enter Appointment ID to book: ");
        String id = scanner.nextLine();

        System.out.println("\nPatients:");
        for (Patient patient : patients) {
            System.out.println("ID: " + patient.uniqueID + " | Name: " + patient.fullName);
        }

        System.out.print("Enter Patient ID: ");
        String patientID = scanner.nextLine();

        for (Timetable entry : timetable) {
            if (entry.id.equals(id) && entry.status.equals("Available")) {
                entry.status = "Booked";
                entry.patientName = patients.stream()
                        .filter(p -> p.uniqueID.equals(patientID))
                        .findFirst()
                        .orElse(new Patient("", "Invalid", ""))
                        .fullName;
                System.out.println("Booked successfully!");
                return;
            }
        }
        System.out.println("Invalid ID or slot not available.");
    }

    static void cancelAppointment() {
        System.out.println("\nBooked Appointments:");
        timetable.stream()
                .filter(entry -> entry.status.equals("Booked"))
                .forEach(entry -> System.out.println(
                        "ID: " + entry.id + " | " + entry.day + " " + entry.time +
                                " | Patient: " + entry.patientName
                ));

        System.out.print("Enter Appointment ID to cancel: ");
        String id = scanner.nextLine();

        timetable.stream()
                .filter(entry -> entry.id.equals(id))
                .findFirst()
                .ifPresentOrElse(
                        entry -> {
                            entry.status = "Available";
                            entry.patientName = "";
                            System.out.println("Cancelled successfully!");
                        },
                        () -> System.out.println("Invalid ID.")
                );
    }

    static void addPatient() {
        String newID = "P" + (patients.size() + 1);
        System.out.print("Enter Patient Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Phone: ");
        String phone = scanner.nextLine();
        patients.add(new Patient(newID, name, phone));
        System.out.println("Added Patient ID: " + newID);
    }

    static void viewTimetable() {
        System.out.println("\n--- Timetable ---");
        timetable.forEach(entry -> System.out.println(
                entry.day + " " + entry.time + " | " +
                        entry.physiotherapistName + " | " +
                        "Status: " + entry.status +
                        (entry.status.equals("Booked") ? " (Patient: " + entry.patientName + ")" : "")
        ));
    }
}