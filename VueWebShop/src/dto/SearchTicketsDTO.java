package dto;

public class SearchTicketsDTO {
	public String manifestationName;
	public double priceFrom;
	public double priceTo;
	public String dateFrom;
	public String dateTo;
	public String sortBy;
	public String ticketType;
	public String ticketStatus;
	
	public SearchTicketsDTO(String manifestationName, double priceFrom, double priceTo, String dateFrom, String dateTo,
			String sortBy, String ticketType, String ticketStatus) {
		this.manifestationName = manifestationName;
		this.priceFrom = priceFrom;
		this.priceTo = priceTo;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		this.sortBy = sortBy;
		this.ticketType = ticketType;
		this.ticketStatus = ticketStatus;
	}
	
	public SearchTicketsDTO() {}

	public String getManifestationName() {
		return manifestationName;
	}

	public void setManifestationName(String manifestationName) {
		this.manifestationName = manifestationName;
	}

	public double getPriceFrom() {
		return priceFrom;
	}

	public void setPriceFrom(double priceFrom) {
		this.priceFrom = priceFrom;
	}

	public double getPriceTo() {
		return priceTo;
	}

	public void setPriceTo(double priceTo) {
		this.priceTo = priceTo;
	}

	public String getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}

	public String getDateTo() {
		return dateTo;
	}

	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public String getTicketType() {
		return ticketType;
	}

	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}

	public String getTicketStatus() {
		return ticketStatus;
	}

	public void setTicketStatus(String ticketStatus) {
		this.ticketStatus = ticketStatus;
	}

}
