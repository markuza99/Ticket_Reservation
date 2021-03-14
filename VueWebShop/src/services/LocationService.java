package services;

import java.util.ArrayList;
import java.util.List;

import dao.interfaces.ILocationDAO;


public class LocationService {
	
	private ILocationDAO locationDAO;
	
	public LocationService(ILocationDAO locationDAO) {
		this.locationDAO = locationDAO;
	}

	public List<String> getLocations() {
//		List<String> locationsId = new ArrayList<>();
//		for(String key : locationDAO.getAll().keySet()) {
//			locationsId.add(key);
//		}
//		return locationsId;
		return null;
	}
}
