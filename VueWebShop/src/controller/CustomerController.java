package controller;

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
import dto.ReservationDTO;
import services.CustomerService;

@Path("/customers")
public class CustomerController {
	private CustomerService customerService = new CustomerService();
	
	//????
	@GET
	@Path("/get-customer")
	@Produces(MediaType.APPLICATION_JSON)
	public Customer getCustomer(@Context HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		return customerService.getCustomer(user.getUsername());
	}

	
	@POST
	@Path("/reserve-ticket")
	@Consumes(MediaType.APPLICATION_JSON)
	public void reserveTickets(ReservationDTO reservationDTO) {
		customerService.reserveTickets(reservationDTO);
	}
}
