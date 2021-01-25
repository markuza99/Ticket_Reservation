package dto;

public class ReservationDTO {
	public int points;
	public String manifestation;
	public int numberOfTickets;
	public String user;
	public double ticketPrice;
	
	public ReservationDTO() {}

	public ReservationDTO(int points, String manifestation, int numberOfTickets, String user
			,double ticketPrice) {
		this.points = points;
		this.manifestation = manifestation;
		this.numberOfTickets = numberOfTickets;
		this.user = user;
		this.ticketPrice = ticketPrice;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public String getManifestation() {
		return manifestation;
	}

	public void setManifestation(String manifestation) {
		this.manifestation = manifestation;
	}

	public int getNumberOfTickets() {
		return numberOfTickets;
	}

	public void setNumberOfTickets(int numberOfTickets) {
		this.numberOfTickets = numberOfTickets;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public double getTicketPrice() {
		return ticketPrice;
	}

	public void setTicketPrice(double ticketPrice) {
		this.ticketPrice = ticketPrice;
	}
	
	
}
