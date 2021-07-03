package services;

import beans.Customer;
import dao.interfaces.ICustomerDAO;


public class CustomerService {
	private ICustomerDAO customerDAO;
	
	public CustomerService(ICustomerDAO customerDAO) {
		this.customerDAO = customerDAO;
	}

	public Customer getCustomer(String username) {
		return customerDAO.read(username);
	}

}
