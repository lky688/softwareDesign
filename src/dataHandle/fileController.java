package dataHandle;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import registry.domain.Admin;
import registry.domain.Session;
import registry.domain.Student;
import registry.domain.Tutor;
import registry.domain.User;

public class fileController {

	private static final String USER_FILE_NAME = "UserList.txt";
	private static final String SESSION_FILE_NAME = "TutorListedSession.txt";

	public static ArrayList<User> getUserList() {
        ArrayList<User> users = new ArrayList<>();

        try (Scanner fileInput = new Scanner(new File(USER_FILE_NAME))) {
            while (fileInput.hasNextLine()) {
                String line = fileInput.nextLine().trim();
                if (line.isEmpty()) {
					continue;
				}

                String[] parts = line.split("\\|");
                if (parts.length < 4) {
					continue;
				}

                String role = parts[2].trim().toLowerCase();
                User user = createUser(parts, role);

                if (user != null) {
                    users.add(user);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + USER_FILE_NAME);
        }
		
        return users;
    }

	private static User createUser(String[] parts, String role) {
        switch (role) {
            case "admin": return new Admin(parts[0], parts[1], parts[2], parts[3]);
            case "tutor": return new Tutor(parts[0], parts[1], parts[2], parts[3]);
            case "student": return new Student(parts[0], parts[1], parts[2], parts[3]);
            default: return null;
        }
    }

	public static void writeUsers(ArrayList<User> users) {
	    try (PrintWriter writer = new PrintWriter(USER_FILE_NAME)) {
	        for (User user: users) {
	            String line = user.getName() + "|" +
	                          user.getEmail() + "|" +
	                          user.getRole() + "|" +
	                          user.getPassword();
	            writer.println(line);
	        }
	        System.out.println("Users successfully written to " + USER_FILE_NAME);
	    } catch (FileNotFoundException e) {
	        System.out.println("Error writing to file: " + e.getMessage());
	    }
	}
		
	public static boolean updateUser(String targetEmail, String field, String newValue) {
        ArrayList<User> users = getUserList();
        boolean updated = false;

        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(targetEmail)) {
                updated = updateUserField(user, field, newValue);
                break;
            }
        }

        if (updated) {
            writeUsers(users);
            System.out.println("User updated successfully.");
            return true;
        } else {
            System.out.println("User not found with email: " + targetEmail);
            return false;
        }
    }

	private static boolean updateUserField(User user, String field, String newValue) {
        switch (field.toLowerCase()) {
            case "name": user.setName(newValue); return true;
            case "role": user.setRole(newValue); return true;
            case "password": user.setPassword(newValue); return true;
            case "email": user.setEmail(newValue); return true;
            default:
                System.out.println("Invalid field: " + field);
                return false;
        }
    }


	public static boolean deleteUserList(String targetEmail) {
        ArrayList<User> users = getUserList();
		User userToBeDeleted = null;

		for (User user: users) {
	        if (user.getEmail().equalsIgnoreCase(targetEmail)) {
				userToBeDeleted = user; // User to be deleted is found
				break;
	    	} 
	    }

        if (userToBeDeleted != null) {
			users.remove(userToBeDeleted);
            writeUsers(users);
            System.out.println("The account has been successfully deleted.");
			return true;
        } else {
            System.out.println("The email was not found, account has not been deleted.");
			return false;
        }
    }

	public static boolean addUserList(String email, String name, String role, String password) {
        ArrayList<User> users = getUserList();

        for (User user: users) {
	        if (user.getEmail().equalsIgnoreCase(email)) {
	            System.out.println("User with this email already exists. Please try another email address.");
	            return false;
	        }
	    }

		String[] parts = {name, email, role, password};
        User newUser = createUser(parts, role.trim().toLowerCase());
        if (newUser == null) return false;

        users.add(newUser);
        writeUsers(users);
        return true;
    }
	


	
	
	public static ArrayList<Session> getListedSession() {
		ArrayList <Session> sessions = new ArrayList<>();

		try (Scanner scanner = new Scanner(new File(SESSION_FILE_NAME))) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();
            	if (line.isEmpty()) {
					continue;
				}

				String[] parts = line.split("\\|");
				Session session = createSession(parts);
				if (session != null) {
					sessions.add(session);
				}
			}
		} catch (FileNotFoundException e) {
        	System.out.println("File not found: " + SESSION_FILE_NAME);
		}
		return sessions;
	}

	private static Session createSession(String[] parts) {
		if (parts.length < 8) return null;
		return new Session(
			Integer.parseInt(parts[0]),
			parts[1], parts[2], parts[3],
			Integer.parseInt(parts[4]),
			Integer.parseInt(parts[5]),
			Integer.parseInt(parts[6]),
			parts[7]
		);
	}

	public static void writeSessions(ArrayList<Session> sessions) {
		try (PrintWriter writer = new PrintWriter(SESSION_FILE_NAME)) {
		    for (Session session: sessions) {
		        writer.printf("%d|%s|%s|%s|%d|%d|%d|%s%n",
		            session.getSessionID(),
		            session.getCourseName(),
		            session.getDate(),
		            session.getStartTime(),
		            session.getDuration(),
		            session.getAvailablePerson(),
		            session.getMaxPerson(),
		            session.getVenue()
		        );
		    }
		} catch (FileNotFoundException e) {
		    System.out.println("Error writing to file: " + e.getMessage());
		}
    }

	public static boolean updateSession(String sessionId, String updateField, String newData) {
	    ArrayList<Session> sessions = getListedSession();
	    boolean updated = false;

	    for (Session session: sessions) {
	        if (Integer.toString(session.getSessionID()).equalsIgnoreCase(sessionId)) {
	            updated = updateSessionField(session, updateField, newData);
	            break;
	        }
	    }

	    if (updated) {
	    	writeSessions(sessions);
	    } else {
	        System.out.println("Session not found for ID: " + sessionId);
	    }
	    return updated;
	}

	private static boolean updateSessionField(Session session, String field, String newValue) {
		switch (field.toLowerCase()) {
			case "date": 
				session.setDate(newValue);
				return true;
			case "starttime": 
				session.setStartTime(newValue);
				return true;
			case "duration": 
				session.setDuration(Integer.parseInt(newValue));
				return true;
			case "availableperson": 
				session.setAvailablePerson(Integer.parseInt(newValue));
				return true;
			case "maxperson": 
				session.setMaxPerson(Integer.parseInt(newValue));
				return true;
			case "venue": 
				session.setVenue(newValue);
				return true;
			case "coursename": 
				session.setCourseName(newValue);
					return true;
			default:
				System.out.println("Invalid field: " + field);
				return false;
		}
	}
	
	public static boolean deleteListedSession(String targetListedSession) {
	    ArrayList<Session> sessions = getListedSession();
		Session sessionToBeDeleted = null;

	    for (Session session : sessions) {
	        if (Integer.toString(session.getSessionID()).equalsIgnoreCase(targetListedSession)) {
				sessionToBeDeleted = session;
	        }
	    }

	    if (sessionToBeDeleted != null) {
			sessions.remove(sessionToBeDeleted);
	    	deleteBookedFile(targetListedSession);
	        writeSessions(sessions);
			return true;
	    } else {
	        System.out.println("Session ID not found, session not deleted.");
			return false;
	    }
	}

	private static void deleteBookedFile(String sessionId) {
		File bookedFile = new File(sessionId + ".txt");
		if (bookedFile.exists()) {
			if (bookedFile.delete()) {
				System.out.println("Deleted booked student file: " + bookedFile.getName());
			} else {
				System.out.println("Failed to delete booked student file: " + bookedFile.getName());
			}
		}
	}
	
	public static void addSession(String courseName, String date, String startTime, int duration,
	    int availablePerson, int maxPerson, String venue) {
		ArrayList<Session> sessions = getListedSession();
		
	    int newId = 0;
	    for (Session session : sessions) {
	        if (session.getSessionID() > newId) {
	            newId = session.getSessionID();
	        }
	    }
	    int newCourseId = newId + 1;
		
		sessions.add(new Session(newCourseId,courseName, date, startTime, duration, availablePerson, maxPerson, venue));
		writeSessions(sessions);
	}
	
	public static boolean addBookedList(User user, String sessionId) {
	    ArrayList<Session> sessions = getListedSession();

	    for (Session session: sessions) {
	        if (Integer.toString(session.getSessionID()).equalsIgnoreCase(sessionId)) {
	            // Session found
	            if (session.getAvailablePerson() < session.getMaxPerson()) {
					return handleBooking(user, sessionId, session);
	            } else {
					System.out.println("The session has been fully occupied.");
	                return false;
	            }
	        }
	    }

	    System.out.println("Session ID not found: " + sessionId);
	    return false;
	}

	private static boolean handleBooking(User user, String sessionId, Session session) {
		File file = new File(sessionId + ".txt");

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

		int newTotalBookedNum = session.getAvailablePerson() + 1;
		return updateSession(sessionId, "availableperson", String.valueOf(newTotalBookedNum));
	}

	private static boolean isUserAlreadyBooked(User user, File file) {
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

    public static boolean deleteBookedSession(User user, String sessionId) {
        ArrayList<Session> sessions = getListedSession();

        for (Session session: sessions) {
            if (Integer.toString(session.getSessionID()).equalsIgnoreCase(sessionId)) {
                if (session.getAvailablePerson() > 0) {
                    int newTotalBookedNum = session.getAvailablePerson() - 1;
                    updateSession(sessionId, "availableperson", String.valueOf(newTotalBookedNum));
                } 

                File file = new File(sessionId + ".txt");
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
    
    public static List<String> getUserBookedSessions(User user) {
        List<String> bookedSessions = new ArrayList<>();
        ArrayList<Session> sessions = getListedSession();

        for (Session session: sessions) {
            File file = new File(session.getSessionID() + ".txt");
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

}

	
