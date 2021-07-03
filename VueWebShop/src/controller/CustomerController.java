package controller;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Customer;
import beans.User;
import dao.CustomerDAO;
import dao.LocationDAO;
import dao.ManifestationDAO;
import dao.TicketDAO;
import services.CustomerService;

@Path("/customers")
public class CustomerController {
	@Context
	ServletContext ctx;
	private CustomerService customerService;
	
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
			ctx.setAttribute("ManifestationDAO", new ManifestationDAO(contextPath));
		}
		if(ctx.getAttribute("CustomerDAO") == null) {
			ctx.setAttribute("CustomerDAO", new CustomerDAO(contextPath));
		}
		customerService = new CustomerService((CustomerDAO) ctx.getAttribute("CustomerDAO"));
	}
	
	@GET
	@Path("/me")
	@Produces(MediaType.APPLICATION_JSON)
	public Customer getCustomer(@Context HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		return customerService.getCustomer(user.getUsername());
	}

}
