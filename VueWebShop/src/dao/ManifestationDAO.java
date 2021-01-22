package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.ws.rs.QueryParam;

import beans.Location;
import beans.Manifestation;
import beans.ManifestationType;
import beans.Status;

public class ManifestationDAO {
	private Map<String, Manifestation> manifestations = new HashMap<>();
	private String contextPath;
	
	public ManifestationDAO(String contextPath) {
		this.contextPath = contextPath;
		loadManifestations();
	}
	
	public List<Manifestation> getAllManifestations() {
		return new ArrayList<Manifestation>(manifestations.values());
	}
	
	public List<Manifestation> getFirstNManifestations(int num) {
		ArrayList<Manifestation> sortedManifestations = (ArrayList<Manifestation>) sortManifestations(true);
		ArrayList<Manifestation> firstNManifestations = new ArrayList<Manifestation>();
		for(Manifestation m : sortedManifestations) {
			if(firstNManifestations.size() == num) {
				break;
			}
			firstNManifestations.add(m);
		}
		return firstNManifestations;
	}
	
	private List<Manifestation> sortManifestations(Boolean ascending) {
		List<Manifestation> sortedManifestations = new ArrayList<>(manifestations.values());
		if(ascending) {
			Collections.sort(sortedManifestations);
		} else {
			Collections.sort(sortedManifestations, Collections.reverseOrder());
		}
		return sortedManifestations;
	}
	
	public List<Manifestation> search(String name,  String dateFrom, String dateTo, String place, int priceFrom, int priceTo, String selected) {
		List<Manifestation> searchedManifestations = new ArrayList<Manifestation>();
		name = name.trim();
		place = place.trim();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime LdateFrom = null;
		LocalDateTime LdateTo = null;
		
		if(!dateFrom.equals("")) {
			dateFrom = dateFrom.trim() + " 00:00:00";
			LdateFrom = LocalDateTime.parse(dateFrom, formatter);;
		}
		if(!dateTo.equals("")) {
			dateTo = dateTo.trim() + " 00:00:00";
			LdateTo = LocalDateTime.parse(dateTo, formatter);
		}
		
		//ako datum je prazan string, prosledi se null
		//ako je mesto prazan string, to je svakako true jer contains radi za prazan string
		//isto i za naziv
		for (Manifestation m : manifestations.values()) {
			if(correspondsSearch(m, name.toLowerCase(), LdateFrom, LdateTo, place.toLowerCase(), priceFrom, priceTo)) {
				searchedManifestations.add(m);
			}
		}
		
		if(selected.equals("Sortiraj po ceni manifestacije rastuce")) {
			Collections.sort(searchedManifestations, new Comparator<Manifestation>() {

				@Override
				public int compare(Manifestation o1, Manifestation o2) {
					// TODO Auto-generated method stub
					return o1.getTicketPrice() - o2.getTicketPrice();
				}
			});
		}
		else if(selected.equals("Sortiraj po ceni manifestacije opadajuce")) {
			Collections.sort(searchedManifestations, new Comparator<Manifestation>() {

				@Override
				public int compare(Manifestation o1, Manifestation o2) {
					// TODO sAuto-generated mssethod stub
					return o2.getTicketPrice() - o1.getTicketPrice();
				}
			});
		}
		else if(selected.equals("Sortiraj po nazivu manifestacije rastuce")) {
			Collections.sort(searchedManifestations, new Comparator<Manifestation>() {

				@Override
				public int compare(Manifestation o1, Manifestation o2) {
					// TODO Auto-generated method stub
					return o1.getName().compareTo(o2.getName());
				}
			});
		}
		else if(selected.equals("Sortiraj po nazivu manifestacije opadajuce")) {
			Collections.sort(searchedManifestations, new Comparator<Manifestation>() {

				@Override
				public int compare(Manifestation o1, Manifestation o2) {
					// TODO Auto-generated method stub
					return o2.getName().compareTo(o1.getName());
				}
			});
		}
		else if(selected.equals("Sortiraj po lokaciji manifestacije rastuce")) {
			Collections.sort(searchedManifestations, new Comparator<Manifestation>() {

				@Override
				public int compare(Manifestation o1, Manifestation o2) {
					// TODO Auto-generated method stub
					return o1.getLocation().getCity().compareTo(o2.getLocation().getCity());
				}
			});
		}
		else if(selected.equals("Sortiraj po lokaciji manifestacije opadajuce")) {
			Collections.sort(searchedManifestations, new Comparator<Manifestation>() {

				@Override
				public int compare(Manifestation o1, Manifestation o2) {
					// TODO Auto-generated method stub
					return o2.getLocation().getCity().compareTo(o1.getLocation().getCity());
				}
			});
		}
		else if(selected.equals("Sortiraj po datumu manifestacije rastuce")) {
			Collections.sort(searchedManifestations, new Comparator<Manifestation>() {

				@Override
				public int compare(Manifestation o1, Manifestation o2) {
					// TODO Auto-generated method stub
					return o1.getDate().compareTo(o2.getDate());
				}
			});
		}
		else if(selected.equals("Sortiraj po datumu manifestacije opadajuce")) {
			Collections.sort(searchedManifestations, new Comparator<Manifestation>() {

				@Override
				public int compare(Manifestation o1, Manifestation o2) {
					// TODO Auto-generated method stub
					return o2.getDate().compareTo(o1.getDate());
				}
			});
		}
		return searchedManifestations;		

	}
	
	private boolean correspondsSearch(Manifestation m,String name,  LocalDateTime dateFrom, LocalDateTime dateTo, String place, int priceFrom, int priceTo) {
		boolean bname = m.getName().toLowerCase().contains(name); //true
		//svaki string sadrzi prazan string
//		boolean btip;
//		if(type.equals("CONCERT") || type.equals("FESTIVAL") || type.equals("THEATER")) {
//			btip = m.getType().equals(ManifestationType.valueOf(type));
//		} else {
//			btip = true;
//		}
		boolean bplace = m.getLocation().getCity().toLowerCase().contains(place);
		boolean bdateFrom = dateFrom == null ? true : m.getDate().isAfter(dateFrom);
		boolean bdateTo = dateTo == null ? true : m.getDate().isBefore(dateTo);
		boolean bpriceFrom = priceFrom == 0 ? true : (m.getTicketPrice() >= priceFrom);
		boolean bpriceTo = priceTo == 0 ? true : (m.getTicketPrice() <= priceTo);
		return bname && bplace && bdateFrom && bdateTo && bpriceFrom && bpriceTo;
	}
	
	private boolean correspondsFilter(Manifestation m, String type, boolean nijeRasprodato) {
		//svaki string sadrzi prazan string
		boolean btip;
		if(type.equals("CONCERT") || type.equals("FESTIVAL") || type.equals("THEATER")) {
			btip = m.getType().equals(ManifestationType.valueOf(type));
		} else {
			btip = true;
		}
		boolean bnotrasp;
		if(nijeRasprodato) {
			if(m.getNumberOfSeats() > 0) {
				bnotrasp = true;
			} else {
				bnotrasp = false;
			}
		} else {
			if(m.getNumberOfSeats() > 0) {
				bnotrasp = false;
			} else {
				bnotrasp = true;
			}
		}
		return btip && bnotrasp;
	}
	
	private void loadManifestations() {
		BufferedReader reader = null;
		try {
			File file = new File(contextPath + "/repositories/manifestations.txt");
			reader = new BufferedReader(new FileReader(file));
			String line;
			StringTokenizer st;
			while((line = reader.readLine()) != null) {
				line = line.trim();
				if(line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while(st.hasMoreTokens()) {
					String id = st.nextToken().trim();
					String name = st.nextToken().trim();
					ManifestationType type = ManifestationType.valueOf(st.nextToken().trim());
					int numberOfSeats = Integer.parseInt(st.nextToken().trim());
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");  
					LocalDateTime maintenance = LocalDateTime.parse(st.nextToken().trim(), formatter);
					int ticketPrice = Integer.parseInt(st.nextToken().trim());
					Status status = (Integer.parseInt(st.nextToken().trim())) == 1 ? 
							Status.ACTIVE : Status.NONACTIVE;
					//location
					Location location = getManifestationLocation(st.nextToken().trim());
					String imagePath = st.nextToken().trim();
					manifestations.put(id, new Manifestation(id, name, type, numberOfSeats, maintenance, ticketPrice,
							status, location, imagePath));
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
		
	}
	
	private Location getManifestationLocation(String id) {
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
					if(!locationId.equals(id)) {
						break;
					}
					Double longitude = Double.parseDouble(st.nextToken().trim());
					Double latitude = Double.parseDouble(st.nextToken().trim());
					String street = st.nextToken().trim();
					String city = st.nextToken().trim();
					return new Location(Integer.parseInt(locationId), longitude, latitude, street, city);
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

	public List<Manifestation> sorting(String selected) {
		// TODO Auto-generated method stub
		List<Manifestation> searchedManifestations = (List<Manifestation>) manifestations.values();
		if(selected.equals("Sortiraj po ceni manifestacije rastuce")) {
			Collections.sort(searchedManifestations, new Comparator<Manifestation>() {

				@Override
				public int compare(Manifestation o1, Manifestation o2) {
					// TODO Auto-generated method stub
					return o1.getTicketPrice() - o2.getTicketPrice();
				}
			});
		}
		else if(selected.equals("Sortiraj po ceni manifestacije opadajuce")) {
			Collections.sort(searchedManifestations, new Comparator<Manifestation>() {

				@Override
				public int compare(Manifestation o1, Manifestation o2) {
					// TODO sAuto-generated method stub
					return o2.getTicketPrice() - o1.getTicketPrice();
				}
			});
		}
		else if(selected.equals("Sortiraj po nazivu manifestacije rastuce")) {
			Collections.sort(searchedManifestations, new Comparator<Manifestation>() {

				@Override
				public int compare(Manifestation o1, Manifestation o2) {
					// TODO Auto-generated method stub
					return o1.getName().compareTo(o2.getName());
				}
			});
		}
		else if(selected.equals("Sortiraj po nazivu manifestacije opadajuce")) {
			Collections.sort(searchedManifestations, new Comparator<Manifestation>() {

				@Override
				public int compare(Manifestation o1, Manifestation o2) {
					// TODO Auto-generated method stub
					return o2.getName().compareTo(o1.getName());
				}
			});
		}
		else if(selected.equals("Sortiraj po lokaciji manifestacije rastuce")) {
			Collections.sort(searchedManifestations, new Comparator<Manifestation>() {

				@Override
				public int compare(Manifestation o1, Manifestation o2) {
					// TODO Auto-generated method stub
					return o1.getLocation().getCity().compareTo(o2.getLocation().getCity());
				}
			});
		}
		else if(selected.equals("Sortiraj po lokaciji manifestacije opadajuce")) {
			Collections.sort(searchedManifestations, new Comparator<Manifestation>() {

				@Override
				public int compare(Manifestation o1, Manifestation o2) {
					// TODO Auto-generated method stub
					return o2.getLocation().getCity().compareTo(o1.getLocation().getCity());
				}
			});
		}
		else if(selected.equals("Sortiraj po datumu manifestacije rastuce")) {
			Collections.sort(searchedManifestations, new Comparator<Manifestation>() {

				@Override
				public int compare(Manifestation o1, Manifestation o2) {
					// TODO Auto-generated method stub
					return o1.getDate().compareTo(o2.getDate());
				}
			});
		}
		else if(selected.equals("Sortiraj po datumu manifestacije opadajuce")) {
			Collections.sort(searchedManifestations, new Comparator<Manifestation>() {

				@Override
				public int compare(Manifestation o1, Manifestation o2) {
					// TODO Auto-generated method stub
					return o2.getDate().compareTo(o1.getDate());
				}
			});
		}
		return searchedManifestations;
	}
	//OOOOOOOOO
	public List<Manifestation> filter(String name,  String dateFrom, String dateTo, String place, int priceFrom, int priceTo, String selected,String izborTipa, boolean nijeRasprodato) {
		// TODO Auto-generated method stub
		List<Manifestation> searchedManifestations = search(name, dateFrom, dateTo, place, priceFrom, priceTo, selected);
		List<Manifestation> filteredMan = new ArrayList<Manifestation>();
		for (Manifestation m : searchedManifestations) {
			if(correspondsFilter(m, izborTipa, nijeRasprodato)) {
				filteredMan.add(m);
			}
		}
		return filteredMan;
	}
}
