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

    //Predefined mapping of expertise to treatments
    protected static final Map<String, List<String>> EXPERTISE_TREATMENTS = Map.of(
            "Physiotherapy", List.of("Neural mobilisation", "Massage",
                    "Mobilisation of the spine and joints", "Pool rehabilitation"),
            "Rehabilitation", List.of("Pool rehabilitation", "Neural mobilisation", "Massage"),
            "Osteopathy", List.of("Mobilisation of the spine and joints", "Acupuncture", "Massage")
    );

    private static int readIntInput() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

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

        // Adding preset of 15 Patients
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

        // Adding preset of 5 Physiotherapists
        physiotherapists.addAll(Arrays.asList(
                new Physiotherapist("PT1", "Dr. Sarah Chen", "101 Wellness Way", "5550201", "Physiotherapy"),
                new Physiotherapist("PT2", "Dr. Michael Patel", "202 Recovery Rd", "5550202", "Rehabilitation"),
                new Physiotherapist("PT3", "Dr. Emily Wilson", "303 Mobility Ln", "5550203", "Osteopathy"),
                new Physiotherapist("PT4", "Dr. David Kim", "404 Flex St", "5550204", "Physiotherapy"),
                new Physiotherapist("PT5", "Dr. Jessica Brown", "505 Balance Ave", "5550205", "Rehabilitation")
        ));

        // Creating a 4-week timetable ranging from 21st April to 16th May
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.with(DayOfWeek.MONDAY);
        int slotId = 1;

        for (int week = 0; week < 4; week++) {
            for (int day = 0; day < 5; day++) {
                LocalDate currentDate = startDate.plusDays(day + (week * 7));
                String[] timeSlots = {"09:00 AM", "11:00 AM", "02:00 PM", "04:00 PM"};

                for (String time : timeSlots) {
                    timetable.add(new Timetable(
                            "T" + slotId++,
                            currentDate,
                            time,
                            "Available",
                            "",
                            physiotherapists.get((slotId - 1) % physiotherapists.size()).fullName,
                            ""
                    ));
                }
            }
        }

        //This pre-books some of the slots for easier testings from your side (invigilators).
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

        // Mixed status pre-bookings with 60 to 40 ratio of 'Booked' and 'Available' statues.
        String[] statusOptions = {"Booked", "Booked", "Booked", "Available", "Available"}; // 60% Booked, 40% Available
        int preBookCount = 15;

        for (int i = 0; i < preBookCount; i++) {
            int slotIndex = availableSlots.get(i);
            Timetable slot = timetable.get(slotIndex);

            String status = statusOptions[rand.nextInt(statusOptions.length)];
            slot.status = status;

            if (status.equals("Booked")) {
                slot.patientName = patients.get(i % patients.size()).fullName;
                slot.therapy = therapies[rand.nextInt(therapies.length)];

                //Assigning physiotherapist to the treatment.
                String neededExpertise = physiotherapists.stream()
                        .filter(p -> p.fullName.equals(slot.physiotherapistName))
                        .findFirst()
                        .get()
                        .expertise;

                List<Physiotherapist> matchingPhysios = physiotherapists.stream()
                        .filter(p -> p.expertise.equals(neededExpertise))
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

        // Show all patients
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

        // Check for active bookings
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
        final String[] therapyWrapper = {""};
        final String[] requiredExpertiseWrapper = {""};

        //Asking patients to find appointments by Treatment, or Physiotherapist and Expertise.
        System.out.println("\nFind Appointments By:");
        System.out.println("1. Treatment");
        System.out.println("2. Physiotherapist/Expertise");
        System.out.print("Choice: ");
        int searchMethod = readIntInput();

        List<Timetable> availableSlots = new ArrayList<>();

        if (searchMethod == 1) {
            // Treatment selection
            System.out.println("\nSelect Treatment:");
            System.out.println("1. Neural mobilisation");
            System.out.println("2. Acupuncture");
            System.out.println("3. Massage");
            System.out.println("4. Mobilisation of the spine and joints");
            System.out.println("5. Pool rehabilitation");
            System.out.print("Choice: ");
            int therapyChoice = readIntInput();

            therapyWrapper[0] = switch(therapyChoice) {
                case 1 -> "Neural mobilisation";
                case 2 -> "Acupuncture";
                case 3 -> "Massage";
                case 4 -> "Mobilisation of the spine and joints";
                case 5 -> "Pool rehabilitation";
                default -> "Massage";
            };

            // Get required expertise for the very treatment.
            requiredExpertiseWrapper[0] = EXPERTISE_TREATMENTS.entrySet().stream()
                    .filter(entry -> entry.getValue().contains(therapyWrapper[0]))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse("Physiotherapy");

            // Filter qualified physiostherapist by expertise
            List<Physiotherapist> qualifiedPhysios = physiotherapists.stream()
                    .filter(p -> p.expertise.equals(requiredExpertiseWrapper[0]))
                    .collect(Collectors.toList());

            //Obtaining availbale slots with the qualified physios.
            availableSlots = timetable.stream()
                    .filter(t -> t.status.equals("Available"))
                    .filter(t -> qualifiedPhysios.stream()
                            .anyMatch(p -> p.fullName.equals(t.physiotherapistName)))
                    .collect(Collectors.toList());

        } else if (searchMethod == 2) {
            // Physiotherapist selection
            System.out.println("\nSelect Physiotherapist:");
            physiotherapists.forEach(p ->
                    System.out.println(p.uniqueID + ": " + p.fullName + " (" + p.expertise + ")"));
            System.out.print("Enter Physiotherapist ID: ");
            String physioID = scanner.nextLine().trim();

            Optional<Physiotherapist> physio = physiotherapists.stream()
                    .filter(p -> p.uniqueID.equalsIgnoreCase(physioID))
                    .findFirst();

            if (!physio.isPresent()) {
                System.out.println("Invalid Physiotherapist ID!");
                return;
            }

            // Get allowed treatments
            List<String> allowedTreatments = EXPERTISE_TREATMENTS.get(physio.get().expertise);
            System.out.println("\nSelect Treatment:");
            for (int i = 0; i < allowedTreatments.size(); i++) {
                System.out.println((i+1) + ". " + allowedTreatments.get(i));
            }
            System.out.print("Choice: ");
            int treatmentChoice = readIntInput();

            if (treatmentChoice < 1 || treatmentChoice > allowedTreatments.size()) {
                System.out.println("Invalid choice!");
                return;
            }

            therapyWrapper[0] = allowedTreatments.get(treatmentChoice - 1);
            requiredExpertiseWrapper[0] = physio.get().expertise;

            availableSlots = timetable.stream()
                    .filter(t -> t.status.equals("Available"))
                    .filter(t -> t.physiotherapistName.equals(physio.get().fullName))
                    .collect(Collectors.toList());
        } else {
            System.out.println("Invalid choice!");
            return;
        }

        // Common booking process
        if (availableSlots.isEmpty()) {
            System.out.println("No available slots found!");
            return;
        }

        // Display slots
        System.out.println("\nAvailable Appointments:");
        System.out.println("+----+------------+-----------+----------------------+---------------------+");
        System.out.println("| ID |    Date    |   Time    | Physiotherapist      | Expertise           |");
        System.out.println("+----+------------+-----------+----------------------+---------------------+");

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM dd");
        Map<String, String> physioExpertise = physiotherapists.stream()
                .collect(Collectors.toMap(p -> p.fullName, p -> p.expertise));

        availableSlots.forEach(s -> {
            String expertise = physioExpertise.get(s.physiotherapistName);
            System.out.printf("| %-2s | %-10s | %-9s | %-20s | %-19s |\n",
                    s.id, s.date.format(dtf), s.time, s.physiotherapistName, expertise);
        });
        System.out.println("+----+------------+-----------+----------------------+---------------------+");

        // Book slot
        System.out.print("Enter Appointment ID: ");
        String apptID = scanner.nextLine();
        Optional<Timetable> slot = availableSlots.stream()
                .filter(s -> s.id.equals(apptID))
                .findFirst();

        if (!slot.isPresent()) {
            System.out.println("Invalid ID!");
            return;
        }

        // Filter patients without active bookings
        List<Patient> availablePatients = patients.stream()
                .filter(p ->
                        timetable.stream()
                                .noneMatch(t ->
                                        t.patientName.equals(p.fullName) &&
                                                t.status.equals("Booked") // Only check for Booked status
                                )
                )
                .collect(Collectors.toList());

        if (availablePatients.isEmpty()) {
            System.out.println("No patients available to book (all have active appointments).");
            return;
        }

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

        boolean hasBooking = timetable.stream()
                .anyMatch(t ->
                        t.patientName.equals(patient.get().fullName) &&
                                t.status.equals("Booked") // Only check for Booked status
                );
        if (hasBooking) {
            System.out.println("Patient already has an active booking!");
            return;
        }
        // Update timetable
        Timetable t = slot.get();
        t.status = "Booked";
        t.patientName = patient.get().fullName;
        t.therapy = therapyWrapper[0]; // Use wrapper value

        System.out.println("\nBooking Successful!");
        System.out.println("Patient: " + t.patientName);
        System.out.println("Date: " + t.date.format(DateTimeFormatter.ISO_DATE) + " " + t.time);
        System.out.println("Physio: " + t.physiotherapistName);
        System.out.println("Therapy: " + t.therapy);
    }

    // Helper methods for input validations.
    //checks for empty space input
    private static String getInput(String prompt, String field) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.println(field + " cannot be empty!");
        }
    }

    //Validates that the telephone number should be integers and 7-15 lenght
    private static String getPhone() {
        while (true) {
            String input = getInput("Enter Phone: ", "Phone")
                    .replaceAll("[^0-9]", "");
            if (input.length() >= 7 && input.length() <= 15) return input;
            System.out.println("Must be 7-15 digits!");
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
                .filter(t -> t.status.equals("Booked"))
                .collect(Collectors.toList());

        if (bookings.isEmpty()) {
            System.out.println("\nNo active bookings found.");
            return;
        }

        // For better and adapt visual display of the View Bookings section
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

    static void addPatient() {
        String id = "P" + (patients.size() + 1);
        System.out.println("New Patient ID: " + id);
        String name = getInput("Enter Name: ", "Name");
        String address = getInput("Enter Address: ", "Address");
        String phone = getPhone();
        patients.add(new Patient(id, name, address, phone));
        System.out.println("Patient added!");
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
                    physio.expertise,
                    count
            );
        });
        System.out.println("═══════════════════════════════════════════════════════════════════════════════════════");
    }

    static void changeCancelAppointment() {
        // Show only booked appointments
        System.out.println("\n--- Booked Appointments ---");
        List<Timetable> bookings = timetable.stream()
                .filter(t -> t.status.equals("Booked"))
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
                .filter(t -> t.id.equals(id) && t.status.equals("Booked"))
                .findFirst()
                .orElse(null);

        if (oldSlot == null) {
            System.out.println("Invalid ID or slot not booked.");
            return;
        }

        System.out.println("\nSelected Slot: " + oldSlot.date + " (" + oldSlot.getDayName() + ") " + oldSlot.time);
        System.out.println("1. Reassign Therapist/Time/Therapy");
        System.out.println("2. Mark as Attended");
        System.out.println("3. Cancel (Make Available)");
        System.out.print("Choose an option: ");
        int option = readIntInput();

        switch (option) {
            case 1: // Reassign
                System.out.println("\n--- Reassignment Process ---");
                System.out.println("Current Patient: " + oldSlot.patientName);

                List<Timetable> availableSlots;
                final String[] therapyWrapper = {oldSlot.therapy};
                final String[] expertiseWrapper = {""};

                System.out.println("\nFind Appointments By:");
                System.out.println("1. Expertise");
                System.out.println("2. Therapy");
                System.out.println("3. Physiotherapist");
                System.out.print("Choice: ");
                int filterChoice = readIntInput();

                switch (filterChoice) {
                    case 1: // Filter by Expertise
                        System.out.println("\nSelect Expertise:");
                        System.out.println("1. Physiotherapy\n2. Rehabilitation\n3. Osteopathy");
                        System.out.print("Choice: ");
                        int expertChoice = readIntInput();

                        String expertise = switch (expertChoice) {
                            case 1 -> "Physiotherapy";
                            case 2 -> "Rehabilitation";
                            case 3 -> "Osteopathy";
                            default -> "";
                        };

                        if (!expertise.isEmpty()) {
                            List<String> expertTreatments = EXPERTISE_TREATMENTS.get(expertise);
                            System.out.println("\nSelect Treatment:");
                            for (int i = 0; i < expertTreatments.size(); i++) {
                                System.out.println((i+1) + ". " + expertTreatments.get(i));
                            }
                            System.out.print("Choice: ");
                            int treatmentChoice = readIntInput();

                            if (treatmentChoice > 0 && treatmentChoice <= expertTreatments.size()) {
                                therapyWrapper[0] = expertTreatments.get(treatmentChoice - 1);
                            }

                            List<Physiotherapist> qualifiedPhysios = physiotherapists.stream()
                                    .filter(p -> p.expertise.equals(expertise))
                                    .collect(Collectors.toList());

                            availableSlots = timetable.stream()
                                    .filter(t -> t.status.equals("Available"))
                                    .filter(t -> qualifiedPhysios.stream()
                                            .anyMatch(p -> p.fullName.equals(t.physiotherapistName)))
                                    .collect(Collectors.toList());
                        } else {
                            availableSlots = List.of();
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
                        int therapyChoice = readIntInput();

                        therapyWrapper[0] = switch (therapyChoice) {
                            case 1 -> "Neural mobilisation";
                            case 2 -> "Acupuncture";
                            case 3 -> "Massage";
                            case 4 -> "Mobilisation of the spine and joints";
                            case 5 -> "Pool rehabilitation";
                            default -> oldSlot.therapy;
                        };

                        expertiseWrapper[0] = EXPERTISE_TREATMENTS.entrySet().stream()
                                .filter(entry -> entry.getValue().contains(therapyWrapper[0]))
                                .map(Map.Entry::getKey)
                                .findFirst()
                                .orElse("Physiotherapy");

                        List<Physiotherapist> qualifiedPhysios = physiotherapists.stream()
                                .filter(p -> p.expertise.equals(expertiseWrapper[0]))
                                .collect(Collectors.toList());

                        availableSlots = timetable.stream()
                                .filter(t -> t.status.equals("Available"))
                                .filter(t -> qualifiedPhysios.stream()
                                        .anyMatch(p -> p.fullName.equals(t.physiotherapistName)))
                                .collect(Collectors.toList());
                        break;

                    case 3: // Filter by Physiotherapist
                        System.out.println("\nSelect Physiotherapist:");
                        physiotherapists.forEach(p ->
                                System.out.println(p.uniqueID + ": " + p.fullName + " (" + p.expertise + ")"));
                        System.out.print("Enter Physiotherapist ID: ");
                        String physioID = scanner.nextLine().trim();

                        Optional<Physiotherapist> physio = physiotherapists.stream()
                                .filter(p -> p.uniqueID.equalsIgnoreCase(physioID))
                                .findFirst();

                        if (!physio.isPresent()) {
                            System.out.println("Invalid Physiotherapist ID!");
                            availableSlots = List.of();
                            break;
                        }

                        List<String> allowedTreatments = EXPERTISE_TREATMENTS.get(physio.get().expertise);
                        System.out.println("\nSelect Treatment:");
                        for (int i = 0; i < allowedTreatments.size(); i++) {
                            System.out.println((i+1) + ". " + allowedTreatments.get(i));
                        }
                        System.out.print("Choice: ");
                        int treatmentChoice = readIntInput();

                        if (treatmentChoice > 0 && treatmentChoice <= allowedTreatments.size()) {
                            therapyWrapper[0] = allowedTreatments.get(treatmentChoice - 1);
                        }

                        availableSlots = timetable.stream()
                                .filter(t -> t.status.equals("Available"))
                                .filter(t -> t.physiotherapistName.equals(physio.get().fullName))
                                .collect(Collectors.toList());
                        break;

                    default:
                        System.out.println("Invalid choice!");
                        return;
                }

                if (availableSlots.isEmpty()) {
                    System.out.println("No available slots found!");
                    return;
                }

                // Display slots
                System.out.println("\nAvailable Appointments:");
                System.out.println("+----+------------+-----------+----------------------+---------------------+");
                System.out.println("| ID |    Date    |   Time    | Physiotherapist      | Expertise           |");
                System.out.println("+----+------------+-----------+----------------------+---------------------+");

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM dd");
                Map<String, String> physioExpertise = physiotherapists.stream()
                        .collect(Collectors.toMap(p -> p.fullName, p -> p.expertise));

                availableSlots.forEach(s -> {
                    String expertise = physioExpertise.get(s.physiotherapistName);
                    System.out.printf("| %-2s | %-10s | %-9s | %-20s | %-19s |\n",
                            s.id, s.date.format(dtf), s.time, s.physiotherapistName, expertise);
                });
                System.out.println("+----+------------+-----------+----------------------+---------------------+");

                // Select new slot
                System.out.print("Enter New Appointment ID: ");
                String newId = scanner.nextLine();
                Optional<Timetable> newSlot = availableSlots.stream()
                        .filter(t -> t.id.equals(newId))
                        .findFirst();

                if (newSlot.isPresent()) {
                    // Update slots
                    Timetable newT = newSlot.get();
                    newT.status = "Booked";
                    newT.patientName = oldSlot.patientName;
                    newT.therapy = therapyWrapper[0]; // Use selected therapy

                    oldSlot.status = "Available";
                    oldSlot.patientName = "";
                    oldSlot.therapy = "";

                    System.out.println("\nReassignment Successful!");
                    System.out.println("Patient: " + newT.patientName);
                    System.out.println("New Date: " + newT.date.format(DateTimeFormatter.ISO_DATE) + " " + newT.time);
                    System.out.println("Physio: " + newT.physiotherapistName);
                    System.out.println("Therapy: " + newT.therapy);
                } else {
                    System.out.println("Invalid slot selection!");
                }
                break;

            case 2: // Mark as Attended
                oldSlot.status = "Attended";
                System.out.println("Appointment marked as Attended!");
                break;

            case 3: // Cancel Appointment
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
