package services;

import java.text.ParseException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;

import beans.Manifestation;
import dao.CommentDAO;
import dao.CustomerDAO;
import dao.LocationDAO;
import dao.ManifestationDAO;
import dao.SellerDAO;
import dao.TicketDAO;
import dao.UserDAO;


public class ManifestationService {
	@Context
	ServletContext ctx;
	
	@PostConstruct
	public void init() {
		String contextPath = ctx.getRealPath("");
		
		if(ctx.getAttribute("ManifestationDAO") == null) {
			LocationDAO locationDAO = (LocationDAO) ctx.getAttribute("LocationDAO");
			System.out.println(contextPath);
			ctx.setAttribute("ManifestationDAO", new ManifestationDAO(contextPath, locationDAO));
		}
		if(ctx.getAttribute("SellerDAO") == null) {
			ManifestationDAO manifestationDAO = (ManifestationDAO) ctx.getAttribute("ManifestationDAO");
			ctx.setAttribute("SellerDAO", new SellerDAO(contextPath, manifestationDAO));
		}
		if(ctx.getAttribute("TicketDAO") == null) {
			ctx.setAttribute("TicketDAO", new TicketDAO(contextPath));
		}
		if(ctx.getAttribute("CustomerDAO") == null) {
			TicketDAO ticketDAO = (TicketDAO) ctx.getAttribute("TicketDAO");
			ManifestationDAO manifestationDAO = (ManifestationDAO) ctx.getAttribute("ManifestationDAO");
			ctx.setAttribute("CustomerDAO", 
			new CustomerDAO(contextPath, ticketDAO, manifestationDAO));
		}
		
		if(ctx.getAttribute("UserDAO") == null) {
			CustomerDAO customerDAO = (CustomerDAO) ctx.getAttribute("CustomerDAO");
			SellerDAO sellerDAO = (SellerDAO) ctx.getAttribute("SellerDAO");
			ctx.setAttribute("UserDAO", new UserDAO(contextPath, customerDAO, sellerDAO));
		}
		if(ctx.getAttribute("CommentDAO") == null) {
			SellerDAO sellerDAO = (SellerDAO) ctx.getAttribute("SellerDAO");
			ctx.setAttribute("CommentDAO", new CommentDAO(contextPath, sellerDAO));
		}
	}
	
	public List<Manifestation> getActiveManifestations() {
		ManifestationDAO manifestationDao = (ManifestationDAO) ctx.getAttribute("ManifestationDAO");
		return  manifestationDao.getActiveManifestations();
	}
	
	public Manifestation getManifestation(String id) {
		ManifestationDAO manifestationDao = (ManifestationDAO) ctx.getAttribute("ManifestationDAO");
		return manifestationDao.getManifestation(id);
	}
	
	public List<Manifestation> getSearchedManifestations(String name,String dateFrom,String dateTo,String place,
			int priceFrom,  int priceTo, String selected) throws ParseException {
		ManifestationDAO manifestationDao = (ManifestationDAO) ctx.getAttribute("ManifestationDAO");
		return manifestationDao.search(name, dateFrom, dateTo, place, priceFrom, priceTo, selected);
	}
	
	public List<Manifestation> getFilterManifestations(String name, String dateFrom, String dateTo,
			String place,int priceFrom,int priceTo,String selected,String izborTipa, boolean nijeRasprodato) throws ParseException {
		ManifestationDAO manifestationDao = (ManifestationDAO) ctx.getAttribute("ManifestationDAO");
		return manifestationDao.filter(name, dateFrom, dateTo, place, priceFrom, priceTo, selected,izborTipa, nijeRasprodato);
	}

	public Boolean updateManifestation(Manifestation manifestation) {
		ManifestationDAO manifestationDAO = (ManifestationDAO) ctx.getAttribute("ManifestationDAO");
		if(manifestationDAO.update(manifestation)) {
			return true;
		}
		return false;
	}
	
	
	
//	@POST
//	@Path("/upload")
//	@Consumes(MediaType.MULTIPART_FORM_DATA)
//	@Produces(MediaType.TEXT_PLAIN)
//	public String upload(@FormParam("file") InputStream file, @FormParam("fileName") String fileName) throws IOException {
//		String contextPath = ctx.getRealPath("");
////		System.out.println(file);
////		System.out.println(fileName);
//		return null;
//	}
}
