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

package org.efs.openreports.providers.persistence;

import java.util.List;

import org.apache.log4j.Logger;
import org.efs.openreports.objects.ReportUser;
import org.efs.openreports.providers.HibernateProvider;
import org.efs.openreports.providers.ProviderException;

import org.hibernate.*;

public class UserPersistenceProvider 
{
	protected static Logger log =
		Logger.getLogger(UserPersistenceProvider.class.getName());	

	public UserPersistenceProvider() throws ProviderException
	{
		super();

		log.info("UserPersistenceProvider Created.");
	}

	public ReportUser getUser(String name) throws ProviderException
	{
		try
		{
			Session session = HibernateProvider.openSession();			
			
			try
			{			
				List list = session.createQuery(
						"from org.efs.openreports.objects.ReportUser as user "
								+ "where user.name = ?").setString(0, name).list();					

				if (list.size() == 0)
					return null;

				ReportUser user = (ReportUser) list.get(0);				

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
		return (ReportUser) HibernateProvider.load(ReportUser.class, id);
	}

	public List getUsers() throws ProviderException
	{
		String fromClause =
			"from org.efs.openreports.objects.ReportUser reportUser order by reportUser.name ";

		return HibernateProvider.query(fromClause);
	}

	public ReportUser insertUser(ReportUser user) throws ProviderException
	{
		return (ReportUser) HibernateProvider.save(user);
	}

	public void updateUser(ReportUser user) throws ProviderException
	{
		HibernateProvider.update(user);
	}

	public void deleteUser(ReportUser user) throws ProviderException
	{
		Session session = HibernateProvider.openSession();
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
			HibernateProvider.rollbackTransaction(tx);			

			throw new ProviderException(he.getMessage());
		}
		finally
		{
			HibernateProvider.closeSession(session);
		}
	}
}