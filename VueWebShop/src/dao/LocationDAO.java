package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import beans.Location;

public class LocationDAO {
	private Map<String, Location> locations = new HashMap<>();
	private String contextPath;

	public LocationDAO(String contextPath) {
		this.contextPath = contextPath;
		loadLocations();
	}
	
	public Location getLocationForManifestation(String id) {
		return locations.get(id);
	}
	
	private Location loadLocations() {
		BufferedReader reader = null;
		try {
			File file = new File(contextPath + "/repositories/locations.txt");
			reader = new BufferedReader(new FileReader(file));
			String line;
			StringTokenizer st;
			while((line = reader.readLine()) != null) {
				line = line.trim();
				if(line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while(st.hasMoreTokens()) {
					String locationId = st.nextToken().trim();
					
					Double longitude = Double.parseDouble(st.nextToken().trim());
					Double latitude = Double.parseDouble(st.nextToken().trim());
					String street = st.nextToken().trim();
					String city = st.nextToken().trim();
					
					locations.put(locationId, new Location(locationId, longitude, latitude, street, city));
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			if(reader != null) {
				try {
					reader.close();
				}
				catch (Exception e) {}
			}
		}
		return null;
	}
}
