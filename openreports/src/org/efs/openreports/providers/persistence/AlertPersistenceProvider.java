/*
 * Copyright (C) 2006 Erik Swenson - erik@oreports.com
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

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import org.efs.openreports.objects.ReportAlert;
import org.efs.openreports.objects.ReportLog;
import org.efs.openreports.providers.HibernateProvider;
import org.efs.openreports.providers.ProviderException;
import org.efs.openreports.util.LocalStrings;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AlertPersistenceProvider 
{
	protected static Logger log =
		Logger.getLogger(AlertPersistenceProvider.class.getName());

	public AlertPersistenceProvider() throws ProviderException
	{
		super();

		log.info("Created");
	}

	public ReportAlert getReportAlert(Integer id)
		throws ProviderException
	{
		return (ReportAlert) HibernateProvider.load(ReportAlert.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<ReportAlert> getReportAlerts() throws ProviderException
	{
		String fromClause =
			"from org.efs.openreports.objects.ReportAlert reportAlert order by reportAlert.name ";
		
		return (List<ReportAlert>) HibernateProvider.query(fromClause);
	}

	public ReportAlert insertReportAlert(ReportAlert reportAlert)
		throws ProviderException
	{
		return (ReportAlert) HibernateProvider.save(reportAlert);
	}

	public void updateReportAlert(ReportAlert reportAlert)
		throws ProviderException
	{
		HibernateProvider.update(reportAlert);
	}

	public void deleteReportAlert(ReportAlert reportAlert)
		throws ProviderException
	{
		Session session = HibernateProvider.openSession();
		Transaction tx = null;

		try
		{
			tx = session.beginTransaction();
				
			//delete alert			
			session.delete(reportAlert);		
				
			//delete report log entries for alert
			Iterator<?> iterator =  session
				.createQuery(
					"from  org.efs.openreports.objects.ReportLog reportLog where reportLog.alert.id = ? ")
				.setInteger(0, reportAlert.getId().intValue()).iterate();
						
			while(iterator.hasNext())
			{
				ReportLog reportLog = (ReportLog) iterator.next();		 	
				session.delete(reportLog);
			}								 
			
			tx.commit();
		}
		catch (HibernateException he)
		{
			HibernateProvider.rollbackTransaction(tx);
						
			if (he.getCause() != null && he.getCause().getMessage() != null && he.getCause().getMessage().toUpperCase().indexOf("CONSTRAINT") > 0)
			{
				throw new ProviderException(LocalStrings.ERROR_ALERT_DELETION);
			}
				
			log.error("deleteReportAlert", he);			
			throw new ProviderException(LocalStrings.ERROR_SERVERSIDE);
		}
		finally
		{
			HibernateProvider.closeSession(session);
		}				
	}
}