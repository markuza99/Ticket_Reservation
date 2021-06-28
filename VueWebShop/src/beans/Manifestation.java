package beans;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Manifestation {
	private String id;
	private String name;
	private ManifestationType type;
	private int numberOfSeats;
	private int remainingNumberOfSeats;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private int ticketPrice;
	private Status status;
	private String location;
	private String image;
	private boolean isDeleted;
	private boolean checked;
	
	public Manifestation(String id, String name, ManifestationType type, int numberOfSeats, int remainingNumberOfSeats, LocalDateTime startTime,
			LocalDateTime endTime, int ticketPrice, Status status, String location, String image, boolean checked, boolean isDeleted) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.numberOfSeats = numberOfSeats;
		this.remainingNumberOfSeats = remainingNumberOfSeats;
		this.startTime = startTime;
		this.endTime = endTime;
		this.ticketPrice = ticketPrice;
		this.status = status;
		this.location = location;
		this.image = image;
		this.checked = checked;
		this.isDeleted = isDeleted;
	}
	
	public Manifestation() {}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
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
		this.startTime = LocalDateTime.parse(startTime);
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = LocalDateTime.parse(endTime);
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
	

//	@Override
//	public int compareTo(Manifestation o) {
//		// TODO Auto-generated method stub
//		return getDate().compareTo(o.getDate());
//	}

	public int getRemainingNumberOfSeats() {
		return remainingNumberOfSeats;
	}

	public void setRemainingNumberOfSeats(int remainingNumberOfSeats) {
		this.remainingNumberOfSeats = remainingNumberOfSeats;
	}
	
	
}
