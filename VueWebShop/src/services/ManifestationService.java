package services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Manifestation;
import dao.ManifestationDAO;

@Path("/manifestationservice")
public class ManifestationService {
	@Context
	ServletContext ctx;
	
	@PostConstruct
	public void init() {
		if(ctx.getAttribute("ManifestationDAO") == null) {
			String contextPath = ctx.getRealPath("");
			System.out.println(contextPath);
			ctx.setAttribute("ManifestationDAO", new ManifestationDAO(contextPath));
		}
	}
	
	@GET
	@Path("/getall")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Manifestation> getManifestations() {
		ManifestationDAO manifestationDao = (ManifestationDAO) ctx.getAttribute("ManifestationDAO");
		return  manifestationDao.getFirstNManifestations(10);
	}
	
	@GET
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Manifestation> getSearchManifestations(@QueryParam("name") String name, @QueryParam("dateFrom") String dateFrom, @QueryParam("dateTo") String dateTo, @QueryParam("place") String place, @QueryParam("priceFrom") int priceFrom, @QueryParam("priceTo") int priceTo, @QueryParam("selected") String selected, @QueryParam("izborTipa") String izborTipa) throws ParseException {
		ManifestationDAO manifestationDao = (ManifestationDAO) ctx.getAttribute("ManifestationDAO");
		
		return manifestationDao.search(name, dateFrom, dateTo, place, priceFrom, priceTo, selected, izborTipa);
	}
	
	@GET
	@Path("/searchSort")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Manifestation> getSearchSortManifestations(@QueryParam("sortByArg") String sortByArg) throws ParseException {
		ManifestationDAO manifestationDao = (ManifestationDAO) ctx.getAttribute("ManifestationDAO");
		return manifestationDao.sorting(sortByArg);

	}
}
