package services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import beans.Role;
import beans.User;
import dao.interfaces.IUserDAO;


public class UserService {
	private IUserDAO userDAO;
	
	public UserService(IUserDAO userDAO) {
		this.userDAO = userDAO;
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
	
	public List<User> getAllUsers() {
		return userDAO.getAll();
	}
	
	public User deleteUser(String username) {
		return userDAO.delete(username);
	}
	
	public User retrieveUser(String username) {
		return userDAO.retrieve(username);
	}
	
	public List<User> search(String searchQuery, String dateFrom, String dateTo) {
		List<User> searchedUsers = new ArrayList<>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate LdateFrom = null;
		LocalDate LdateTo = null;
		
		if(!dateFrom.equals("")) {
			LdateFrom = LocalDate.parse(dateFrom, formatter);;
		}
		if(!dateTo.equals("")) {
			LdateTo = LocalDate.parse(dateTo, formatter);
		}
		
		for(User user : userDAO.getAll()) {
			if(correspondsSearch(user, searchQuery, LdateFrom, LdateTo)) {
				searchedUsers.add(user);
			}
		}
		return searchedUsers;
	}
	
	private boolean correspondsSearch(User user, String searchQuery, LocalDate dateFrom, LocalDate dateTo) {
		boolean btext = searchQuery.trim() == "" ? true : (user.getUsername().contains(searchQuery) || user.getFirstName().contains(searchQuery)
				|| user.getLastName().contains(searchQuery));
		boolean bdateFrom = dateFrom == null ? true : user.getBirthDate().isAfter(dateFrom) || user.getBirthDate().isEqual(dateFrom);
		boolean bdateTo = dateTo == null ? true : user.getBirthDate().isBefore(dateTo) || user.getBirthDate().isEqual(dateTo);
		return btext && bdateFrom && bdateTo;
	}
	
	public List<User> filter(String searchQuery, String dateFrom, String dateTo, String role, String userStatus) {
		List<User> searchedUsers = search(searchQuery, dateFrom, dateTo);
		List<User> filteredUsers = new ArrayList<>();
		for(User user : searchedUsers) {
			if(correspondsFilter(user, role, userStatus)) {
				filteredUsers.add(user);
			}
		}
		return filteredUsers;
	}
	
	private boolean correspondsFilter(User user, String role, String userStatus) {
		boolean buserType = role.equals("Svi") ? true : (user.getRole() == Role.valueOf(role));
		String deleted = "0";
		if(user.getIsDeleted()) {
			deleted = "1";
		}
		boolean buserStatus = userStatus.equals("Svi") ? true : userStatus.equals(deleted);
		return buserType && buserStatus;
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
