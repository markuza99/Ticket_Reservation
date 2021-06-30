package dao.interfaces;

import java.util.List;

import beans.Ticket;

public interface ITicketDAO extends CRUD<Ticket, String>{
	List<Ticket> getAll();
	Ticket retrieve(String id);
	void writeToFile();
}
