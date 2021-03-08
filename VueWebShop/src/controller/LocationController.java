package controller;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import services.LocationService;

@Path("/locations")
public class LocationController {
	private LocationService locationService = new LocationService();
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> getLocations() {
		return locationService.getLocations();
	}
}
