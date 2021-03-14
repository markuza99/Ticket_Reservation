package dao.interfaces;

import java.util.List;

import beans.Customer;
import beans.CustomerType;
import beans.Ticket;

public interface ICustomerDAO extends CRUD<Customer, String> {
	List<Customer> getAll();
	Customer retrieve(String id);
	List<CustomerType> getCustomerTypes();
	void setCustomerTypes(List<CustomerType> customerTypes);
}
