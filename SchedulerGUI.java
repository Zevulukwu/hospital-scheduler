import javax.swing.*; // For GUI components: JFrame, JButton, JList, etc.
import javax.swing.table.DefaultTableModel; // For tables
import java.awt.*; // For layout and color
import java.time.LocalDate; // For dates
import java.time.LocalTime; // For times
import java.util.List; // For List interface
import java.util.ArrayList; // For ArrayList

// SchedulerGUI provides a graphical interface for managing health professionals,
 // their diaries, appointments, and searching for available slots.
 
public class SchedulerGUI {

    private SchedulerSystem system; //  system that manages data
    private JFrame frame; // Main window

    private DefaultListModel<String> professionalListModel; // Model for list of professionals
    private JList<String> professionalList; // GUI list showing professional names
    private DefaultTableModel appointmentTableModel; // Table model for diary appointments
    private JTable appointmentTable; // Table showing appointments

    //Constructor receives a SchedulerSystem 
     
    public SchedulerGUI(SchedulerSystem system) {
        this.system = system; // Store  reference
    }

    // Initialize and show the GUI window
     
    public void createAndShowGUI() {
        frame = new JFrame("Hospital Operation Scheduler"); // Create main window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close app when window closed
        frame.setSize(900, 600); // Set size of window
        frame.setLayout(new BorderLayout()); // Use border layout

        // == Left Panel: Health Professionals List 
        professionalListModel = new DefaultListModel<>(); // Model for list
        professionalList = new JList<>(professionalListModel); // Create list
        JScrollPane listScroll = new JScrollPane(professionalList); // Add scroll bar
        listScroll.setPreferredSize(new Dimension(200, 0)); // Width fixed

        // Button panel for adding/editing/deleting professionals
        JPanel professionalButtonPanel = new JPanel(); // Panel for buttons
        professionalButtonPanel.setLayout(new GridLayout(3,1,5,5)); // 3 buttons stacked
        JButton addProfBtn = new JButton("Add HP"); // Button to add professional
        JButton editProfBtn = new JButton("Edit HP"); // Button to edit professional
        JButton delProfBtn = new JButton("Delete HP"); // Button to delete professional
        professionalButtonPanel.add(addProfBtn); // Add buttons to panel
        professionalButtonPanel.add(editProfBtn);
        professionalButtonPanel.add(delProfBtn);

        // Left side container combining list + buttons
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(listScroll, BorderLayout.CENTER); // List in center
        leftPanel.add(professionalButtonPanel, BorderLayout.SOUTH); // Buttons at bottom

        frame.add(leftPanel, BorderLayout.WEST); // Add left panel to main frame

        // == Center Panel: Appointments Table 
        appointmentTableModel = new DefaultTableModel(new Object[]{"Date","Start","End","Type"}, 0); // Columns
        appointmentTable = new JTable(appointmentTableModel); // Create table
        JScrollPane tableScroll = new JScrollPane(appointmentTable); // Add scroll

        // Buttons for appointments
        JPanel appointmentButtonPanel = new JPanel(new GridLayout(4,1,5,5));
        JButton addApptBtn = new JButton("Add Appointment");
        JButton editApptBtn = new JButton("Edit Appointment");
        JButton delApptBtn = new JButton("Delete Appointment");
        JButton undoBtn = new JButton("Undo");
        appointmentButtonPanel.add(addApptBtn);
        appointmentButtonPanel.add(editApptBtn);
        appointmentButtonPanel.add(delApptBtn);
        appointmentButtonPanel.add(undoBtn);

        // Center panel container
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(tableScroll, BorderLayout.CENTER); // Table in center
        centerPanel.add(appointmentButtonPanel, BorderLayout.EAST); // Buttons on right
        frame.add(centerPanel, BorderLayout.CENTER); // Add to main frame

        // == Bottom Panel: Save/Load/Search 
        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton saveBtn = new JButton("Save Diary"); // Save diary button
        JButton loadBtn = new JButton("Load Diary"); // Load diary button
        JButton searchBtn = new JButton("Search Slots"); // Search common slots button
        bottomPanel.add(saveBtn);
        bottomPanel.add(loadBtn);
        bottomPanel.add(searchBtn);
        frame.add(bottomPanel, BorderLayout.SOUTH); // Add bottom panel

        // == Event Listeners 

        // When professional selected, show their diary in table
        professionalList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) { // Only act when finished adjusting
                updateAppointmentTable(); // Refresh table
            }
        });

        // Add professional button
        addProfBtn.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(frame, "Name:");
            String profession = JOptionPane.showInputDialog(frame, "Profession:");
            String office = JOptionPane.showInputDialog(frame, "Office:");
            if (name != null && !name.isEmpty()) {
                system.addProfessional(name, profession, office); // Add 
                professionalListModel.addElement(name); // Add to GUI list
            }
        });

        // Edit professional
        editProfBtn.addActionListener(e -> {
            String selected = professionalList.getSelectedValue();
            if (selected != null) {
                String newName = JOptionPane.showInputDialog(frame, "New Name:", selected);
                String newProfession = JOptionPane.showInputDialog(frame, "New Profession:");
                String newOffice = JOptionPane.showInputDialog(frame, "New Office:");
                system.editProfessional(selected, newName, newProfession, newOffice);
                professionalListModel.set(professionalList.getSelectedIndex(), newName); // Update GUI list
            }
        });

        // Delete professional
        delProfBtn.addActionListener(e -> {
            String selected = professionalList.getSelectedValue();
            if (selected != null) {
                system.deleteProfessional(selected); // Remove 
                professionalListModel.removeElement(selected); // Remove from GUI list
                appointmentTableModel.setRowCount(0); // Clear table
            }
        });

        // Add appointment
        addApptBtn.addActionListener(e -> {
            String selected = professionalList.getSelectedValue();
            if (selected != null) {
                String dateStr = JOptionPane.showInputDialog(frame, "Date (YYYY-MM-DD):");
                String startStr = JOptionPane.showInputDialog(frame, "Start Time (HH:MM):");
                String endStr = JOptionPane.showInputDialog(frame, "End Time (HH:MM):");
                String type = JOptionPane.showInputDialog(frame, "Type:");
                LocalDate date = LocalDate.parse(dateStr);
                LocalTime start = LocalTime.parse(startStr);
                LocalTime end = LocalTime.parse(endStr);
                SchedulerSystem.Appointment appt = new SchedulerSystem.Appointment(date,start,end,type);
                system.addAppointment(selected, appt); // Add 
                updateAppointmentTable(); // Refresh table
            }
        });

        // Edit appointment
        editApptBtn.addActionListener(e -> {
            String selected = professionalList.getSelectedValue();
            int row = appointmentTable.getSelectedRow();
            if (selected != null && row != -1) {
                String dateStr = JOptionPane.showInputDialog(frame, "Date (YYYY-MM-DD):");
                String startStr = JOptionPane.showInputDialog(frame, "Start Time (HH:MM):");
                String endStr = JOptionPane.showInputDialog(frame, "End Time (HH:MM):");
                String type = JOptionPane.showInputDialog(frame, "Type:");
                LocalDate date = LocalDate.parse(dateStr);
                LocalTime start = LocalTime.parse(startStr);
                LocalTime end = LocalTime.parse(endStr);
                SchedulerSystem.Appointment appt = new SchedulerSystem.Appointment(date,start,end,type);
                system.editAppointment(selected,row,appt); // Edit 
                updateAppointmentTable(); // Refresh
            }
        });

        // Delete appointment
        delApptBtn.addActionListener(e -> {
            String selected = professionalList.getSelectedValue();
            int row = appointmentTable.getSelectedRow();
            if (selected != null && row != -1) {
                system.deleteAppointment(selected,row); // Delete 
                updateAppointmentTable(); // Refresh table
            }
        });

        // Undo last action
        undoBtn.addActionListener(e -> {
            system.undoLastAction(); // Undo 
            updateAppointmentTable(); // Refresh table
        });

        // Save diary
        saveBtn.addActionListener(e -> {
            String selected = professionalList.getSelectedValue();
            if (selected != null) {
                String filename = JOptionPane.showInputDialog(frame, "Filename:");
                system.saveDiary(selected, filename); // Save
            }
        });

        // Load diary
        loadBtn.addActionListener(e -> {
            String selected = professionalList.getSelectedValue();
            if (selected != null) {
                String filename = JOptionPane.showInputDialog(frame, "Filename:");
                system.loadDiary(selected, filename); // Load 
                updateAppointmentTable(); // Refresh table
            }
        });

        // Search available slots
        searchBtn.addActionListener(e -> {
            List<String> names = new ArrayList<>();
            for (String name : professionalList.getSelectedValuesList()) { names.add(name); }
            String dateStr = JOptionPane.showInputDialog(frame,"Date (YYYY-MM-DD):");
            String startStr = JOptionPane.showInputDialog(frame,"Start Time (HH:MM):");
            String durationStr = JOptionPane.showInputDialog(frame,"Duration (minutes):");
            LocalDate date = LocalDate.parse(dateStr);
            LocalTime start = LocalTime.parse(startStr);
            int duration = Integer.parseInt(durationStr);
            system.searchCommonSlots(names,date,start,duration); // Print results
        });

        frame.setVisible(true); // Show GUI
    }

    //Refresh appointment table for selected professional
     
    private void updateAppointmentTable() {
        appointmentTableModel.setRowCount(0); // Clear table
        String selected = professionalList.getSelectedValue();
        if (selected == null) return;
        SchedulerSystem.HealthProfessional hp = system.getProfessionals().get(selected);
        if (hp == null) return;
        for (SchedulerSystem.Appointment appt : hp.getDiary().getAppointments()) {
            appointmentTableModel.addRow(new Object[]{
                appt.getDate(), appt.getStartTime(), appt.getEndTime(), appt.getType()
            });
        }
    }

    //Main method to launch GUI
     
    public static void main(String[] args) {
        SchedulerSystem system = new SchedulerSystem(); 
        SchedulerGUI gui = new SchedulerGUI(system); // GUI instance
        SwingUtilities.invokeLater(() -> gui.createAndShowGUI()); // Run GUI on event-dispatch thread
    }
}
