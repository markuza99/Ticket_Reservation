package dao.interfaces;

import java.util.List;

import beans.User;

public interface IUserDAO extends CRUD<User,String>{
	List<User> getAll();
	User retrieve(String id);
}
