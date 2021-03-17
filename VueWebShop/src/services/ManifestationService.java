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
		List<Manifestation> sortedManifestations = manifestationDAO.getAll();
		sort.sortByDate(true, sortedManifestations);

		List<Manifestation> activeManifestations = new ArrayList<Manifestation>();
		for(Manifestation m : sortedManifestations) {
			if(m.getStatus() == Status.ACTIVE) {
				activeManifestations.add(m);
			}
		}
		return activeManifestations;
	}
	
	public Manifestation getManifestation(String id) {
		return manifestationDAO.read(id);
	}


	public Manifestation updateManifestation(Manifestation manifestation) {
		if(!checkManifestationMaintainance(manifestation.getDate(), manifestation.getLocation(), manifestation.getId())) {
			return null;
		}
		return manifestationDAO.update(manifestation);
	}
	
	public List<Manifestation> searchAllManifestations(String name,  String dateFrom, String dateTo, String place, int priceFrom, int priceTo) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		LocalDateTime LdateFrom = null;
		LocalDateTime LdateTo = null;
		if(!dateFrom.equals("")) {
			LdateFrom = LocalDateTime.parse(dateFrom, formatter);
		}
		if(!dateTo.equals("")) {
			LdateTo = LocalDateTime.parse(dateTo, formatter);
		}

		return searchGivenManifestations(manifestationDAO.getAll(), name, LdateFrom, LdateTo, place, priceFrom, priceTo);
	}
	
	public List<Manifestation> searchGivenManifestations(List<Manifestation> manifestations, String name,  LocalDateTime dateFrom, LocalDateTime dateTo, String place, int priceFrom, int priceTo) {
		List<Manifestation> searchedManifestations = new ArrayList<Manifestation>();
		
		for (Manifestation m : manifestations) {
			if(correspondsSearch(m, name.toLowerCase(), dateFrom, dateTo, place.toLowerCase(), priceFrom, priceTo)) {
				searchedManifestations.add(m);
			}
		}
		
		return searchedManifestations;
	}

	public List<Manifestation> sortGivenManifestations(List<Manifestation> manifestations, String sortBy) {
		List<Location> locations = locationDAO.getAll();
		SortManifestations sort = new SortManifestations();
		
		switch(sortBy) {
		case "priceAsc":
			sort.sortByPrice(true, manifestations);
			break;
		case "priceDesc":
			sort.sortByPrice(false, manifestations);
			break;
		case "nameAsc":
			sort.sortByName(true, manifestations);
			break;
		case "nameDesc":
			sort.sortByName(false, manifestations);
			break;
		case "locationAsc":
			sort.sortLocations(true, locations);
			manifestations = sort.sortByLocation(manifestations, locations);
			break;
		case "locationDesc":
			sort.sortLocations(false, locations);
			manifestations = sort.sortByLocation(manifestations, locations);
			break;
		case "dateAsc":
			sort.sortByDate(true, manifestations);
			break;
		case "dateDesc":
			sort.sortByDate(false, manifestations);
			break;
		}
		return manifestations;
	}
	public List<Manifestation> sortManifestations(List<Manifestation> manifestations, String sortBy) {
		return sortGivenManifestations(manifestationDAO.getAll(), sortBy);
	}
	
	
	public List<Manifestation> filterManifestations(List<Manifestation> manifestations, String manifestationType, String ticketCondition) throws ParseException {		
		List<Manifestation> filteredManifestations = new ArrayList<Manifestation>();
		for (Manifestation m : manifestations) {
			if(correspondsFilter(m, manifestationType, ticketCondition)) {
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

	private boolean correspondsFilter(Manifestation manifestation, String manifestationType, String ticketCondition) {
		boolean btype = manifestationType.equals("ALL") || manifestationType.equals("") ? true : manifestation.getType().equals(ManifestationType.valueOf(manifestationType));
		boolean bnotSoldOut;
		if(ticketCondition.equals("notSoldOut")) {
			bnotSoldOut = manifestation.getRemainingNumberOfSeats() > 0 ? true : false;
		} else if(ticketCondition.equals("soldOut")) {
			bnotSoldOut = manifestation.getRemainingNumberOfSeats() > 0 ? false : true;
		} else {
			bnotSoldOut = true;
		}
		return btype && bnotSoldOut;
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
