package beans.value_objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import beans.Location;
import beans.Manifestation;
import beans.Ticket;

public class SortTickets {

	public SortTickets() {}
	
	public void sortByPrice(boolean ascending, List<Ticket> tickets) {
		Collections.sort(tickets, new Comparator<Ticket>() {
			@Override
			public int compare(Ticket o1, Ticket o2) {
				// TODO Auto-generated method stub
				if(ascending) {
					return (int) (o1.getPrice() - o2.getPrice());					
				} else {
					return (int) (o2.getPrice() - o1.getPrice());
				}
			}
		});
	}
	
	public List<Ticket> sortByManifestationAttribute(List<Ticket> tickets, List<Manifestation> manifestations) {
		List<Ticket> newTickets = new ArrayList<Ticket>();
		for(Manifestation m : manifestations) {
			for(Ticket t : tickets) {
				if(t.getManifestationId().equals(m.getId())) {
					newTickets.add(t);
				}
			}
		}
		return newTickets;
	}
}
