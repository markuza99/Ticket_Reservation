package services;

import java.time.LocalDateTime;
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
	
	public Ticket reserveTickets(ReservationDTO reservationDTO, String username) {
		Manifestation manifestation = manifestationDAO.read(reservationDTO.manifestationId);
		if(manifestation.getRemainingNumberOfSeats() < reservationDTO.numberOfTickets) {
			return null;
		}
		manifestation.setRemainingNumberOfSeats(manifestation.getRemainingNumberOfSeats() - reservationDTO.numberOfTickets);
		manifestationDAO.update(manifestation);
		
		String ticketId = generateRandomId();
		while(ticketDAO.read(ticketId) != null) {
			ticketId = generateRandomId();
		}
		Ticket ticket = ticketDAO.create(new Ticket(ticketId,reservationDTO.manifestationId,LocalDateTime.now(),reservationDTO.ticketPrice,
				username,TicketStatus.RESERVED,TicketType.valueOf(reservationDTO.ticketType), false));
		
		Customer customer = customerDAO.read(username);
		customer.getTickets().add(ticketId);
		changeCustomersPoints(reservationDTO.points, customer);
		customerDAO.update(customer);
		
		return ticket;
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
}
