package tutoring.domain;

public abstract class User {
	
	private String name;
	private String email;
	private String password;
	private String role;

	public User(String name,String email,String role,String password) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
	}
	
	//Empty default constructor
	public User() {}
	
	// Abstract method to be implemented by subclasses
	public abstract void getInput();

	// Getters
	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getRole() {
		return role;
	}
	
	// Setters
	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRole(String role) {
		this.role = role;
	}

	// Determine whether email and password match existing user
	public boolean matchesCredentials(String email, String password) {
		return this.email.equalsIgnoreCase(email) && this.password.equals(password);
	}
}
