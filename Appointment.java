import java.io.Serializable;      // For saving/loading objects to a file
import java.time.LocalDate;        // Represents date without time
import java.time.LocalTime;        // Represents time without date  

// patient appointment//

public class Appointment implements Serializable {

    private LocalDate date;             // Appointment date //
    private LocalTime startTime;        //Start time//
    private LocalTime endTime;          //end time//
    private String treatmentType;

    // Constructor to create an appointment//

    public Appointment(LocalDate date, LocalTime startTime, LocalTime endTime, String treatmentType) {
        this.date = date;
        this.startTime = startTime;       // Set the start time
        this.endTime = endTime;           // Set the end time
        this.treatmentType = treatmentType;
    }

    public LocalDate getDate() {            // Gets the date
        return date;
    }

    public LocalTime getStartTime() {          // Gets the time
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getTreatmentType() {       // Gets the type of appointment
        return treatmentType;
    }
 
     // Convert appointment to a readable string
     
    @Override
    public String toString() {           
        return "Date: " + date +
                " Start: " + startTime +
                " End: " + endTime +
                " Treatment: " + treatmentType;
    }
}