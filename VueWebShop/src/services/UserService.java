package services;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Role;
import beans.User;
import dao.CustomerDAO;
import dao.LocationDAO;
import dao.ManifestationDAO;
import dao.SellerDAO;
import dao.TicketDAO;
import dao.UserDAO;

@Path("/users")
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
	
	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public User login(@Context HttpServletRequest request, User user) {
		UserDAO userDao = (UserDAO) ctx.getAttribute("UserDAO");
		User loggedUser = userDao.find(user.getUsername(), user.getPassword());
		
		if(loggedUser != null) {
			request.getSession().setAttribute("user", loggedUser);
		}
		
		return loggedUser;
	}
	
	@GET
	@Path("/role")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Role getRole(@Context HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		if(user != null)
			return user.getRole();
		return null;
	}
	
	@GET
	@Path("/me")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public User getUser(@Context HttpServletRequest request) {
		return (User) request.getSession().getAttribute("user");
	}
	
	@POST
	@Path("/logout")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public boolean doLogOut(@Context HttpServletRequest request) {
		User u = null;
		u = (User) request.getSession().getAttribute("user");
		if(u != null) {
			request.getSession().invalidate();
		}
		return true;
	}
	
	
	@POST
	@Path("/register")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public User registerUser(@Context HttpServletRequest request, User user) {
		UserDAO userDao = (UserDAO) ctx.getAttribute("UserDAO");
		User u = userDao.registration(user);
		return u;
	}
	
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> allUsers() {
		UserDAO userDao = (UserDAO) ctx.getAttribute("UserDAO");
		return userDao.getAllUsers();
	}
	
	@DELETE
	@Path("/{username}")
	public List<User> deleteUser(@PathParam("username") String username) {
		UserDAO userDao = (UserDAO) ctx.getAttribute("UserDAO");
		return userDao.deleteUser(username);
	}
	
	@PUT
	@Path("/retrieve/{username}")
	public List<User> retrieveUser(@PathParam("username") String username) {
		UserDAO userDao = (UserDAO) ctx.getAttribute("UserDAO");
		return userDao.retrieveUser(username);
	}
	
	@GET
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> search(@QueryParam("searchQuery") String searchQuery, @QueryParam("dateFrom") String dateFrom, @QueryParam("dateTo") String dateTo) {
		UserDAO userDao = (UserDAO) ctx.getAttribute("UserDAO");
		return userDao.search(searchQuery, dateFrom, dateTo);
	}
	
	@GET
	@Path("/filter")
	public List<User> filter(@QueryParam("searchQuery") String searchQuery, @QueryParam("dateFrom") String dateFrom,
			@QueryParam("dateTo") String dateTo, @QueryParam("role") String role,
			@QueryParam("userStatus") String userStatus) {
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
