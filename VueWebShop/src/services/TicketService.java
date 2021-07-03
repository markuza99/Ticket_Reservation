package services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import beans.Customer;
import beans.CustomerType;
import beans.Manifestation;
import beans.Seller;
import beans.Ticket;
import beans.TicketStatus;
import beans.TicketType;
import beans.value_objects.SortManifestations;
import beans.value_objects.SortTickets;
import dao.interfaces.ICustomerDAO;
import dao.interfaces.IManifestationDAO;
import dao.interfaces.ISellerDAO;
import dao.interfaces.ITicketDAO;
import dto.ReservationDTO;
import dto.SearchTicketsDTO;
import dto.TicketRepresentationDTO;


public class TicketService {
	private ITicketDAO ticketDAO;
	private IManifestationDAO manifestationDAO;
	private ICustomerDAO customerDAO;
	private ISellerDAO sellerDAO;
	
	public TicketService(ITicketDAO ticketDAO, IManifestationDAO manifestationDAO, ICustomerDAO customerDAO, ISellerDAO sellerDAO) {
		this.ticketDAO = ticketDAO;
		this.manifestationDAO = manifestationDAO;
		this.customerDAO = customerDAO;
		this.sellerDAO = sellerDAO;
	}
	
	public Boolean userAttended(String username, String manifestationId) {
		for(Ticket ticket : ticketDAO.getAll()) {
			if(ticket.getManifestationId().equals(manifestationId)
			&& ticket.getUser().equals(username)) {
				return true;
			}
		}
		return false;
	}
	
	public Ticket reserveTickets(ReservationDTO reservationDTO, String username) {
		
		if(!updateManifestationNumberOfSeats(reservationDTO.manifestationId, reservationDTO.numberOfTickets)) {
			return null;
		}
		
		Ticket ticket = createTicket(reservationDTO, username);
		
		addTicketToCustomer(username, ticket.getId(), reservationDTO.points);
		
		return ticket;
	}
	
	
	private boolean updateManifestationNumberOfSeats(String manifestationId, int numberOfTickets) {
		Manifestation manifestation = manifestationDAO.read(manifestationId);
		
		if(manifestation == null || manifestation.getRemainingNumberOfSeats() < numberOfTickets) {
			return false;
		}
		
		manifestation.setRemainingNumberOfSeats(manifestation.getRemainingNumberOfSeats() - numberOfTickets);
		manifestationDAO.update(manifestation);
		return true;
	}
	
	private Ticket createTicket(ReservationDTO reservationDTO, String username) {
		String ticketId = generateRandomId();
		while(ticketDAO.read(ticketId) != null) {
			ticketId = generateRandomId();
		}
		double ticketPrice = getTicketTotalPrice(username, reservationDTO.numberOfTickets, reservationDTO.ticketType, reservationDTO.manifestationId);
		return ticketDAO.create(
				new Ticket(ticketId,reservationDTO.manifestationId,LocalDateTime.now(),ticketPrice,
				username,TicketStatus.RESERVED,TicketType.valueOf(reservationDTO.ticketType), reservationDTO.numberOfTickets, false));
	}
	
	private void addTicketToCustomer(String username, String ticketId, int points) {
		Customer customer = customerDAO.read(username);
		
		if(customer == null) {
			ArrayList<String> tickets = new ArrayList<String>();
			customer = new Customer(username, tickets, 0, getInitialCustomerType());
		}
		
		customer.getTickets().add(ticketId);
		changeCustomersPoints(points, customer, customerDAO.getCustomerTypes());
		customerDAO.update(customer);
	}
	
	private CustomerType getInitialCustomerType() {
		List<CustomerType> customerTypes = customerDAO.getCustomerTypes();
		Collections.sort(customerTypes);
		return customerTypes.get(0);
	}
	
	private void changeCustomersPoints(int points, Customer customer, List<CustomerType> customerTypes) {
		Collections.sort(customerTypes);
		int currentPoints = customer.getPoints();
		int newPoints = currentPoints + points;
		customer.setPoints(newPoints);
		
		int typePosition = customerTypes.indexOf(customer.getCustomerType());
		int typesSize = customerTypes.size();
		if(typesSize - typePosition != 1) {
			while(true) {
				CustomerType newType = customerTypes.get(typePosition + 1);
				if(newPoints > newType.getPoints())
					customer.setCustomerType(newType);
				else {
					break;
				}
				typePosition++;
				if(typesSize - typePosition == 1)
					break;
			} 
			
		}
	}
	
	private String generateRandomId() {
	    
	    String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
	            + "0123456789"
	            + "abcdefghijklmnopqrstuvxyz"; 
	    StringBuilder sb = new StringBuilder(10);
	    for (int i = 0; i < 10; i++) {  
	        int index = (int)(AlphaNumericString.length() * Math.random()); 
	        sb.append(AlphaNumericString.charAt(index)); 
	    } 
	
	    return sb.toString(); 
	}

	public double getTicketTotalPrice(String username, int numberOfTickets, String ticketType, String manifestationId) {
		// TODO Auto-generated method stub
		
		Customer customer = customerDAO.read(username);
		double discount = 0;
		if(customer != null) {
			discount = customer.getCustomerType().getDiscount();
		}
		double originalPrice = manifestationDAO.read(manifestationId).getTicketPrice();
		
		double ticketPrice = (originalPrice - originalPrice * discount) * numberOfTickets; 
		
		if(ticketType.equals("FAN_PIT")) {
			ticketPrice = ticketPrice * 2;
		} else if(ticketType.equals("VIP")){
			ticketPrice = ticketPrice * 4;
		}
		
		return ticketPrice;
	}
	
	private List<Manifestation> getManifestationsByTickets(List<Ticket> tickets) {
		List<Manifestation> manifestations = new ArrayList<Manifestation>();
		
		for(Ticket t: tickets) {
			Manifestation manifestation = manifestationDAO.read(t.getManifestationId());
			if(!manifestations.contains(manifestation)) {
				manifestations.add(manifestation);
			}
		}
		return manifestations;
	}
	
	private List<Ticket> sortTickets(List<Ticket> tickets, List<Manifestation> manifestations, String sortBy) {

		SortTickets sort = new SortTickets();
		SortManifestations sortManifestation = new SortManifestations();
		switch(sortBy) {
		case "priceAsc":
			sort.sortByPrice(true, tickets);
			break;
		case "priceDesc":
			sort.sortByPrice(false, tickets);
			break;
		case "manifestationNameAsc":
			sortManifestation.sortByName(true, manifestations);
			tickets = sort.sortByManifestationAttribute(tickets, manifestations);
			break;
		case "manifestationNameDesc":
			sortManifestation.sortByName(false, manifestations);
			tickets = sort.sortByManifestationAttribute(tickets, manifestations);
			break;
		case "manifestationDateAsc":
			sortManifestation.sortByDate(true, manifestations);
			tickets = sort.sortByManifestationAttribute(tickets, manifestations);
			break;
		case "manifestationDateDesc":
			sortManifestation.sortByDate(false, manifestations);
			tickets = sort.sortByManifestationAttribute(tickets, manifestations);
			break;
		}
		return tickets;
	}
	
	private List<Ticket> filterTickets(List<Ticket> tickets, String ticketType, String ticketStatus) {
		List<Ticket> filteredTickets = new ArrayList<Ticket>();
		
		for(Ticket t : tickets) {
			if(correspondsFilter(t, ticketType, ticketStatus)) {
				filteredTickets.add(t);
			}
		}
		
		return filteredTickets;
	}
	
	private List<Ticket> searchTickets(List<Ticket> tickets, SearchTicketsDTO searchTicketsDTO) {
		List<Ticket> searchedTickets = new ArrayList<Ticket>();
		LocalDateTime dateFrom = null;
		LocalDateTime dateTo = null;
		if(searchTicketsDTO.dateFrom != null) {
			if(!searchTicketsDTO.dateFrom.equals(""))
				dateFrom = LocalDateTime.parse(searchTicketsDTO.dateFrom);
		}
		if(searchTicketsDTO.dateTo != null) {
			if(!searchTicketsDTO.dateTo.equals(""))
				dateTo = LocalDateTime.parse(searchTicketsDTO.dateTo);
		}
		
		for(Ticket t : tickets) {
			if(correspondsSearch(t, searchTicketsDTO.manifestationName, dateFrom, dateTo, 
				searchTicketsDTO.priceFrom, searchTicketsDTO.priceTo)) {
				searchedTickets.add(t);
			}
		}
		
		return searchedTickets;
	}
	
	private boolean correspondsSearch(Ticket ticket, String name,  LocalDateTime dateFrom, LocalDateTime dateTo, double priceFrom, double priceTo) {
		Manifestation manifestation = manifestationDAO.read(ticket.getManifestationId());
		boolean bname = name == null ? true : manifestation.getName().toLowerCase().contains(name.toLowerCase());
		boolean bdateFrom = dateFrom == null ? true : manifestation.getStartTime().isAfter(dateFrom);
		boolean bdateTo = dateTo == null ? true : manifestation.getEndTime().isBefore(dateTo);
		boolean bpriceFrom = priceFrom == 0 ? true : (ticket.getPrice() >= priceFrom);
		boolean bpriceTo = priceTo == 0 ? true : (ticket.getPrice() <= priceTo);
		return bname && bdateFrom && bdateTo && bpriceFrom && bpriceTo;
	}
	
	private boolean correspondsFilter(Ticket ticket, String ticketType, String ticketStatus) {
		boolean btype = ticketType.equals("all") || ticketType.equals("")  ? true : ticket.getTicketType().equals(TicketType.valueOf(ticketType));
		boolean bstatus = ticketStatus.equals("all") || ticketStatus.equals("") ? true : ticket.getTicketStatus().equals(TicketStatus.valueOf(ticketStatus));
		return btype && bstatus;
	}

	public List<TicketRepresentationDTO> getCustomerTickets(String username, SearchTicketsDTO searchTicketsDTO) {
		Customer customer = customerDAO.read(username);
		if(customer == null) return null;
		List<Ticket> customerTickets = new ArrayList<Ticket>();
		for(String ticketId: customer.getTickets()) {
			Ticket ticket = ticketDAO.read(ticketId);
			if(ticket.getIsDeleted()) continue;
			Manifestation manifestation = manifestationDAO.read(ticket.getManifestationId());
			if(manifestation.getIsDeleted()) continue;
			customerTickets.add(ticket);
		}
		
		return searchSortFilterTickets(customerTickets, searchTicketsDTO);
		
	}

	public List<TicketRepresentationDTO> getSellerTickets(String username, SearchTicketsDTO searchTicketsDTO) {
		Seller seller = sellerDAO.read(username);
		List<Ticket> sellerTickets = new ArrayList<Ticket>();
		for(Ticket ticket : ticketDAO.getAll()) {
			if(ticket.getIsDeleted()) continue;
			for(String manifestation : seller.getManifestations()) {
				if(ticket.getManifestationId().equals(manifestation)) {
					sellerTickets.add(ticket);
				}
			}
		}
		
		return searchSortFilterTickets(sellerTickets, searchTicketsDTO);
	}

	private List<TicketRepresentationDTO> searchSortFilterTickets(List<Ticket> tickets,
			SearchTicketsDTO searchTicketsDTO) {
		List<Ticket> searchedTickets = searchTickets(tickets, searchTicketsDTO);
		
		List<Manifestation> manifestations = getManifestationsByTickets(searchedTickets);
		
		if(searchTicketsDTO.sortBy == null) searchTicketsDTO.sortBy = "";
		if(searchTicketsDTO.ticketType == null) searchTicketsDTO.ticketType = "";
		if(searchTicketsDTO.ticketStatus == null) searchTicketsDTO.ticketStatus = "";
		
		searchedTickets =  sortTickets(searchedTickets, manifestations, searchTicketsDTO.sortBy);
		
		List<Ticket> filteredTickets = filterTickets(searchedTickets, searchTicketsDTO.ticketType, searchTicketsDTO.ticketStatus);
		
		List<TicketRepresentationDTO> ticketsDTO = new ArrayList<TicketRepresentationDTO>();
		for(Ticket t : filteredTickets) {
			Manifestation manifestation = manifestationDAO.read(t.getManifestationId());
			ticketsDTO.add(new TicketRepresentationDTO(t, manifestation, t.getUser()));
		}
		
		return ticketsDTO;
	}

	public List<TicketRepresentationDTO> getAllTickets(SearchTicketsDTO searchTicketsDTO) {
		List<Ticket> tickets = ticketDAO.getAll();
		return searchSortFilterTickets(tickets, searchTicketsDTO);
	}

	public Ticket cancelReservation(String ticketId, String username) {
		Ticket ticket = ticketDAO.read(ticketId);
		Manifestation manifestation = manifestationDAO.read(ticket.getManifestationId());
		if(manifestation.getStartTime().isBefore(LocalDateTime.now()) || ticket.getTicketStatus() == TicketStatus.CANCELED) {
			return null;
		}
		ticket.setTicketStatus(TicketStatus.CANCELED);
		int remaining = manifestation.getRemainingNumberOfSeats();
		manifestation.setRemainingNumberOfSeats(remaining+1);
		removeCustomerPoints(username, ticket.getPrice());
		manifestationDAO.update(manifestation);
		return ticketDAO.update(ticket);
	}

	private void removeCustomerPoints(String username, double price) {
		List<CustomerType> customerTypes = customerDAO.getCustomerTypes();
		Collections.sort(customerTypes);
		Customer customer = customerDAO.read(username);
		int currentPoints = customer.getPoints();
		int points = (int) (price/1000 * 133 * 4);
		int newPoints = currentPoints - points;
		if(newPoints < 0) {
			newPoints = 0;
		}
		customer.setPoints(newPoints);
		
		int typePosition = customerTypes.indexOf(getInitialCustomerType());
		int typesSize = customerTypes.size();
		if(typesSize - typePosition != 1) {
			while(true) {
				CustomerType newType = customerTypes.get(typePosition + 1);
				if(newPoints > newType.getPoints())
					customer.setCustomerType(newType);
				else {
					break;
				}
				typePosition++;
				if(typesSize - typePosition == 1)
					break;
			} 
			
		}
		customerDAO.update(customer);
	}

	public void deleteTicket(String ticketId) {
		Ticket ticket = ticketDAO.read(ticketId);
		if(ticket == null) return;
		ticket.setIsDeleted("1");
		ticketDAO.update(ticket);
	}

	public void retrieveTicket(String ticketId) {
		Ticket ticket = ticketDAO.read(ticketId);
		if(ticket == null) return;
		ticket.setIsDeleted("0");
		ticketDAO.update(ticket);
	}
}
