package registry.domain;
import java.util.*;
import dataHandle.fileController;

public class App {
	/*public static void main(String[]args) {
		String choice="";
		Scanner scanner = new Scanner(System.in);
		do {
			System.out.println("Please select your identity\n");
			System.out.println("Enter 1 for Student");
			System.out.println("Enter 2 for Tutor");
			System.out.println("Enter 3 for Admin\n");
			System.out.print("Your choice:");
			choice = scanner.next();
			if(choice.equals("1")) {
				Student stud = new Student("ABC","abc@gmail.com","abc","student");
				stud.getInput();
			}
			else if(choice.equals("2")) {
				Tutor tutor = new Tutor();
				tutor.getInput();
			}
			else if(choice.equals("3")) {
				Admin admin = new Admin();
				System.out.println("-----------------Admin Page---------------------");
				admin.getInput();
			}
			else{
				System.out.println("Unknown input,please try again!");
			}
		}while(!Arrays.asList("1","2","3").contains(choice));
	}
	*/
	
	public static void main(String[]args) {
		Scanner scanner = new Scanner(System.in);
		ArrayList<User> users = fileController.getUserList();
		String email,password;
		User matchedUser = null;

        while (true) {
            System.out.println("Please enter your email:");
            email = scanner.nextLine().trim();

            System.out.println("Please enter your password:");
            password = scanner.nextLine().trim();

            // Check credentials
            for (User user : users) {

                if (user.getEmail().equalsIgnoreCase(email) && user.getPassword().equals(password)) {
                    matchedUser = user;
                    break;
                }
            }

            if (matchedUser != null) {
                System.out.println("Login successful!");
                System.out.println("Welcome, " + matchedUser.getName());
                break;  // Exit the loop after successful login
            } else {
                System.out.println("Invalid email or password. Please try again.\n");
            }
        }

        	matchedUser.getInput();
        

		
	}
	
}
