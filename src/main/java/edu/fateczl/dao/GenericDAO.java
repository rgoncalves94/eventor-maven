package edu.fateczl.dao;

public interface GenericDAO <T> {
	
	public void persist(T object);
	
	public void refresh(T object);
	
	public void merge(T object);
	
	public void remove(T object);
	
	public T find(Class<T> clazz, long id);
}
