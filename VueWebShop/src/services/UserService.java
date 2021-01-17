package services;


import java.time.LocalDateTime;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
		if(ctx.getAttribute("userDAO") == null) {
			String contextPath = ctx.getRealPath("");
			ctx.setAttribute("userDAO", new UserDAO(contextPath));
		}
	}
	
	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public User login(@Context HttpServletRequest request, User user) {
		UserDAO userDao = (UserDAO) ctx.getAttribute("userDAO");
		User loggedUser = userDao.find(user.getUsername(), user.getPassword());
		
		if(loggedUser != null) {
			request.getSession().setAttribute("user", loggedUser);
		}
		
		return loggedUser;
	}
	
	@GET
	@Path("/testlogin")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public User testlogin(@Context HttpServletRequest request) {
		return (User) request.getSession().getAttribute("user");
	}
	
<<<<<<< Updated upstream
	@POST
	@Path("/registration")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public User registration(@Context HttpServletRequest request, User user) {
		UserDAO userDao = (UserDAO) ctx.getAttribute("userDAO");
		return (User)userDao.registration(user);
	}
	
	@GET
    @Path("/testReg")
    @Produces(MediaType.TEXT_PLAIN)
    public String testReg() {
		LocalDateTime dt = LocalDateTime.now();
        return dt.toString();
    }
=======
	
	@POST
	@Path("/register")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public User registerUser(@Context HttpServletRequest request, User user) {
		UserDAO userDao = (UserDAO) ctx.getAttribute("userDAO");
		User u = userDao.registration(user);
		return u;
	}
	
>>>>>>> Stashed changes
}
