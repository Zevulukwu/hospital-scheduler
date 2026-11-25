import java.io.*; // For saving/loading diaries
import java.time.*; // LocalDate and LocalTime for appointments
import java.time.format.DateTimeFormatter; // Formatting dates and times
import java.util.*; // Collections: List, ArrayList, HashMap, Stack

/**
 * SchedulerSystem manages health professionals, their diaries, undo feature, and searching available slots
 */
public class SchedulerSystem {

    // == Inner Classes 

    // Represents a health professional (doctor, nurse, )
    public static class HealthProfessional {
        private String name; // Name of the professional
        private String profession; // Job role
        private String office; // Office location
        private Diary diary; // Diary storing appointments

        // Constructor
        public HealthProfessional(String name, String profession, String office) {
            this.name = name;
            this.profession = profession;
            this.office = office;
            this.diary = new Diary(); // Initialize diary
        }

        // Getter and setter methods
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getProfession() { return profession; }
        public void setProfession(String profession) { this.profession = profession; }
        public String getOffice() { return office; }
        public void setOffice(String office) { this.office = office; }
        public Diary getDiary() { return diary; }

        @Override
        public String toString() {
            return name + " (" + profession + ") - " + office; // Display professional info
        }
    }

    // Represents a single appointment
    public static class Appointment implements Serializable {
        private LocalDate date; // Date of the appointment
        private LocalTime startTime; // Start time
        private LocalTime endTime; // End time
        private String type; // Type of appointment (operation, consultation,)

        // Constructor
        public Appointment(LocalDate date, LocalTime startTime, LocalTime endTime, String type) {
            this.date = date;
            this.startTime = startTime;
            this.endTime = endTime;
            this.type = type;
        }

        // Getters
        public LocalDate getDate() { return date; }
        public LocalTime getStartTime() { return startTime; }
        public LocalTime getEndTime() { return endTime; }
        public String getType() { return type; }

        @Override
        public String toString() {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Date formatter
            DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm"); // Time formatter
            return date.format(df) + " | " + startTime.format(tf) + " - " + endTime.format(tf) + " | " + type;
        }
    }

    // Represents a diary holding multiple appointments
    public static class Diary implements Serializable {
        private List<Appointment> appointments; // List of appointments

        public Diary() { appointments = new ArrayList<>(); } // Initialize empty list

        public List<Appointment> getAppointments() { return appointments; }

        public void addAppointment(Appointment a) { appointments.add(a); } // Add appointment

        public void deleteAppointment(Appointment a) { appointments.remove(a); } // Delete appointment
    }

    // Represents an undoable action (add, delete, edit)
    public static class UndoAction {
        enum ActionType { ADD, DELETE, EDIT } // Types of actions

        private ActionType action; // Action type
        private Appointment appointment; // New or deleted appointment
        private Appointment oldAppointment; // Old appointment for edit
        private Diary diary; // Diary affected
        private int index; // Index for edit

        // Constructor for add/delete
        public UndoAction(ActionType action, Appointment appointment, Diary diary) {
            this.action = action;
            this.appointment = appointment;
            this.diary = diary;
        }

        // Constructor for edit
        public UndoAction(ActionType action, Appointment oldAppointment, Appointment newAppointment, Diary diary, int index) {
            this.action = action;
            this.oldAppointment = oldAppointment;
            this.appointment = newAppointment;
            this.diary = diary;
            this.index = index;
        }

        // Getters
        public ActionType getAction() { return action; }
        public Appointment getAppointment() { return appointment; }
        public Appointment getOldAppointment() { return oldAppointment; }
        public Diary getDiary() { return diary; }
        public int getIndex() { return index; }
    }

    // == SchedulerSystem Data 
    private HashMap<String, HealthProfessional> professionals; // Store professionals by name
    private Stack<UndoAction> undoStack; // Stack to store undoable actions

    // Constructor
    public SchedulerSystem() {
        professionals = new HashMap<>(); // Initialize map
        undoStack = new Stack<>(); // Initialize undo stack
    }

    // == Health Professional Operations 

    // Add a professional
    public void addProfessional(String name, String profession, String office) {
        HealthProfessional hp = new HealthProfessional(name, profession, office); // Create professional
        professionals.put(name, hp); // Add to map
    }

    // Edit a professional
    public void editProfessional(String name, String newName, String newProfession, String newOffice) {
        HealthProfessional hp = professionals.get(name); // Find professional
        if (hp == null) return; // Exit if not found
        if (!newName.isEmpty()) hp.setName(newName);
        if (!newProfession.isEmpty()) hp.setProfession(newProfession);
        if (!newOffice.isEmpty()) hp.setOffice(newOffice);
        if (!newName.isEmpty() && !newName.equals(name)) { // Update key in map if name changed
            professionals.remove(name);
            professionals.put(newName, hp);
        }
    }

    // Delete professional
    public void deleteProfessional(String name) { professionals.remove(name); }

    // Get all professionals
    public HashMap<String, HealthProfessional> getProfessionals() { return professionals; }

    // == Appointment Operations 

    // Add appointment
    public void addAppointment(String hpName, Appointment a) {
        HealthProfessional hp = professionals.get(hpName);
        if (hp == null) return;
        hp.getDiary().addAppointment(a); // Add appointment
        undoStack.push(new UndoAction(UndoAction.ActionType.ADD, a, hp.getDiary())); // Push undo
    }

    // Edit appointment
    public void editAppointment(String hpName, int index, Appointment newAppointment) {
        HealthProfessional hp = professionals.get(hpName);
        if (hp == null) return;
        Diary diary = hp.getDiary();
        Appointment oldAppointment = diary.getAppointments().get(index); // Save old
        diary.getAppointments().set(index, newAppointment); // Replace
        undoStack.push(new UndoAction(UndoAction.ActionType.EDIT, oldAppointment, newAppointment, diary, index)); // Undo
    }

    // Delete appointment
    public void deleteAppointment(String hpName, int index) {
        HealthProfessional hp = professionals.get(hpName);
        if (hp == null) return;
        Diary diary = hp.getDiary();
        Appointment a = diary.getAppointments().remove(index);
        undoStack.push(new UndoAction(UndoAction.ActionType.DELETE, a, diary));
    }

    // Undo last action
    public void undoLastAction() {
        if (undoStack.isEmpty()) return;
        UndoAction last = undoStack.pop();
        switch (last.getAction()) {
            case ADD -> last.getDiary().deleteAppointment(last.getAppointment());
            case DELETE -> last.getDiary().addAppointment(last.getAppointment());
            case EDIT -> last.getDiary().getAppointments().set(last.getIndex(), last.getOldAppointment());
        }
    }

    // == Save/Load Diaries 
    public void saveDiary(String hpName, String filename) {
        HealthProfessional hp = professionals.get(hpName);
        if (hp == null) return;
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(hp.getDiary().getAppointments()); // Save list
        } catch (Exception e) { e.printStackTrace(); }
    }

    @SuppressWarnings("unchecked")
    public void loadDiary(String hpName, String filename) {
        HealthProfessional hp = professionals.get(hpName);
        if (hp == null) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            List<Appointment> loaded = (List<Appointment>) ois.readObject();
            hp.getDiary().getAppointments().clear();
            hp.getDiary().getAppointments().addAll(loaded);
        } catch (Exception e) { e.printStackTrace(); }
    }

    // == Search common slots across professionals 
    public void searchCommonSlots(List<String> names, LocalDate date, LocalTime startTime, int durationMinutes) {
        LocalTime workEnd = LocalTime.of(18, 0); // End of work day
        LocalTime current = startTime; // Starting time
        List<LocalTime> available = new ArrayList<>(); // Store available slots

        while (!current.plusMinutes(durationMinutes).isAfter(workEnd)) {
            boolean slotFree = true;
            for (String hpName : names) {
                HealthProfessional hp = professionals.get(hpName);
                if (hp == null) continue;
                for (Appointment a : hp.getDiary().getAppointments()) {
                    if (!a.getDate().equals(date)) continue;
                    if (!(current.plusMinutes(durationMinutes).isBefore(a.getStartTime()) || current.isAfter(a.getEndTime()))) {
                        slotFree = false; break; // Overlap found
                    }
                }
                if (!slotFree) break;
            }
            if (slotFree) available.add(current); // Add available slot
            current = current.plusMinutes(15); // Move to next interval
        }

        // Print available slots
        System.out.println("Available slots:");
        for (LocalTime t : available) System.out.println(t + " to " + t.plusMinutes(durationMinutes));
    }
}