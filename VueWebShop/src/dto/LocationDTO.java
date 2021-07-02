package dto;

import beans.Location;

public class LocationDTO {
	public double longitude;
	public double latitude;
	public String street;
	public String number;
	public int postNumber;
	public String city;
	public String state;
	
	public LocationDTO(double longitude, double latitude, String street, String number, int postNumber, String city,
			String state) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.street = street;
		this.number = number;
		this.postNumber = postNumber;
		this.city = city;
		this.state = state;
	}
	
	public LocationDTO(Location location) {
		this.longitude = location.getLongitude();
		this.latitude = location.getLatitude();
		this.street = location.getStreet();
		this.number = location.getNumber();
		this.postNumber = location.getPostNumber();
		this.city = location.getCity();
		this.state = location.getState();
	}
	
	public LocationDTO() {}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getPostNumber() {
		return postNumber;
	}

	public void setPostNumber(int postNumber) {
		this.postNumber = postNumber;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	
}
