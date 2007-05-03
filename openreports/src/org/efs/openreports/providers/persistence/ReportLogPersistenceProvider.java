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

import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.efs.openreports.objects.ReportLog;
import org.efs.openreports.providers.HibernateProvider;
import org.efs.openreports.providers.ProviderException;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Restrictions;

public class ReportLogPersistenceProvider 
{
	protected static Logger log =
		Logger.getLogger(ReportLogPersistenceProvider.class.getName());

	public ReportLogPersistenceProvider() throws ProviderException
	{
		super();

		log.info("ReportLogPersistenceProvider Created.");
	}

	public ReportLog getReportLog(Integer id) throws ProviderException
	{
		return (ReportLog) HibernateProvider.load(ReportLog.class, id);
	}

	public List getReportLogs(String status, Integer userId, Integer reportId, Integer alertId, int maxRows) throws ProviderException
	{
		Session session = HibernateProvider.openSession();		

		try
		{
			Criteria criteria = session.createCriteria(ReportLog.class);
			criteria.setMaxResults(maxRows);
			
			if (status != null)
			{
				criteria.add(Expression.eq("status", status));
			}
			
			if (userId != null)
			{
				criteria.add(Expression.eq("user.id", userId));
			}
			
			if (reportId != null)
			{
				if (reportId.intValue() == -1)
				{
					criteria.add(Restrictions.isNull("report"));
				}
				else
				{
					criteria.add(Expression.eq("report.id", reportId));
				}
			}
			
			if (alertId != null)
			{
				if (alertId.intValue() == -1)
				{
					criteria.add(Restrictions.isNull("alert"));
				}
				else
				{
					criteria.add(Expression.eq("alert.id", alertId));
				}
			}
			
			List list = criteria.list();		

			return list;
		}
		catch (HibernateException he)
		{			
			throw new ProviderException(he);
		}
		finally
		{
			HibernateProvider.closeSession(session);
		}
	}

	public ReportLog insertReportLog(ReportLog reportLog)
		throws ProviderException
	{
		return (ReportLog) HibernateProvider.save(reportLog);
	}

	public void updateReportLog(ReportLog reportLog) throws ProviderException
	{
		HibernateProvider.update(reportLog);
	}

	public void deleteReportLog(ReportLog reportLog) throws ProviderException
	{
		try
		{
			HibernateProvider.delete(reportLog);
		}
		catch (ConstraintException e)
		{
			throw new ProviderException(e.getMessage());
		}
	}
	
	public List getTopReportsByUser() throws ProviderException
	{
		String fromClause = "select log.user.name, log.report.name, count(*) from org.efs.openreports.objects.ReportLog log group by log.user.name, log.report.name order by log.user.name, count(*) desc ";

		return HibernateProvider.query(fromClause);
	}
	
	public List getTopReports() throws ProviderException
	{
		String fromClause = "select log.report.name, count(*) from org.efs.openreports.objects.ReportLog log group by log.report.name order by count(*) desc ";

		return HibernateProvider.query(fromClause);
	}
	
	public List getTopFailures() throws ProviderException
	{
		String fromClause = "select log.report.name, count(*) from org.efs.openreports.objects.ReportLog log where log.status = 'failure' group by log.report.name order by count(*) desc ";

		return HibernateProvider.query(fromClause);
	}
	
	public List getTopEmptyReports() throws ProviderException
	{
		String fromClause = "select log.report.name, count(*) from org.efs.openreports.objects.ReportLog log where log.status = 'empty' group by log.report.name order by count(*) desc ";

		return HibernateProvider.query(fromClause);
	}	
	
	public List getTopReportsForPeriod(int daysBack) throws ProviderException
	{
		try
		{
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -daysBack);			
			
			Session session = HibernateProvider.openSession();			
			
			try
			{			
				List list = session.createQuery(
						"select log.report.name, count(*) from org.efs.openreports.objects.ReportLog log " 
						+ " where log.startTime > ? group by log.report.name order by count(*) desc").setDate(0, calendar.getTime()).list();					

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
	
	public List getTopAlertsByUser() throws ProviderException
	{
		String fromClause = "select log.user.name, log.alert.name, count(*) from org.efs.openreports.objects.ReportLog log group by log.user.name, log.alert.name order by log.user.name, count(*) desc ";

		return HibernateProvider.query(fromClause);
	}
	
	public List getTopAlerts() throws ProviderException
	{
		String fromClause = "select log.alert.name, count(*) from org.efs.openreports.objects.ReportLog log group by log.alert.name order by count(*) desc ";

		return HibernateProvider.query(fromClause);
	}
	
	public List getTopTriggeredAlerts() throws ProviderException
	{
		String fromClause = "select log.alert.name, count(*) from org.efs.openreports.objects.ReportLog log where log.status = 'triggered' group by log.alert.name order by count(*) desc ";

		return HibernateProvider.query(fromClause);
	}
	
	public List getTopNotTriggeredAlerts() throws ProviderException
	{
		String fromClause = "select log.alert.name, count(*) from org.efs.openreports.objects.ReportLog log where log.status = 'not triggered' group by log.alert.name order by count(*) desc ";

		return HibernateProvider.query(fromClause);
	}
	
}