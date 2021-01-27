package services;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.User;
import dao.UserDAO;

@Path("/userservice")
public class UserService {
	@Context
	ServletContext ctx;
	
	public UserService() {}
	
	@PostConstruct
	public void init() {
		if(ctx.getAttribute("UserDAO") == null) {
			String contextPath = ctx.getRealPath("");
			ctx.setAttribute("UserDAO", new UserDAO(contextPath));
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
	@Path("/test-login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public User testlogin(@Context HttpServletRequest request) {
		return (User) request.getSession().getAttribute("user");
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
	
	@PUT
	@Path("/delete-user/{username}")
	public List<User> deleteUser(@PathParam("username") String username) {
		UserDAO userDao = (UserDAO) ctx.getAttribute("UserDAO");
		return userDao.deleteUser(username);
	}
	
	@PUT
	@Path("/retrieve-user/{username}")
	public List<User> retrieveUser(@PathParam("username") String username) {
		UserDAO userDao = (UserDAO) ctx.getAttribute("UserDAO");
		return userDao.retrieveUser(username);
	}
}
