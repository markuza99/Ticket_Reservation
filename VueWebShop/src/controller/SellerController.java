package controller;

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
import services.SellerService;

@Path("/sellers")
public class SellerController {
	@Context
	ServletContext ctx;
	private SellerService sellerService;
	
	@PostConstruct
	public void init() {
		String contextPath = ctx.getRealPath("");
		if(ctx.getAttribute("LocationDAO") == null) {
			ctx.setAttribute("LocationDAO", new LocationDAO(contextPath));
		}
		if(ctx.getAttribute("ManifestationDAO") == null) {
			System.out.println(contextPath);
			ctx.setAttribute("ManifestationDAO", new ManifestationDAO(contextPath));
		}
		if(ctx.getAttribute("SellerDAO") == null) {
			ctx.setAttribute("SellerDAO", new SellerDAO(contextPath));
		}
		sellerService = new SellerService((SellerDAO) ctx.getAttribute("SellerDAO"));
	}
	
}
