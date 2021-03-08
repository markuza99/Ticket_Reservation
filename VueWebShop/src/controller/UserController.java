package controller;

import java.util.List;

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
import services.UserService;

@Path("/users")
public class UserController {
	private UserService userService = new UserService();
	
	
	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public User login(@Context HttpServletRequest request, User user) {
		
		User loggedUser = userService.login(user);
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
	public boolean logOut(@Context HttpServletRequest request) {
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
	public User registerUser(User user) {
		return userService.registerUser(user);
	}
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> allUsers() {
		return userService.allUsers();
	}
	
	@DELETE
	@Path("/{username}")
	public List<User> deleteUser(@PathParam("username") String username) {
		return userService.deleteUser(username);
	}
	
	@PUT
	@Path("/retrieve/{username}")
	public List<User> retrieveUser(@PathParam("username") String username) {
		return userService.retrieveUser(username);
	}
	
	@GET
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> search(@QueryParam("searchQuery") String searchQuery, @QueryParam("dateFrom") String dateFrom, @QueryParam("dateTo") String dateTo) {
		return userService.search(searchQuery, dateFrom, dateTo);
	}
	
	@GET
	@Path("/filter")
	public List<User> filter(@QueryParam("searchQuery") String searchQuery, @QueryParam("dateFrom") String dateFrom,
			@QueryParam("dateTo") String dateTo, @QueryParam("role") String role,
			@QueryParam("userStatus") String userStatus) {
		return userService.filter(searchQuery, dateFrom, dateTo, role, userStatus);
	}
}
