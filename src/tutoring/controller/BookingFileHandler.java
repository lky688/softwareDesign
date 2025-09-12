package tutoring.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import tutoring.domain.Session;
import tutoring.domain.User;

public class BookingFileHandler {
	
	private static final String BOOKED_SESSIONS_DIR = "bookedSessions";
	
	/*
	 * Session Bookings Related File Operations
	*/

	private static void ensureBookedSessionsDirExists() {
		File dir = new File(BOOKED_SESSIONS_DIR);
		if (!dir.exists()) {
			dir.mkdirs(); // creates the folder if not exist
		}
	}
	
	public static boolean handleBooking(User user, String sessionId, Session session) {
		ensureBookedSessionsDirExists();
		File file = new File(BOOKED_SESSIONS_DIR, sessionId + ".txt");
	
		// Check if user already booked this session
		if (file.exists() && isUserAlreadyBooked(user, file)) {
			System.out.println("You have already booked this session.");
			return false;
		}
	
		// If not booked, create file if necessary and append email
		try {
			if (!file.exists()) {
				file.createNewFile(); // Create file if it doesn't exist
			}
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
				writer.write(user.getEmail()); // Append the user email
				writer.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	
		int newTotalBookedNum = session.getOccupiedCapacity() + 1;
		return SessionFileHandler.updateSession(sessionId, "occupiedcapacity", String.valueOf(newTotalBookedNum));
	}

	public static ArrayList<User> getStudentsBySessionId(String sessionId) {
		ArrayList<User> students = new ArrayList<>();
		ensureBookedSessionsDirExists();
		File file = new File(BOOKED_SESSIONS_DIR, sessionId + ".txt");
	
		if (!file.exists()) {
			return students; // no students joined yet
		}
	
		ArrayList<User> allUsers = UserFileHandler.getAllUsers();
	
		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String email = scanner.nextLine().trim();
				if (!email.isEmpty()) {
					// find the user object by email
					for (User user: allUsers) {
						if (user.getEmail().equalsIgnoreCase(email)) {
							students.add(user);
							break; // current student is found, break the loop to move to next student email
						}
					}
				}
			}
			scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		return students;
	}

	public static List<String> getBookedSessionsForUser(User user) {
	    List<String> bookedSessions = new ArrayList<>();
	    ArrayList<Session> sessions = SessionFileHandler.getAllSessions();
	
	    for (Session session: sessions) {
	        ensureBookedSessionsDirExists();
			File file = new File(BOOKED_SESSIONS_DIR, session.getSessionID() + ".txt");
	        if (file.exists()) {
	            try {
	                Scanner scanner = new Scanner(file);
	                while (scanner.hasNextLine()) {
	                    String emailRecorded = scanner.nextLine();
	                    if (emailRecorded.trim().equalsIgnoreCase(user.getEmail())) {
	                        String sessionInfo = String.format(
	                            "%s (%d) - Date: %s - Duration: %d minutes - Venue: %s",
	                            session.getCourseName(),
	                            session.getSessionID(),
	                            session.getDate(),       
	                            session.getDuration(),   
	                            session.getVenue()       
	                        );
	                        bookedSessions.add(sessionInfo);
	                    }
	                }
	                scanner.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	    return bookedSessions;
	}

	public static boolean deleteBookedSession(User user, String sessionId) {
	    ArrayList<Session> sessions = SessionFileHandler.getAllSessions();
	
	    for (Session session: sessions) {
	        if (Integer.toString(session.getSessionID()).equalsIgnoreCase(sessionId)) {
	            if (session.getOccupiedCapacity() > 0) {
	                int newTotalBookedNum = session.getOccupiedCapacity() - 1;
	                SessionFileHandler.updateSession(sessionId, "occupiedcapacity", String.valueOf(newTotalBookedNum));
	            } 
	
	            ensureBookedSessionsDirExists();
				File file = new File(BOOKED_SESSIONS_DIR, sessionId + ".txt");
	            if (file.exists()) {
	                try {
	                    Scanner scanner = new Scanner(file);
	                    List<String> lines = new ArrayList<>();
	                    while (scanner.hasNextLine()) {
	                        String line = scanner.nextLine();
	                        if (!line.trim().equalsIgnoreCase(user.getEmail())) {
	                            lines.add(line);
	                        }
	                    }
	                    scanner.close();
	                    FileWriter fw = new FileWriter(file, false); 
	                    for (String email: lines) {
	                    	fw.write(email + "\n");
	                    }
	                    fw.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	            return true;
	        }
	    }
	
	    System.out.println("Session ID not found: " + sessionId);
	    return false;
	}
	
	public static void deleteBookedFile(String sessionId) {
		ensureBookedSessionsDirExists();
		File bookedFile = new File(BOOKED_SESSIONS_DIR, sessionId + ".txt");
		if (bookedFile.exists()) {
			if (bookedFile.delete()) {
				System.out.println("Deleted booked student file: " + bookedFile.getName());
			} else {
				System.out.println("Failed to delete booked student file: " + bookedFile.getName());
			}
		}
	}

	public static boolean isUserAlreadyBooked(User user, File file) {
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.trim().equalsIgnoreCase(user.getEmail())) return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

}
