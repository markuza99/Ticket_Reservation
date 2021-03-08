package services;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;

import beans.Customer;
import dao.CustomerDAO;
import dao.LocationDAO;
import dao.ManifestationDAO;
import dao.TicketDAO;
import dto.ReservationDTO;


public class CustomerService {
	@Context
	ServletContext ctx;
	
	@PostConstruct
	public void init() {
		String contextPath = ctx.getRealPath("");
		if(ctx.getAttribute("TicketDAO") == null) {
			ctx.setAttribute("TicketDAO", new TicketDAO(contextPath));
		}
		if(ctx.getAttribute("LocationDAO") == null) {
			ctx.setAttribute("LocationDAO", new LocationDAO(contextPath));
		}
		if(ctx.getAttribute("ManifestationDAO") == null) {
			LocationDAO locationDAO = (LocationDAO) ctx.getAttribute("LocationDAO");
			ctx.setAttribute("ManifestationDAO", new ManifestationDAO(contextPath, locationDAO));
		}
		if(ctx.getAttribute("CustomerDAO") == null) {
			TicketDAO ticketDAO = (TicketDAO) ctx.getAttribute("TicketDAO");
			ManifestationDAO manifestationDAO = (ManifestationDAO) ctx.getAttribute("ManifestationDAO");
			ctx.setAttribute("CustomerDAO", new CustomerDAO(contextPath, ticketDAO, manifestationDAO));
		}
	}
	
	//????
	public Customer getCustomer(String username) {
		CustomerDAO customerDAO = (CustomerDAO) ctx.getAttribute("CustomerDAO");
		return customerDAO.getCustomer(username);
	}

	
	public void reserveTickets(ReservationDTO reservationDTO) {
		CustomerDAO customerDAO = (CustomerDAO) ctx.getAttribute("CustomerDAO");
		customerDAO.reserve(reservationDTO);
	}
}
