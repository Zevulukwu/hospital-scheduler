import javax.swing.SwingUtilities;  // For safely launching GUI
import java.util.Scanner;           // For console input

// == MAIN ENTRY POINT 
public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);            // Scanner to read user input
        SchedulerSystem system = new SchedulerSystem(); 

        System.out.println("Welcome to the Hospital Operation Scheduler!");
        System.out.println("Select mode:");
        System.out.println("1. Graphical User Interface (GUI)");
        System.out.println("2. Console Menu");
        System.out.print("Enter choice (1 or 2): ");

        String choice = sc.nextLine(); // Read user choice

        if (choice.equals("1")) {
            // Launch GUI
            SwingUtilities.invokeLater(() -> {
                SchedulerGUI gui = new SchedulerGUI(system); 
                gui.createAndShowGUI();                       // Display GUI
            });
        } else if (choice.equals("2")) {
            // Launch console menu
            ConsoleMenu menu = new ConsoleMenu(system); // Pass backend system
            menu.beginSystem();                          // Start console menu loop
        } else {
            System.out.println("Invalid choice. Exiting program."); // Handle invalid input
        }

        sc.close(); // Close scanner
    }
}