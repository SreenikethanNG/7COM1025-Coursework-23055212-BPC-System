//Main Class BPC System
import java.util.*;
import java.time.*;
import java.time.format.*;
import java.util.stream.Collectors;

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
            System.out.println("2. Change/Cancel Appointment");
            System.out.println("3. Add Patient");
            System.out.println("4. Remove Patient");
            System.out.println("5. View Timetable");
            System.out.println("6. View Bookings");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: bookAppointment(); break;
                case 2: changeCancelAppointment(); break;
                case 3: addPatient(); break;
                case 4: removePatient(); break;
                case 5: viewTimetable(); break;
                case 6: viewBookings(); break;
                case 7: System.exit(0);
                default: System.out.println("Invalid choice!");
            }
        }
    }

    static void initializeData() {
        patients.clear();
        physiotherapists.clear();
        timetable.clear();

        // 15 Patients
        patients.addAll(Arrays.asList(
                new Patient("P1", "Emma Johnson", "12 Maple St", "5550101"),
                new Patient("P2", "Liam Smith", "34 Oak Ave", "5550102"),
                new Patient("P3", "Olivia Williams", "56 Pine Rd", "5550103"),
                new Patient("P4", "Noah Brown", "78 Elm St", "5550104"),
                new Patient("P5", "Ava Jones", "90 Cedar Ln", "5550105"),
                new Patient("P6", "William Garcia", "112 Birch Dr", "5550106"),
                new Patient("P7", "Sophia Miller", "134 Spruce Way", "5550107"),
                new Patient("P8", "Benjamin Davis", "156 Aspen Ct", "5550108"),
                new Patient("P9", "Isabella Rodriguez", "178 Redwood Blvd", "5550109"),
                new Patient("P10", "James Wilson", "190 Sequoia Pl", "5550110"),
                new Patient("P11", "Mia Martinez", "212 Willow Way", "5550111"),
                new Patient("P12", "Ethan Anderson", "234 Cypress Dr", "5550112"),
                new Patient("P13", "Charlotte Taylor", "256 Magnolia Ln", "5550113"),
                new Patient("P14", "Alexander Thomas", "278 Juniper Ave", "5550114"),
                new Patient("P15", "Amelia Hernandez", "300 Sycamore St", "5550115")
        ));

        // 5 Physiotherapists
        physiotherapists.addAll(Arrays.asList(
                new Physiotherapist("PT1", "Dr. Sarah Chen", "101 Wellness Way", "5550201", "Physiotherapy"),
                new Physiotherapist("PT2", "Dr. Michael Patel", "202 Recovery Rd", "5550202", "Rehabilitation"),
                new Physiotherapist("PT3", "Dr. Emily Wilson", "303 Mobility Ln", "5550203", "Osteopathy"),
                new Physiotherapist("PT4", "Dr. David Kim", "404 Flex St", "5550204", "Physiotherapy"),
                new Physiotherapist("PT5", "Dr. Jessica Brown", "505 Balance Ave", "5550205", "Rehabilitation")
        ));

        // Creating 4-week timetable
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.with(DayOfWeek.MONDAY);
        int slotId = 1;

        for (int week = 0; week < 4; week++) {
            for (int day = 0; day < 5; day++) { // Monday-Friday
                LocalDate currentDate = startDate.plusDays(day + (week * 7));
                String[] timeSlots = {"09:00 AM", "11:00 AM", "02:00 PM", "04:00 PM"};

                for (String time : timeSlots) {
                    timetable.add(new Timetable(
                            "T" + slotId++,
                            currentDate,
                            time,
                            "Available",
                            "",
                            physiotherapists.get((slotId-1) % physiotherapists.size()).fullName,
                            ""
                    ));
                }
            }
        }

        Random rand = new Random();
        List<Integer> availableSlots = new ArrayList<>();
        for (int i = 0; i < timetable.size(); i++) {
            availableSlots.add(i);
        }
        Collections.shuffle(availableSlots);

        String[] therapies = {
                "Neural mobilisation",
                "Acupuncture",
                "Massage",
                "Mobilisation of the spine and joints",
                "Pool rehabilitation"
        };

        // Mixed status pre-bookings
        String[] statusOptions = {"Booked", "Booked", "Booked", "Available", "Available"}; // 60% Booked, 40% Available
        int preBookCount = 15;

        for(int i = 0; i < preBookCount; i++) {
            int slotIndex = availableSlots.get(i);
            Timetable slot = timetable.get(slotIndex);

            String status = statusOptions[rand.nextInt(statusOptions.length)];
            slot.status = status;

            if(status.equals("Booked")) {
                slot.patientName = patients.get(i % patients.size()).fullName;
                slot.therapy = therapies[rand.nextInt(therapies.length)];

                String neededExpertise = physiotherapists.stream()
                        .filter(p -> p.fullName.equals(slot.physiotherapistName))
                        .findFirst()
                        .get()
                        .profession;

                List<Physiotherapist> matchingPhysios = physiotherapists.stream()
                        .filter(p -> p.profession.equals(neededExpertise))
                        .collect(Collectors.toList());

                slot.physiotherapistName = matchingPhysios.get(rand.nextInt(matchingPhysios.size())).fullName;
            } else { // Available status
                slot.patientName = "";
                slot.therapy = "";
            }
        }
    }

    static void removePatient() {
        System.out.println("\n--- Remove Patient ---");

        // Showing all patients
        System.out.println("\nCurrent Patients:");
        patients.forEach(p -> System.out.println(p.uniqueID + ": " + p.fullName));

        System.out.print("Enter Patient ID to remove: ");
        String patientID = scanner.nextLine().trim();

        Optional<Patient> patient = patients.stream()
                .filter(p -> p.uniqueID.equalsIgnoreCase(patientID))
                .findFirst();

        if (!patient.isPresent()) {
            System.out.println("Patient not found!");
            return;
        }

        // Checking for active bookings
        boolean hasActiveAppointments = timetable.stream()
                .anyMatch(t -> t.patientName.equalsIgnoreCase(patient.get().fullName) &&
                        t.status.equals("Booked"));

        if (hasActiveAppointments) {
            System.out.println("Cannot remove patient with active appointments!");
            System.out.println("Cancel appointments first before removing patient.");
            return;
        }

        System.out.print("Are you sure you want to permanently remove "
                + patient.get().fullName + "? (Y/N): ");
        String confirm = scanner.nextLine().trim().toUpperCase();

        if (confirm.equals("Y")) {
            patients.remove(patient.get());
            System.out.println("Patient removed successfully!");
        } else {
            System.out.println("Removal cancelled.");
        }
    }

    static void bookAppointment() {
        System.out.println("\nFind Appointments By:");
        System.out.println("1. Expertise\n2. Physiotherapist\n3. Show All");
        System.out.print("Choice: ");
        int filter = scanner.nextInt();
        scanner.nextLine();

        List<Timetable> slots = new ArrayList<>();
        switch(filter) {
            case 1:
                System.out.println("\nSelect Expertise:");
                System.out.println("1. Physiotherapy\n2. Rehabilitation\n3. Osteopathy");
                System.out.print("Choice: ");
                int expertChoice = scanner.nextInt();
                scanner.nextLine();

                String expertise = switch(expertChoice) {
                    case 1 -> "Physiotherapy";
                    case 2 -> "Rehabilitation";
                    case 3 -> "Osteopathy";
                    default -> "";
                };

                if(!expertise.isEmpty()) {
                    Map<String, String> physioExpertise = physiotherapists.stream()
                            .collect(Collectors.toMap(p -> p.fullName, p -> p.profession));
                    slots = timetable.stream()
                            .filter(t -> t.status.equals("Available") &&
                                    physioExpertise.get(t.physiotherapistName).equals(expertise))
                            .collect(Collectors.toList());
                }
                break;

            case 2:
                System.out.println("\nPhysiotherapists:");
                physiotherapists.forEach(p ->
                        System.out.println(p.uniqueID + ": " + p.fullName));
                System.out.print("Enter ID: ");
                String physioID = scanner.nextLine();

                Optional<Physiotherapist> physio = physiotherapists.stream()
                        .filter(p -> p.uniqueID.equalsIgnoreCase(physioID))
                        .findFirst();

                if (physio.isPresent()) {
                    slots = timetable.stream()
                            .filter(t -> t.status.equals("Available") &&
                                    t.physiotherapistName.equals(physio.get().fullName))
                            .collect(Collectors.toList());
                }
                break;

            case 3:
                slots = timetable.stream()
                        .filter(t -> t.status.equals("Available"))
                        .collect(Collectors.toList());
                break;
        }

        // Display slots
        System.out.println("\nAvailable Appointments:");
        System.out.println("+----+------------+-----------+----------------------+---------------------+");
        System.out.println("| ID |    Date    |   Time    | Physiotherapist      | Expertise           |");
        System.out.println("+----+------------+-----------+----------------------+---------------------+");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM dd");
        Map<String, String> expertiseMap = physiotherapists.stream()
                .collect(Collectors.toMap(p -> p.fullName, p -> p.profession));

        slots.forEach(s -> {
            String expertise = expertiseMap.get(s.physiotherapistName);
            System.out.printf("| %-2s | %-10s | %-9s | %-20s | %-19s |\n",
                    s.id, s.date.format(dtf), s.time, s.physiotherapistName, expertise);
        });
        System.out.println("+----+------------+-----------+----------------------+---------------------+");

        // Book slot
        System.out.print("Enter Appointment ID: ");
        String apptID = scanner.nextLine();
        Optional<Timetable> slot = slots.stream()
                .filter(s -> s.id.equals(apptID))
                .findFirst();

        if (!slot.isPresent()) {
            System.out.println("Invalid ID!");
            return;
        }

        // Filtering patients without active bookings
        List<Patient> availablePatients = patients.stream()
                .filter(p -> timetable.stream()
                        .noneMatch(t -> t.patientName.equals(p.fullName) &&
                                (t.status.equals("Booked") || t.status.equals("Attended"))))
                .collect(Collectors.toList());

        if (availablePatients.isEmpty()) {
            System.out.println("No patients available to book (all have active appointments).");
            return;
        }

        // Selecting patient from available list
        System.out.println("\nAvailable Patients:");
        availablePatients.forEach(p -> System.out.println(p.uniqueID + ": " + p.fullName));
        System.out.print("Enter Patient ID: ");
        String patientID = scanner.nextLine();
        Optional<Patient> patient = availablePatients.stream()
                .filter(p -> p.uniqueID.equalsIgnoreCase(patientID))
                .findFirst();

        if (!patient.isPresent()) {
            System.out.println("Invalid Patient ID or patient has an existing booking!");
            return;
        }

        // Check existing bookings
        boolean hasBooking = timetable.stream()
                .anyMatch(t -> t.patientName.equals(patient.get().fullName) &&
                        !t.status.equals("Available"));
        if (hasBooking) {
            System.out.println("Patient already has an active booking!");
            return;
        }

        // Selecting therapy
        System.out.println("\nSelect Therapy:");
        System.out.println("1. Neural mobilisation");
        System.out.println("2. Acupuncture");
        System.out.println("3. Massage");
        System.out.println("4. Mobilisation of the spine and joints");
        System.out.println("5. Pool rehabilitation");
        System.out.print("Choice: ");
        int therapyChoice = scanner.nextInt();
        scanner.nextLine();

        String therapy = switch(therapyChoice) {
            case 1 -> "Neural mobilisation";
            case 2 -> "Acupuncture";
            case 3 -> "Massage";
            case 4 -> "Mobilisation of the spine and joints";
            case 5 -> "Pool rehabilitation";
            default -> "Massage";
        };

        // Updating timetable
        Timetable t = slot.get();
        t.status = "Booked";
        t.patientName = patient.get().fullName;
        t.therapy = therapy;
        System.out.println("\nBooking Successful!");
        System.out.println("Patient: " + t.patientName);
        System.out.println("Date: " + t.date.format(DateTimeFormatter.ISO_DATE) + " " + t.time);
        System.out.println("Physio: " + t.physiotherapistName);
        System.out.println("Therapy: " + t.therapy);
    }

    // Helper methods
    private static String getInput(String prompt, String field) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.println(field + " cannot be empty!");
        }
    }

    private static String getPhone() {
        while (true) {
            String input = getInput("Enter Phone: ", "Phone")
                    .replaceAll("[^0-9]", "");
            if (input.length() >= 7 && input.length() <= 15) return input;
            System.out.println("Must be 7-15 digits!");
        }
    }

    private static String getDay() {
        while (true) {
            String day = getInput("Enter Day (e.g Monday): ", "Day");
            try {
                DayOfWeek.valueOf(day.toUpperCase());
                return day;
            } catch (Exception e) {
                System.out.println("Invalid day! Use full name (Monday-Friday)");
            }
        }
    }

    private static String getTime() {
        while (true) {
            String time = getInput("Enter Time (e.g 09:00 AM): ", "Time");
            if (time.matches("^(1[0-2]|0?[9]):[0-5][0-9] (AM|PM)$")) return time;
            System.out.println("Invalid time format!");
        }
    }

    static void viewTimetable() {
        System.out.println("\n--- View Timetable / Report ---");
        System.out.println("1. View Timetable");
        System.out.println("2. Generate Physiotherapist Report (Attended Appointments)");
        System.out.print("Choose an option: ");
        int subChoice = scanner.nextInt();
        scanner.nextLine();

        switch (subChoice) {
            case 1:
                displayTimetable(); // Existing timetable display
                break;
            case 2:
                generatePhysioReport();
                break;
            default:
                System.out.println("Invalid choice!");
        }
    }

    static void displayTimetable() {
        System.out.println("\n════════════════════════════════ TIMETABLE ═══════════════════════════════");
        System.out.println("| Date       | Day       | Time      | Status      | Patient          | Physiotherapist      | Therapy");
        System.out.println("|------------|-----------|-----------|-------------|------------------|----------------------|----------");

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        timetable.forEach(t -> {
            String status = t.status.equals("Available") ? "⚪ Available" :
                    t.status.equals("Booked") ? "🔵 Booked" :
                            "🟢 Attended";
            System.out.printf("| %-10s | %-9s | %-9s | %-11s | %-16s | %-20s | %-25s\n",
                    t.date.format(dtf),
                    t.getDayName(),
                    t.time,
                    status,
                    t.patientName.isEmpty() ? "-" : t.patientName,
                    t.physiotherapistName,
                    t.therapy.isEmpty() ? "-" : t.therapy
            );
        });
        System.out.println("══════════════════════════════════════════════════════════════════════════════════════════════════════════");
    }



    static void viewBookings() {
        List<Timetable> bookings = timetable.stream()
                .filter(t -> t.status.equals("Booked")) // Only show active bookings
                .collect(Collectors.toList());

        if (bookings.isEmpty()) {
            System.out.println("\nNo active bookings found.");
            return;
        }

        // Calculating column widths based on actual data
        int dateWidth = Math.max(10, bookings.stream()
                .mapToInt(t -> t.date.toString().length())
                .max().orElse(10));
        int timeWidth = Math.max(8, bookings.stream()
                .mapToInt(t -> t.time.length())
                .max().orElse(8));
        int patientWidth = Math.max(20, bookings.stream()
                .mapToInt(t -> t.patientName.length())
                .max().orElse(20));
        int physioWidth = Math.max(20, bookings.stream()
                .mapToInt(t -> t.physiotherapistName.length())
                .max().orElse(20));
        int therapyWidth = Math.max(25, bookings.stream()
                .mapToInt(t -> t.therapy.length())
                .max().orElse(25));

        // Build format strings
        String rowFormat = "║ %-" + dateWidth + "s ║ %-" + timeWidth + "s ║ %-" + patientWidth + "s ║ %-" + physioWidth + "s ║ %-" + therapyWidth + "s ║%n";

        // Create borders
        String border = "╔" + "═".repeat(dateWidth+2) + "╦" + "═".repeat(timeWidth+2) + "╦" +
                "═".repeat(patientWidth+2) + "╦" + "═".repeat(physioWidth+2) + "╦" +
                "═".repeat(therapyWidth+2) + "╗";

        System.out.println("\n" + border);
        System.out.printf(rowFormat, "Date", "Time", "Patient", "Physiotherapist", "Therapy");
        System.out.println("╠" + "═".repeat(dateWidth+2) + "╬" + "═".repeat(timeWidth+2) + "╬" +
                "═".repeat(patientWidth+2) + "╬" + "═".repeat(physioWidth+2) + "╬" +
                "═".repeat(therapyWidth+2) + "╣");

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
        bookings.forEach(t -> {
            System.out.printf(rowFormat,
                    dateFormat.format(t.date),
                    t.time,
                    t.patientName,
                    t.physiotherapistName,
                    t.therapy
            );
        });
        System.out.println("╚" + "═".repeat(dateWidth+2) + "╩" + "═".repeat(timeWidth+2) + "╩" +
                "═".repeat(patientWidth+2) + "╩" + "═".repeat(physioWidth+2) + "╩" +
                "═".repeat(therapyWidth+2) + "╝");
    }

    private static String truncate(String text, int maxLength) {
        return text.length() > maxLength ? text.substring(0, maxLength-3) + "..." : text;
    }

    static void addPatient() {
        String id = "P" + (patients.size() + 1);
        System.out.println("New Patient ID: " + id);
        String name = getInput("Enter Name: ", "Name");
        String address = getInput("Enter Address: ", "Address");
        String phone = getPhone();
        patients.add(new Patient(id, name, address, phone));
        System.out.println("Patient added!");
    }

    private static String getExpertiseForTherapy(String therapy) {
        return switch (therapy) {
            case "Neural mobilisation", "Mobilisation of the spine and joints" -> "Physiotherapy";
            case "Pool rehabilitation" -> "Rehabilitation";
            case "Acupuncture", "Massage" -> "Osteopathy";
            default -> "";
        };
    }

    static void generatePhysioReport() {
        // Count attended appointments per physiotherapist
        Map<String, Long> attendedCounts = timetable.stream()
                .filter(t -> t.status.equals("Attended"))
                .collect(Collectors.groupingBy(
                        t -> t.physiotherapistName,
                        Collectors.counting()
                ));

        // Sort physiotherapists by attended count (descending)
        List<Physiotherapist> sortedPhysios = physiotherapists.stream()
                .sorted((p1, p2) -> {
                    long count1 = attendedCounts.getOrDefault(p1.fullName, 0L);
                    long count2 = attendedCounts.getOrDefault(p2.fullName, 0L);
                    return Long.compare(count2, count1); // Sort descending
                })
                .collect(Collectors.toList());

        // Print the report
        System.out.println("\n════════════════════════════════ Physiotherapist Report ═══════════════════════════════");
        System.out.printf("| %-25s | %-20s | %-10s |\n", "Physiotherapist", "Expertise", "Attended");
        System.out.println("═══════════════════════════════════════════════════════════════════════════════════════");

        sortedPhysios.forEach(physio -> {
            long count = attendedCounts.getOrDefault(physio.fullName, 0L);
            System.out.printf("| %-25s | %-20s | %-10d |\n",
                    physio.fullName,
                    physio.profession,
                    count
            );
        });
        System.out.println("═══════════════════════════════════════════════════════════════════════════════════════");
    }

    static void changeCancelAppointment() {
        // Show booked appointments
        System.out.println("\n--- Booked Appointments ---");
        List<Timetable> bookings = timetable.stream()
                .filter(t -> t.status.equals("Booked")) // Only show Booked status
                .collect(Collectors.toList());

        if (bookings.isEmpty()) {
            System.out.println("No active bookings found.");
            return;
        }

        bookings.forEach(entry -> System.out.println(
                "ID: " + entry.id + " | " + entry.date + " " + entry.time +
                        " | Patient: " + entry.patientName +
                        " | Physio: " + entry.physiotherapistName +
                        " | Therapy: " + entry.therapy
        ));

        // Select appointment
        System.out.print("\nEnter Appointment ID to modify: ");
        String id = scanner.nextLine();
        Timetable oldSlot = timetable.stream()
                .filter(t -> t.id.equals(id) && t.status.equals("Booked")) // Only allow Booked slots
                .findFirst()
                .orElse(null);

        if (oldSlot == null) {
            System.out.println("Invalid ID or slot not in modifiable state!");
            return;
        }

        System.out.println("\nSelected Slot: " + oldSlot.date + " (" + oldSlot.getDayName() + ") " + oldSlot.time);
        System.out.println("1. Reassign Therapist/Time/Therapy");
        System.out.println("2. Mark as Attended");
        System.out.println("3. Cancel (Make Available)");
        System.out.print("Choose an option: ");
        int option = scanner.nextInt();
        scanner.nextLine();


        switch (option) {
            case 1: // Reassign
                System.out.println("\n--- Reassign Process ---");
                System.out.println("Current Patient: " + oldSlot.patientName);

                // Filter available slots by expertise/therapy
                System.out.println("\nFind Appointments By:");
                System.out.println("1. Expertise");
                System.out.println("2. Therapy");
                System.out.println("3. Show All");
                System.out.print("Choice: ");
                int filterChoice = scanner.nextInt();
                scanner.nextLine();

                List<Timetable> availableSlots = new ArrayList<>();
                switch (filterChoice) {
                    case 1: // Filter by Expertise
                        System.out.println("\nSelect Expertise:");
                        System.out.println("1. Physiotherapy\n2. Rehabilitation\n3. Osteopathy");
                        System.out.print("Choice: ");
                        int expertChoice = scanner.nextInt();
                        scanner.nextLine();

                        String expertise = switch (expertChoice) {
                            case 1 -> "Physiotherapy";
                            case 2 -> "Rehabilitation";
                            case 3 -> "Osteopathy";
                            default -> "";
                        };

                        if (!expertise.isEmpty()) {
                            Map<String, String> physioExpertise = physiotherapists.stream()
                                    .collect(Collectors.toMap(p -> p.fullName, p -> p.profession));
                            availableSlots = timetable.stream()
                                    .filter(t -> t.status.equals("Available") &&
                                            physioExpertise.get(t.physiotherapistName).equals(expertise))
                                    .collect(Collectors.toList());
                        }
                        break;

                    case 2: // Filter by Therapy
                        System.out.println("\nSelect Therapy:");
                        System.out.println("1. Neural mobilisation");
                        System.out.println("2. Acupuncture");
                        System.out.println("3. Massage");
                        System.out.println("4. Mobilisation of the spine and joints");
                        System.out.println("5. Pool rehabilitation");
                        System.out.print("Choice: ");
                        int therapyFilterChoice = scanner.nextInt();
                        scanner.nextLine();

                        String therapy = switch (therapyFilterChoice) {
                            case 1 -> "Neural mobilisation";
                            case 2 -> "Acupuncture";
                            case 3 -> "Massage";
                            case 4 -> "Mobilisation of the spine and joints";
                            case 5 -> "Pool rehabilitation";
                            default -> oldSlot.therapy;
                        };

                        String requiredExpertise = getExpertiseForTherapy(therapy);
                        availableSlots = timetable.stream()
                                .filter(t -> t.status.equals("Available"))
                                .filter(t -> physiotherapists.stream()
                                        .anyMatch(p -> p.fullName.equals(t.physiotherapistName) &&
                                                p.profession.equals(requiredExpertise)))
                                .collect(Collectors.toList());
                        break;

                    case 3: // Show All
                        availableSlots = timetable.stream()
                                .filter(t -> t.status.equals("Available"))
                                .collect(Collectors.toList());
                        break;

                    default:
                        System.out.println("Invalid choice. Showing all slots.");
                        availableSlots = timetable.stream()
                                .filter(t -> t.status.equals("Available"))
                                .collect(Collectors.toList());
                }

                // Display filtered slots
                System.out.println("\nAvailable Appointments:");
                System.out.println("+----+------------+-----------+----------------------+---------------------+");
                System.out.println("| ID |    Date    |   Time    | Physiotherapist      | Expertise           |");
                System.out.println("+----+------------+-----------+----------------------+---------------------+");
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM dd");
                Map<String, String> expertiseMap = physiotherapists.stream()
                        .collect(Collectors.toMap(p -> p.fullName, p -> p.profession));

                availableSlots.forEach(s -> {
                    String expertise = expertiseMap.get(s.physiotherapistName);
                    System.out.printf("| %-2s | %-10s | %-9s | %-20s | %-19s |\n",
                            s.id, s.date.format(dtf), s.time, s.physiotherapistName, expertise);
                });
                System.out.println("+----+------------+-----------+----------------------+---------------------+");

                // Select new slot
                System.out.print("Enter New Appointment ID: ");
                String newId = scanner.nextLine();
                Timetable newSlot = availableSlots.stream()
                        .filter(t -> t.id.equals(newId))
                        .findFirst()
                        .orElse(null);

                if (newSlot == null) {
                    System.out.println("Invalid slot selection!");
                    break;
                }

                // Select therapy
                System.out.println("\nSelect New Therapy:");
                System.out.println("1. Neural mobilisation");
                System.out.println("2. Acupuncture");
                System.out.println("3. Massage");
                System.out.println("4. Mobilisation of the spine and joints");
                System.out.println("5. Pool rehabilitation");
                System.out.print("Choice: ");
                int therapyChoice = scanner.nextInt();
                scanner.nextLine();

                String newTherapy = switch (therapyChoice) {
                    case 1 -> "Neural mobilisation";
                    case 2 -> "Acupuncture";
                    case 3 -> "Massage";
                    case 4 -> "Mobilisation of the spine and joints";
                    case 5 -> "Pool rehabilitation";
                    default -> oldSlot.therapy;
                };

                // Update slots
                newSlot.status = "Booked";
                newSlot.patientName = oldSlot.patientName;
                newSlot.therapy = newTherapy;

                oldSlot.status = "Available";
                oldSlot.patientName = "";
                oldSlot.therapy = "";

                System.out.println("\nReassignment Successful!");
                System.out.println("New Appointment: " + newSlot.date + " " + newSlot.time);
                System.out.println("Physio: " + newSlot.physiotherapistName);
                System.out.println("Therapy: " + newSlot.therapy);
                break;

            case 2: // MARK AS ATTENDED
                oldSlot.status = "Attended";
                System.out.println("Appointment marked as Attended!");
                break;


            case 3: // CANCEL APPOINTMENT
                oldSlot.status = "Available";
                oldSlot.patientName = "";
                oldSlot.therapy = "";
                System.out.println("Appointment Cancelled. Slot is now Available.");
                break;

            default:
                System.out.println("Invalid option.");
        }
    }
}
