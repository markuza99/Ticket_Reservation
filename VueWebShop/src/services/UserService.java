package services;

import java.util.List; 

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import beans.User;
import dao.CustomerDAO;
import dao.LocationDAO;
import dao.ManifestationDAO;
import dao.SellerDAO;
import dao.TicketDAO;
import dao.UserDAO;


public class UserService {
	@Context
	ServletContext ctx;
	
	public UserService() {}
	
	@PostConstruct
	public void init() {
		String contextPath = ctx.getRealPath("");
		if(ctx.getAttribute("TicketDAO") == null) {
			ctx.setAttribute("TicketDAO", new TicketDAO(contextPath));
		}
		if(ctx.getAttribute("LocationDAO") == null) {
			ctx.setAttribute("LocationDAO", new LocationDAO(contextPath));
		}
		if(ctx.getAttribute("ManifestationDAO") == null) {
			LocationDAO locationDAO = (LocationDAO) ctx.getAttribute("LocationDAO");
			ctx.setAttribute("ManifestationDAO",
			new ManifestationDAO(contextPath, locationDAO));
		}
		if(ctx.getAttribute("CustomerDAO") == null) {
			TicketDAO ticketDAO = (TicketDAO) ctx.getAttribute("TicketDAO");
			ManifestationDAO manifestationDAO = (ManifestationDAO) ctx.getAttribute("ManifestationDAO");
			ctx.setAttribute("CustomerDAO", 
			new CustomerDAO(contextPath, ticketDAO, manifestationDAO));
		}
		if(ctx.getAttribute("SellerDAO") == null) {
			ManifestationDAO manifestationDAO = (ManifestationDAO) ctx.getAttribute("ManifestationDAO");
			ctx.setAttribute("SellerDAO", new SellerDAO(contextPath, manifestationDAO));
		}
		if(ctx.getAttribute("UserDAO") == null) {
			CustomerDAO customerDAO = (CustomerDAO) ctx.getAttribute("CustomerDAO");
			SellerDAO sellerDAO = (SellerDAO) ctx.getAttribute("SellerDAO");
			ctx.setAttribute("UserDAO", new UserDAO(contextPath, customerDAO, sellerDAO));
		}
	}
	
	public User login(User user) {
		UserDAO userDao = (UserDAO) ctx.getAttribute("UserDAO");
		User loggedUser = userDao.find(user.getUsername(), user.getPassword());
		
		return loggedUser;
	}
	
	public User registerUser(User user) {
		UserDAO userDao = (UserDAO) ctx.getAttribute("UserDAO");
		User u = userDao.registration(user);
		return u;
	}
	
	public List<User> allUsers() {
		UserDAO userDao = (UserDAO) ctx.getAttribute("UserDAO");
		return userDao.getAllUsers();
	}
	
	public List<User> deleteUser(String username) {
		UserDAO userDao = (UserDAO) ctx.getAttribute("UserDAO");
		return userDao.deleteUser(username);
	}
	
	public List<User> retrieveUser(String username) {
		UserDAO userDao = (UserDAO) ctx.getAttribute("UserDAO");
		return userDao.retrieveUser(username);
	}
	
	public List<User> search(String searchQuery, String dateFrom, String dateTo) {
		UserDAO userDao = (UserDAO) ctx.getAttribute("UserDAO");
		return userDao.search(searchQuery, dateFrom, dateTo);
	}
	
	public List<User> filter(String searchQuery, String dateFrom, String dateTo, String role, String userStatus) {
		UserDAO userDao = (UserDAO) ctx.getAttribute("UserDAO");
		List<User> searchedUsers = userDao.search(searchQuery, dateFrom, dateTo);
		return userDao.filter(searchedUsers, role, userStatus);
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
