package controller;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Manifestation;
import beans.User;
import services.SellerService;

@Path("/sellers")
public class SellerController {
	private SellerService sellerService = new SellerService();
	
	@POST
	@Path("/add-manifestation")
	@Consumes(MediaType.APPLICATION_JSON)
	public Manifestation addManifestation(@Context HttpServletRequest request, Manifestation manifestation) {
		User user = (User) request.getSession().getAttribute("user");
		return sellerService.addManifestation(user.getUsername(), manifestation);
	}
}
