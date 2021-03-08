package services;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import beans.Manifestation;
import dao.LocationDAO;
import dao.ManifestationDAO;
import dao.SellerDAO;


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
	
	public Manifestation addManifestation(String username, Manifestation manifestation) {
		SellerDAO sellerDAO = (SellerDAO) ctx.getAttribute("SellerDAO");
		return sellerDAO.add(manifestation, username);
	}
}
