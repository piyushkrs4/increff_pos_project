package com.increff.pos.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public abstract class AbstractDao {
	@PersistenceContext
	private EntityManager em;
	protected <T> T getSingle(TypedQuery<T> query) {
		return query.getResultList().stream().findFirst().orElse(null);
	}

	protected <T> TypedQuery<T> getQuery(String jpql, Class<T> clazz) {
		return em.createQuery(jpql, clazz);
	}

	protected EntityManager em() {
		return em;
	}
	public <T> void insert(T p){
		em.persist(p);
	}
	public <T> T select(Integer id, Class<T> clazz) {
		String select_id = "select p from " + clazz.getName() + " p where id=:id";
		TypedQuery<T> query = getQuery(select_id, clazz);
		query.setParameter("id", id);
		return getSingle(query);
	}

	public <T> List<T> selectAll(Class<T> clazz) {
		String select_id = "select p from " + clazz.getName() + " p";
		TypedQuery<T> query = getQuery(select_id, clazz);
		return query.getResultList();
	}
	public <T> Integer delete(Integer id, Class<T> clazz) {
		String delete_id = "delete from " + clazz.getName() + " p where id=:id";
		Query query = em.createQuery(delete_id);
		query.setParameter("id", id);
		return query.executeUpdate();
	}

}
