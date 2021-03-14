package dao.interfaces;

import java.util.List;

import beans.Customer;

public interface ICustomerDAO extends CRUD<Customer, String> {
	List<Customer> getAll();
	Customer retrieve(String id);
}
