package registry.domain;

import dataHandle.fileController;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Report {

    private ArrayList<Session> sessions;
    private LocalDateTime generatedAt;

    public Report() {
        this.sessions = fileController.getAllSessions();
        this.generatedAt = LocalDateTime.now();
    }

    public void generateSessionsReport() {
        System.out.println("=======================================================");
        System.out.println("SESSIONS BOOKING SUMMARY REPORT");
        System.out.println("=======================================================");
        System.out.println("Report generated at: " + formatDateTime(generatedAt));
        System.out.println("-------------------------------------------------------");

        if(sessions.isEmpty()) {
            System.out.println("No sessions are created.");
            System.out.println("-------------------------------------------------------");
        }
        for (Session session : sessions) {
            System.out.printf("Session ID: %d%n", session.getSessionID());
            System.out.printf("Course Name: %s%n", session.getCourseName());
            System.out.printf("Date: %s%n", session.getDate());
            System.out.printf("Start Time: %s%n", session.getStartTime());
            System.out.printf("Duration: %d minutes%n", session.getDuration());
            System.out.printf("Venue: %s%n", session.getVenue());
            System.out.printf("Number of Booked Students: %d%n%n", session.getOccupiedCapacity());
            
            ArrayList<User> students = fileController.getStudentsBySessionId(Integer.toString(session.getSessionID()));

            System.out.println("List of Booked Students: ");
            if (students.isEmpty()) {
                System.out.println("None");
            } else {
                System.out.printf("%-25s%s%n", "NAME", "EMAIL");
                for (User student: students){
                    System.out.printf("%-25s%s%n", student.getName(), student.getEmail());
                }
            }
            System.out.println("\n-------------------------------------------------------");
        }
        System.out.println();
    }

    private static String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm");
        return dateTime.format(dateTimeFormat);
    }

}
