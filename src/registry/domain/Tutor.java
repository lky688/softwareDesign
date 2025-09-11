package registry.domain; 	

import dataHandle.fileController;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class Tutor extends User{

	public Tutor(String name, String email, String role, String password) {
		super(name, email , role, password);
	}
	
	public Tutor() {
		super();
	}
	
	@Override
	public void getInput() {
	    Scanner scanner = new Scanner(System.in);

	    while (true) {
			// Print the menu options
			printMenu();

	        String choice = scanner.nextLine().trim();

	        if (choice.equalsIgnoreCase("logout")) {
	            System.out.println("Logging out...");
				System.out.println("Logged out successfully.");
	            break; // exit loop and method
	        }

	        switch (choice) {
	            case "1":
	                // Add new session
					handleAddSession(scanner);
	                break;
	            case "2":
	                // Delete session
	                handleDeleteSession(scanner);
	                break;
	            case "3":
	                // Edit session
	                handleEditSession(scanner);
					break;
				case "4":
					// View all session
					displayAllSessions();
					System.out.print("Enter any keys to return to menu: ");
					scanner.nextLine();
					System.out.println("Returning to menu...\n");
					break;
				case "5":
					Report report = new Report();
					report.generateSessionsReport();
					System.out.print("Enter any keys to return to menu: ");
					scanner.nextLine();
					System.out.println("Returning to menu...\n");
					break;
	            default:
	                System.out.println("Invalid option, please try again.");
	                break;
	        }
	    }
	}
	
	private void printMenu() {
		System.out.println("---------- Tutor Menu ----------");
		System.out.println("Enter 1 for Adding New Session");
		System.out.println("Enter 2 for Deleting Session");
		System.out.println("Enter 3 for Editing Session");
		System.out.println("Enter 4 for Viewing All Sessions Created");
		System.out.println("Enter 5 for Generating Sessions Report");
		System.out.println("Type 'logout' to exit");
		System.out.print("\nYour choice: ");
	}

	private void printEditMenu() {
        System.out.println("Which field would you like to update?");
        System.out.println("1. Course Name");
        System.out.println("2. Date");
        System.out.println("3. Start Time");
        System.out.println("4. Duration (minutes)");
        System.out.println("5. Max Person");
        System.out.println("6. Venue");
        System.out.println("0. Back to menu");
    }

	private void handleAddSession(Scanner scanner) {
	    System.out.print("Enter course name: ");
	    String courseName = scanner.nextLine();

	    String date = getValidDate(scanner, "Enter date (DD-MM-YYYY, example: 17-06-2025): ");

	    String startTime = getValidTime(scanner, "Enter start time (HH:mm, example: 14:00): ");

	    int duration = getValidIntegerInput(scanner, "Enter duration (minutes): ");

	    int maxPerson = getValidIntegerInput(scanner, "Enter maximum capacity of students: ");

	    System.out.print("Enter venue: ");
	    String venue = scanner.nextLine();

	    fileController.addSession(courseName, date, startTime, duration, 0, maxPerson, venue);
		System.out.println("Session added successfully.");
	}

	private void handleDeleteSession(Scanner scanner) {
		boolean isSessionAvailable = displayAllSessions();

		if (!isSessionAvailable) return;

	    while (true) {
	        System.out.print("Enter session ID to delete (or type 'back' to cancel): ");
	        String sessionID = scanner.nextLine().trim();

	        if (sessionID.equalsIgnoreCase("back")) {
	            System.out.println("Cancelled session deletion. Returning to menu...\n");
	            break;
	        }

	        ArrayList<Session> sessions = fileController.getAllSessions();
	        boolean sessionFound = isSessionFound(sessions, sessionID);

	        if (sessionFound) {
	            fileController.deleteSessionById(sessionID);
	            System.out.println("Session deleted successfully.");
	            return; // exit delete loop
	        } else {
	            System.out.println("Session ID not found. Please try again.");
	        }
	    }
	}

	private void handleEditSession(Scanner scanner) {
		boolean isSessionAvailable = displayAllSessions();

		if (!isSessionAvailable) return;

        while (true) {
            System.out.print("Enter session ID to edit (or type 'back' to cancel): ");
            String sessionID = scanner.nextLine().trim();

            if (sessionID.equalsIgnoreCase("back")) {
                System.out.println("Returning to menu...\n");
                return;
            }

            ArrayList<Session> sessions = fileController.getAllSessions();
			boolean sessionFound = isSessionFound(sessions, sessionID);
            if (!sessionFound) {
				System.out.println("Session ID not found. Please try again.");
				continue;
			}

            printEditMenu();
            int editChoice = getValidIntegerInput(scanner, "Enter your choice (0-6): ");

            if (editChoice == 0) {
                System.out.println("Cancelled editing. Returning to menu...\n");
                return;
            }

            String field;
			boolean isDate = false;
			boolean isTime = false;
			boolean isNumeric = false;

	        switch (editChoice) {
	            case 1: field = "courseName"; break;
	            case 2: field = "date"; isDate = true; break;
	            case 3: field = "startTime"; isTime = true; break;
	            case 4: field = "duration"; isNumeric = true; break;
	            case 5: field = "maxPerson"; isNumeric = true; break;
	        	case 6: field = "venue"; break;
	            default:
	                System.out.println("Invalid choice. Please try again.");
	                continue;
	        }

            String newValue;

			if (isDate) {
	            newValue = getValidDate(scanner, "Enter new date (DD-MM-YYYY): ");
	        } else if (isTime) {
	            newValue = getValidTime(scanner, "Enter new start time (HH:mm): ");
	        } else if (isNumeric) {
	            int intValue = getValidIntegerInput(scanner, "Enter new integer value for " + field + ": ");
	            newValue = Integer.toString(intValue);
	        } else {
	            System.out.print("Enter new value for " + field + ": ");
	            newValue = scanner.nextLine().trim();
	        }

            boolean result = fileController.updateSession(sessionID, field, newValue);
            if (result) {
	            System.out.println("Session updated successfully.");
	        } else {
	            System.out.println("Failed to update session. Try again.");
	        }
            return;
        }
    }

	private boolean isSessionFound(ArrayList<Session> sessions, String sessionID) {
		for (Session session: sessions) {
	        if (Integer.toString(session.getSessionID()).equals(sessionID)) {
                return true;
	        }
	    }
		return false;
	}

	private int getValidIntegerInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                int number = Integer.parseInt(input);
                return number; 
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter an integer value.");
            }
        }
    }

	private String getValidDate(Scanner scanner, String prompt) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

		while (true) {
			System.out.print(prompt);
			String input = scanner.nextLine().trim();
			try {
				LocalDate.parse(input, formatter);
				return input;
			} catch (DateTimeParseException e) {
				System.out.println("Invalid date format. Please use DD-MM-YYYY.");
			}
		}
	}

    private String getValidTime(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                LocalTime.parse(input, DateTimeFormatter.ofPattern("HH:mm"));
                return input;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid time format. Please use HH:mm.");
            }
        }
    }
	
	public static boolean displayAllSessions() {
	    ArrayList<Session> sessions = fileController.getAllSessions();

		if (sessions.isEmpty()) {
            System.out.println("\nNo available sessions at the moment.");
            return false;
        }

	    System.out.println("\nAvailable Sessions:");
		System.out.println("----------------------------");
	    for (Session session: sessions) {
	        System.out.printf("Session ID: %d%n", session.getSessionID());
	        System.out.printf("Course Name: %s%n", session.getCourseName());
	        System.out.printf("Date: %s%n", session.getDate());
	        System.out.printf("Start Time: %s%n", session.getStartTime());
	        System.out.printf("Duration: %d minutes%n", session.getDuration());
	        System.out.printf("Occupied Capacity: %d/%d%n", session.getOccupiedCapacity(), session.getMaxPerson());
	        System.out.printf("Venue: %s%n", session.getVenue());
	        System.out.println("----------------------------");
	    }
		System.out.println();

	    return true;
	}
}
