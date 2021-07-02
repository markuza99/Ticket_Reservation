package services;

import java.util.ArrayList;
import java.util.List;

import beans.Comment;
import beans.Customer;
import beans.Manifestation;
import beans.Role;
import beans.Seller;
import beans.Ticket;
import beans.TicketStatus;
import beans.User;
import beans.value_objects.SortUsers;
import dao.interfaces.ICommentDAO;
import dao.interfaces.ICustomerDAO;
import dao.interfaces.IManifestationDAO;
import dao.interfaces.ISellerDAO;
import dao.interfaces.ITicketDAO;
import dao.interfaces.IUserDAO;
import dto.SearchUsersDTO;
import dto.UserDTO;
import dto.UserForViewDTO;


public class UserService {
	private IUserDAO userDAO;
	private ISellerDAO sellerDAO;
	private ICustomerDAO customerDAO;
	private ITicketDAO ticketDAO;
	private ICommentDAO commentDAO;
	private IManifestationDAO manifestationDAO;
	
	public UserService(IUserDAO userDAO, ISellerDAO sellerDAO, ICustomerDAO customerDAO, ITicketDAO ticketDAO,
			ICommentDAO commentDAO, IManifestationDAO manifestationDAO) {
		this.userDAO = userDAO;
		this.sellerDAO = sellerDAO;
		this.customerDAO = customerDAO;
		this.ticketDAO = ticketDAO;
		this.commentDAO = commentDAO;
		this.manifestationDAO = manifestationDAO;
	}
	
	public User login(User user) {
		User existingUser = userDAO.read(user.getUsername());
		if(existingUser == null)
			return null;
		if (!user.getPassword().equals(existingUser.getPassword())) {
			return null;
		}
		if(existingUser.getIsDeleted()) return null;
		return existingUser;
	}
	public User registerUser(User user) {
        User u = userDAO.create(user);
        if(u == null) {
        	return null;
        }
        
        return u;
	}
	
	private List<UserForViewDTO> searchSortFilterUsers(List<User> users,
			SearchUsersDTO searchUsersDTO) {
		List<User> searchedUsers = searchUsers(users, searchUsersDTO);
		
		if(searchUsersDTO.sortBy == null) searchUsersDTO.sortBy = "";
		if(searchUsersDTO.type == null) searchUsersDTO.type = "";
		if(searchUsersDTO.role == null) searchUsersDTO.role = "";
		
		searchedUsers =  sortUsers(searchedUsers, searchUsersDTO.sortBy);
		
		List<User> filteredUsers = filterUsers(searchedUsers, searchUsersDTO.type, searchUsersDTO.role);
		
		List<UserForViewDTO> usersDTO = new ArrayList<UserForViewDTO>();
		for(User u : filteredUsers) {
			int points = 0;
			String type = "NO_TYPE";
			if(u.getRole() == Role.CUSTOMER) {
				type = "regularni";
				Customer customer = customerDAO.read(u.getUsername());
				if(customer != null) {
					points = customer.getPoints();
					type = customer.getCustomerType().getTypeName();
				}
			}
			usersDTO.add(new UserForViewDTO(u, points, type));
		}
		
		return usersDTO;
	}
	
	private List<User> filterUsers(List<User> users, String type, String role) {
		List<User> filteredUsers = new ArrayList<User>();
		
		for(User u : users) {
			if(correspondsFilter(u, type, role)) {
				filteredUsers.add(u);
			}
		}
		
		return filteredUsers;
	}

	private List<User> sortUsers(List<User> users, String sortBy) {
		SortUsers sort = new SortUsers(); 
		switch(sortBy) {
		case "firstNameAsc":
			sort.sortByFirstName(true, users);
			break;
		case "firstNameDesc":
			sort.sortByFirstName(false, users);
			break;
		case "lastNameAsc":
			sort.sortByLastName(true, users);
			break;
		case "lastNameDesc":
			sort.sortByLastName(false, users);
			break;
		case "usernameAsc":
			sort.sortByUsername(true, users);
			break;
		case "usernameDesc":
			sort.sortByUsername(false, users);
			break;
		case "pointsAsc":
			sort.sortByPoints(true, users, customerDAO);
			break;
		case "pointsDesc":
			sort.sortByPoints(false, users, customerDAO);
			break;
		}
		return users;
	}

	private List<User> searchUsers(List<User> users, SearchUsersDTO searchUsersDTO) {
		List<User> searchedUsers = new ArrayList<User>();
		
		for(User u : users) {
			if(correspondsSearch(u, searchUsersDTO.searchQuery)) {
				searchedUsers.add(u);
			}
		}
		
		return searchedUsers;
	}

	private boolean correspondsSearch(User u, String searchQuery) {
		if(searchQuery == null) return true;
		boolean bname = u.getFirstName().toLowerCase().contains(searchQuery.toLowerCase());
		boolean bsurname = u.getLastName().toLowerCase().contains(searchQuery.toLowerCase());
		boolean busername = u.getUsername().toLowerCase().contains(searchQuery.toLowerCase());
		return bname || bsurname || busername;
	}

	public List<UserForViewDTO> getAllUsers(SearchUsersDTO searchUsersDTO) {
		List<User> users = userDAO.getAll();
		return searchSortFilterUsers(users, searchUsersDTO);
	}
	
	public List<UserForViewDTO> getUsersForSeller(String username, SearchUsersDTO searchUsersDTO) {
		ArrayList<User> buyers = new ArrayList<User>();
		Seller seller = sellerDAO.read(username);
		ArrayList<Ticket> tickets = (ArrayList<Ticket>) ticketDAO.getAll();
		for (String m : seller.getManifestations()) {
			for (Ticket ticket : tickets) {
				if(ticket.getIsDeleted() || ticket.getTicketStatus() == TicketStatus.CANCELED) continue;
				if(m.equals(ticket.getManifestationId())) {
					User buyer = userDAO.read(ticket.getUser());
					if(buyer.getIsDeleted()) continue;
					if(!buyers.contains(buyer)) {
						buyers.add(buyer);
					}
				}
			}
		}
		return searchSortFilterUsers(buyers, searchUsersDTO);
	}
	
	public User deleteUser(String username) {
		User user = userDAO.read(username);
		if(user.getRole() == Role.CUSTOMER) {
			deleteCustomerTickets(username);
			deleteCustomerComments(username);
		} else if(user.getRole() == Role.SELLER) {
			deleteSellerManifestations(username);
		}
		return userDAO.delete(username);
	}
	
	private void deleteSellerManifestations(String username) {
		Seller seller = sellerDAO.read(username);
		if(seller == null) return;
		for(String manifestationId : seller.getManifestations()) {
			Manifestation manifestation = manifestationDAO.read(manifestationId);
			if(manifestation.getIsDeleted()) continue;
			manifestation.setIsDeleted("1");
			manifestationDAO.update(manifestation);
		}
	}

	private void deleteCustomerComments(String username) {
		for(Comment comment : commentDAO.getAll()) {
			if(comment.getUser().equals(username)) {
				if(comment.getIsDeleted()) continue;
				comment.setIsDeleted("1");
				commentDAO.update(comment);
			}
			
		}
	}

	private void deleteCustomerTickets(String username) {
		Customer customer = customerDAO.read(username);
		if(customer == null) return;
		for(String tickedId : customer.getTickets()) {
			Ticket ticket = ticketDAO.read(tickedId);
			if(ticket.getIsDeleted()) continue;
			ticket.setIsDeleted("1");
			ticketDAO.update(ticket);
		}
	}
	
	public User retrieveUser(String username) {
		User user = userDAO.read(username);
		if(user.getRole() == Role.CUSTOMER) {
			retrieveCustomerTickets(username);
			retrieveCustomerComments(username);
		} else if(user.getRole() == Role.SELLER) {
			retrieveSellerManifestations(username);
		}
		return userDAO.retrieve(username);
	}

	
	private void retrieveSellerManifestations(String username) {
		Seller seller = sellerDAO.read(username);
		if(seller == null) return;
		for(String manifestationId : seller.getManifestations()) {
			Manifestation manifestation = manifestationDAO.read(manifestationId);
			if(!manifestation.getIsDeleted()) continue;
			manifestation.setIsDeleted("0");
			manifestationDAO.update(manifestation);
		}
	}

	private void retrieveCustomerComments(String username) {
		for(Comment comment : commentDAO.getAll()) {
			if(comment.getUser().equals(username)) {
				if(!comment.getIsDeleted()) continue;
				comment.setIsDeleted("0");
				commentDAO.update(comment);
			}
			
		}
	}

	private void retrieveCustomerTickets(String username) {
		Customer customer = customerDAO.read(username);
		if(customer == null) return;
		for(String tickedId : customer.getTickets()) {
			Ticket ticket = ticketDAO.read(tickedId);
			if(!ticket.getIsDeleted()) continue;
			ticket.setIsDeleted("0");
			ticketDAO.update(ticket);
		}
	}

	private boolean correspondsFilter(User user, String type, String role) {
		boolean buserRole = role.equals("all") || role.equals("") ? true : (user.getRole() == Role.valueOf(role));
		boolean buserType = false;
		
		if(type.equals("") || type.equals("all")) buserType = true;
		else {
			if(user.getRole() == Role.CUSTOMER) {
				Customer customer = customerDAO.read(user.getUsername());
				if(customer == null) {
					if(type.equals("regularni")) buserType = true;
				} else {
					if(customer.getCustomerType().getTypeName().equals(type) || type.equals("")) {
						buserType = true;
					}
				}
			}
		}
		
		return buserType && buserRole;
	}

	public void updateUser(String username, UserDTO userDTO) {
		User userToUpdate = userDAO.read(username);
		userToUpdate.setFirstName(userDTO.firstName);
		userToUpdate.setLastName(userDTO.lastName);
		userToUpdate.setPassword(userDTO.password);
		userToUpdate.setBirthDate(userDTO.date);
		userToUpdate.setGender(userDTO.gender);
		userDAO.update(userToUpdate);
	}
	
}
