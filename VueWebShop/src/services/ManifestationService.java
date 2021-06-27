package services;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import beans.Customer;
import beans.Location;
import beans.Manifestation;
import beans.ManifestationType;
import beans.Seller;
import beans.Status;
import beans.Ticket;
import beans.value_objects.SortManifestations;
import dao.interfaces.ICustomerDAO;
import dao.interfaces.ILocationDAO;
import dao.interfaces.IManifestationDAO;
import dao.interfaces.ISellerDAO;
import dao.interfaces.ITicketDAO;
import dto.ManifestationDTO;
import dto.ManifestationForGridViewDTO;
import dto.ManifestationForViewDTO;
import dto.ManifestationWithLocationDTO;


public class ManifestationService {
	private IManifestationDAO manifestationDAO;
	private ILocationDAO locationDAO;
	private ISellerDAO sellerDAO;
	private ICustomerDAO customerDAO;
	private ITicketDAO ticketDAO;
	
	public ManifestationService(IManifestationDAO manifestationDAO, ILocationDAO locationDAO, ISellerDAO sellerDAO, ICustomerDAO customerDAO,
			ITicketDAO ticketDAO) {
		this.manifestationDAO = manifestationDAO;
		this.locationDAO = locationDAO;
		this.sellerDAO = sellerDAO;
		this.customerDAO = customerDAO;
		this.ticketDAO = ticketDAO;
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
	
	public List<Manifestation> getAllManifestations() {
		SortManifestations sort = new SortManifestations();
		List<Manifestation> sortedManifestations = manifestationDAO.getAll();
		sort.sortByDate(true, sortedManifestations);

		return sortedManifestations;
	}
	
	public ManifestationForViewDTO getManifestation(String id) {
		if(manifestationDAO.read(id) == null) return null;
		return new ManifestationForViewDTO(manifestationDAO.read(id));
	}


	public Manifestation updateManifestation(ManifestationForViewDTO manifestationDTO) {
		LocalDateTime startTime = LocalDateTime.parse(manifestationDTO.getStartTime());
		LocalDateTime endTime = LocalDateTime.parse(manifestationDTO.getEndTime());
		if(!checkManifestationMaintainance(startTime, endTime ,manifestationDTO.getLocation(), manifestationDTO.getId())) {
			return null;
		}
		
		Manifestation manifestation = convertManifestationDTOToManifestation(manifestationDTO);
		
		return manifestationDAO.update(manifestation);
	}
	
	private Manifestation convertManifestationDTOToManifestation(ManifestationForViewDTO manifestationDTO) {
		Manifestation manifestation = manifestationDAO.read(manifestationDTO.getId());
		manifestation.setName(manifestationDTO.getName());
		manifestation.setType(manifestationDTO.getType());
		manifestation.setNumberOfSeats(manifestationDTO.getNumberOfSeats());
		manifestation.setRemainingNumberOfSeats(manifestationDTO.getNumberOfSeats());
		manifestation.setStartTime(manifestationDTO.getStartTime());
		manifestation.setEndTime(manifestationDTO.getEndTime());
		manifestation.setTicketPrice(manifestationDTO.getTicketPrice());
		manifestation.setLocation(manifestationDTO.getLocation());
		manifestation.setImage(manifestationDTO.getImage());
		
		return manifestation;
	}
	
	public List<Manifestation> searchAllManifestations(String name,  String dateFrom, String dateTo, String place, int priceFrom, int priceTo) {
		
		LocalDateTime LdateFrom = null;
		LocalDateTime LdateTo = null;
		if(!dateFrom.equals("")) {
			LdateFrom = LocalDateTime.parse(dateFrom);
		}
		if(!dateTo.equals("")) {
			LdateTo = LocalDateTime.parse(dateTo);
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
		return sortGivenManifestations(manifestations, sortBy);
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
		boolean bdateFrom = dateFrom == null ? true : m.getStartTime().isAfter(dateFrom);
		boolean bdateTo = dateTo == null ? true : m.getEndTime().isBefore(dateTo);
		boolean bpriceFrom = priceFrom == 0 ? true : (m.getTicketPrice() >= priceFrom);
		boolean bpriceTo = priceTo == 0 ? true : (m.getTicketPrice() <= priceTo);
		return bname && bplace && bdateFrom && bdateTo && bpriceFrom && bpriceTo;
	}

	private boolean correspondsFilter(Manifestation manifestation, String manifestationType, String ticketCondition) {
		boolean btype = manifestationType.equals("all") || manifestationType.equals("") ? true : manifestation.getType().equals(ManifestationType.valueOf(manifestationType));
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
	
	private boolean validateManifestation(ManifestationDTO manifestationDTO) {
		if(locationDAO.read(manifestationDTO.getLocation()) == null) return false;
		if(manifestationDAO.read(manifestationDTO.getId()) != null) return false;
		
		if(!checkManifestationMaintainance(manifestationDTO.getStartTime(), manifestationDTO.getEndTime(), 
				manifestationDTO.getLocation(), manifestationDTO.getId())) return false;
			
		return true;
	}
	
	private Manifestation createManifestation(ManifestationDTO manifestationDTO) {
		int numberOfSeats = manifestationDTO.getNumberOfSeats();
		
		Manifestation manifestation = new Manifestation(manifestationDTO.getId(), manifestationDTO.getName(), manifestationDTO.getType(),
				numberOfSeats,numberOfSeats,manifestationDTO.getStartTime(), manifestationDTO.getEndTime(), manifestationDTO.getTicketPrice(), Status.ACTIVE,
				manifestationDTO.getLocation(), manifestationDTO.getImage64base(), false);
		manifestationDAO.create(manifestation);
		
		return manifestation;
	}
	

	public Manifestation addManifestation(ManifestationDTO manifestationDTO, String username) throws IOException {
		
		if(!validateManifestation(manifestationDTO)) {
			return null;
		}
		
		Manifestation manifestation = createManifestation(manifestationDTO);
		AddManifestationToSeller(manifestation.getId(), username);

		return manifestation;
	}
	
	private void AddManifestationToSeller(String id, String username) {
		Seller seller = sellerDAO.read(username);
		if(seller != null) {
			seller.getManifestations().add(id);
			sellerDAO.update(seller);
		} else {
			ArrayList<String> sellersManifestations = new ArrayList<String>();
			sellersManifestations.add(id);
			seller = new Seller(username, sellersManifestations);
			sellerDAO.create(seller);
		}
	}

	public Boolean checkManifestationMaintainance(LocalDateTime startTime, LocalDateTime endTime, String location, String id) {
		for(Manifestation manifestation : manifestationDAO.getAll()) {
			
			if(manifestation.getId().equals(id))
				continue;
			if(manifestation.getLocation().equals(location)) {
				if(startTime.isAfter(manifestation.getStartTime())) {
					if(startTime.isBefore(manifestation.getEndTime())) {
						return false;
					}
				}
					
				if(endTime.isAfter(manifestation.getStartTime())) {
					if(endTime.isBefore(manifestation.getEndTime())) {
						return false;	
					}
				}
			}
		}
		return true;
	}

	public List<ManifestationWithLocationDTO> getActiveManifestationsWithLocation() {
		List<ManifestationWithLocationDTO> manifestations = new ArrayList<ManifestationWithLocationDTO>();
		
		for(Manifestation m : getActiveManifestations()) {
			Location location = locationDAO.read(m.getLocation());
			manifestations.add(new ManifestationWithLocationDTO(m.getId(), m.getName(), m.getType(), m.getStartTime(), m.getEndTime(), m.getTicketPrice(),
					m.getStatus(), location , m.getImage(), m.getIsDeleted()));
		}
		return manifestations;
	}

	public List<ManifestationWithLocationDTO> convertToManifestationsWithLocationDTO(List<Manifestation> manifestations) {
		List<ManifestationWithLocationDTO> convertedManifestations = new ArrayList<ManifestationWithLocationDTO>();
		for(Manifestation manifestation : manifestations) {
			convertedManifestations.add(convertToManifestationWithLocationDTO(manifestation));
		}
		return convertedManifestations;
	}

	public ManifestationWithLocationDTO convertToManifestationWithLocationDTO(Manifestation m) {
		Location location = locationDAO.read(m.getLocation());
		return new ManifestationWithLocationDTO(m.getId(), m.getName(), m.getType(), m.getStartTime(), m.getEndTime(), m.getTicketPrice(),
				m.getStatus(), location , m.getImage(), m.getIsDeleted());
	}

	public List<ManifestationForGridViewDTO> getSellerManifestations(String username) {
		List<ManifestationForGridViewDTO> manifestations = new ArrayList<ManifestationForGridViewDTO>();
		Seller seller = sellerDAO.read(username);
		
		for(Manifestation m : getAllManifestations()) {
			if(!seller.getManifestations().contains(m.getId())) {
				continue;
			}
			Location location = locationDAO.read(m.getLocation());
			ManifestationForGridViewDTO manifestation = new ManifestationForGridViewDTO(m);
			manifestation.setLocation(location);
			manifestations.add(manifestation);
		}
		return manifestations;
	}
	
	public List<ManifestationForGridViewDTO> getAllManifestationsWithLocationDTO() {
		List<ManifestationForGridViewDTO> manifestations = new ArrayList<ManifestationForGridViewDTO>();
		
		for(Manifestation m : getAllManifestations()) {
			Location location = locationDAO.read(m.getLocation());
			ManifestationForGridViewDTO manifestation = new ManifestationForGridViewDTO(m);
			manifestation.setLocation(location);
			manifestations.add(manifestation);
		}
		return manifestations;
	}

	public List<ManifestationForGridViewDTO> getUserManifestations(String username) {
		List<Manifestation> manifestations = new ArrayList<Manifestation>();
		Customer customer = customerDAO.read(username);
		for(String ticketId : customer.getTickets()) {
			Ticket ticket = ticketDAO.read(ticketId);
			Manifestation manifestation = manifestationDAO.read(ticket.getManifestationId());
			if(!manifestations.contains(manifestation)) {
				manifestations.add(manifestation);
			}
		}
		
		List<ManifestationForGridViewDTO> manifestationsWithLocation = new ArrayList<ManifestationForGridViewDTO>();
		
		for(Manifestation m : manifestations) {
			Location location = locationDAO.read(m.getLocation());
			ManifestationForGridViewDTO manifestation = new ManifestationForGridViewDTO(m);
			manifestation.setLocation(location);
			manifestationsWithLocation.add(manifestation);
		}
		return manifestationsWithLocation;
		
	}
}
