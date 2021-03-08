package services;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;

import dao.LocationDAO;


public class LocationService {
	@Context
	ServletContext ctx;
	
	@PostConstruct
	public void init() {
		String contextPath = ctx.getRealPath("");
		if(ctx.getAttribute("LocationDAO") == null) {
			ctx.setAttribute("LocationDAO", new LocationDAO(contextPath));
		}
	}
	
	public List<String> getLocations() {
		LocationDAO locationDAO = (LocationDAO) ctx.getAttribute("LocationDAO");
		return locationDAO.getLocationsId();
	}
}
