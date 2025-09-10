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
	public static ArrayList<User> getUserList() {
	    String filePath = "UserList.txt";
	    ArrayList<User> users = new ArrayList<>();
	    
	    try {
	        File file = new File(filePath);
	        Scanner fileInput = new Scanner(file);

	        while (fileInput.hasNextLine()) {
	            String info = fileInput.nextLine();
	            if (info.trim().isEmpty()) continue;
	            

	            String[] parts = info.split("\\|");
	            User user = null;
	            if (parts.length < 4) {
	                continue;
	            }

	            String role = parts[2].trim().toLowerCase();

	            switch (role) {
	                case "admin":
	                    user = new Admin(parts[0], parts[1], parts[2], parts[3]);
	                    break;
	                case "tutor":
	                    user = new Tutor(parts[0], parts[1], parts[2], parts[3]);
	                    break;
	                case "student":
	                    user = new Student(parts[0], parts[1], parts[2], parts[3]);
	                    break;
	            }

	            users.add(user);
	        }

	        fileInput.close();

	    } catch (FileNotFoundException e) {
	        System.out.println("File not found: " + filePath);
	        e.printStackTrace();
	    }

	    return users;
	}
	
	public static void writeUsers(ArrayList<User> users) {
	    try (PrintWriter writer = new PrintWriter("UserList.txt")) {
	        for (User user : users) {
	            String line = user.getName() + "|" +
	                          user.getEmail() + "|" +
	                          user.getRole() + "|" +
	                          user.getPassword();
	            writer.println(line);
	        }
	        System.out.println("Users successfully written to UserList.txt");
	    } catch (FileNotFoundException e) {
	        System.out.println("Error writing to file: " + e.getMessage());
	        e.printStackTrace();
	    }
	}

	    public static boolean updateUser(String TargetAcc, String section, String updateData) {
	        ArrayList<User> users = getUserList();
	        boolean updatedStatus = false;

	        for (User user : users) {
	            if (user.getEmail().equalsIgnoreCase(TargetAcc)) {
	                switch (section.toLowerCase()) {
	                    case "name":
	                        user.setName(updateData);
	                        updatedStatus = true;
	                        break;
	                    case "role":
	                        user.setRole(updateData);
	                        updatedStatus = true;
	                        break;
	                    case "password":
	                        user.setPassword(updateData);
	                        updatedStatus = true;
	                        break;
	                    case "email":
	                        user.setEmail(updateData);
	                        updatedStatus = true;
	                        break;
	                    default:
	                        System.out.println("Invalid section: " + section);
	                        return false;
	                }
	                break;
	            }
	        }

	        if (updatedStatus) {
	            writeUsers(users);
	            System.out.println("User updated successfully.");
	            return true;
	        } else {
	            System.out.println("User not found with email: " + TargetAcc);
	            return false;
	        }
	    }

	    public static boolean deleteUserList(String targetAcc) {
	        boolean found = false;
	        ArrayList<User> users = getUserList();
	        ArrayList<User> newUsersList = new ArrayList<>(); // initialize properly

	        for (User user : users) {
	            if (user.getEmail().equalsIgnoreCase(targetAcc)) {
	                found = true; // skip this user to delete
	            } else {
	                newUsersList.add(user);
	            }
	        }

	        if (found) {
	            writeUsers(newUsersList); // save the updated list
	            System.out.println("The account has been successfully deleted.");
	        } else {
	            System.out.println("The email was not found, account has not been deleted.");
	        }

	        return found;
	    }
      
	
	public static boolean addUserList(String newEmail, String newUsername, String newRole, String newPassword) {
	    ArrayList<User> users = getUserList();

	    for (User user : users) {
	        if (user.getEmail().equalsIgnoreCase(newEmail)) {
	            System.out.println("User with this email already exists: ");
	            System.out.println("Please try with other email address");
	            return false;
	        }
	    }
	    
	    User newUser = null;
	    
	    switch (newRole.trim().toLowerCase()) {
		    case "admin":
		        newUser = new Admin(newUsername, newEmail, newRole, newPassword);
		        break;
		    case "tutor":
		        newUser = new Tutor(newUsername, newEmail, newRole, newPassword);
		        break;
		    case "student":
		        newUser = new Student(newUsername, newEmail, newRole, newPassword);
		        break;
	    }
	    users.add(newUser);
	    writeUsers(users);
	    return true;
	}
	
	
	public static ArrayList<Session> getListedSession() {
		ArrayList<Session> sessions = new ArrayList<>();
		try{
			File file = new File("TutorListedSession.txt");
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String input = scanner.nextLine();
				if (input.trim().isEmpty()) continue;
				String[] parts = input.split("\\|");
				Session session = new Session(Integer.parseInt(parts[0]),parts[1],parts[2],parts[3],Integer.parseInt(parts[4]),Integer.parseInt(parts[5]),Integer.parseInt(parts[6]),parts[7]);
				sessions.add(session);
				
			}
			
		}catch (FileNotFoundException e) {
        System.out.println("File not found");
	}
		return sessions;

}
	public static void writeSessions(ArrayList<Session> sessions) {
		 
		
		try (PrintWriter writer = new PrintWriter("TutorListedSession.txt")) {
		    for (Session session : sessions) {
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
		    System.out.println("Error writing to file");
		}
        
    }
	public static boolean updateSession(String courseID, String updateField, String newData) {
	    ArrayList<Session> sessions = getListedSession();
	    boolean updated = false;

	    for (Session session: sessions) {
	        if (Integer.toString(session.getSessionID()).equalsIgnoreCase(courseID)) {
	            switch (updateField.toLowerCase()) {
	                case "date":
	                    session.setDate(newData);
	                    updated = true;
	                    break;
	                case "starttime":
	                    session.setStartTime(newData);
	                    updated = true;
	                    break;
	                case "duration":
	                    session.setDuration(Integer.parseInt(newData));
	                    updated = true;
	                    break;
	                case "availableperson":
	                    session.setAvailablePerson(Integer.parseInt(newData));
	                    updated = true;
	                    break;
	                case "maxperson":
	                    session.setMaxPerson(Integer.parseInt(newData));
	                    updated = true;
	                    break;
	                case "venue":
	                    session.setVenue(newData);
	                    updated = true;
	                    break;
	                case "coursename":
	                    session.setCourseName(newData);
	                    updated = true;
	                    break;
	                default:
	                    System.out.println("Invalid field");
	            }
	            break;
	        }
	    }

	    if (updated) {
	    	writeSessions(sessions);
	    } else {
	        System.out.println("Session not found for course");
	    }
	    return updated;
	}
	
	public static boolean deleteListedSession(String targetListedSession) {
	    boolean found = false;
	    ArrayList<Session> sessions = getListedSession();  
	    ArrayList<Session> newSessionsList = new ArrayList<>();

	    for (Session session : sessions) {
	        if (Integer.toString(session.getSessionID()).equalsIgnoreCase(targetListedSession)) {
	            found = true; 
	        } else {
	            newSessionsList.add(session);
	        }
	    }

	    if (found) {
	    	File bookedFile = new File(targetListedSession + ".txt");
	        if (bookedFile.exists()) {
	            if (bookedFile.delete()) {
	                System.out.println("Deleted booked student file: " + bookedFile.getName());
	            } else {
	                System.out.println("Failed to delete booked student file: " + bookedFile.getName());
	            }
	        }
	        writeSessions(newSessionsList);
	    } else {
	        System.out.println("The session ID was not found, session has not been deleted.");
	    }

	    return found;
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
	
	public static boolean addBookedList(User user, String courseID) {
	    ArrayList<Session> sessions = getListedSession();

	    for (Session s : sessions) {
	        if (Integer.toString(s.getSessionID()).equalsIgnoreCase(courseID)) {

	            // Session found
	            if (s.getAvailablePerson() < s.getMaxPerson()) {

	                File file = new File(courseID + ".txt");

	                // Check if user already booked this session
	                if (file.exists()) {
	                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
	                        String line;
	                        while ((line = reader.readLine()) != null) {
	                            if (line.trim().equalsIgnoreCase(user.getEmail())) {
	                                System.out.println("You have already booked this session.");
	                                return false;
	                            }
	                        }
	                    } catch (IOException e) {
	                        e.printStackTrace();
	                    }
	                }

	                // If not booked, create file if necessary and append email
	                try {
	                    if (!file.exists()) {
	                        file.createNewFile(); // Create file if it doesn't exist
	                    }
	                } catch (IOException e) {
	                    e.printStackTrace();
	                    return false;
	                }

	                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
	                    writer.write(user.getEmail());
	                    writer.newLine();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                    return false;
	                }

	                int newTotalBookedNum = s.getAvailablePerson() + 1;
	                return updateSession(courseID, "availableperson", String.valueOf(newTotalBookedNum));
	            } else {
	                return false;
	            }
	        }
	    }

	    System.out.println("Course ID not found: " + courseID);
	    return false;
	}

    public static boolean deleteBookedSession(User user, String courseID) {
        ArrayList<Session> sessions = getListedSession();

        for (Session s : sessions) {
            if (Integer.toString(s.getSessionID()).equalsIgnoreCase(courseID)) {

                if (s.getAvailablePerson() > 0) {
                    int newTotalBookedNum = s.getAvailablePerson() - 1;
                    updateSession(courseID, "availableperson", String.valueOf(newTotalBookedNum));
                } 
                File file = new File(courseID + ".txt");
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
                        for (String email : lines) {
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

        System.out.println("Course ID not found: " + courseID);
        return false;
    }
    
    public static List<String> getUserBookedSessions(User user) {
        List<String> bookedSessions = new ArrayList<>();

        ArrayList<Session> sessions = getListedSession();

        for (Session s : sessions) {
            File file = new File(s.getSessionID() + ".txt");
            if (file.exists()) {
                try {
                    Scanner scanner = new Scanner(file);
                    while (scanner.hasNextLine()) {
                        String emailRecorded = scanner.nextLine();
                        if (emailRecorded.trim().equalsIgnoreCase(user.getEmail())) {
                            
                            String sessionInfo = String.format(
                                "%s (%d) - Date: %s - Duration: %d minutes - Venue: %s",
                                s.getCourseName(),
                                s.getSessionID(),
                                s.getDate(),       
                                s.getDuration(),   
                                s.getVenue()       
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

	
