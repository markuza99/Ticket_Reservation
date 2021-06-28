package dao.interfaces;

import java.util.List;

import beans.Seller;

public interface ISellerDAO extends CRUD<Seller, String> {
	List<Seller> getAll();
	Seller retrieve(String id);
	Seller getSellerForManifestation(String id);
}
