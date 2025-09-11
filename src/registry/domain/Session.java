package registry.domain;

public class Session {
	private int sessionID;
	private String courseName;
	private String date;
	private String startTime;
	private int duration;
	private int occupiedCapacity;
	private int maxPerson;
	private String venue;
	
	public Session(int sessionID,String courseName,String date,String startTime,int duration,int occupiedCapacity,int maxPerson,String venue) {
		this.sessionID = sessionID;
		this.courseName = courseName;
		this.date = date;
		this.startTime = startTime;
		this.duration = duration;
		this.occupiedCapacity = occupiedCapacity;
		this.maxPerson = maxPerson;
		this.venue = venue;
	}
	
	public Session(String sessionID,String courseName,String date,String startTime,String duration,String occupiedCapacity,String maxPerson,String venue) {
		this.sessionID = Integer.parseInt(sessionID);
		this.courseName = courseName;
		this.date = date;
		this.startTime = startTime;
		this.duration = Integer.parseInt(duration);
		this.occupiedCapacity = Integer.parseInt(occupiedCapacity);
		this.maxPerson = Integer.parseInt(maxPerson);
		this.venue = venue;
	}
	
	// Getters
	public int getSessionID() {
		return sessionID;
	}
	public String getCourseName() {
		return courseName;
	}
	public String getDate() {
		return date;
	}
	public String getStartTime() {
		return startTime;
	}
	public int getDuration() {
		return duration;
	}
	public int getOccupiedCapacity() {
		return occupiedCapacity;
	}
	public int getMaxPerson() {
		return maxPerson;
	}
	public String getVenue() {
		return venue;
	}
	
	// Setters
	public void setSessionID(int sessionID) {
		this.sessionID = sessionID;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public void setOccupiedCapacity(int occupiedCapacity) {
		this.occupiedCapacity = occupiedCapacity;
	}
	public void setMaxPerson(int maxPerson) {
		this.maxPerson = maxPerson;
	}
	public void setVenue(String venue) {
		this.venue = venue;
	}

}
