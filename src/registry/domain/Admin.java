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
	    String choice;
	    Scanner scanner = new Scanner(System.in);

	    do {
	        printMenu();
	        choice = scanner.nextLine().trim(); 

	        if (choice.equalsIgnoreCase("logout")) {
	            System.out.println("You have successfully logged out.");
	            break; 
	        }

			switch (choice) {
				case "1":
					handleAddUser(scanner); break;
				case "2":
					handleDeleteUser(scanner); break;
				case "3":
					handleEditUser(scanner); break;
				default:
					System.out.println("Unknown input, please try again.");
			}

	    } while (!choice.equalsIgnoreCase("logout")); // repeat menu until logout
	}
	
	public void handleAddUser(Scanner scanner) {
		String email, name, role, password;

		// Get valid email
		do {
			System.out.print("Enter email: ");
			email = scanner.nextLine();
		} while ((!checkValidEmail(email) || isEmailExist(email,"add")));

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

	private void handleDeleteUser(Scanner scanner) {
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

	private void handleEditUser(Scanner scanner) {
        ArrayList<User> users = fileController.getUserList();
        if (users.isEmpty()) {
            System.out.println("No users available.");
            return;
        }

        printUserList(users);

        String email;
        while (true) {
            System.out.print("Select an email to update (or type 'back' to cancel): ");
            email = scanner.nextLine().trim();

            if (email.equalsIgnoreCase("back")) {
                System.out.println("Update cancelled.");
                return;
            }

            if (!isEmailExist(email, "delete")) {
                System.out.println("Email not found. Please try again.");
                continue;
            }

            printSingleUser(email, users);
            updateUserDetails(email, scanner);
            break;
        }
    }

	private void updateUserDetails(String email, Scanner scanner) {
        while (true) {
            System.out.println("-----------------------------------------");
            System.out.println("Select a field to update:");
            System.out.println("1. Username");
            System.out.println("2. Role");
            System.out.println("3. Email");
            System.out.println("4. Password");
            System.out.print("Your choice: ");

            int selection;
            if (scanner.hasNextInt()) {
                selection = scanner.nextInt();
                scanner.nextLine(); // consume newline
            } else {
                System.out.println("Invalid input! Enter a number between 1 and 4.");
                scanner.nextLine(); // consume invalid
                continue;
            }

            switch (selection) {
                case 1:
					updateUserName(email, scanner); break;
                case 2:
					updateUserRole(email, scanner); break;
                case 3:
					updateUserEmail(email, scanner); break;
                case 4:
					updateUserPassword(email, scanner); break;
                default:
                    System.out.println("Invalid choice! Enter a number between 1 and 4."); continue;
            }
            break;
        }
    }

    private void updateUserName(String email, Scanner scanner) {
        String name;
        do {
            System.out.print("Enter new name: ");
            name = scanner.nextLine();
            if (!checkValidName(name)) System.out.println("Invalid name. Try again!");
        } while (!checkValidName(name));

        fileController.updateUser(email, "name", name);
        System.out.println("Username updated successfully to: " + name);
    }

    private void updateUserRole(String email, Scanner scanner) {
        String role;
        do {
            System.out.print("Enter role (tutor/admin/student): ");
            role = scanner.nextLine().toLowerCase();
            if (!checkValidRole(role)) System.out.println("Invalid role. Try again!");
        } while (!checkValidRole(role));

        fileController.updateUser(email, "role", role);
        System.out.println("Role updated successfully to: " + role);
    }

    private void updateUserEmail(String email, Scanner scanner) {
        String newEmail;
        do {
            System.out.print("Enter new email: ");
            newEmail = scanner.nextLine();
            if (!checkValidEmail(newEmail) || isEmailExist(newEmail, "add")) {
                System.out.println("Invalid or existing email. Try again!");
            }
        } while (!checkValidEmail(newEmail) || isEmailExist(newEmail, "add"));

        fileController.updateUser(email, "email", newEmail);
        System.out.println("Email updated successfully to: " + newEmail);
    }

    private void updateUserPassword(String email, Scanner scanner) {
        String password;
        do {
            System.out.print("Enter new password: ");
            password = scanner.nextLine();
            if (!checkValidPassword(password)) System.out.println("Invalid password. Try again!");
        } while (!checkValidPassword(password));

        fileController.updateUser(email, "password", password);
        System.out.println("Password updated successfully.");
	}

	private void printUserList(ArrayList<User> users) {
        System.out.println("List of users:");
        System.out.println("-------------------------------------------------------------------");
        System.out.printf("%-20s %-15s %-25s\n", "Username", "Role", "Email");
        System.out.println("-------------------------------------------------------------------");
        for (User user : users) {
            System.out.printf("%-20s %-15s %-25s\n",
                    user.getName(), user.getRole(), user.getEmail());
        }
        System.out.println("-------------------------------------------------------------------");
    }

	private void printSingleUser(String email, ArrayList<User> users) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                System.out.printf("%-20s %-15s %-25s %-15s\n", "Username", "Role", "Email", "Password");
                System.out.println("-------------------------------------------------------------------");
                System.out.printf("%-20s %-15s %-25s %-15s\n",
                        user.getName(), user.getRole(), user.getEmail(), user.getPassword());
            }
        }
    }
	
	// Email: basic format check
	public static boolean checkValidEmail(String email) {
        if(email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")){
        	return true;
		} else{
        	System.out.println("Invalid email format");
        	return false;
        }
    }
	
	public static boolean isEmailExist(String email, String type) {
	    ArrayList<User> users = fileController.getUserList();  
	    for (User user: users) {
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