package dao.interfaces;

public interface CRUD<T,ID> {
	
	T create(T entity);
	T read(ID id);
	T update(T entity);
	T delete(ID id);

}
