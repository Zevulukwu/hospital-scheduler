import java.time.LocalDate;                // For appointment dates
import java.time.LocalTime;                // For appointment times
import java.time.LocalDateTime;            // For displaying current system time
import java.util.ArrayList;                // For dynamic lists of strings
import java.util.List;                     // List interface
import java.util.Scanner;                  // For console input

// == CONSOLE MENU SYSTEM 
public class ConsoleMenu {

    private SchedulerSystem system;     //manage health professionals and appointments
    private Scanner userInput;          // Scanner object for reading user input from console

    // Constructor to initialize the console menu with a SchedulerSystem
    public ConsoleMenu(SchedulerSystem system) {
        this.system = system;                                  // Store reference to backend system
        this.userInput = new Scanner(System.in);               // Initialize scanner for input
    }

    // Starts the main menu loop
    public void beginSystem() {
        System.out.println("Hi there!...Welcome to the Hospital Operation Scheduler System"); // Welcome message

        int selectedOption = -1;  // Stores the user's menu choice
        int menuCounter = 0;      // Counts how many times the menu has been displayed

        // Main loop continues until user chooses exit (option 13)
        while(selectedOption != 13) {
            menuCounter++; // Increment menu display counter
            System.out.println("\nMenu has been opened " + menuCounter + " times"); // Show menu counter

            displayMenuOptions();                // Display the menu options
            selectedOption = getUserChoice();   // Read user input
            handleMenuSelection(selectedOption); // Execute the selected menu action
        }

        userInput.close(); // Close the Scanner when the program ends
    }

    // Displays all menu options and current system time
    private void displayMenuOptions() {
        System.out.println("System Time: " + LocalDateTime.now()); // Show current date and time
        System.out.println("\n--- Hospital Operation Scheduler ---"); // Header
        System.out.println("1. Add Health Professional");            // Option 1
        System.out.println("2. Edit Health Professional");           // Option 2
        System.out.println("3. Delete Health Professional");         // Option 3
        System.out.println("4. Display Health Professionals");       // Option 4
        System.out.println("5. Add Appointment");                     // Option 5
        System.out.println("6. Edit Appointment");                    // Option 6
        System.out.println("7. Delete Appointment");                  // Option 7
        System.out.println("8. Display Diary");                       // Option 8
        System.out.println("9. Save Diary");                          // Option 9
        System.out.println("10. Load Diary");                         // Option 10
        System.out.println("11. Search Common Slot Across Multiple Diaries"); // Option 11
        System.out.println("12. Undo Last Action");                   // Option 12
        System.out.println("13. Exit");                                // Option 13
    }

    // Reads user's choice from console and returns it as an integer
    private int getUserChoice() {
        System.out.print("Select option: ");                    // Prompt user
        return Integer.parseInt(userInput.nextLine());          // Read line, parse to int, return
    }

    // Executes the menu action corresponding to user's selection
    private void handleMenuSelection(int option) {
        switch (option) { // Classic switch-case structure
            case 1: { // Add Health Professional
                System.out.print("Name: ");                         // Ask for name
                String name = userInput.nextLine();                 // Read name

                System.out.print("Profession: ");                   // Ask for profession
                String prof = userInput.nextLine();                 // Read profession

                System.out.print("Office: ");                       // Ask for office
                String office = userInput.nextLine();               // Read office

                system.addProfessional(name, prof, office);         // Add HP to backend
                break;                                              // Exit case
            }

            case 2: { // Edit Health Professional
                System.out.print("Enter HP name to edit: ");       // Ask for HP to edit
                String name = userInput.nextLine();                // Read HP name

                System.out.print("New name (empty to keep): ");     // Ask new name
                String newName = userInput.nextLine();             // Read new name

                System.out.print("New profession (empty to keep): "); // Ask new profession
                String newProf = userInput.nextLine();             // Read new profession

                System.out.print("New office (empty to keep): ");  // Ask new office
                String newOffice = userInput.nextLine();           // Read new office

                system.editProfessional(name, newName, newProf, newOffice); // Update HP
                break;                                              // Exit case
            }

            case 3: { // Delete Health Professional
                System.out.print("Enter HP name to delete: ");    // Ask which HP to delete
                String name = userInput.nextLine();               // Read HP name
                system.deleteProfessional(name);                  // Remove HP from backend
                break;                                            // Exit case
            }

            case 4: { // Display all Health Professionals
                for (SchedulerSystem.HealthProfessional hp : system.getProfessionals().values()) { // Loop all HPs
                    System.out.println(hp);                      // Print HP info
                }
                break;                                            // Exit case
            }

            case 5: { // Add Appointment
                System.out.print("HP Name: ");                    // Ask for HP
                String name = userInput.nextLine();               // Read HP name

                System.out.print("Date (YYYY-MM-DD): ");         // Ask date
                LocalDate date = LocalDate.parse(userInput.nextLine()); // Parse date

                System.out.print("Start time (HH:MM): ");        // Ask start time
                LocalTime start = LocalTime.parse(userInput.nextLine()); // Parse start

                System.out.print("End time (HH:MM): ");          // Ask end time
                LocalTime end = LocalTime.parse(userInput.nextLine());   // Parse end

                System.out.print("Type: ");                       // Ask appointment type
                String type = userInput.nextLine();               // Read type

                system.addAppointment(name, new SchedulerSystem.Appointment(date, start, end, type)); // Add appointment
                break;                                            // Exit case
            }

            case 6: { // Edit Appointment
                System.out.print("HP Name: ");                   // Ask for HP
                String name = userInput.nextLine();              // Read HP name

                List<SchedulerSystem.Appointment> apps =         // Get current appointments
                        system.getProfessionals().get(name).getDiary().getAppointments();

                for (int i = 0; i < apps.size(); i++) {          // Loop to show appointments
                    System.out.println(i + ": " + apps.get(i));  // Print with index
                }

                System.out.print("Select appointment index: ");  // Ask which to edit
                int idx = Integer.parseInt(userInput.nextLine()); // Read index

                System.out.print("New Date (YYYY-MM-DD): ");     // Ask new date
                LocalDate date = LocalDate.parse(userInput.nextLine()); // Parse date

                System.out.print("New Start time (HH:MM): ");    // Ask new start
                LocalTime start = LocalTime.parse(userInput.nextLine()); // Parse start

                System.out.print("New End time (HH:MM): ");      // Ask new end
                LocalTime end = LocalTime.parse(userInput.nextLine());   // Parse end

                System.out.print("New Type: ");                   // Ask new type
                String type = userInput.nextLine();               // Read type

                system.editAppointment(name, idx, new SchedulerSystem.Appointment(date, start, end, type)); // Update appointment
                break;                                            // Exit case
            }

            case 7: { // Delete Appointment
                System.out.print("HP Name: ");                   // Ask for HP
                String name = userInput.nextLine();              // Read HP name

                List<SchedulerSystem.Appointment> apps =         // Get appointments
                        system.getProfessionals().get(name).getDiary().getAppointments();

                for (int i = 0; i < apps.size(); i++) {          // Loop to show appointments
                    System.out.println(i + ": " + apps.get(i));  // Print with index
                }

                System.out.print("Select appointment index: ");  // Ask which to delete
                int idx = Integer.parseInt(userInput.nextLine()); // Read index

                system.deleteAppointment(name, idx);             // Delete appointment
                break;                                            // Exit case
            }

            case 8: { // Display Diary
                System.out.print("HP Name: ");                   // Ask for HP
                String name = userInput.nextLine();              // Read HP name

                List<SchedulerSystem.Appointment> apps =         // Get appointments
                        system.getProfessionals().get(name).getDiary().getAppointments();

                for (SchedulerSystem.Appointment a : apps) {     // Loop all appointments
                    System.out.println(a);                       // Print appointment
                }
                break;                                            // Exit case
            }

            case 9: { // Save Diary
                System.out.print("HP Name: ");                   // Ask for HP
                String name = userInput.nextLine();              // Read HP name

                System.out.print("Filename: ");                  // Ask for file
                String file = userInput.nextLine();              // Read filename

                system.saveDiary(name, file);                    // Save diary to file
                break;                                            // Exit case
            }

            case 10: { // Load Diary
                System.out.print("HP Name: ");                   // Ask for HP
                String name = userInput.nextLine();              // Read HP name

                System.out.print("Filename: ");                  // Ask for file
                String file = userInput.nextLine();              // Read filename

                system.loadDiary(name, file);                    // Load diary from file
                break;                                            // Exit case
            }

            case 11: { // Search Common Slots
                System.out.print("Enter HP names (comma separated): "); // Ask for multiple HPs
                String[] arr = userInput.nextLine().split(",");        // Split input

                List<String> names = new ArrayList<>();                // Store trimmed names
                for (String s : arr) names.add(s.trim());              // Trim spaces

                System.out.print("Date (YYYY-MM-DD): ");              // Ask date
                LocalDate date = LocalDate.parse(userInput.nextLine()); // Parse date

                System.out.print("Start time (HH:MM): ");            // Ask start time
                LocalTime start = LocalTime.parse(userInput.nextLine()); // Parse start

                System.out.print("Duration (minutes): ");            // Ask duration
                int duration = Integer.parseInt(userInput.nextLine());   // Parse duration

                system.searchCommonSlots(names, date, start, duration); // Search and print slots
                break;                                            // Exit case
            }

            case 12: { // Undo Last Action
                system.undoLastAction();                        // Undo last action in system
                break;                                          // Exit case
            }

            case 13: { // Exit
                System.out.println("Exiting...");               // Goodbye message
                break;                                          // Exit case
            }

            default: { // Invalid option
                System.out.println("Invalid option.");          // Warn user
                break;                                          // Exit default
            }
        }
    }
}