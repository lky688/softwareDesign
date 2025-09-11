package registry.domain;

import dataHandle.fileController;
import java.util.*;

public class App {
	
	public static void main(String[] args) {
        ArrayList<User> users = fileController.getAllUsers();
        User matchedUser = login(users);

        System.out.println("\nLogin successful!");
        System.out.println("Welcome, " + matchedUser.getName() + ".\n");
        
        matchedUser.getInput();
    }

	public static User login(List<User> users) {
		Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Please enter your email: ");
            String email = scanner.nextLine().trim();

            System.out.print("Please enter your password: ");
            String password = scanner.nextLine().trim();

            for (User user : users) {
                if (user.matchesCredentials(email, password)) {
                    return user; // successful login
                }
            }

            System.out.println("Invalid email or password. Please try again.\n");
        }
    }
}
