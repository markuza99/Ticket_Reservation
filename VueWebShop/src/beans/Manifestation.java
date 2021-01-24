package beans;

import java.time.LocalDateTime;

public class Manifestation implements Comparable<Manifestation>{
	private String id;
	private String name;
	private ManifestationType type;
	private int numberOfSeats;
	private int remainingNumberOfSeats;
	private LocalDateTime date;
	private int ticketPrice;
	private Status status;
	private Location location;
	private String image;
	
	public Manifestation(String id, String name, ManifestationType type, int numberOfSeats, int remainingNumberOfSeats, LocalDateTime date, int ticketPrice,
			Status status, Location location, String image) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.numberOfSeats = numberOfSeats;
		this.remainingNumberOfSeats = remainingNumberOfSeats;
		this.date = date;
		this.ticketPrice = ticketPrice;
		this.status = status;
		this.location = location;
		this.image = image;
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

	@Override
	public String toString() {
		return "Manifestation [id=" + id + ", name=" + name + ", type=" + type + ", numberOfSeats=" + numberOfSeats
				+ ", date=" + date + ", ticketPrice=" + ticketPrice + ", status=" + status + ", location=" + location
				+ ", image=" + image + "]";
	}

	@Override
	public int compareTo(Manifestation o) {
		// TODO Auto-generated method stub
		return getDate().compareTo(o.getDate());
	}

	public int getRemainingNumberOfSeats() {
		return remainingNumberOfSeats;
	}

	public void setRemainingNumberOfSeats(int remainingNumberOfSeats) {
		this.remainingNumberOfSeats = remainingNumberOfSeats;
	}
	
	
}
