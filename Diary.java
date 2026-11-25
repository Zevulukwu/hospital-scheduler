import java.io.Serializable;
import java.util.ArrayList;
import java.util.Stack;

//Represents a professional's diary
 
public class Diary implements Serializable {

    private ArrayList<Appointment> appointments = new ArrayList<>();
    private Stack<Appointment> undoStack = new Stack<>();

    // Add appointment
     
    public void addAppointment(Appointment a) {
        appointments.add(a);
        undoStack.push(a);
    }

    //Delete appointment
     
    public void deleteAppointment(int index) {

        if (index >= 0 && index < appointments.size()) {

            Appointment removed = appointments.remove(index);
            undoStack.push(removed);
        }
    }

    //Display all appointments
     
    public void displayAppointments() {

        if (appointments.isEmpty()) {
            System.out.println("No appointments.");
            return;
        }

        for (int i = 0; i < appointments.size(); i++) {
            System.out.println(i + ": " + appointments.get(i));
        }
    }

    //Undo last action
     
    public void undo() {

        if (!undoStack.isEmpty()) {

            Appointment last = undoStack.pop();
            appointments.remove(last);

            System.out.println("Undo successful.");
        }
    }

    public ArrayList<Appointment> getAppointments() {
        return appointments;
    }
}