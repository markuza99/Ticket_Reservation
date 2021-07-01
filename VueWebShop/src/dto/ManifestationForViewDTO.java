package dto;

import java.time.LocalDateTime;

import beans.Location;
import beans.Manifestation;
import beans.ManifestationType;
import beans.Status;

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
	private String locationId;
	private LocationDTO locationDTO;
	private String image;
	private String seller;
	private boolean manifestationPassed;
	
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
	
	public ManifestationForViewDTO(Manifestation manifestation, String seller, Location location) {
		this.id = manifestation.getId();
		this.name = manifestation.getName();
		this.type = getType(manifestation);
		this.numberOfSeats = manifestation.getNumberOfSeats();
		this.remainingNumberOfSeats = manifestation.getRemainingNumberOfSeats();
		this.startTime = manifestation.getStartTime().toString();
		this.endTime = manifestation.getEndTime().toString();
		this.ticketPrice = manifestation.getTicketPrice();
		this.status = getStatus(manifestation);
		this.image = manifestation.getImage();
		this.seller = seller;
		this.manifestationPassed = manifestation.getStartTime().isBefore(LocalDateTime.now());
		setLocationString(location);
		this.locationId = location.getId();
		this.locationDTO = new LocationDTO(location);
	}
	
	public LocationDTO getLocationDTO() {
		return locationDTO;
	}

	public void setLocationDTO(LocationDTO locationDTO) {
		this.locationDTO = locationDTO;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	private void setLocationString(Location location) {
		this.location = location.getStreet() + " " + location.getNumber() + ", " + location.getCity() + ", " + location.getState();
	}
	
	private String getStatus(Manifestation manifestation) {
		if(manifestation.getStatus() == Status.ACTIVE) return "Aktivna";
		else return "Neaktivna";
	}

	private String getType(Manifestation manifestation) {
		if(manifestation.getType() == ManifestationType.CONCERT) return "Koncert";
		else if(manifestation.getType() == ManifestationType.FESTIVAL) return "Festival";
		else return "Pozoriste";
	}

	public String getSeller() {
		return seller;
	}

	public boolean isManifestationPassed() {
		return manifestationPassed;
	}

	public void setManifestationPassed(boolean manifestationPassed) {
		this.manifestationPassed = manifestationPassed;
	}

	public void setSeller(String seller) {
		this.seller = seller;
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
