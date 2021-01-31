package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import beans.Customer;
import beans.CustomerType;
import beans.Manifestation;
import beans.Ticket;
import beans.TicketStatus;
import beans.TicketType;
import dto.ReservationDTO;

public class CustomerDAO {
	private Map<String, Customer> customers = new HashMap<>();
	private List<CustomerType> customerTypes = new ArrayList<>();
	private String contextPath;
	private TicketDAO ticketDAO;
	private ManifestationDAO manifestationDAO;
	
	public CustomerDAO(String contextPath, TicketDAO ticketDAO, ManifestationDAO manifestationDAO) {
		this.contextPath = contextPath;
		this.ticketDAO = ticketDAO;
		this.manifestationDAO = manifestationDAO;
		loadCustomerTypes();
		Collections.sort(customerTypes);
		loadCustomers();
	}
	
	public Map<String, Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(Map<String, Customer> customers) {
		this.customers = customers;
	}

	public CustomerType getCustomerType(String id) {
		for(CustomerType ct : customerTypes) {
			if(ct.getTypeName().equals(id)) {
				return ct;
			}
		}
		return null;
	}
	
	public Customer getCustomer(String id) {
		return customers.get(id);
	}
	
	private void changeCustomersPoints(ReservationDTO reservationDTO, Customer customer) {
		int currentPoints = customer.getPoints();
		int newPoints = currentPoints+reservationDTO.points;
		customer.setPoints(newPoints);
		
		int typePosition = customerTypes.indexOf(customer.getCustomerType());
		int typesSize = customerTypes.size();
		if(typesSize - typePosition == 1) {
			//onda je poslednji
		} else {
			//naredni elem
			while(true) {
				CustomerType newType = customerTypes.get(typePosition + 1);
				if(newPoints > newType.getPoints())
					customer.setCustomerType(newType);
				else {
					break;
				}
				typePosition++;
				if(typesSize - typePosition == 1)
					break;
			}
			
		}
	}
	
	public void reserve(ReservationDTO reservationDTO) {
		Customer customer = customers.get(reservationDTO.user);
		Ticket ticket = ticketDAO.addTicket(reservationDTO);
		
		customer.getTickets().add(ticket);
		
		manifestationDAO.reduceNumberOfSeats(reservationDTO);
		
		changeCustomersPoints(reservationDTO, customer);
		
		write();
	}
	
	public String getCustomerLine(Customer customer) {
		StringBuilder customerString = new StringBuilder(); 
		customerString.append(customer.getUsername() + ";");
		if(customer.getTickets().size() == 0) {
			customerString.append(" ;");
		} else {
			for(Ticket t : customer.getTickets()) {
				customerString.append(t.getId() + ":");
			}
			customerString.append(";");
		}
		
		String type = customer.getCustomerType().getTypeName();
		customerString.append(customer.getPoints() + ";" + type);
        return customerString.toString();
	}
	
	public void append(String line) {
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
	
	private void write() {
        File file = new File(contextPath + "/repositories/customers.txt");

        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            for(Customer customer : customers.values()) {
            	pw.println(getCustomerLine(customer));
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
					ArrayList<Ticket> ticketsArray = new ArrayList<Ticket>();
					String username = st.nextToken().trim();
					String tickets = st.nextToken().trim();
					if(!tickets.equals("")) {
						StringTokenizer st2 = new StringTokenizer(tickets, ":");
						while(st2.hasMoreTokens()) {
							String ticketId = st2.nextToken().trim();
							Ticket ticket = ticketDAO.getOneTicket(ticketId);
							ticketsArray.add(ticket);
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
	public void updateCustomer(String oldUser, String newUser) {
		Customer cust = customers.get(oldUser);
		if(cust != null) {
			customers.remove(oldUser);
			cust.setUsername(newUser);
			customers.put(newUser, cust);
			write();
		}
		
	}
}
