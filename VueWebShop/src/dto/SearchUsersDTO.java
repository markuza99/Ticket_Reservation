package dto;


public class SearchUsersDTO {
	public String searchQuery; // ime prezime username
	public String role;
	public String type;
	public String sortBy; // ime prezime username br bodova
	
	public SearchUsersDTO() {}
	
	public SearchUsersDTO(String searchQuery, String role, String type, String sortBy) {
		this.searchQuery = searchQuery;
		this.role = role;
		this.type = type;
		this.sortBy = sortBy;
	}
	
	public String getSearchQuery() {
		return searchQuery;
	}
	public void setSearchQuery(String searchQuery) {
		this.searchQuery = searchQuery;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSortBy() {
		return sortBy;
	}
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
	

	
}
