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
	private CustomerDAO customerDAO;
	
	public CustomerService(CustomerDAO customerDAO) {
		this.customerDAO = customerDAO;
	}
	
	//????
	public Customer getCustomer(String username) {
		return customerDAO.getCustomer(username);
	}

	
	public void reserveTickets(ReservationDTO reservationDTO) {
		customerDAO.reserve(reservationDTO);
	}
}
