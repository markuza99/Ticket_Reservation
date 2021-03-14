package services;

import java.time.LocalDateTime;

import beans.Customer;
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
	
	public Ticket reserveTickets(ReservationDTO reservationDTO) {
	//provera jel ima karata
		Customer customer = customerDAO.read(reservationDTO.user);
		String ticketId = generateRandomId();
		while(ticketDAO.read(ticketId) != null) {
			ticketId = generateRandomId();
		}
		Ticket ticket = ticketDAO.create(new Ticket(ticketId,reservationDTO.manifestation,LocalDateTime.now(),reservationDTO.ticketPrice,
				reservationDTO.user,TicketStatus.RESERVED,TicketType.FAN_PIT));
		
	//	customer.getTickets().add(ticket);
	//	
	//	manifestationDAO.reduceNumberOfSeats(reservationDTO.manifestation,reservationDTO.numberOfTickets);
	//	//prvo se skida broj karata pa se tek onda porucuje
	//	
	//	changeCustomersPoints(reservationDTO, customer);
	//	
	//	write();
		return ticket;
	}
}
