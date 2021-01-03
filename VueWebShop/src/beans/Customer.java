package beans;

import java.util.ArrayList;

public class Customer extends User {
	private ArrayList<Ticket> tickets;
	private int points;
	private CustomerType customerType;
	
	public Customer(ArrayList<Ticket> tickets, int points, CustomerType customerType) {
		this.tickets = tickets;
		this.points = points;
		this.customerType = customerType;
	}

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

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString() + "tickets: \n" + tickets;
	}
	
	
}
