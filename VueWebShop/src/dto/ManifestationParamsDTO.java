package dto;

public class ManifestationParamsDTO {
	public String name;
	public String place;
	public int priceFrom;
	public int priceTo;
	public String dateFrom;
	public String dateTo;
	public String sortBy;
	public String type;
	public String status;
	public String ticketCondition;
	
	public ManifestationParamsDTO(String name, String place, int priceFrom, int priceTo, String dateFrom,
			String dateTo, String sortBy, String type, String status, String ticketCondition) {
		this.name = name;
		this.place = place;
		this.priceFrom = priceFrom;
		this.priceTo = priceTo;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		this.sortBy = sortBy;
		this.type = type;
		this.status = status;
		this.ticketCondition = ticketCondition;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public int getPriceFrom() {
		return priceFrom;
	}

	public void setPriceFrom(int priceFrom) {
		this.priceFrom = priceFrom;
	}

	public int getPriceTo() {
		return priceTo;
	}

	public void setPriceTo(int priceTo) {
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTicketCondition() {
		return ticketCondition;
	}

	public void setTicketCondition(String ticketCondition) {
		this.ticketCondition = ticketCondition;
	}
	
	
}
