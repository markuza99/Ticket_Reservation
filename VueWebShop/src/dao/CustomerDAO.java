package dao;

import java.io.BufferedReader; 
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import beans.Customer;
import beans.CustomerType;
import dao.interfaces.ICustomerDAO;

public class CustomerDAO implements ICustomerDAO {
	private Map<String, Customer> customers = new HashMap<>();
	private List<CustomerType> customerTypes = new ArrayList<>();
	private String contextPath;
	
	public CustomerDAO(String contextPath) {
		this.contextPath = contextPath;
		loadCustomerTypes();
		loadCustomers();
	}
	
	@Override
	public Customer create(Customer customer) {
		if(read(customer.getUsername()) != null) {
            return null;
		}
		customers.put(customer.getUsername(), customer);
		appendToFile(customerCSVRepresentation(customer));
		return customer;
	}

	@Override
	public Customer read(String username) {
		return customers.get(username);
	}

	@Override
	public Customer update(Customer customer) {
		customers.put(customer.getUsername(), customer);
		writeToFile();
		return customer;
	}
	
	@Override
	public Customer delete(String username) {
		return null;
	}

	@Override
	public List<Customer> getAll() {
		return new ArrayList<Customer>(customers.values());
	}
	
	@Override
	public Customer retrieve(String username) {
		return null;
	}
	
	public CustomerType getCustomerType(String id) {
		for(CustomerType ct : customerTypes) {
			if(ct.getTypeName().equals(id)) {
				return ct;
			}
		}
		return null;
	}
	
	public List<CustomerType> getCustomerTypes() {
		return customerTypes;
	}

	public void setCustomerTypes(List<CustomerType> customerTypes) {
		this.customerTypes = customerTypes;
	}

	public String customerCSVRepresentation(Customer customer) {
		StringBuilder customerString = new StringBuilder(); 
		customerString.append(customer.getUsername() + ";");
		if(customer.getTickets().size() == 0) {
			customerString.append(" ;");
		} else {
			for(String id : customer.getTickets()) {
				customerString.append(id + ":");
			}
			customerString.append(";");
		}
		
		String type = customer.getCustomerType().getTypeName();
		customerString.append(customer.getPoints() + ";" + type);
        return customerString.toString();
	}
	
	public void appendToFile(String line) {
		File file = new File(contextPath + "/repositories/customers.txt");

        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            	pw.println(line);
            
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(pw != null) {
                try {
                    pw.close();
                }
                catch (Exception e) {}
            }
        }
	}
	
	private void writeToFile() {
        File file = new File(contextPath + "/repositories/customers.txt");

        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            for(Customer customer : customers.values()) {
            	pw.println(customerCSVRepresentation(customer));
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(pw != null) {
                try {
                    pw.close();
                }
                catch (Exception e) {}
            }
        }
    }
	
	private void loadCustomerTypes() {
		BufferedReader reader = null;
		try {
			File file = new File(contextPath + "/repositories/customerTypes.txt");
			reader = new BufferedReader(new FileReader(file));
			String line;
			StringTokenizer st;
			while((line = reader.readLine()) != null) {
				line = line.trim();
				if(line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while(st.hasMoreTokens()) {
					
					String typeName = st.nextToken().trim();
					double discount = Double.parseDouble(st.nextToken().trim());
					int points = Integer.parseInt(st.nextToken().trim());
					
					customerTypes.add(new CustomerType(typeName,discount, points));
					
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			if(reader != null) {
				try {
					reader.close();
				}
				catch (Exception e) {}
			}
		}
	}
	
	private void loadCustomers() {
		BufferedReader reader = null;
		try {
			File file = new File(contextPath + "/repositories/customers.txt");
			reader = new BufferedReader(new FileReader(file));
			String line;
			StringTokenizer st;
			while((line = reader.readLine()) != null) {
				line = line.trim();
				if(line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while(st.hasMoreTokens()) {
					ArrayList<String> ticketsArray = new ArrayList<String>();
					String username = st.nextToken().trim();
					String tickets = st.nextToken().trim();
					if(!tickets.equals("")) {
						StringTokenizer st2 = new StringTokenizer(tickets, ":");
						while(st2.hasMoreTokens()) {
							String ticketId = st2.nextToken().trim();
							ticketsArray.add(ticketId);
						}
					}
					
					int points = Integer.parseInt(st.nextToken().trim());
					String customerTypeName = st.nextToken().trim();
					
					CustomerType customerType = getCustomerType(customerTypeName);
					customers.put(username, new Customer(username,ticketsArray, points, customerType));
					
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			if(reader != null) {
				try {
					reader.close();
				}
				catch (Exception e) {}
			}
		}
		
	}

}
