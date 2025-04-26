// Base class for Personnel
public class Personnel {
    String uniqueID; //Unique IDs for each persons
    String fullName;
    String address;
    String telephoneNumber;

    public Personnel(String uniqueID, String fullName, String address, String telephoneNumber) {
        this.uniqueID = uniqueID;
        this.fullName = fullName;
        this.address = address;
        this.telephoneNumber = telephoneNumber;
    }
}