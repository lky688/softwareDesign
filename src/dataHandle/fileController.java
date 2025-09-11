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

	private static final String USER_FILE_NAME = "users.txt";
	private static final String SESSION_FILE_NAME = "sessions.txt";
	private static final String BOOKED_SESSIONS_DIR = "bookedSessions";

	/*
	 * Users Related File Operations
	 */

	public static ArrayList<User> getAllUsers() {
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

	public static void saveUsersToFile(ArrayList<User> users) {
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
        ArrayList<User> users = getAllUsers();
        boolean updated = false;

        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(targetEmail)) {
                updated = updateUserField(user, field, newValue);
                break;
            }
        }

        if (updated) {
            saveUsersToFile(users);
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


	public static boolean deleteUserByEmail(String targetEmail) {
        ArrayList<User> users = getAllUsers();
		User userToBeDeleted = null;

		for (User user: users) {
	        if (user.getEmail().equalsIgnoreCase(targetEmail)) {
				userToBeDeleted = user; // User to be deleted is found
				break;
	    	} 
	    }

        if (userToBeDeleted != null) {
			users.remove(userToBeDeleted);
            saveUsersToFile(users);
            System.out.println("The account has been successfully deleted.");
			return true;
        } else {
            System.out.println("The email was not found, account has not been deleted.");
			return false;
        }
    }

	public static boolean addUser(String email, String name, String role, String password) {
        ArrayList<User> users = getAllUsers();

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
        saveUsersToFile(users);
        return true;
    }
	

	/*
	 * Sessions Related File Operations
	*/

	public static ArrayList<Session> getAllSessions() {
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

	public static void saveSessionsToFile(ArrayList<Session> sessions) {
		try (PrintWriter writer = new PrintWriter(SESSION_FILE_NAME)) {
		    for (Session session: sessions) {
		        writer.printf("%d|%s|%s|%s|%d|%d|%d|%s%n",
		            session.getSessionID(),
		            session.getCourseName(),
		            session.getDate(),
		            session.getStartTime(),
		            session.getDuration(),
		            session.getOccupiedCapacity(),
		            session.getMaxPerson(),
		            session.getVenue()
		        );
		    }
		} catch (FileNotFoundException e) {
		    System.out.println("Error writing to file: " + e.getMessage());
		}
    }

	public static boolean updateSession(String sessionId, String updateField, String newData) {
	    ArrayList<Session> sessions = getAllSessions();
	    boolean updated = false;

	    for (Session session: sessions) {
	        if (Integer.toString(session.getSessionID()).equalsIgnoreCase(sessionId)) {
	            updated = updateSessionField(session, updateField, newData);
	            break;
	        }
	    }

	    if (updated) {
	    	saveSessionsToFile(sessions);
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
			case "occupiedcapacity": 
				session.setOccupiedCapacity(Integer.parseInt(newValue));
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
	
	public static boolean deleteSessionById(String sessionId) {
	    ArrayList<Session> sessions = getAllSessions();
		Session sessionToBeDeleted = null;

	    for (Session session : sessions) {
	        if (Integer.toString(session.getSessionID()).equalsIgnoreCase(sessionId)) {
				sessionToBeDeleted = session;
	        }
	    }

	    if (sessionToBeDeleted != null) {
			sessions.remove(sessionToBeDeleted);
	    	deleteBookedFile(sessionId);
	        saveSessionsToFile(sessions);
			return true;
	    } else {
	        System.out.println("Session ID not found, session not deleted.");
			return false;
	    }
	}

	private static void deleteBookedFile(String sessionId) {
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
	
	public static void addSession(String courseName, String date, String startTime, int duration,
	    int occupiedCapacity, int maxPerson, String venue) {
		ArrayList<Session> sessions = getAllSessions();
		
	    int newId = 0;
	    for (Session session : sessions) {
	        if (session.getSessionID() > newId) {
	            newId = session.getSessionID();
	        }
	    }
	    int newCourseId = newId + 1;
		
		sessions.add(new Session(newCourseId,courseName, date, startTime, duration, occupiedCapacity, maxPerson, venue));
		saveSessionsToFile(sessions);
	}
	
	public static boolean addBookedList(User user, String sessionId) {
	    ArrayList<Session> sessions = getAllSessions();

	    for (Session session: sessions) {
	        if (Integer.toString(session.getSessionID()).equalsIgnoreCase(sessionId)) {
	            // Session found
	            if (session.getOccupiedCapacity() < session.getMaxPerson()) {
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
		return updateSession(sessionId, "occupiedcapacity", String.valueOf(newTotalBookedNum));
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
        ArrayList<Session> sessions = getAllSessions();

        for (Session session: sessions) {
            if (Integer.toString(session.getSessionID()).equalsIgnoreCase(sessionId)) {
                if (session.getOccupiedCapacity() > 0) {
                    int newTotalBookedNum = session.getOccupiedCapacity() - 1;
                    updateSession(sessionId, "occupiedcapacity", String.valueOf(newTotalBookedNum));
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
    
    public static List<String> getBookedSessionsForUser(User user) {
        List<String> bookedSessions = new ArrayList<>();
        ArrayList<Session> sessions = getAllSessions();

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

	public static ArrayList<User> getStudentsBySessionId(String sessionId) {
		ArrayList<User> students = new ArrayList<>();
		ensureBookedSessionsDirExists();
    	File file = new File(BOOKED_SESSIONS_DIR, sessionId + ".txt");

		if (!file.exists()) {
			return students; // no students joined yet
		}

		ArrayList<User> allUsers = getAllUsers();

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

	private static void ensureBookedSessionsDirExists() {
		File dir = new File(BOOKED_SESSIONS_DIR);
		if (!dir.exists()) {
			dir.mkdirs(); // creates the folder if not exist
		}
	}
}
