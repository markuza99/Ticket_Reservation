package controller;

import java.text.ParseException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import beans.Manifestation;
import services.ManifestationService;

@Path("/manifestations")
public class ManifestationController {
	private ManifestationService manifestationService = new ManifestationService();
	
	@GET
	@Path("/active")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Manifestation> getActiveManifestations() {
		return manifestationService.getActiveManifestations();
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Manifestation getManifestation(@PathParam("id") String id) {
		return manifestationService.getManifestation(id);
	}
	
	@GET
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Manifestation> getSearchedManifestations(@QueryParam("name") String name, @QueryParam("dateFrom") String dateFrom, @QueryParam("dateTo") String dateTo, @QueryParam("place") String place, @QueryParam("priceFrom") int priceFrom, @QueryParam("priceTo") int priceTo, @QueryParam("selected") String selected) throws ParseException {
		return manifestationService.getSearchedManifestations(name, dateFrom, dateTo, place, priceFrom, priceTo, selected);
	}
	

	@GET
	@Path("/filter")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Manifestation> getFilterManifestations(@QueryParam("name") String name, @QueryParam("dateFrom") String dateFrom, @QueryParam("dateTo") String dateTo, @QueryParam("place") String place, @QueryParam("priceFrom") int priceFrom, @QueryParam("priceTo") int priceTo, @QueryParam("selected") String selected, @QueryParam("type") String izborTipa, @QueryParam("not_sold_out") boolean nijeRasprodato) throws ParseException {
		return manifestationService.getFilterManifestations(name, dateFrom, dateTo, place, priceFrom, priceTo, selected, izborTipa, nijeRasprodato);
	}

	@PUT
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Boolean updateManifestation(Manifestation manifestation) {
		return manifestationService.updateManifestation(manifestation);
	}
}
