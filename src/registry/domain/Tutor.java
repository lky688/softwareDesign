package registry.domain; 	

import java.util.ArrayList;
import java.util.Scanner;
import dataHandle.fileController;

public class Tutor extends User{


	public Tutor(String name, String email, String role, String password) {
		super(name,email , role, password);
	}
	
	public Tutor() {
		super();
	}
	
	@Override
	public void getInput() {
	    Scanner scanner = new Scanner(System.in);

	    while (true) {
	        System.out.println("\n--- Menu ---");
	        System.out.println("Enter 1 for Adding New Session");
	        System.out.println("Enter 2 for Deleting Session");
	        System.out.println("Enter 3 for Editing Session");
	        System.out.println("Type 'logout' to exit");
	        System.out.print("Your choice: ");

	        String choice = scanner.nextLine().trim();

	        if (choice.equalsIgnoreCase("logout")) {
	            System.out.println("Logging out...");
	            break; // exit loop and method
	        }

	        switch (choice) {
	            case "1":
	                // Add new session
	                System.out.print("Enter course name: ");
	                String courseName = scanner.nextLine();

	                System.out.print("Enter date (e.g., 2025-09-09): ");
	                String date = scanner.nextLine();

	                System.out.print("Enter start time (e.g., 14:00): ");
	                String startTime = scanner.nextLine();

	                int duration = getValidIntegerInput(scanner, "Enter duration (in minutes): ");

	                int maxPerson = getValidIntegerInput(scanner, "Enter maximum number of persons: ");

	                System.out.print("Enter venue: ");
	                String venue = scanner.nextLine();

	                fileController.addSession(courseName, date, startTime, duration, 0, maxPerson, venue);
	                break;

	            case "2":
	                // Delete session
	                boolean skip = printAvailableSessions();
	                while (true && skip) {
	                    System.out.print("Please enter the session ID to delete (or type 'back' to cancel): ");
	                    String targetSessionID = scanner.nextLine().trim();

	                    if (targetSessionID.equalsIgnoreCase("back")) {
	                        System.out.println("Cancelled session deletion. Returning to menu...");
	                        break;
	                    }

	                    ArrayList<Session> sessions = fileController.getListedSession();
	                    boolean found = false;

	                    for (Session s : sessions) {
	                        if (Integer.toString(s.getSessionID()).equals(targetSessionID)) {
	                            found = true;
	                            break;
	                        }
	                    }

	                    if (found) {
	                        fileController.deleteListedSession(targetSessionID);
	                        System.out.println("Session deleted successfully.");
	                        break; // exit delete loop
	                    } else {
	                        System.out.println("Session ID not found. Please try again or type 'back' to return.");
	                    }
	                }
	                break;

	            case "3":
	                // Edit session
	                skip = printAvailableSessions();
	                while (true && skip) {
	                    System.out.print("Enter the session ID you want to edit (or type 'back' to cancel): ");
	                    String sessionID = scanner.nextLine().trim();

	                    if (sessionID.equalsIgnoreCase("back")) {
	                        System.out.println("Returning to menu...");
	                        break;
	                    }

	                    ArrayList<Session> sessions = fileController.getListedSession();
	                    boolean found = false;

	                    for (Session s : sessions) {
	                        if (Integer.toString(s.getSessionID()).equals(sessionID)) {
	                            found = true;
	                            break;
	                        }
	                    }

	                    if (!found) {
	                        System.out.println("Session ID not found. Please try again.");
	                        continue;
	                    }

	                    System.out.println("Which field would you like to update?");
	                    System.out.println("1. Course Name");
	                    System.out.println("2. Date");
	                    System.out.println("3. Start Time");
	                    System.out.println("4. Duration (in minutes)");
	                    System.out.println("5. Available Person");
	                    System.out.println("6. Max Person");
	                    System.out.println("7. Venue");
	                    System.out.println("0. Back to menu");

	                    int editChoice = getValidIntegerInput(scanner, "Enter your choice (0-7): ");

	                    if (editChoice == 0) {
	                        System.out.println("Cancelled editing. Returning to menu...");
	                        break;
	                    }

	                    String field = null;
	                    boolean isNumeric = false;

	                    switch (editChoice) {
	                        case 1: field = "courseName"; break;
	                        case 2: field = "date"; break;
	                        case 3: field = "startTime"; break;
	                        case 4: field = "duration"; isNumeric = true; break;
	                        case 5: field = "availablePerson"; isNumeric = true; break;
	                        case 6: field = "maxPerson"; isNumeric = true; break;
	                        case 7: field = "venue"; break;
	                        default:
	                            System.out.println("Invalid choice. Please try again.");
	                            continue;
	                    }

	                    String newValue;

	                    if (isNumeric) {
	                        int intValue = getValidIntegerInput(scanner, "Enter new integer value for " + field + ": ");
	                        newValue = Integer.toString(intValue);
	                    } else {
	                        System.out.print("Enter new value for " + field + ": ");
	                        newValue = scanner.nextLine().trim();
	                    }

	                    boolean result = fileController.updateSession(sessionID, field, newValue);
	                    if (result) {
	                        System.out.println("Session updated successfully.");
	                        break; // exit edit loop after success
	                    } else {
	                        System.out.println("Failed to update session. Try again.");
	                    }
	                }
	                break;

	            default:
	                System.out.println("Invalid option, please try again.");
	                break;
	        }
	    }
	}
	
	private static int getValidIntegerInput(Scanner scanner, String prompt) {
        int number;
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                number = Integer.parseInt(input);
                break; 
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter an integer value.");
            }
        }
        return number;
    }
	
	public static boolean printAvailableSessions() {
	    ArrayList<Session> sessions = fileController.getListedSession();

	    boolean found = false;
	    System.out.println("Available Sessions:");
	    for (Session session : sessions) {
	    	{
	            found = true;
	            System.out.printf("Session ID: %d%n", session.getSessionID());
	            System.out.printf("Course Name: %s%n", session.getCourseName());
	            System.out.printf("Date: %s%n", session.getDate());
	            System.out.printf("Start Time: %s%n", session.getStartTime());
	            System.out.printf("Duration: %d minutes%n", session.getDuration());
	            System.out.printf("Available Slots: %d/%d%n", session.getAvailablePerson(), session.getMaxPerson());
	            System.out.printf("Venue: %s%n", session.getVenue());
	            System.out.println("----------------------------");
	        }
	    }

	    if (!found) {
	        System.out.println("No available sessions at the moment.");   
	        return false;
	    }
	    return true;
	 
	}
	
}
