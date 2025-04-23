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
        testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
        BoostPhysioClinic.scanner = new Scanner(testIn);
    }

    //This test is to check if the Physiotherapist can do treatments matching their expertise.
    @Test
    void physioTreatment_ValidatesAgainstExpertise() {
        // Setup
        Physiotherapist pt = new Physiotherapist("PT1", "Dr. Test", "1 St", "1111", "Osteopathy");

        // Test valid treatment
        assertTrue(BoostPhysioClinic.EXPERTISE_TREATMENTS.get(pt.expertise)
                        .contains("Mobilisation of the spine and joints"),
                "Osteopaths should offer joint mobilisation");

        // Test invalid treatment
        assertFalse(BoostPhysioClinic.EXPERTISE_TREATMENTS.get(pt.expertise)
                        .contains("Pool rehabilitation"),
                "Osteopaths shouldn't offer pool rehab");
    }


    //Tests if new slots can be added and retrived from the timetable
    @Test
    void timetable_AddsAndUpdatesSlots() {
        // Clear existing data
        BoostPhysioClinic.timetable.clear();

        // Add new slot
        Timetable newSlot = new Timetable("T100", LocalDate.now(), "10:00 AM",
                "Available", "", "Dr. Lee", "");
        BoostPhysioClinic.timetable.add(newSlot);

        // Verify
        assertEquals(1, BoostPhysioClinic.timetable.size());
        assertEquals("T100", BoostPhysioClinic.timetable.get(0).id);
    }

    //Test for the adding patients workflow through the system interface.
    @Test
    void addPatient_IncrementsPatientList() {
        // Clear existing data
        BoostPhysioClinic.patients.clear();

        // Add patient
        provideInput("Test Patient\nTest Address\n5551234\n");
        BoostPhysioClinic.addPatient();

        // Verify
        assertEquals(1, BoostPhysioClinic.patients.size());
        assertEquals("P1", BoostPhysioClinic.patients.get(0).uniqueID);
    }

    //Validates for the treatment to physiotherapist mapping
    @Test
    void treatment_MatchesPhysioExpertise() {
        // Test valid treatment-expertise pairing
        assertTrue(BoostPhysioClinic.EXPERTISE_TREATMENTS.get("Physiotherapy")
                        .contains("Massage"),
                "Massage should be a valid Physiotherapy treatment");

        // Test invalid pairing
        assertFalse(BoostPhysioClinic.EXPERTISE_TREATMENTS.get("Rehabilitation")
                        .contains("Acupuncture"),
                "Acupuncture shouldn't be in Rehabilitation");
    }

    //Test for Class heirarchy, where Patient inherits from Personnel
    @Test
    void personnel_IsBaseClass() {
        Patient patient = new Patient("P3", "Bob", "Addr", "3333");
        assertTrue(patient instanceof Personnel,
                "Patient should inherit from Personnel");
    }
}
