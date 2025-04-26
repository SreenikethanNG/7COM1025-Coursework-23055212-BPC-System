import org.junit.jupiter.api.*;
import java.time.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class BoostPhysioClinicTest {

    private ByteArrayInputStream testIn;
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream testOut;

    @BeforeEach
    void setUp() {
        BoostPhysioClinic.patients.clear();
        BoostPhysioClinic.physiotherapists.clear();
        BoostPhysioClinic.timetable.clear();
        BoostPhysioClinic.initializeData();

        // Redirect System.out for testing output
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    @AfterEach
    void tearDown() {
        System.setIn(System.in);
        System.setOut(originalOut);
    }

    // Helper method to simulate user input
    private void provideInput(String data) {
        testIn = new ByteArrayInputStream(data.getBytes()); // For converting String into input stream
        System.setIn(testIn);
        BoostPhysioClinic.scanner = new Scanner(testIn);
    }

    @Test
    void physioTreatment_ValidatesAgainstExpertise() {
        Physiotherapist pt = new Physiotherapist("PT1", "Dr. Test", "1 St", "1111", "Osteopathy");
        assertTrue(BoostPhysioClinic.EXPERTISE_TREATMENTS.get(pt.expertise)
                        .contains("Mobilisation of the spine and joints"),
                "Osteopaths should offer joint mobilisation");
        assertFalse(BoostPhysioClinic.EXPERTISE_TREATMENTS.get(pt.expertise)
                        .contains("Pool rehabilitation"),
                "Osteopaths shouldn't offer pool rehab");
    }

    @Test
    void timetable_AddsAndUpdatesSlots() {
        BoostPhysioClinic.timetable.clear();
        Timetable newSlot = new Timetable("T100", LocalDate.now(), "10:00 AM",
                "Available", "", "Dr. Lee", "");
        BoostPhysioClinic.timetable.add(newSlot);
        assertEquals(1, BoostPhysioClinic.timetable.size());
        assertEquals("T100", BoostPhysioClinic.timetable.get(0).id);
    }

    @Test
    void addPatient_IncrementsPatientList() {
        BoostPhysioClinic.patients.clear();
        provideInput("Test Patient\nTest Address\n5551234\n");
        BoostPhysioClinic.addPatient();
        assertEquals(1, BoostPhysioClinic.patients.size());
        assertEquals("P1", BoostPhysioClinic.patients.get(0).uniqueID);
    }

    @Test
    void treatment_MatchesPhysioExpertise() {
        assertTrue(BoostPhysioClinic.EXPERTISE_TREATMENTS.get("Physiotherapy")
                        .contains("Massage"),
                "Massage should be a valid Physiotherapy treatment");
        assertFalse(BoostPhysioClinic.EXPERTISE_TREATMENTS.get("Rehabilitation")
                        .contains("Acupuncture"),
                "Acupuncture shouldn't be in Rehabilitation");
    }

    @Test
    void personnel_IsBaseClass() {
        Patient patient = new Patient("P3", "Bob", "Addr", "3333");
        assertTrue(patient instanceof Personnel,
                "Patient should inherit from Personnel");
    }
}
