package services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import beans.Customer;
import beans.CustomerType;
import beans.Manifestation;
import beans.Ticket;
import beans.TicketStatus;
import beans.TicketType;
import dao.interfaces.ICustomerDAO;
import dao.interfaces.IManifestationDAO;
import dao.interfaces.ITicketDAO;
import dto.ReservationDTO;


public class TicketService {
	private ITicketDAO ticketDAO;
	private IManifestationDAO manifestationDAO;
	private ICustomerDAO customerDAO;
	
	public TicketService(ITicketDAO ticketDAO, IManifestationDAO manifestationDAO, ICustomerDAO customerDAO) {
		this.ticketDAO = ticketDAO;
		this.manifestationDAO = manifestationDAO;
		this.customerDAO = customerDAO;
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
		
		//dodati number of tickets
		//promeniti
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
		return ticketDAO.create(new Ticket(ticketId,reservationDTO.manifestationId,LocalDateTime.now(),reservationDTO.ticketPrice,
				username,TicketStatus.RESERVED,TicketType.valueOf(reservationDTO.ticketType), false));
	}
	
	private CustomerType getInitialCustomerType() {
		List<CustomerType> customerTypes = customerDAO.getCustomerTypes();
		Collections.sort(customerTypes);
		return customerTypes.get(0);
	}
	
	private void addTicketToCustomer(String username, String ticketId, int points) {
		Customer customer = customerDAO.read(username);
		
		if(customer == null) {
			ArrayList<String> tickets = new ArrayList<String>();
			customer = new Customer(username, tickets, 0, getInitialCustomerType());
		}
		
		customer.getTickets().add(ticketId);
		changeCustomersPoints(points, customer);
		customerDAO.update(customer);
	}
	
	
	
	private void changeCustomersPoints(int points, Customer customer) {
		List<CustomerType> customerTypes = customerDAO.getCustomerTypes();
		int currentPoints = customer.getPoints();
		int newPoints = currentPoints + points;
		customer.setPoints(newPoints);
		
		Collections.sort(customerTypes);
		int typePosition = customerTypes.indexOf(customer.getCustomerType());
		int typesSize = customerTypes.size();
		if(typesSize - typePosition == 1) {
			//onda je poslednji
		} else {
			//naredni elem
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
	
}
