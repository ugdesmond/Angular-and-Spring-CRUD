package com.angular.buisnesslogic;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import com.angular.dao.GenericDAO;
import com.angular.model.User;


@Component
public class UserLogic extends GenericDAO<User, Long> {
	
	public boolean checkIfUserExist(User user){
		
		Criteria cr = getCurrentSession().createCriteria(User.class);
		List<User> users = new ArrayList<>();
		Criterion userNam = Restrictions.eq("username", user.getUsername());
		try {	
			cr.add(userNam);
			cr.addOrder(Order.asc("id"));
			users = cr.list();
		} catch (HibernateException e){
	
			e.printStackTrace();
		}
		if(!users.isEmpty() && users != null ){
			return true;
			
		}
		return Boolean.FALSE;
		
		
	}

}
