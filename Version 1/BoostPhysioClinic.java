import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BoostPhysioClinic {
    static List<Patient> patients = new ArrayList<>();
    static List<Physiotherapist> physiotherapists = new ArrayList<>();
    static List<Timetable> timetable = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initializeData();

        while (true) {
            System.out.println("\n--- Boost Physio Clinic (BPC) ---");
            System.out.println("1. Book Appointment");
            System.out.println("2. View Timetable");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    bookAppointment();
                    break;
                case 2:
                    viewTimetable();
                    break;
                case 3:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    static void initializeData() {
        // Sample data for patients
        patients.add(new Patient("P1", "John Doe", "123 Main St", "123-456-7890"));
        patients.add(new Patient("P2", "Jane Smith", "456 Elm St", "987-654-3210"));

        // Sample data for physiotherapists
        physiotherapists.add(new Physiotherapist("PT1", "Dr. Alice", "789 Oak St", "555-123-4567", "Physiotherapist"));
        physiotherapists.add(new Physiotherapist("PT2", "Dr. Bob", "321 Pine St", "555-987-6543", "Rehabilitation"));

        // Sample timetable entries
        timetable.add(new Timetable("T1", "Monday", "10:00 AM", "Available", "", "Dr. Alice", ""));
        timetable.add(new Timetable("T2", "Monday", "11:00 AM", "Available", "", "Dr. Bob", ""));
    }

    static void bookAppointment() {
        System.out.println("\nAvailable Appointments:");
        for (Timetable entry : timetable) {
            if (entry.status.equals("Available")) {
                System.out.println("ID: " + entry.id + ", Day: " + entry.day + ", Time: " + entry.time + ", Physiotherapist: " + entry.physiotherapistName);
            }
        }

        System.out.print("Enter Appointment ID to book: ");
        String appointmentID = scanner.nextLine();

        System.out.println("\nExisting Patients:");
        for (Patient patient : patients) {
            System.out.println("ID: " + patient.uniqueID + ", Name: " + patient.fullName);
        }

        System.out.print("Enter Patient ID to book appointment: ");
        String patientID = scanner.nextLine();

        Patient selectedPatient = null;
        for (Patient patient : patients) {
            if (patient.uniqueID.equals(patientID)) {
                selectedPatient = patient;
                break;
            }
        }

        if (selectedPatient == null) {
            System.out.println("Invalid Patient ID. Please try again.");
            return;
        }

        for (Timetable entry : timetable) {
            if (entry.id.equals(appointmentID) && entry.status.equals("Available")) {
                entry.status = "Booked";
                entry.patientName = selectedPatient.fullName;
                System.out.println("Appointment booked successfully for " + selectedPatient.fullName + "!");
                return;
            }
        }

        System.out.println("Invalid Appointment ID or Appointment not available.");
    }

    static void viewTimetable() {
        System.out.println("\n--- Timetable ---");
        for (Timetable entry : timetable) {
            System.out.println("ID: " + entry.id + ", Day: " + entry.day + ", Time: " + entry.time + ", Status: " + entry.status + ", Patient: " + entry.patientName + ", Physiotherapist: " + entry.physiotherapistName);
        }
    }
}
