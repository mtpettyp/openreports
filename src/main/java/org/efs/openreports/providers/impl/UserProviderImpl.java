/*
 * Copyright (C) 2002 Erik Swenson - erik@oreports.com
 * 
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 *  
 */

package org.efs.openreports.providers.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.efs.openreports.objects.ReportGroup;
import org.efs.openreports.objects.ReportUser;
import org.efs.openreports.providers.HibernateProvider;
import org.efs.openreports.providers.ProviderException;
import org.efs.openreports.providers.UserProvider;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class UserProviderImpl implements UserProvider
{
	protected static Logger log =
		Logger.getLogger(UserProviderImpl.class.getName());
	
	private HibernateProvider hibernateProvider;

	public UserProviderImpl(HibernateProvider hibernateProvider) throws ProviderException
	{
		this.hibernateProvider = hibernateProvider;

		log.info("UserProviderImpl created");
	}

	@SuppressWarnings("unchecked")
	public ReportUser getUser(String name, String password) throws ProviderException
	{
		try
		{
			Session session = hibernateProvider.openSession();			
			
			try
			{			
				List<ReportUser> list = session.createQuery(
						"from org.efs.openreports.objects.ReportUser as user "
								+ "where user.name = ? and user.password = ?").setString(0, name).setString(1, password).list();					

				if (list.size() == 0)
					return null;

				ReportUser user = list.get(0);				

				return user;
			}
			catch (HibernateException he)
			{			
				throw he;
			}
			finally
			{
				session.close();
			}
		}
		catch (HibernateException he)
		{
			throw new ProviderException(he);
		}
	}

	public ReportUser getUser(Integer id) throws ProviderException
	{
		return (ReportUser) hibernateProvider.load(ReportUser.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<ReportUser> getUsers() throws ProviderException
	{
		String fromClause =
			"from org.efs.openreports.objects.ReportUser reportUser order by reportUser.name ";

		return (List<ReportUser>) hibernateProvider.query(fromClause);
	}

	public ReportUser insertUser(ReportUser user) throws ProviderException
	{
		return (ReportUser) hibernateProvider.save(user);
	}

	public void updateUser(ReportUser user) throws ProviderException
	{
		hibernateProvider.update(user);
	}

	public void deleteUser(ReportUser user) throws ProviderException
	{
		Session session = hibernateProvider.openSession();
		Transaction tx = null;

		try
		{
			tx = session.beginTransaction();
			
			//delete user			
			session.delete(user);
			
			//delete report log entries for user
			session
					.createQuery(
							"DELETE org.efs.openreports.objects.ReportLog reportLog where reportLog.user.id = ? ")
					.setInteger(0, user.getId().intValue()).executeUpdate();					
			
			tx.commit();
		}
		catch (HibernateException he)
		{
			hibernateProvider.rollbackTransaction(tx);			

			throw new ProviderException(he.getMessage());
		}
		finally
		{
			hibernateProvider.closeSession(session);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ReportUser> getUsersForGroup(ReportGroup reportGroup) throws ProviderException
	{
		try
		{
			Session session = hibernateProvider.openSession();			
			
			try
			{			
				List<ReportUser> list = session.createQuery(
						"from org.efs.openreports.objects.ReportUser as reportUser "
								+ "where ? in elements(reportUser.groups)").setEntity(0, reportGroup).list();					

				if (list.size() == 0) return null;						

				return list;
			}
			catch (HibernateException he)
			{			
				throw he;
			}
			finally
			{
				session.close();
			}
		}
		catch (HibernateException he)
		{
			throw new ProviderException(he);
		}
	}
}