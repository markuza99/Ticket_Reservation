package dto;

public class ReservationDTO {
	public int points;
	public String manifestationId;
	public int numberOfTickets;
	public double ticketPrice;
	public String ticketType;

	public int getPoints() {
		return points;
	}

	public String getTicketType() {
		return ticketType;
	}

	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public String getManifestationId() {
		return manifestationId;
	}

	public void setManifestationId(String manifestationId) {
		this.manifestationId = manifestationId;
	}

	public int getNumberOfTickets() {
		return numberOfTickets;
	}

	public void setNumberOfTickets(int numberOfTickets) {
		this.numberOfTickets = numberOfTickets;
	}

	public double getTicketPrice() {
		return ticketPrice;
	}

	public void setTicketPrice(double ticketPrice) {
		this.ticketPrice = ticketPrice;
	}
	
	
}
