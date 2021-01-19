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
	@Path("/getcontextpath")
	@Produces(MediaType.TEXT_PLAIN)
	public String getContextPath() {
		return ctx.getRealPath("");
	}
	
	@GET
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Manifestation> getSearchManifestations(@QueryParam("naziv") String naziv, @QueryParam("datumOdMan") String datOd, @QueryParam("datumDoMan") String datDo, @QueryParam("mestoMan") String mesto, @QueryParam("cenaOdMan") String cenaOd, @QueryParam("cenaDoMan") String cenaDo) throws ParseException {
		ManifestationDAO manifestationDao = (ManifestationDAO) ctx.getAttribute("ManifestationDAO");
		
		manifestationDao.search(naziv, datOd, datDo, mesto, cenaOd, cenaDo);
		List<Manifestation> manSearch = new ArrayList<Manifestation>();
		if(naziv != null && !naziv.trim().isEmpty()) {
			for (Manifestation m : manifestationDao.getAllManifestations()) {
				if(m.getName().toLowerCase().startsWith(naziv.toLowerCase())) {
					manSearch.add(m);
				}
			}
		} else {
			manSearch = (ArrayList<Manifestation>) manifestationDao.getAllManifestations();
		}
		if(datOd != null && !datOd.trim().isEmpty()) {
			

		}
		
		if(datDo != null && !datDo.trim().isEmpty()) {
			System.out.println("OVO  JE DATDO  " + datDo);
			String splitArgs[] = datDo.split("\\-");
			int yy = Integer.parseInt(splitArgs[0]);
			int mm = Integer.parseInt(splitArgs[1]);
			int dd = Integer.parseInt(splitArgs[2]);
			ListIterator<Manifestation> iter = manSearch.listIterator();
			while(iter.hasNext()){
//				System.out.println(iter.next().getDate().getYear() + "@@" + yy);
//				System.out.println(iter.next().getDate().getMonthValue() + "@@" + mm);
//				System.out.println(iter.next().getDate().getDayOfMonth() + "@@" + dd);
//				System.out.println("DUZINA NIZA JE " + manSearch.size());
//				System.out.println("============================");
			    if(iter.next().getDate().getYear() <= yy){
			        iter.remove();
			    } else {
			    	if(iter.next().getDate().getMonthValue() <= mm) {
			    		iter.remove();
			    	} else {
			    		if(iter.next().getDate().getDayOfMonth() <= dd) {
			    			iter.remove();
			    		}
			    	}
			    }
			}
			
		}

		if(mesto != null && !mesto.trim().isEmpty()) {
			ListIterator<Manifestation> iter = manSearch.listIterator();
			while(iter.hasNext()){
			    if(!iter.next().getLocation().getCity().toLowerCase().startsWith(mesto)) {
			    	iter.remove();
			    }
			}
		}
		
		if(cenaOd != null && !cenaOd.trim().isEmpty()) {
			ListIterator<Manifestation> iter = manSearch.listIterator();
			while(iter.hasNext()){
			    if(iter.next().getTicketPrice() <= Integer.parseInt(cenaOd.trim())) {
			    	iter.remove();
			    }
			}
		}
		
		if(cenaDo != null && !cenaDo.trim().isEmpty()) {
			ListIterator<Manifestation> iter = manSearch.listIterator();
			while(iter.hasNext()){
			    if(iter.next().getTicketPrice() >= Integer.parseInt(cenaDo.trim())) {
			    	iter.remove();
			    }
			}
		}
		return manSearch;
	}
}
