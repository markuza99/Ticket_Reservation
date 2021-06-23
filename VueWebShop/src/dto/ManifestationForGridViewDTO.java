package dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import beans.Location;
import beans.Manifestation;
import beans.ManifestationType;

public class ManifestationForGridViewDTO {
	public String id;
	public String name;
	public String type;
	public double price;
	public String date;
	public String location;
	public String image;
	public boolean manifestationPassed;
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	
	public ManifestationForGridViewDTO() {}
	
	public ManifestationForGridViewDTO(Manifestation manifestation) {
		this.id = manifestation.getId();
		this.name = manifestation.getName();
		this.type = getType(manifestation);
		this.price = manifestation.getTicketPrice();
		this.date = formatter.format(manifestation.getStartTime());
		this.image = manifestation.getImage();
		this.manifestationPassed = manifestation.getStartTime().isBefore(LocalDateTime.now());
	}
	
	private String getType(Manifestation manifestation) {
		if(manifestation.getType() == ManifestationType.CONCERT) return "Koncert";
		else if(manifestation.getType() == ManifestationType.FESTIVAL) return "Festival";
		else return "Pozoriste";
	}

	public boolean getManifestationPassed() {
		return manifestationPassed;
	}

	public void setManifestationPassed(boolean manifestationPassed) {
		this.manifestationPassed = manifestationPassed;
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
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public void setLocation(Location location) {
		this.location = location.getStreet() + " " + location.getNumber() + ", " + location.getCity() + ", " + location.getState();
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
}
