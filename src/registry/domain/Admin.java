package registry.domain;

import dataHandle.fileController;
import java.util.ArrayList;
import java.util.Scanner;

public class Admin extends User {

	public Admin(String name, String email, String role, String password) {
		super(name, email, role, password);
	}
	
	public Admin() {
		super();
	};

	public static void printMenu() {
		System.out.println("Enter 1 for Adding New User(Student/Tutor/Admin)");
		System.out.println("Enter 2 for Delete Existing User(Student/Tutor/Admin)");
		System.out.println("Enter 3 for Edit Existing User(Student/Tutor/Admin)\n");
		System.out.println("Type 'logout' to exit");
		System.out.print("Your choice:");
	}
	
	@Override
	public void getInput() {
	    String email, name, role, password;
	    String choice;
	    Scanner scanner = new Scanner(System.in);

	    do {
	        printMenu();
	        choice = scanner.nextLine().trim(); 

	        if (choice.equalsIgnoreCase("logout")) {
	            System.out.println("You have successfully logged out.");
	            break; 
	        }

	        if (choice.equals("1")) {
				addUser(scanner);
	        }
	        else if (choice.equals("2")) {
	            deleteUser(scanner);
	        }
	        else if (choice.equals("3")) {
	            
	            ArrayList<User> users = fileController.getUserList();

	            if (users.isEmpty()) {
	                System.out.println("No users available.");
	            }

	            System.out.println("List of users:");
	            System.out.println("-----------------------------------------");
	            System.out.printf("%-20s %-15s %-25s\n", "Username", "Role", "Email");
	            System.out.println("-----------------------------------------");

	            for (User user : users) {
	                System.out.printf("%-20s %-15s %-25s\n",
	                        user.getName(),
	                        user.getRole(),
	                        user.getEmail());
	            }

	            System.out.println("-----------------------------------------");

	            do {    
	                System.out.print("Please select an email to update the profile (or type 'back' to cancel): ");
	                email = scanner.nextLine().trim();

	                if (email.equalsIgnoreCase("back")) {
	                    System.out.println("Update cancelled.");
	                    break;
	                }

	                if (!isEmailExist(email,"delete")) {
	                    System.out.println("Email not found. Please try again.");
	                    continue;
	                }

	                System.out.println("-----------------------------------------");
	                for (User user : users) {
	                    if(user.getEmail().equals(email)) {
	                        System.out.printf("%-20s %-15s %-25s %-15s\n", "Username", "Role", "Email","Password");
	                        System.out.println("-----------------------------------------");
	                        System.out.printf("%-20s %-15s %-25s %-15s \n",
	                                user.getName(),
	                                user.getRole(),
	                                user.getEmail(),
	                                user.getPassword());
	                    }
	                }

	                int selection;
	                while (true) {
	                    System.out.println("-----------------------------------------");
	                    System.out.println("Please select a subject to update");
	                    System.out.println("Enter 1 for editing Username");
	                    System.out.println("Enter 2 for editing Role");
	                    System.out.println("Enter 3 for editing Email");
	                    System.out.println("Enter 4 for editing Password");
	                    System.out.print("Your choice: ");

	                    if (scanner.hasNextInt()) {
	                        selection = scanner.nextInt();
	                        scanner.nextLine(); // consume the newline
	                    } else {
	                        System.out.println("Invalid input! Please enter a number between 1 and 4.");
	                        scanner.nextLine(); // consume invalid input
	                        continue;
	                    }

	                    switch (selection) {
	                        case 1: // Edit Username
	                            do {
	                                System.out.print("Enter name: ");
	                                name = scanner.nextLine();
	                                if (!checkValidName(name)) {
	                                    System.out.println("Invalid name format. Please try again!");
	                                }
	                            } while (!checkValidName(name));
	                            fileController.updateUser(email, "name", name);
	                            System.out.println("Username updated successfully to: " + name);
	                            break;

	                        case 2: // Edit Role
	                            do {
	                                System.out.print("Enter role (tutor/admin/student): ");
	                                role = scanner.nextLine().toLowerCase();
	                                if (!checkValidRole(role)) {
	                                    System.out.println("Invalid role. Please try again!");
	                                }
	                            } while (!checkValidRole(role));
	                            fileController.updateUser(email, "role", role);
	                            System.out.println("Role updated successfully to: " + role);
	                            break;

	                        case 3: // Edit Email
	                            String newEmail;
	                            do {
	                                System.out.print("Enter email: ");
	                                newEmail = scanner.nextLine();
	                                if (!checkValidEmail(newEmail, "add") || isEmailExist(newEmail, "add")) {
	                                    System.out.println("Invalid or existing email. Please try again!");
	                                }
	                            } while (!checkValidEmail(newEmail, "add") || isEmailExist(newEmail, "add"));
	                            fileController.updateUser(email, "email", newEmail);
	                            System.out.println("Email updated successfully to: " + newEmail);
	                            break;

	                        case 4: // Edit Password
	                            do {
	                                System.out.print("Enter password: ");
	                                password = scanner.nextLine();
	                                if (!checkValidPassword(password)) {
	                                    System.out.println("Invalid password format. Please try again!");
	                                }
	                            } while (!checkValidPassword(password));
	                            fileController.updateUser(email, "password", password);
	                            System.out.println("Password updated successfully.");
	                            break;

	                        default:
	                            System.out.println("Invalid choice! Please enter a number between 1 and 4.");
	                            continue; 
	                    }
	                    break; 
	                }
	            } while (true);
	        }
	        else {
	            System.out.println("Unknown input, please try again!");
	        }

	    } while (!choice.equalsIgnoreCase("logout")); // repeat menu until logout
	}
	
	public void addUser(Scanner scanner) {
		String email, name, role, password;

		// Get valid email
		do {
			System.out.print("Enter email: ");
			email = scanner.nextLine();
		} while ((!checkValidEmail(email,"add") || isEmailExist(email,"add")));

		// Get valid name
		do {
			System.out.print("Enter name: ");
			name = scanner.nextLine();
			if(!checkValidName(name)) {
				System.out.println("Invalid name format (can only contain alphabet). Please try again!");
			}
		} while (!checkValidName(name));

		// Get valid role
		do {
			System.out.print("Enter role (tutor/admin/student): ");
			role = scanner.nextLine().toLowerCase();
			if(!checkValidRole(role)) {
				System.out.println("Invalid role. Please try again!");
			}
		} while (!checkValidRole(role));

		// Get valid password
		do {
			System.out.print("Enter password: ");
			password = scanner.nextLine();
			if(!checkValidPassword(password)) {
				System.out.println("Password must be at least 8 characters long and contain at least one uppercase letter.");
				System.out.println("Please try again.");
			}
		} while (!checkValidPassword(password));

		fileController.addUserList(email, name, role, password);
		System.out.println("User added successfully.");
	}

	private void deleteUser(Scanner scanner) {
        ArrayList<User> users = fileController.getUserList();
        if (users.isEmpty()) {
            System.out.println("No users available.");
            return;
        }

        printUserList(users);

        String email;
        while (true) {
            System.out.print("Enter email to delete (or type 'back' to cancel): ");
            email = scanner.nextLine().trim();

            if (email.equalsIgnoreCase("back")) {
                System.out.println("Deletion cancelled.");
                return;
            }

            if (!isEmailExist(email, "delete")) {
                System.out.println("Email not found. Please try again.");
                continue;
            }

            break;
        }

        fileController.deleteUserList(email);
        System.out.println("Successfully deleted.");
    }

	private void printUserList(ArrayList<User> users) {
        System.out.println("List of users:");
        System.out.println("-----------------------------------------");
        System.out.printf("%-20s %-15s %-25s\n", "Username", "Role", "Email");
        System.out.println("-----------------------------------------");
        for (User user : users) {
            System.out.printf("%-20s %-15s %-25s\n",
                    user.getName(), user.getRole(), user.getEmail());
        }
        System.out.println("-----------------------------------------");
    }
	
	// Email: basic format check
	public static boolean checkValidEmail(String email,String type) {
		if(type.equals("add")) {
    
        if(email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")){
        	return true;
        }
        else{
        	System.out.println("Invalid email format");
        	return false;
        }
        
        }
		return false;
    }
	
	public static boolean isEmailExist(String email, String type) {
	    ArrayList<User> users = fileController.getUserList();  
	    for (User user : users) {
	        if (user.getEmail().equalsIgnoreCase(email)) {
	        	if(type.equals("add")) {
	        		System.out.println("Email already exist");
	        	}
	            return true;
	        }
	    }
	    return false;
	}

    // Name: no digits allowed
    public static boolean checkValidName(String name) {
        return !name.matches(".*\\d.*") && !name.trim().isEmpty();
    }

    // Role: must be one of the allowed
    public static boolean checkValidRole(String role) {
        return role.equals("tutor") || role.equals("admin") || role.equals("student");
    }

    // Password: at least 8 chars and 1 uppercase
    public static boolean checkValidPassword(String password) {
        return password.length() >= 8 && password.matches(".*[A-Z].*");
    }
}
