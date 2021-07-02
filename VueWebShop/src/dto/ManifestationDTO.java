package dto;

import java.time.LocalDateTime;

import beans.Location;
import beans.Manifestation;
import beans.ManifestationType;

public class ManifestationDTO {
	private String id;
	private String name;
	private String type;
	private int numberOfSeats;
	private String startTime;
	private String endTime;
	private int ticketPrice;
	private String image64base;
	public LocationDTO locationDTO;
	public boolean checked;
	
	public ManifestationDTO(Manifestation manifestation, Location location) {
		this.id = manifestation.getId();
		this.name = manifestation.getName();
		this.type = manifestation.getType().toString();
		this.numberOfSeats = manifestation.getNumberOfSeats();
		this.startTime = manifestation.getStartTime().toString();
		this.endTime = manifestation.getEndTime().toString();
		this.ticketPrice = manifestation.getTicketPrice();
		this.image64base = manifestation.getImage();
		this.locationDTO = new LocationDTO(location);
		this.checked = manifestation.isChecked();
	}
	
	public ManifestationDTO() {}
	
	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public LocationDTO getLocationDTO() {
		return locationDTO;
	}

	public void setLocationDTO(LocationDTO location) {
		this.locationDTO = location;
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
	
	public String getImage64base() {
		return image64base;
	}
	
	public void setImage64base(String image64base) {
		this.image64base = image64base;
	}
	
}
