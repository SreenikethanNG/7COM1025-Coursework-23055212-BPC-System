// Physiotherapist.java
public class Physiotherapist extends Personnel {
    String profession;

    public Physiotherapist(String uniqueID, String fullName, String address,
                           String telephoneNumber, String profession) {
        super(uniqueID, fullName, address, telephoneNumber);
        this.profession = profession;
    }
}