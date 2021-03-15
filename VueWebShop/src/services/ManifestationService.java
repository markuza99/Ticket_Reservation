package services;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import beans.Location;
import beans.Manifestation;
import beans.ManifestationType;
import beans.Seller;
import beans.Status;
import beans.value_objects.SortManifestations;
import dao.interfaces.ILocationDAO;
import dao.interfaces.IManifestationDAO;
import dao.interfaces.ISellerDAO;
import dto.ManifestationDTO;


public class ManifestationService {
	private IManifestationDAO manifestationDAO;
	private ILocationDAO locationDAO;
	private ISellerDAO sellerDAO;
	
	public ManifestationService(IManifestationDAO manifestationDAO, ILocationDAO locationDAO, ISellerDAO sellerDAO) {
		this.manifestationDAO = manifestationDAO;
		this.locationDAO = locationDAO;
		this.sellerDAO = sellerDAO;
	}
	
	public List<Manifestation> getActiveManifestations() {
		SortManifestations sort = new SortManifestations();
		sort.sortByDate(true, manifestationDAO.getAll());

		List<Manifestation> activeManifestations = new ArrayList<Manifestation>();
		for(Manifestation m : manifestationDAO.getAll()) {
			if(m.getStatus() == Status.ACTIVE) {
				activeManifestations.add(m);
			}
		}
		return null;
	}
	
	public Manifestation getManifestation(String id) {
		return manifestationDAO.read(id);
	}
	
	public List<Manifestation> searchManifestations(String name,String dateFrom,String dateTo,String place,
			int priceFrom,  int priceTo, String selected) throws ParseException {
		return search(name, dateFrom, dateTo, place, priceFrom, priceTo, selected);
	}
	
	public List<Manifestation> filterManifestations(String name, String dateFrom, String dateTo,
			String place,int priceFrom,int priceTo,String selected,String izborTipa, boolean nijeRasprodato) throws ParseException {
		return filter(name, dateFrom, dateTo, place, priceFrom, priceTo, selected,izborTipa, nijeRasprodato);
	}

	public Manifestation updateManifestation(Manifestation manifestation) {
		if(!checkManifestationMaintainance(manifestation.getDate(), manifestation.getLocation(), manifestation.getId())) {
			return null;
		}
		return manifestationDAO.update(manifestation);
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
		
		for (Manifestation m : manifestationDAO.getAll()) {
			if(correspondsSearch(m, name.toLowerCase(), LdateFrom, LdateTo, place.toLowerCase(), priceFrom, priceTo)) {
				searchedManifestations.add(m);
			}
		}

		searchedManifestations = sortManifestations(selected, searchedManifestations);
		
		return searchedManifestations;
	}
	
	private List<Manifestation> sortManifestations(String selected, List<Manifestation> searchedManifestations) {
		List<Location> locations = locationDAO.getAll();
		SortManifestations sort = new SortManifestations();
		
		switch(selected) {
		case "priceAsc":
			sort.sortByPrice(true, searchedManifestations);
			break;
		case "priceDesc":
			sort.sortByPrice(false, searchedManifestations);
			break;
		case "nameAsc":
			sort.sortByName(true, searchedManifestations);
			break;
		case "nameDesc":
			sort.sortByName(false, searchedManifestations);
			break;
		case "locationAsc":
			sort.sortLocations(true, locations);
			searchedManifestations = sort.sortByLocation(searchedManifestations, locations);
			break;
		case "locationDesc":
			sort.sortLocations(false, locations);
			searchedManifestations = sort.sortByLocation(searchedManifestations, locations);
			break;
		case "dateAsc":
			sort.sortByDate(true, searchedManifestations);
			break;
		case "dateDesc":
			sort.sortByDate(false, searchedManifestations);
			break;
		}
		return searchedManifestations;
		
	}

	
	public List<Manifestation> filter(String name,  String dateFrom, String dateTo, String place, int priceFrom, int priceTo, String selected,String type, boolean notSoldOut) {
		List<Manifestation> searchedManifestations = search(name, dateFrom, dateTo, place, priceFrom, priceTo, selected);
		List<Manifestation> filteredManifestations = new ArrayList<Manifestation>();
		for (Manifestation m : searchedManifestations) {
			if(correspondsFilter(m, type, notSoldOut)) {
				filteredManifestations.add(m);
			}
		}
		return filteredManifestations;
	}
	
	private boolean correspondsSearch(Manifestation m,String name,  LocalDateTime dateFrom, LocalDateTime dateTo, String place, int priceFrom, int priceTo) {
		boolean bname = m.getName().toLowerCase().contains(name);
		String locationId = m.getLocation();
		Location location = locationDAO.read(locationId);
		boolean bplace = location.getCity().toLowerCase().contains(place);
		boolean bdateFrom = dateFrom == null ? true : m.getDate().isAfter(dateFrom);
		boolean bdateTo = dateTo == null ? true : m.getDate().isBefore(dateTo);
		boolean bpriceFrom = priceFrom == 0 ? true : (m.getTicketPrice() >= priceFrom);
		boolean bpriceTo = priceTo == 0 ? true : (m.getTicketPrice() <= priceTo);
		return bname && bplace && bdateFrom && bdateTo && bpriceFrom && bpriceTo;
	}

	private boolean correspondsFilter(Manifestation m, String type, boolean nijeRasprodato) {
		boolean btip = type.equals("SVE") || type.equals("") ? true : m.getType().equals(ManifestationType.valueOf(type));
		boolean bnotrasp;
		if(nijeRasprodato) {
			bnotrasp = m.getNumberOfSeats() > 0 ? true : false;
		} else {
			bnotrasp = m.getNumberOfSeats() > 0 ? false : true;
		}
		return btip && bnotrasp;
	}
	
	public Manifestation addManifestation(ManifestationDTO manifestationDTO, String username) throws IOException {
		String base64Image = manifestationDTO.getImage64base().split(",")[1];
		byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);

		manifestationDAO.saveImage(imageBytes, manifestationDTO.getImageName());
	        
		if(manifestationDAO.read(manifestationDTO.getId()) != null) {
			return null;
		}
		
		if(!checkManifestationMaintainance(manifestationDTO.getDate(), manifestationDTO.getLocation(), manifestationDTO.getId())) {
			return null;
		}
		
		int numberOfSeats = manifestationDTO.getNumberOfSeats();
		Manifestation manifestation = new Manifestation(manifestationDTO.getId(), manifestationDTO.getName(), manifestationDTO.getType(),
				numberOfSeats,numberOfSeats,manifestationDTO.getDate(), manifestationDTO.getTicketPrice(), Status.ACTIVE,
				manifestationDTO.getLocation(), manifestationDTO.getImageName(), false);
		manifestationDAO.create(manifestation);
		
		Seller seller = sellerDAO.read(username);
		if(seller != null) {
			seller.getManifestations().add(manifestationDTO.getId());
			sellerDAO.update(seller);
		} else {
			ArrayList<String> sellersManifestations = new ArrayList<String>();
			sellersManifestations.add(manifestationDTO.getId());
			seller = new Seller(username, sellersManifestations);
			sellerDAO.create(seller);
		}

		return manifestation;
	}
	
	public Boolean checkManifestationMaintainance(LocalDateTime date, String location, String id) {
		for(Manifestation manifestation : manifestationDAO.getAll()) {
			
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
