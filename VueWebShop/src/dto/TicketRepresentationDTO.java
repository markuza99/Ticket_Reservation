package dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import beans.Manifestation;
import beans.Ticket;
import beans.TicketStatus;
import beans.TicketType;
import beans.User;

public class TicketRepresentationDTO {
	
	private String id;
	private String manifestationName;
	private String manifestationId;
	private double price;
	private String reservationDate;
	private String manifestationDate;
	private String user;
	private String status;
	private String type;
	private int numberOfTickets;
	private boolean manifestationPassed;
	private boolean deleted;
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	
	public TicketRepresentationDTO() {}
	
	public TicketRepresentationDTO(Ticket ticket, Manifestation manifestation, String username) {
		this.id = ticket.getId();
		this.manifestationName = manifestation.getName();
		this.price = ticket.getPrice();
		this.manifestationId = manifestation.getId();
		this.reservationDate = formatter.format(ticket.getDateTime());
		this.manifestationDate = formatter.format(manifestation.getStartTime());
		this.user = username;
		this.status = getTicketStatus(ticket.getTicketStatus());
		this.type = getTicketType(ticket.getTicketType());
		this.numberOfTickets = ticket.getNumberOfTickets();
		this.manifestationPassed = (manifestation.getStartTime().isBefore(LocalDateTime.now())) || ((int) ChronoUnit.DAYS.between(LocalDateTime.now(), manifestation.getStartTime())<7);
		this.deleted = ticket.getIsDeleted();
	}
	

	public String getManifestationId() {
		return manifestationId;
	}

	public void setManifestationId(String manifestationId) {
		this.manifestationId = manifestationId;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	private String getTicketStatus(TicketStatus status) {
		if(status == TicketStatus.RESERVED) return "Rezervisano";
		return "Otkazano";
	}
	
	private String getTicketType(TicketType type) {
		if(type == TicketType.VIP) return "Vip";
		else if(type == TicketType.REGULAR) return "Regularna";
		return "Fan pit";
	}
	
	public boolean isManifestationPassed() {
		return manifestationPassed;
	}

	public void setManifestationPassed(boolean manifestationPassed) {
		this.manifestationPassed = manifestationPassed;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getManifestationName() {
		return manifestationName;
	}
	public void setManifestationName(String manifestationName) {
		this.manifestationName = manifestationName;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getReservationDate() {
		return reservationDate;
	}
	public void setReservationDate(String reservationDate) {
		this.reservationDate = reservationDate;
	}
	public String getManifestationDate() {
		return manifestationDate;
	}
	public void setManifestationDate(String manifestationDate) {
		this.manifestationDate = manifestationDate;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getNumberOfTickets() {
		return numberOfTickets;
	}
	public void setNumberOfTickets(int numberOfTickets) {
		this.numberOfTickets = numberOfTickets;
	}
	
	
}
