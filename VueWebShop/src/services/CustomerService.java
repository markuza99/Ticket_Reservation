package services;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Customer;
import beans.User;
import dao.CustomerDAO;
import dao.ManifestationDAO;
import dao.TicketDAO;
import dto.ReservationDTO;

@Path("/customerservice")
public class CustomerService {
	@Context
	ServletContext ctx;
	
	@PostConstruct
	public void init() {
		String contextPath = ctx.getRealPath("");
		if(ctx.getAttribute("TicketDAO") == null) {
			ctx.setAttribute("TicketDAO", new TicketDAO(contextPath));
		}
		if(ctx.getAttribute("ManifestationDAO") == null) {
			ctx.setAttribute("ManifestationDAO", new ManifestationDAO(contextPath));
		}
		if(ctx.getAttribute("CustomerDAO") == null) {
			TicketDAO ticketDAO = (TicketDAO) ctx.getAttribute("TicketDAO");
			ManifestationDAO manifestationDAO = (ManifestationDAO) ctx.getAttribute("ManifestationDAO");
			ctx.setAttribute("CustomerDAO", new CustomerDAO(contextPath, ticketDAO, manifestationDAO));
		}
	}
	
	@GET
	@Path("/get-customer")
	@Produces(MediaType.APPLICATION_JSON)
	public Customer getCustomer(@Context HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		CustomerDAO customerDAO = (CustomerDAO) ctx.getAttribute("CustomerDAO");
		return customerDAO.getCustomer(user.getUsername());
	}

	
	@POST
	@Path("/reserve-ticket")
	@Consumes(MediaType.APPLICATION_JSON)
	public void reserveTickets(ReservationDTO reservationDTO) {
		CustomerDAO customerDAO = (CustomerDAO) ctx.getAttribute("CustomerDAO");
		customerDAO.reserve(reservationDTO);
	}
}