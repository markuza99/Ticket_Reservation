package dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import beans.Location;
import beans.ManifestationType;
import beans.Status;

public class ManifestationWithLocationDTO {
	private String id;
	private String name;
	private ManifestationType type;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private int ticketPrice;
	private Status status;
	private LocationDTO location;
	private String image;
	private boolean isDeleted;
	
	public ManifestationWithLocationDTO(String id, String name, ManifestationType type, LocalDateTime startTime,
			LocalDateTime endTime, int ticketPrice, Status status, LocationDTO location, String image,
			boolean isDeleted) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.startTime = startTime;
		this.endTime = endTime;
		this.ticketPrice = ticketPrice;
		this.status = status;
		this.location = location;
		this.image = image;
		this.isDeleted = isDeleted;
	}
	
	
	public LocationDTO getLocation() {
		return location;
	}

	public void setLocation(LocationDTO location) {
		this.location = location;
	}

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
	public void setType(ManifestationType type) {
		this.type = type;
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
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public boolean isDeleted() {
		return isDeleted;
	}
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	
}
