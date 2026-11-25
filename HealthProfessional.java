// Represents a health professional ( doctor, nurse, physiotherapist)
public class HealthProfessional {

    // Name of the health professional
    private String name;

    // Profession/job title ( Doctor, Nurse)
    private String profession;

    // Work location or office of the professional
    private String location;

    // Each professional has a diary to store their appointments
    private Diary diary;

    // Constructor to initialize a health professional with name, profession, and location
    public HealthProfessional(String name, String profession, String location) {
        this.name = name; // Set the professional's name
        this.profession = profession; // Set the profession
        this.location = location; // Set the office/location

        // Initialize an empty diary for this professional
        this.diary = new Diary();
    }

    // Getter method for the name of the professional
    public String getName() {
        return name; // Return the professional's name
    }

    // Getter method for the professional's diary
    public Diary getDiary() {
        return diary; // Return the diary object
    }

    // Converts the professional's information to a readable string
    @Override
    public String toString() {
        // Format: "Name | Profession | Location"
        return name + " | " + profession + " | " + location;
    }
}