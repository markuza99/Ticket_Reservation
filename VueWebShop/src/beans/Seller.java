package beans;

import java.util.ArrayList;

public class Seller extends User {
	private String username;
	private ArrayList<String> manifestations;
	
	public Seller(String username, ArrayList<String> manifestations) {
		this.username = username;
		this.manifestations = manifestations;
	}

	public Seller() {}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public ArrayList<String> getManifestations() {
		return manifestations;
	}

	public void setManifestations(ArrayList<String> manifestations) {
		this.manifestations = manifestations;
	}
	
	
}
