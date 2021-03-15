package dao.interfaces;

import java.util.List;

import beans.Comment;

public interface ICommentDAO extends CRUD<Comment, String> {
	List<Comment> getAll();
	Comment retrieve(String id);
}
