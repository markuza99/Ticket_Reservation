package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import beans.Location;
import beans.Manifestation;
import beans.ManifestationType;
import beans.Status;
import dto.ReservationDTO;

public class ManifestationDAO {
	private Map<String, Manifestation> manifestations = new HashMap<>();
	private String contextPath;
	private LocationDAO locationDAO;
	
	public ManifestationDAO(String contextPath, LocationDAO locationDAO) {
		this.contextPath = contextPath;
		this.locationDAO = locationDAO;
		loadManifestations();
	}
	
	public List<Manifestation> getAllManifestations() {
		return new ArrayList<Manifestation>(manifestations.values());
	}
	
	public void reduceNumberOfSeats(ReservationDTO reservationDTO) {

		Manifestation manifestation = getOneManifestation(reservationDTO.manifestation);
		int remaining = manifestation.getRemainingNumberOfSeats() - reservationDTO.numberOfTickets;
		manifestation.setRemainingNumberOfSeats(remaining);
		write();
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
		
		// TO DO : srediti sorting (napraviti klasu)
//		if(selected.equals("Sortiraj po ceni manifestacije rastuce")) {
//			Collections.sort(searchedManifestations, new Comparator<Manifestation>() {
//
//				@Override
//				public int compare(Manifestation o1, Manifestation o2) {
//					// TODO Auto-generated method stub
//					return o1.getTicketPrice() - o2.getTicketPrice();
//				}
//			});
//		}
//		else if(selected.equals("Sortiraj po ceni manifestacije opadajuce")) {
//			Collections.sort(searchedManifestations, new Comparator<Manifestation>() {
//
//				@Override
//				public int compare(Manifestation o1, Manifestation o2) {
//					// TODO sAuto-generated mssethod stub
//					return o2.getTicketPrice() - o1.getTicketPrice();
//				}
//			});
//		}
//		else if(selected.equals("Sortiraj po nazivu manifestacije rastuce")) {
//			Collections.sort(searchedManifestations, new Comparator<Manifestation>() {
//
//				@Override
//				public int compare(Manifestation o1, Manifestation o2) {
//					// TODO Auto-generated method stub
//					return o1.getName().compareTo(o2.getName());
//				}
//			});
//		}
//		else if(selected.equals("Sortiraj po nazivu manifestacije opadajuce")) {
//			Collections.sort(searchedManifestations, new Comparator<Manifestation>() {
//
//				@Override
//				public int compare(Manifestation o1, Manifestation o2) {
//					// TODO Auto-generated method stub
//					return o2.getName().compareTo(o1.getName());
//				}
//			});
//		}
//		else if(selected.equals("Sortiraj po lokaciji manifestacije rastuce")) {
//			Collections.sort(searchedManifestations, new Comparator<Manifestation>() {
//
//				@Override
//				public int compare(Manifestation o1, Manifestation o2) {
//					// TODO Auto-generated method stub
//					return o1.getLocation().getCity().compareTo(o2.getLocation().getCity());
//				}
//			});
//		}
//		else if(selected.equals("Sortiraj po lokaciji manifestacije opadajuce")) {
//			Collections.sort(searchedManifestations, new Comparator<Manifestation>() {
//
//				@Override
//				public int compare(Manifestation o1, Manifestation o2) {
//					// TODO Auto-generated method stub
//					return o2.getLocation().getCity().compareTo(o1.getLocation().getCity());
//				}
//			});
//		}
//		else if(selected.equals("Sortiraj po datumu manifestacije rastuce")) {
//			Collections.sort(searchedManifestations, new Comparator<Manifestation>() {
//
//				@Override
//				public int compare(Manifestation o1, Manifestation o2) {
//					// TODO Auto-generated method stub
//					return o1.getDate().compareTo(o2.getDate());
//				}
//			});
//		}
//		else if(selected.equals("Sortiraj po datumu manifestacije opadajuce")) {
//			Collections.sort(searchedManifestations, new Comparator<Manifestation>() {
//
//				@Override
//				public int compare(Manifestation o1, Manifestation o2) {
//					// TODO Auto-generated method stub
//					return o2.getDate().compareTo(o1.getDate());
//				}
//			});
//		}
		return searchedManifestations;		

	}
	
	private boolean correspondsSearch(Manifestation m,String name,  LocalDateTime dateFrom, LocalDateTime dateTo, String place, int priceFrom, int priceTo) {
		boolean bname = m.getName().toLowerCase().contains(name);
		String locationId = m.getLocation();
		Location location = locationDAO.getLocationForManifestation(locationId);
		boolean bplace = location.getCity().toLowerCase().contains(place);
		boolean bdateFrom = dateFrom == null ? true : m.getDate().isAfter(dateFrom);
		boolean bdateTo = dateTo == null ? true : m.getDate().isBefore(dateTo);
		boolean bpriceFrom = priceFrom == 0 ? true : (m.getTicketPrice() >= priceFrom);
		boolean bpriceTo = priceTo == 0 ? true : (m.getTicketPrice() <= priceTo);
		return bname && bplace && bdateFrom && bdateTo && bpriceFrom && bpriceTo;
	}

	private boolean correspondsFilter(Manifestation m, String type, boolean nijeRasprodato) {
		//svaki string sadrzi prazan string
		boolean btip = type.equals("SVE") || type.equals("") ? true : m.getType().equals(ManifestationType.valueOf(type));
		boolean bnotrasp;
		if(nijeRasprodato) {
			bnotrasp = m.getNumberOfSeats() > 0 ? true : false;
		} else {
			bnotrasp = m.getNumberOfSeats() > 0 ? false : true;
		}
		return btip && bnotrasp;
	}
	
	public String getManifestationLine(Manifestation manifestation) {
		StringBuilder manifestationString = new StringBuilder();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		manifestationString.append(manifestation.getId() + ";" + manifestation.getName() + ";"
				+ manifestation.getType() + ";" + manifestation.getNumberOfSeats() + ";"
				+ manifestation.getRemainingNumberOfSeats() + ";" + manifestation.getDate().format(formatter) + ";"
				+ manifestation.getTicketPrice() + ";");
		if(manifestation.getStatus() == Status.ACTIVE) {
			manifestationString.append("1;");
		} else {
			manifestationString.append("0;");
		}
		manifestationString.append(manifestation.getLocation() + ";"
				+ manifestation.getImage());
        return manifestationString.toString();
	}
	
	public void write() {
		File file = new File(contextPath + "/repositories/manifestations.txt");

        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            for(Manifestation manifestation : manifestations.values()) {
            	pw.println(getManifestationLine(manifestation));
            }
            
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
					int remainingNumberOfSeats = Integer.parseInt(st.nextToken().trim());
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
					LocalDateTime maintenance = LocalDateTime.parse(st.nextToken().trim(), formatter);
					int ticketPrice = Integer.parseInt(st.nextToken().trim());
					Status status = (Integer.parseInt(st.nextToken().trim())) == 1 ? 
							Status.ACTIVE : Status.NONACTIVE;
					String location = st.nextToken().trim();
					String imagePath = st.nextToken().trim();
					manifestations.put(id, new Manifestation(
							id, name, type, numberOfSeats,
							remainingNumberOfSeats, maintenance, ticketPrice,
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
	
	

	public Manifestation getOneManifestation(String id) {
		return manifestations.get(id);
	}

	
	
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

	public Boolean update(Manifestation manifestation) {
		//provera da li vec ima manifestacija u isto vreme na istoj adresi
		if(!checkManifestationMaintainance(manifestation.getDate(), manifestation.getLocation(), manifestation.getId())) {
			return false;
		}
		manifestations.put(manifestation.getId(), manifestation);
		return true;
	}

	private Boolean checkManifestationMaintainance(LocalDateTime date, String location, String id) {
		// TODO Auto-generated method stub
		for(Manifestation manifestation : manifestations.values()) {
			System.out.println(manifestation.getDate());
			System.out.println(date);
			System.out.println(manifestation.getDate().isEqual(date));
			if(manifestation.getId().equals(id))
				continue;
			if(manifestation.getLocation().equals(location) &&
			   manifestation.getDate().isEqual(date)) {
				return false;
			}
		}
		return true;
	}
	
	

}
