package dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import beans.ManifestationType;

public class ManifestationDTO {
	private String id;
	private String name;
	private ManifestationType type;
	private int numberOfSeats;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private int ticketPrice;
	private String location;
	private String imageName;
	private String image64base;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public ManifestationType getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = ManifestationType.valueOf(type);
	}
	
	public int getNumberOfSeats() {
		return numberOfSeats;
	}
	
	public void setNumberOfSeats(int numberOfSeats) {
		this.numberOfSeats = numberOfSeats;
	}
	
	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		this.startTime = LocalDateTime.parse(startTime, formatter);
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		this.endTime = LocalDateTime.parse(endTime, formatter);
	}
	
	public int getTicketPrice() {
		return ticketPrice;
	}
	
	public void setTicketPrice(int ticketPrice) {
		this.ticketPrice = ticketPrice;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getImageName() {
		return imageName;
	}
	
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	
	public String getImage64base() {
		return image64base;
	}
	
	public void setImage64base(String image64base) {
		this.image64base = image64base;
	}
	
}
