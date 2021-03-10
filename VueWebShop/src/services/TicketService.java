package services;

import dao.TicketDAO;


public class TicketService {
	private TicketDAO ticketDAO ;
	
	public TicketService(TicketDAO ticketDAO) {
		this.ticketDAO = ticketDAO;
	}
	
	public Boolean userAttended(String username, String manifestationId) {
		return ticketDAO.userAttended(manifestationId, username);
	}
}
