package registry.domain;

import dataHandle.fileController;
import java.util.List;
import java.util.Scanner;


public class Student extends User {

	public Student(String name, String email, String role, String password) {
		super(name, email , role, password);
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
	            break; // Exit the input loop and end the method
	        }

	        switch (choice) {
	            case "1":
	                handleBooking(scanner);
	                break;	
	            case "2":
	                handleCancellation(scanner);
	                break;
	            case "3":
	                displayUserBookedSessions();
	                break;
	            default:
	                System.out.println("Invalid option, please try again.");
	        }
	    }
	}

	private void printMenu() {
        System.out.println("\n--- Menu ---");
        System.out.println("Enter 1 for Booking New Session");
        System.out.println("Enter 2 for Cancel Booked Session");
        System.out.println("Enter 3 for Viewing Booked Session");
        System.out.println("Type 'logout' to exit");
        System.out.print("Your choice: ");
    }
	
	private void handleBooking(Scanner scanner) {
        Tutor.printAvailableSessions();
        while (true) {
            System.out.print("Enter Course ID to book (or 'back' to return): ");
            String courseID = scanner.nextLine();
			
            if (courseID.equalsIgnoreCase("back")) return;

			boolean success = fileController.addBookedList(this, courseID);
            if (success) {
                System.out.println("Session booked successfully for: " + this.getEmail());
                return;
            } else {
                System.out.println("Failed to book the session.");
            }
        }
    }

	private void handleCancellation(Scanner scanner) {
        displayUserBookedSessions();
        while (true) {
            System.out.print("Enter Course ID to cancel (or 'back' to return): ");
            String courseID = scanner.nextLine();

            if (courseID.equalsIgnoreCase("back")) return;

			boolean success = fileController.deleteBookedSession(this, courseID);
            if (success) {
                System.out.println("Booking cancelled for: " + this.getEmail());
                return;
            } else {
                System.out.println("Failed to cancel the booking.");
            }
        }
    }

	public void displayUserBookedSessions() {
	    List<String> bookedSessions = fileController.getBookedSessionsForUser(this);

	    if (bookedSessions.isEmpty()) {
	        System.out.println("You have no booked sessions.");
	    } else {
			System.out.println("Your booked sessions:");
            System.out.println("CourseName (Course ID) - Date - Duration - Venue");
            System.out.println("------------------------------------------------");
	        for (String sessionInfo: bookedSessions) {
	            System.out.println(sessionInfo);
	        }
	    }
	}
}


