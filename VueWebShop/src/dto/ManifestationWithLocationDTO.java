package dto;

import java.time.LocalDateTime;

import beans.Location;
import beans.ManifestationType;
import beans.Status;

public class ManifestationWithLocationDTO {
	private String id;
	private String name;
	private ManifestationType type;
	private LocalDateTime date;
	private int ticketPrice;
	private Status status;
	private Location location;
	private String image;
	private boolean isDeleted;
	
	public ManifestationWithLocationDTO(String id, String name, ManifestationType type, LocalDateTime date,
			int ticketPrice, Status status, Location location, String image, boolean isDeleted) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.date = date;
		this.ticketPrice = ticketPrice;
		this.status = status;
		this.location = location;
		this.image = image;
		this.isDeleted = isDeleted;
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
	public LocalDateTime getDate() {
		return date;
	}
	public void setDate(LocalDateTime date) {
		this.date = date;
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
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
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
