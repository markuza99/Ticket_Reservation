package services;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Manifestation;
import beans.User;
import dao.LocationDAO;
import dao.ManifestationDAO;
import dao.SellerDAO;

@Path("/sellerservice")
public class SellerService {
	@Context
	ServletContext ctx;
	
	@PostConstruct
	public void init() {
		String contextPath = ctx.getRealPath("");
		if(ctx.getAttribute("LocationDAO") == null) {
			ctx.setAttribute("LocationDAO", new LocationDAO(contextPath));
		}
		if(ctx.getAttribute("ManifestationDAO") == null) {
			LocationDAO locationDAO = (LocationDAO) ctx.getAttribute("LocationDAO");
			System.out.println(contextPath);
			ctx.setAttribute("ManifestationDAO", new ManifestationDAO(contextPath, locationDAO));
		}
		if(ctx.getAttribute("SellerDAO") == null) {
			ManifestationDAO manifestationDAO = (ManifestationDAO) ctx.getAttribute("ManifestationDAO");
			ctx.setAttribute("SellerDAO", new SellerDAO(contextPath, manifestationDAO));
		}
	}
	
	@POST
	@Path("/add-manifestation")
	@Consumes(MediaType.APPLICATION_JSON)
	public List<Manifestation> addManifestation(@Context HttpServletRequest request, Manifestation manifestation) {
		SellerDAO sellerDAO = (SellerDAO) ctx.getAttribute("SellerDAO");
		User user = (User) request.getSession().getAttribute("user");
		return sellerDAO.add(manifestation, user.getUsername());
	}
}
