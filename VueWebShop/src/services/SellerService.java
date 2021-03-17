package services;

import dao.interfaces.ISellerDAO;


public class SellerService {
	private ISellerDAO sellerDAO;
	
	public SellerService(ISellerDAO sellerDAO) {
		this.sellerDAO = sellerDAO;
	}
	

//	public Manifestation addManifestation(String username, Manifestation manifestation) {
//		return sellerDAO.add(manifestation, username);
//	}
}
