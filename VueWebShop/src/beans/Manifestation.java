package beans;

import java.time.LocalDateTime;

public class Manifestation {
	private int id;
	private ManifestationType type;
	private int numberOfSeats;
	private LocalDateTime date;
	private int ticketPrice;
	private Status status;
	private Location location;
	private String image;
	
	public Manifestation(int id, ManifestationType type, int numberOfSeats, LocalDateTime date, int ticketPrice,
			Status status, Location location, String image) {
		this.id = id;
		this.type = type;
		this.numberOfSeats = numberOfSeats;
		this.date = date;
		this.ticketPrice = ticketPrice;
		this.status = status;
		this.location = location;
		this.image = image;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ManifestationType getType() {
		return type;
	}

	public void setType(ManifestationType type) {
		this.type = type;
	}

	public int getNumberOfSeats() {
		return numberOfSeats;
	}

	public void setNumberOfSeats(int numberOfSeats) {
		this.numberOfSeats = numberOfSeats;
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
	
	
}
