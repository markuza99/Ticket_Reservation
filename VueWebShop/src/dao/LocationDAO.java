package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import beans.Location;
import dao.interfaces.ILocationDAO;

public class LocationDAO implements ILocationDAO {
	private Map<String, Location> locations = new HashMap<>();
	private String contextPath;

	public LocationDAO(String contextPath) {
		this.contextPath = contextPath;
		loadLocations();
	}

	@Override
	public Location create(Location location) {
		locations.put(location.getId(), location);
		appendToFile(ticketCSVRepresentation(location));
		return location;
	}

	private void appendToFile(String ticketCSVRepresentation) {
		File file = new File(contextPath + "/repositories/locations.txt");

        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            	pw.println(ticketCSVRepresentation);
            
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(pw != null) {
                try {
                    pw.close();
                }
                catch (Exception e) {}
            }
        }
	}

	private String ticketCSVRepresentation(Location location) {
		StringBuilder locationString = new StringBuilder();
		locationString.append(location.getId() + ";" + location.getLongitude() + ";"
				+ location.getLatitude() + ";" + location.getStreet() + ";"
				+ location.getNumber() + ";" + location.getPostNumber() + ";"
				+ location.getCity() + ";" + location.getState());
        return locationString.toString();
	}

	@Override
	public Location read(String id) {
		return locations.get(id);
	}

	@Override
	public Location update(Location entity) {
		return null;
	}

	@Override
	public Location delete(String id) {
		return null;
	}

	@Override
	public List<Location> getAll() {
		return new ArrayList<Location>(locations.values());
	}

	@Override
	public Location retrieve(String id) {
		return null;
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
					String number = st.nextToken().trim();
					int postNumber = Integer.parseInt(st.nextToken().trim());
					String city = st.nextToken().trim();
					String state = st.nextToken().trim();
					
					locations.put(locationId, new Location(locationId, longitude, latitude, street, number,
							postNumber, city, state));
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
