package services;

import java.util.List;

import beans.Location;
import dao.interfaces.ILocationDAO;


public class LocationService {
	
	private ILocationDAO locationDAO;
	
	public LocationService(ILocationDAO locationDAO) {
		this.locationDAO = locationDAO;
	}

	public List<Location> getLocations() {
		return locationDAO.getAll();
	}
	
	public Location getLocation(String id) {
		return locationDAO.read(id);
	}
}
