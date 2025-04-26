//Physiotherapist class
//Extends Personnel and adds expertise information about treatments they can perform
public class Physiotherapist extends Personnel {
    String expertise; //specalist area of the physiotherapist
    public Physiotherapist(String uniqueID, String fullName, String address,
                           String telephoneNumber, String expertise) {
        super(uniqueID, fullName, address, telephoneNumber);
        this.expertise = expertise;
    }
}