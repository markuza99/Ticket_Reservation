package beans.value_objects;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import beans.Customer;
import beans.Role;
import beans.User;
import dao.interfaces.ICustomerDAO;


public class SortUsers {
	
	public void sortByUsername(boolean ascending, List<User> users) {
		Collections.sort(users, new Comparator<User>() {
			@Override
			public int compare(User o1, User o2) {
				if(ascending) {
					return o1.getUsername().compareTo(o2.getUsername());				
				} else {
					return o2.getUsername().compareTo(o1.getUsername());
				}
			}
		});
	}
	
	public void sortByFirstName(boolean ascending, List<User> users) {
		Collections.sort(users, new Comparator<User>() {
			@Override
			public int compare(User o1, User o2) {
				if(ascending) {
					return o1.getFirstName().compareTo(o2.getFirstName());				
				} else {
					return o2.getFirstName().compareTo(o1.getFirstName());
				}
			}
		});
	}
	
	public void sortByLastName(boolean ascending, List<User> users) {
		Collections.sort(users, new Comparator<User>() {
			@Override
			public int compare(User o1, User o2) {
				if(ascending) {
					return o1.getLastName().compareTo(o2.getLastName());				
				} else {
					return o2.getLastName().compareTo(o1.getLastName());
				}
			}
		});
	}
	
	public void sortByPoints(boolean ascending, List<User> users, ICustomerDAO customerDAO) {
		Collections.sort(users, new Comparator<User>() {
			@Override
			public int compare(User o1, User o2) {
				int pointsO1 = 0;
				if(o1.getRole() == Role.CUSTOMER) {
					Customer customer = customerDAO.read(o1.getUsername());
					if(customer != null)
						pointsO1 = customer.getPoints();
				}
				int pointsO2 = 0;
				if(o2.getRole() == Role.CUSTOMER) {
					Customer customer = customerDAO.read(o2.getUsername());
					if(customer != null)
						pointsO2 = customer.getPoints();
				}
				if(ascending) {
					return pointsO1 - pointsO2;					
				} else {
					return pointsO2 - pointsO1;
				}
			}
		});
	}
}
