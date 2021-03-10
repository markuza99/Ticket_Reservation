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
	private ManifestationDAO manifestationDAO;
	
	
	public ManifestationService(ManifestationDAO manifestationDAO) {
		this.manifestationDAO = manifestationDAO;
	}
	
	
	public List<Manifestation> getActiveManifestations() {
		return  manifestationDAO.getActiveManifestations();
	}
	
	public Manifestation getManifestation(String id) {
		return manifestationDAO.getManifestation(id);
	}
	
	public List<Manifestation> getSearchedManifestations(String name,String dateFrom,String dateTo,String place,
			int priceFrom,  int priceTo, String selected) throws ParseException {
		return manifestationDAO.search(name, dateFrom, dateTo, place, priceFrom, priceTo, selected);
	}
	
	public List<Manifestation> getFilterManifestations(String name, String dateFrom, String dateTo,
			String place,int priceFrom,int priceTo,String selected,String izborTipa, boolean nijeRasprodato) throws ParseException {
		return manifestationDAO.filter(name, dateFrom, dateTo, place, priceFrom, priceTo, selected,izborTipa, nijeRasprodato);
	}

	public Boolean updateManifestation(Manifestation manifestation) {
		return manifestationDAO.update(manifestation);
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
