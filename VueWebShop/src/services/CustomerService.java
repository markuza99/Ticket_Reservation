package services;

import beans.Customer;
import dao.interfaces.ICustomerDAO;


public class CustomerService {
	private ICustomerDAO customerDAO;
	
	public CustomerService(ICustomerDAO customerDAO) {
		this.customerDAO = customerDAO;
	}
	
	//????
//	public Customer getCustomer(String username) {
//		return customerDAO.read(username);
//	}

	
//	public void reserveTickets(ReservationDTO reservationDTO) {
//		customerDAO.reserve(reservationDTO);
		
//		Customer customer = customerDAO.read(reservationDTO.user);
//		Ticket ticket = ticketDAO.addTicket(reservationDTO);
//		
//		customer.getTickets().add(ticket);
//		
//		manifestationDAO.reduceNumberOfSeats(reservationDTO.manifestation,reservationDTO.numberOfTickets);
//		//prvo se skida broj karata pa se tek onda porucuje
//		
//		changeCustomersPoints(reservationDTO, customer);
//		
//		write();
		
//	}
}
