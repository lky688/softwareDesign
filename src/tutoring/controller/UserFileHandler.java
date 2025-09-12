package tutoring.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import tutoring.domain.Admin;
import tutoring.domain.Student;
import tutoring.domain.Tutor;
import tutoring.domain.User;

public class UserFileHandler {
	
	private static final String USER_FILE_NAME = "users.txt";

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

}
