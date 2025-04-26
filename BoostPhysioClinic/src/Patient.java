// Extending the base Personnel Class with patient specific functionality
public class Patient extends Personnel {
    public Patient(String uniqueID, String fullName, String address, String telephoneNumber) {
        super(uniqueID, fullName, address, telephoneNumber);
    }
}