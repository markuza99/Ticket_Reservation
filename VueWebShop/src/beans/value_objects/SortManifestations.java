package beans.value_objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import beans.Location;
import beans.Manifestation;

public class SortManifestations {
//	public List<Manifestation> manifestations;
//	public List<Location> locations;
	
	public SortManifestations() {
//		this.manifestations = manifestations;
	}
	
//	public SortManifestations(List<Manifestation> manifestations, List<Location> locations) {
//		this.manifestations = manifestations;
//		this.locations = locations;
//	}
	
	public void sortByPrice(boolean ascending, List<Manifestation> manifestations) {
		Collections.sort(manifestations, new Comparator<Manifestation>() {
			@Override
			public int compare(Manifestation o1, Manifestation o2) {
				// TODO Auto-generated method stub
				if(ascending) {
					return o1.getTicketPrice() - o2.getTicketPrice();					
				} else {
					return o2.getTicketPrice() - o1.getTicketPrice();
				}
			}
		});
	}
	
	public void sortByName(boolean ascending, List<Manifestation> manifestations) {
		Collections.sort(manifestations, new Comparator<Manifestation>() {

			@Override
			public int compare(Manifestation o1, Manifestation o2) {
				// TODO Auto-generated method stub
				if(ascending) {
					return o1.getName().compareTo(o2.getName());
				} else {
					return o2.getName().compareTo(o1.getName());
				}
			}
		});
	}
	
	public void sortLocations(boolean ascending, List<Location> locations) {
		Collections.sort(locations, new Comparator<Location>() {

			@Override
			public int compare(Location o1, Location o2) {
				if(ascending) {
					return o1.getCity().compareTo(o2.getCity());
				} else {
					return o2.getCity().compareTo(o1.getCity());
				}
			}
			
		});
	}
	
	public List<Manifestation> sortByLocation(List<Manifestation> manifestations, List<Location> locations) {
		List<Manifestation> newMan = new ArrayList<Manifestation>();
		for(Location l : locations) {
			for(Manifestation m : manifestations) {
				if(m.getLocation().equals(l.getId())) {
					newMan.add(m);
				}
			}
		}
		return newMan;
	}
	
	public void sortByDate(boolean ascending, List<Manifestation> manifestations) {
		Collections.sort(manifestations, new Comparator<Manifestation>() {

			@Override
			public int compare(Manifestation o1, Manifestation o2) {
				// TODO Auto-generated method stub
				if(ascending) {
					return o1.getStartTime().compareTo(o2.getStartTime());
				} else {
					return o2.getStartTime().compareTo(o1.getStartTime());
				}
			}
		});
	}
}
