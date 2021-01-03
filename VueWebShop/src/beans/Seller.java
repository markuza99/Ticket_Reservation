package beans;

import java.util.ArrayList;

public class Seller extends User {
	private ArrayList<Manifestation> manifestations;

	public Seller(ArrayList<Manifestation> manifestations) {
		this.manifestations = manifestations;
	}

	public ArrayList<Manifestation> getManifestations() {
		return manifestations;
	}

	public void setManifestations(ArrayList<Manifestation> manifestations) {
		this.manifestations = manifestations;
	}
	
	
}
