package services;

import beans.Manifestation;
import dao.SellerDAO;


public class SellerService {
	private SellerDAO sellerDAO;
	
	public SellerService(SellerDAO sellerDAO) {
		this.sellerDAO = sellerDAO;
	}
	

//	public Manifestation addManifestation(String username, Manifestation manifestation) {
//		return sellerDAO.add(manifestation, username);
//	}
}
