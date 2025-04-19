import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

class BoostPhysioClinicTest {

    @BeforeEach
    void resetSystemState() {
        BoostPhysioClinic.patients.clear();
        BoostPhysioClinic.physiotherapists.clear();
        BoostPhysioClinic.timetable.clear();
        BoostPhysioClinic.treatments.clear();
        BoostPhysioClinic.initializeData();
        BoostPhysioClinic.scanner = new Scanner(System.in); // Reset scanner
    }

    // Helper to simulate user input with ALL required newlines
    private void simulateInput(String input) {
        System.setIn(new ByteArrayInputStream((input + "\n").getBytes()));
        BoostPhysioClinic.scanner = new Scanner(System.in);
    }

    @Test
    void testSuccessfulAppointmentBooking() {
        // Add trailing \n to therapy choice (1\n)
        simulateInput("T1\nP001\n1\n");
        BoostPhysioClinic.bookAppointment();

        Timetable slot = getSlotById("T1");
        assertEquals("Booked", slot.status);
        assertEquals("Sarah Johnson", slot.patientName);
        assertEquals("Neural Mobilization", slot.therapy);
    }

    @Test
    void testAppointmentCancellation() {
        // Book first with full input
        simulateInput("T1\nP001\n1\n");
        BoostPhysioClinic.bookAppointment();

        // Cancel with confirmation
        simulateInput("T1\n3\n");
        BoostPhysioClinic.modifyAppointment();

        Timetable slot = getSlotById("T1");
        assertEquals("Available", slot.status);
        assertTrue(slot.patientName.isEmpty());
    }

    @Test
    void testAddPhysiotherapist() {
        // Add \n after each input field
        simulateInput("PT003\nDr. New Physio\nSports Therapy\nMonday\n02:00 PM\n");
        BoostPhysioClinic.addPhysiotherapist();

        Physiotherapist physio = BoostPhysioClinic.physiotherapists.getLast();
        assertEquals("PT003", physio.uniqueID);
        assertEquals("Sports Therapy", physio.profession);

        Timetable slot = BoostPhysioClinic.timetable.getLast();
        assertEquals("Monday", slot.day);
    }

    // Helper to find timetable slot
    private Timetable getSlotById(String id) {
        return BoostPhysioClinic.timetable.stream()
                .filter(s -> s.id.equals(id))
                .findFirst()
                .orElse(null);
    }
}