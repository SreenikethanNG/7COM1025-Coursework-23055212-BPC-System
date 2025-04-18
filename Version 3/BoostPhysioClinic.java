import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BoostPhysioClinic {
    static List<Patient> patients = new ArrayList<>();
    static List<Physiotherapist> physiotherapists = new ArrayList<>();
    static List<Timetable> timetable = new ArrayList<>();
    static List<Treatment> treatments = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initializeData();

        while(true) {
            System.out.println("\n--- Boost Physio Clinic Management System ---");
            System.out.println("1. Book Appointment");
            System.out.println("2. Modify/Cancel Appointment");
            System.out.println("3. Register New Patient");
            System.out.println("4. Add Physiotherapist");
            System.out.println("5. View Full Timetable");
            System.out.println("6. View All Bookings");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch(choice) {
                case 1: bookAppointment(); break;
                case 2: modifyAppointment(); break;
                case 3: registerPatient(); break;
                case 4: addPhysiotherapist(); break;
                case 5: viewFullTimetable(); break;
                case 6: viewAllBookings(); break;
                case 7:
                    System.out.println("Exiting system...");
                    System.exit(0);
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }

    static void initializeData() {
        // Initialize sample patients
        patients.add(new Patient("P001", "Sarah Johnson", "12 Oak Street", "555-0101"));
        patients.add(new Patient("P002", "Michael Chen", "34 Maple Road", "555-0202"));

        // Initialize physiotherapists
        physiotherapists.add(new Physiotherapist("PT001", "Dr. Emily Wilson",
                "Clinic Central", "555-1001", "Sports Rehabilitation"));
        physiotherapists.add(new Physiotherapist("PT002", "Dr. James Thompson",
                "Clinic Central", "555-1002", "Post-Surgical Therapy"));

        // Create initial timetable
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        int slotID = 1;
        for(String day : days) {
            timetable.add(new Timetable("T" + slotID++, day, "09:00 AM", "Available", "", "Dr. Emily Wilson", ""));
            timetable.add(new Timetable("T" + slotID++, day, "10:30 AM", "Available", "", "Dr. James Thompson", ""));
        }
    }

    static void bookAppointment() {
        System.out.println("\n== Available Appointment Slots ==");
        timetable.stream()
                .filter(slot -> slot.status.equals("Available"))
                .forEach(slot -> System.out.printf(
                        "ID: %-3s | %-9s | %-8s | Physio: %-15s%n",
                        slot.id, slot.day, slot.time, slot.physiotherapistName));

        System.out.print("\nEnter appointment ID to book: ");
        String appointmentID = scanner.nextLine();

        System.out.println("\n== Registered Patients ==");
        patients.forEach(patient -> System.out.printf(
                "ID: %-4s | Name: %-15s | Phone: %s%n",
                patient.uniqueID, patient.fullName, patient.telephoneNumber));

        System.out.print("Enter patient ID: ");
        String patientID = scanner.nextLine();

        Patient patient = patients.stream()
                .filter(p -> p.uniqueID.equals(patientID))
                .findFirst()
                .orElse(null);

        if(patient == null) {
            System.out.println("Error: Invalid patient ID!");
            return;
        }

        System.out.println("\n== Therapy Types ==");
        String[] therapies = {
                "Neural Mobilization",
                "Therapeutic Exercise",
                "Manual Therapy",
                "Electrotherapy",
                "Hydrotherapy"
        };
        for(int i=0; i<therapies.length; i++) {
            System.out.println((i+1) + ". " + therapies[i]);
        }
        System.out.print("Select therapy (1-5): ");
        int therapyChoice = scanner.nextInt();
        scanner.nextLine();

        String selectedTherapy = therapyChoice >=1 && therapyChoice <=5 ?
                therapies[therapyChoice-1] : "General Therapy";

        boolean bookingSuccess = false;
        for(Timetable slot : timetable) {
            if(slot.id.equals(appointmentID) && slot.status.equals("Available")) {
                slot.status = "Booked";
                slot.patientName = patient.fullName;
                slot.therapy = selectedTherapy;
                treatments.add(new Treatment(
                        selectedTherapy,
                        slot.day,
                        slot.time,
                        slot.physiotherapistName
                ));
                bookingSuccess = true;
                break;
            }
        }

        if(bookingSuccess) {
            System.out.println("\nSuccessfully booked appointment!");
            System.out.printf("Patient: %s%nTherapy: %s%n",
                    patient.fullName, selectedTherapy);
        } else {
            System.out.println("Failed to book appointment. Slot may be taken.");
        }
    }

    static void modifyAppointment() {
        System.out.println("\n== Current Bookings ==");
        timetable.stream()
                .filter(slot -> slot.status.equals("Booked"))
                .forEach(slot -> System.out.printf(
                        "ID: %-3s | %-9s | %-8s | Patient: %-15s | Therapy: %s%n",
                        slot.id, slot.day, slot.time, slot.patientName, slot.therapy));

        System.out.print("\nEnter appointment ID to modify: ");
        String appointmentID = scanner.nextLine();

        Timetable slot = timetable.stream()
                .filter(s -> s.id.equals(appointmentID))
                .findFirst()
                .orElse(null);

        if(slot == null || !slot.status.equals("Booked")) {
            System.out.println("Invalid or unavailable appointment ID!");
            return;
        }

        System.out.println("\n1. Change Patient\n2. Change Therapy\n3. Cancel Appointment");
        System.out.print("Select modification type: ");
        int modChoice = scanner.nextInt();
        scanner.nextLine();

        switch(modChoice) {
            case 1:
                System.out.println("\n== Available Patients ==");
                patients.forEach(p -> System.out.printf(
                        "ID: %s | Name: %s%n", p.uniqueID, p.fullName));
                System.out.print("Enter new patient ID: ");
                String newPatientID = scanner.nextLine();

                Patient newPatient = patients.stream()
                        .filter(p -> p.uniqueID.equals(newPatientID))
                        .findFirst()
                        .orElse(null);

                if(newPatient != null) {
                    slot.patientName = newPatient.fullName;
                    System.out.println("Patient updated successfully!");
                } else {
                    System.out.println("Invalid patient ID!");
                }
                break;

            case 2:
                System.out.println("\n== Therapy Types ==");
                System.out.println("1. Neural Mobilization\n2. Therapeutic Exercise\n" +
                        "3. Manual Therapy\n4. Electrotherapy\n5. Hydrotherapy");
                System.out.print("Select new therapy: ");
                int therapyChoice = scanner.nextInt();
                scanner.nextLine();

                String[] therapies = {
                        "Neural Mobilization",
                        "Therapeutic Exercise",
                        "Manual Therapy",
                        "Electrotherapy",
                        "Hydrotherapy"
                };
                slot.therapy = (therapyChoice >=1 && therapyChoice <=5) ?
                        therapies[therapyChoice-1] : "General Therapy";
                System.out.println("Therapy updated!");
                break;

            case 3:
                slot.status = "Available";
                slot.patientName = "";
                slot.therapy = "";
                treatments.removeIf(t ->
                        t.date.equals(slot.day) &&
                                t.time.equals(slot.time) &&
                                t.physiotherapistName.equals(slot.physiotherapistName));
                System.out.println("Appointment cancelled successfully!");
                break;

            default:
                System.out.println("Invalid modification choice!");
        }
    }

    static void registerPatient() {
        String newID = "P" + String.format("%03d", patients.size()+1);
        System.out.printf("\n== Registering New Patient [%s] ==%n", newID);

        System.out.print("Full Name: ");
        String name = scanner.nextLine();
        System.out.print("Address: ");
        String address = scanner.nextLine();
        System.out.print("Phone Number: ");
        String phone = scanner.nextLine();

        patients.add(new Patient(newID, name, address, phone));
        System.out.printf("Successfully registered %s!%n", name);
    }

    static void addPhysiotherapist() {
        System.out.print("\nEnter Physiotherapist ID (PTXXX format): ");
        String ptID = scanner.nextLine();
        System.out.print("Full Name: ");
        String name = scanner.nextLine();
        System.out.print("Specialization: ");
        String specialization = scanner.nextLine();

        physiotherapists.add(new Physiotherapist(
                ptID, name, "Clinic Central", "555-XXXX", specialization));

        // Add availability slots
        System.out.print("Add availability day (e.g., Monday): ");
        String day = scanner.nextLine();
        System.out.print("Time slot (e.g., 02:00 PM): ");
        String time = scanner.nextLine();

        String newSlotID = "T" + (timetable.size()+1);
        timetable.add(new Timetable(newSlotID, day, time, "Available", "", name, ""));

        System.out.printf("Added %s with %s availability!%n", name, day);
    }

    static void viewFullTimetable() {
        System.out.println("\n== Complete Clinic Timetable ==");
        System.out.println("ID  | Day       | Time     | Status     | Patient          | Physiotherapist      | Therapy");
        System.out.println("------------------------------------------------------------------------------------------");
        timetable.forEach(slot -> System.out.printf(
                "%-3s | %-9s | %-8s | %-10s | %-15s | %-20s | %s%n",
                slot.id, slot.day, slot.time, slot.status,
                slot.patientName, slot.physiotherapistName, slot.therapy));
    }

    static void viewAllBookings() {
        System.out.println("\n== Active Bookings ==");
        treatments.forEach(treatment -> System.out.printf(
                "Date: %-9s | Time: %-8s | Physio: %-15s | Therapy: %s%n",
                treatment.date, treatment.time,
                treatment.physiotherapistName, treatment.name));
    }
}