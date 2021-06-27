package dto;

import beans.Manifestation;
import beans.ManifestationType;

public class ManifestationForViewDTO {
	private String id;
	private String name;
	private String type;
	private int numberOfSeats;
	private int remainingNumberOfSeats;
	private String startTime;
	private String endTime;
	private int ticketPrice;
	private String status;
	private String location;
	private String image;
	
	public ManifestationForViewDTO(String id, String name, String type, int numberOfSeats, int remainingNumberOfSeats,
			String startTime, String endTime, int ticketPrice, String status, String location, String image) {
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
	}
	
	public ManifestationForViewDTO() {}
	
	public ManifestationForViewDTO(Manifestation manifestation) {
		this.id = manifestation.getId();
		this.name = manifestation.getName();
		this.type = manifestation.getType().toString();
		this.numberOfSeats = manifestation.getNumberOfSeats();
		this.remainingNumberOfSeats = manifestation.getRemainingNumberOfSeats();
		this.startTime = manifestation.getStartTime().toString();
		this.endTime = manifestation.getEndTime().toString();
		this.ticketPrice = manifestation.getTicketPrice();
		this.status = manifestation.getStatus().toString();
		this.location = manifestation.getLocation();
		this.image = manifestation.getImage();
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getNumberOfSeats() {
		return numberOfSeats;
	}

	public void setNumberOfSeats(int numberOfSeats) {
		this.numberOfSeats = numberOfSeats;
	}

	public int getRemainingNumberOfSeats() {
		return remainingNumberOfSeats;
	}

	public void setRemainingNumberOfSeats(int remainingNumberOfSeats) {
		this.remainingNumberOfSeats = remainingNumberOfSeats;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getTicketPrice() {
		return ticketPrice;
	}

	public void setTicketPrice(int ticketPrice) {
		this.ticketPrice = ticketPrice;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
	
}
