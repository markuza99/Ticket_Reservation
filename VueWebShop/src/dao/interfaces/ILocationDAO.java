package dao.interfaces;

import java.util.List;

import beans.Location;

public interface ILocationDAO extends CRUD<Location, String>{
	List<Location> getAll();
	Location retrieve(String id);
}
