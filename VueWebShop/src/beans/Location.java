package beans;

import dto.LocationDTO;

public class Location {
	private String id;
	private double longitude;
	private double latitude;
	private String street;
	private String number;
	private int postNumber;
	private String city;
	private String state;
	
	public Location(String id, double longitude, double latitude, String street, String number,
			int postNumber, String city, String state) {
		this.id = id;
		this.longitude = longitude;
		this.latitude = latitude;
		this.street = street;
		this.city = city;
		this.number = number;
		this.postNumber = postNumber;
		this.state = state;
	}
	
	public Location(LocationDTO locationDTO) {
		this.longitude = locationDTO.longitude;
		this.latitude = locationDTO.latitude;
		this.street = locationDTO.street;
		this.city = locationDTO.city;
		this.number = locationDTO.number;
		this.postNumber = locationDTO.postNumber;
		this.state = locationDTO.state;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	
}
