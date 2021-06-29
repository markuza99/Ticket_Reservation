package services;

import java.util.ArrayList;
import java.util.List;

import beans.Customer;
import beans.Manifestation;
import beans.Role;
import beans.Seller;
import beans.Ticket;
import beans.User;
import beans.value_objects.SortUsers;
import dao.interfaces.ICustomerDAO;
import dao.interfaces.ISellerDAO;
import dao.interfaces.ITicketDAO;
import dao.interfaces.IUserDAO;
import dto.SearchUsersDTO;
import dto.TicketRepresentationDTO;
import dto.UserForViewDTO;


public class UserService {
	private IUserDAO userDAO;
	private ISellerDAO sellerDAO;
	private ICustomerDAO customerDAO;
	private ITicketDAO ticketDAO;
	
	public UserService(IUserDAO userDAO, ISellerDAO sellerDAO, ICustomerDAO customerDAO, ITicketDAO ticketDAO) {
		this.userDAO = userDAO;
		this.sellerDAO = sellerDAO;
		this.customerDAO = customerDAO;
		this.ticketDAO = ticketDAO;
	}
	
	public User login(User user) {
		User existingUser = userDAO.read(user.getUsername());
		if(existingUser == null)
			return null;
		if (!user.getPassword().equals(existingUser.getPassword())) {
			return null;
		}
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
				if(m.equals(ticket.getManifestationId())) {
					User buyer = userDAO.read(ticket.getUser());
					if(!buyers.contains(buyer)) {
						buyers.add(buyer);
					}
				}
			}
		}
		return searchSortFilterUsers(buyers, searchUsersDTO);
	}
	
	public User deleteUser(String username) {
		return userDAO.delete(username);
	}
	
	public User retrieveUser(String username) {
		return userDAO.retrieve(username);
	}
	
//	public List<User> search(User u,String searchQuery, String dateFrom, String dateTo) {
//		List<User> searchedUsers = new ArrayList<>();
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//		LocalDate LdateFrom = null;
//		LocalDate LdateTo = null;
//		ArrayList<User> usrs = new ArrayList<User>();
//		if(!dateFrom.equals("")) {
//			LdateFrom = LocalDate.parse(dateFrom, formatter);;
//		}
//		if(!dateTo.equals("")) {
//			LdateTo = LocalDate.parse(dateTo, formatter);
//		}
//		if(u.getRole().equals(Role.ADMIN)) {
//			usrs = (ArrayList<User>) userDAO.getAll();
//		} else {
//			usrs = (ArrayList<User>) getUsersForList(u);
//		}
//		for(User user : usrs) {
//			if(correspondsSearch(user, searchQuery, LdateFrom, LdateTo)) {
//				searchedUsers.add(user);
//			}
//		}
//		return searchedUsers;
//	}
	
//	private boolean correspondsSearch(User user, String searchQuery, LocalDate dateFrom, LocalDate dateTo) {
//		boolean btext = searchQuery.trim() == "" ? true : (user.getUsername().contains(searchQuery) || user.getFirstName().contains(searchQuery)
//				|| user.getLastName().contains(searchQuery));
//		boolean bdateFrom = dateFrom == null ? true : user.getBirthDate().isAfter(dateFrom) || user.getBirthDate().isEqual(dateFrom);
//		boolean bdateTo = dateTo == null ? true : user.getBirthDate().isBefore(dateTo) || user.getBirthDate().isEqual(dateTo);
//		return btext && bdateFrom && bdateTo;
//	}
	
//	public List<User> filter(User u,String searchQuery, String dateFrom, String dateTo, String role, String userStatus) {
//		List<User> searchedUsers = search(u,searchQuery, dateFrom, dateTo);
//		List<User> filteredUsers = new ArrayList<>();
//		for(User user : searchedUsers) {
//			if(correspondsFilter(user, role, userStatus)) {
//				filteredUsers.add(user);
//			}
//		}
//		return filteredUsers;
//	}
	
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
	
//	@PUT
//	@Path("/")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	public User createItem(@Context HttpServletRequest request, User user) {
//		User u = (User) request.getSession().getAttribute("user");
//		UserDAO userDao = (UserDAO) ctx.getAttribute("UserDAO");
//		User updatedUser = userDao.updateUser(user, u.getUsername());
//		if(updatedUser==null) {
//			return null;
//		}
//		TicketDAO TicketDao = (TicketDAO) ctx.getAttribute("TicketDAO");
//		TicketDao.updateTicket(u.getUsername(), updatedUser.getUsername());
//		CustomerDAO CustomerDao = (CustomerDAO) ctx.getAttribute("CustomerDAO");
//		CustomerDao.updateCustomer(u.getUsername(), updatedUser.getUsername());
//		request.getSession().setAttribute("user", updatedUser);
//		return updatedUser;
//	}
}
