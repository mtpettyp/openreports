/*
 * Copyright (C) 2003 Erik Swenson - erik@oreports.com
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

import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.efs.openreports.objects.ReportLog;
import org.efs.openreports.providers.HibernateProvider;
import org.efs.openreports.providers.ProviderException;
import org.efs.openreports.providers.ReportLogProvider;
import org.efs.openreports.util.ConstraintException;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class ReportLogProviderImpl implements ReportLogProvider
{
	protected static Logger log =
		Logger.getLogger(ReportLogProviderImpl.class.getName());	

	private HibernateProvider hibernateProvider;
	
	public ReportLogProviderImpl(HibernateProvider hibernateProvider) throws ProviderException
	{
		this.hibernateProvider = hibernateProvider;

		log.info("ReportLogProviderImpl created");
	}


	public ReportLog getReportLog(Integer id) throws ProviderException
	{
		return (ReportLog) hibernateProvider.load(ReportLog.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public List<ReportLog> getReportLogs(String status, Integer userId, Integer reportId, Integer alertId, int maxRows) throws ProviderException
	{
		Session session = hibernateProvider.openSession();		

		try
		{
			Criteria criteria = session.createCriteria(ReportLog.class);
			criteria.setMaxResults(maxRows);
			
			if (status != null && !status.equals("-1"))
			{
				criteria.add(Restrictions.eq("status", status));
			}
			
			if (userId != null && !userId.equals(new Integer(-1)))
			{
				criteria.add(Restrictions.eq("user.id", userId));
			}
			
			if (reportId != null && !reportId.equals(new Integer(-1)))
			{
				if (reportId.intValue() == -1)
				{
					criteria.add(Restrictions.isNull("report"));
				}
				else
				{
					criteria.add(Restrictions.eq("report.id", reportId));
				}
			}
			
			if (alertId != null && !alertId.equals(new Integer(-1)))
			{
				if (alertId.intValue() == -1)
				{
					criteria.add(Restrictions.isNull("alert"));
				}
				else
				{
					criteria.add(Restrictions.eq("alert.id", alertId));
				}
			}
			
			return criteria.list();
		}
		catch (HibernateException he)
		{			
			throw new ProviderException(he);
		}
		finally
		{
			hibernateProvider.closeSession(session);
		}
	}

	public ReportLog insertReportLog(ReportLog reportLog)
		throws ProviderException
	{
		return (ReportLog) hibernateProvider.save(reportLog);
	}

	public void updateReportLog(ReportLog reportLog) throws ProviderException
	{
		hibernateProvider.update(reportLog);
	}

	public void deleteReportLog(ReportLog reportLog) throws ProviderException
	{
		try
		{
			hibernateProvider.delete(reportLog);
		}
		catch (ConstraintException e)
		{
			throw new ProviderException(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getTopReportsByUser() throws ProviderException
	{
		String fromClause = "select log.user.name, log.report.name, count(*) from org.efs.openreports.objects.ReportLog log group by log.user.name, log.report.name order by log.user.name, count(*) desc ";

		return (List<Object[]>) hibernateProvider.query(fromClause);
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getTopReports() throws ProviderException
	{
		String fromClause = "select log.report.name, count(*) from org.efs.openreports.objects.ReportLog log group by log.report.name order by count(*) desc ";

		return (List<Object[]>) hibernateProvider.query(fromClause);
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getTopFailures() throws ProviderException
	{
		String fromClause = "select log.report.name, count(*) from org.efs.openreports.objects.ReportLog log where log.status = 'failure' group by log.report.name order by count(*) desc ";

		return (List<Object[]>) hibernateProvider.query(fromClause);
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getTopEmptyReports() throws ProviderException
	{
		String fromClause = "select log.report.name, count(*) from org.efs.openreports.objects.ReportLog log where log.status = 'empty' group by log.report.name order by count(*) desc ";

		return (List<Object[]>) hibernateProvider.query(fromClause);
	}	
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getTopReportsForPeriod(int daysBack) throws ProviderException
	{
		try
		{
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -daysBack);			
			
			Session session = hibernateProvider.openSession();			
			
			try
			{			
				List<Object[]> list = session.createQuery(
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
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getTopAlertsByUser() throws ProviderException
	{
		String fromClause = "select log.user.name, log.alert.name, count(*) from org.efs.openreports.objects.ReportLog log group by log.user.name, log.alert.name order by log.user.name, count(*) desc ";

		return (List<Object[]>) hibernateProvider.query(fromClause);
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getTopAlerts() throws ProviderException
	{
		String fromClause = "select log.alert.name, count(*) from org.efs.openreports.objects.ReportLog log group by log.alert.name order by count(*) desc ";

		return (List<Object[]>) hibernateProvider.query(fromClause);
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getTopTriggeredAlerts() throws ProviderException
	{
		String fromClause = "select log.alert.name, count(*) from org.efs.openreports.objects.ReportLog log where log.status = 'triggered' group by log.alert.name order by count(*) desc ";

		return (List<Object[]>) hibernateProvider.query(fromClause);
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getTopNotTriggeredAlerts() throws ProviderException
	{
		String fromClause = "select log.alert.name, count(*) from org.efs.openreports.objects.ReportLog log where log.status = 'not triggered' group by log.alert.name order by count(*) desc ";

		return (List<Object[]>) hibernateProvider.query(fromClause);
	}
}