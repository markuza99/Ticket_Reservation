package beans;

import java.util.ArrayList;

public class Seller {
	private String username;
	private ArrayList<Manifestation> manifestations;
	
	public Seller(String username, ArrayList<Manifestation> manifestations) {
		this.username = username;
		this.manifestations = manifestations;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Seller() {}

	public ArrayList<Manifestation> getManifestations() {
		return manifestations;
	}

	public void setManifestations(ArrayList<Manifestation> manifestations) {
		this.manifestations = manifestations;
	}
	
	
}
