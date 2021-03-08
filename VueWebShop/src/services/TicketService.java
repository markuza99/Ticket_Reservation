package services;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;

import dao.TicketDAO;


public class TicketService {
	@Context
	ServletContext ctx;
	
	@PostConstruct
	public void init() {
		String contextPath = ctx.getRealPath("");
		if(ctx.getAttribute("TicketDAO") == null) {
			ctx.setAttribute("TicketDAO", new TicketDAO(contextPath));
		}
	}
	
	public Boolean userAttended(String username, String manifestationId) {
		TicketDAO ticketDAO = (TicketDAO) ctx.getAttribute("TicketDAO");
		return ticketDAO.userAttended(manifestationId, username);
	}
}
