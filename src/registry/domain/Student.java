package registry.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import dataHandle.fileController;


public class Student extends User{

	public Student(String name, String email, String role, String password) {
		super(name,email , role, password);
	}
	
	@Override
	public void getInput() {
	    Scanner scanner = new Scanner(System.in);

	    while (true) {
	        System.out.println("\n--- Menu ---");
	        System.out.println("Enter 1 for Booking New Session");
	        System.out.println("Enter 2 for Cancel Booked Session");
	        System.out.println("Enter 3 for Viewing Booked Session");
	        System.out.println("Type 'logout' to exit");
	        System.out.print("Your choice: ");
	        
	        String choice = scanner.nextLine().trim();

	        if (choice.equalsIgnoreCase("logout")) {
	            System.out.println("Logging out...");
	            break; // Exit the input loop and end the method
	        }

	        switch (choice) {
	            case "1":
	                Tutor.printAvailableSessions();
	                while (true) {
	                    System.out.print("Enter Course ID to book (or type 'back' to return): ");
	                    String courseID = scanner.nextLine();

	                    if (courseID.equalsIgnoreCase("back")) {
	                        break; // return to main menu
	                    }

	                    boolean success = fileController.addBookedList(this, courseID);
	                    if (success) {
	                        System.out.println("Session booked successfully for: " + this.getEmail());
	                        break; // booking successful, return to main menu
	                    } else {
	                        System.out.println("Failed to book the session.");
	                    }
	                }
	                break;

	            case "2":
	                displayUserBookedSessions(this);
	                while (true) {
	                    System.out.print("Enter Course ID to cancel (or type 'back' to return): ");
	                    String courseID = scanner.nextLine();

	                    if (courseID.equalsIgnoreCase("back")) {
	                        break; // return to main menu
	                    }

	                    boolean success = fileController.deleteBookedSession(this, courseID);
	                    if (success) {
	                        System.out.println("Booking cancelled for: " + this.getEmail());
	                        break;
	                    } else {
	                        System.out.println("Failed to cancel the booking.");
	                    }
	                }
	                break;

	            case "3":
	                displayUserBookedSessions(this);
	                break;

	            default:
	                System.out.println("Invalid option, please try again.");
	        }
	    }
	}
	
	public static void displayUserBookedSessions(User user) {
	    List<String> bookedSessions = fileController.getUserBookedSessions(user);

	    if (bookedSessions.isEmpty()) {
	        System.out.println("You have no booked sessions.");
	    } else {
	        System.out.println("Your booked sessions (format: CourseName (Course ID) - Date - Duration - Venue):");
	        System.out.println("-----------------------------------------------------------------------");
	        for (String sessionInfo : bookedSessions) {
	            System.out.println(sessionInfo);
	        }
	    }
	}
	}


