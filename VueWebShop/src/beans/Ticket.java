package beans;

import java.time.LocalDateTime;

public class Ticket {
	private String id;
	private String manifestationId;
	private LocalDateTime dateTime;
	private double price;
	private String user;
	private TicketStatus ticketStatus;
	private TicketType ticketType;
	private int numberOfTickets;
	private boolean isDeleted;
	
	public Ticket() {}
	
	public Ticket(String id, String manifestationId, LocalDateTime dateTime, double price, String user,
			TicketStatus ticketStatus, TicketType ticketType, int numberOfTickets, boolean isDeleted) {
		this.id = id;
		this.manifestationId = manifestationId;
		this.dateTime = dateTime;
		this.price = price;
		this.user = user;
		this.ticketStatus = ticketStatus;
		this.ticketType = ticketType;
		this.numberOfTickets = numberOfTickets;
		this.isDeleted = isDeleted;
	}

	
	public int getNumberOfTickets() {
		return numberOfTickets;
	}

	public void setNumberOfTickets(int numberOfTickets) {
		this.numberOfTickets = numberOfTickets;
	}

	public boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted.equals("1") ? true : false;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getManifestationId() {
		return manifestationId;
	}

	public void setManifestationId(String manifestationId) {
		this.manifestationId = manifestationId;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public TicketStatus getTicketStatus() {
		return ticketStatus;
	}

	public void setTicketStatus(TicketStatus ticketStatus) {
		this.ticketStatus = ticketStatus;
	}

	public TicketType getTicketType() {
		return ticketType;
	}

	public void setTicketType(String ticketType) {
		this.ticketType = TicketType.valueOf(ticketType);
	}
	
	
}
