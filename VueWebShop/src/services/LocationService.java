package services;

import java.util.List;
import dao.LocationDAO;


public class LocationService {
	
	private LocationDAO locationDAO;
	
	public LocationService(LocationDAO locationDAO) {
		this.locationDAO = locationDAO;
	}

	public List<String> getLocations() {
		return locationDAO.getLocationsId();
	}
}
