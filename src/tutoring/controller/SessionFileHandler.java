package tutoring.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import tutoring.domain.Session;
import tutoring.domain.User;

public class SessionFileHandler {

    private static final String SESSION_FILE_NAME = "sessions.txt";

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
	    	BookingFileHandler.deleteBookedFile(sessionId);
	        saveSessionsToFile(sessions);
			return true;
	    } else {
	        System.out.println("Session ID not found, session not deleted.");
			return false;
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
					return BookingFileHandler.handleBooking(user, sessionId, session);
	            } else {
					System.out.println("The session has been fully occupied.");
	                return false;
	            }
	        }
	    }

	    System.out.println("Session ID not found: " + sessionId);
	    return false;
	}

}
