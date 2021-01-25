package beans;

import java.util.ArrayList;

public class Customer {
	private String username;
	private ArrayList<Ticket> tickets;
	private int points;
	private CustomerType customerType;
	
	public Customer(String username, ArrayList<Ticket> tickets, int points, CustomerType customerType) {
		this.username = username;
		this.tickets = tickets;
		this.points = points;
		this.customerType = customerType;
	}
	
	public Customer() {}

	public ArrayList<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(ArrayList<Ticket> tickets) {
		this.tickets = tickets;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public CustomerType getCustomerType() {
		return customerType;
	}

	public void setCustomerType(CustomerType customerType) {
		this.customerType = customerType;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString() + "tickets: \n" + tickets;
	}

	
	
}
