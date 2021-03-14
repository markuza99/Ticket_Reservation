package beans;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Manifestation implements Comparable<Manifestation>{
	private String id;
	private String name;
	private ManifestationType type;
	private int numberOfSeats;
	private int remainingNumberOfSeats;
	private LocalDateTime date;
	private int ticketPrice;
	private Status status;
	private String location;
	private String image;
	private boolean isDeleted;
	
	public Manifestation(String id, String name, ManifestationType type, int numberOfSeats, int remainingNumberOfSeats, LocalDateTime date, int ticketPrice,
			Status status, String location, String image, boolean isDeleted) {
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
		this.isDeleted = isDeleted;
	}
	
	public Manifestation() {}

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

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		this.date = LocalDateTime.parse(date, formatter);
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

	public void setStatus(String status) {
		this.status = Status.valueOf(status);
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted.equals("1") ? true : false;
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
