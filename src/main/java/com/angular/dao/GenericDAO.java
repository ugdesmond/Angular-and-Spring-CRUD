package com.angular.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public abstract class  GenericDAO<E, K extends Serializable> { 
	
	
	 
	protected Class<? extends E> tclass;

	 
	@SuppressWarnings("unchecked")
	public GenericDAO() {
        Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        tclass = (Class<? extends E>) pt.getActualTypeArguments()[0];
    }
     

	
	@Autowired
	private SessionFactory sessionFactory;

	public Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	
	public  boolean create(  E t) {
		boolean msg = false;
		try {
			getCurrentSession().save(t);
			msg = true;
		} catch (Exception e) {
			e.printStackTrace();
			msg = false;
		}
		return msg;
	}


	public List<E> getAll() {
		List<E> objects = new ArrayList<>();
		try {
			Criteria cr = getCurrentSession().createCriteria(tclass);
			cr.addOrder(Order.asc("id"));
			objects = cr.list();
		} catch (HibernateException e) {

			e.printStackTrace();
		}
		return objects;
	}

	
	

	@SuppressWarnings("unchecked")
	public  E getById( K  id) {
		E object = null;
		try {
			object = (E) getCurrentSession().get(tclass.getName(), id);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return object;
	}
	
	public  boolean update( E  t) {
		boolean msg = false;
		try {
			getCurrentSession().update(t);
			msg = true;
		} catch (Exception e) {

			e.printStackTrace();
			msg = false;
		}
		return msg;
	}
	
	public  boolean delete( E  t) {
		Boolean msg = false;
		try {
			getCurrentSession().delete(t);
			msg = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}

	
	public  List<E> getByColName( String colName, Object value) {
		List<E> objects = new ArrayList<>();
		try {
			Criteria cr = getCurrentSession().createCriteria(tclass);
			cr.add(Restrictions.eq(colName, value));
			cr.addOrder(Order.asc("id"));
			objects = cr.list();
		} catch (HibernateException e) {

			e.printStackTrace();
		}
		return objects;

	}

}
