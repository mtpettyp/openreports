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

package org.efs.openreports.providers.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.efs.openreports.objects.ReportAlert;
import org.efs.openreports.objects.ReportDataSource;
import org.efs.openreports.objects.ReportLog;
import org.efs.openreports.objects.ReportUserAlert;
import org.efs.openreports.providers.AlertProvider;
import org.efs.openreports.providers.DataSourceProvider;
import org.efs.openreports.providers.HibernateProvider;
import org.efs.openreports.providers.ProviderException;
import org.efs.openreports.providers.ReportLogProvider;
import org.efs.openreports.util.LocalStrings;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AlertProviderImpl implements AlertProvider
{
	protected static Logger log = Logger.getLogger(AlertProviderImpl.class.getName());
	
	private DataSourceProvider dataSourceProvider;
	private ReportLogProvider reportLogProvider;	
	private HibernateProvider hibernateProvider;
	
	public AlertProviderImpl(DataSourceProvider dataSourceProvider, ReportLogProvider reportLogProvider, HibernateProvider hibernateProvider) throws ProviderException
	{
		this.dataSourceProvider = dataSourceProvider;
		this.reportLogProvider = reportLogProvider;
		this.hibernateProvider = hibernateProvider;

		log.info("Created");
	}			

	public ReportAlert getReportAlert(Integer id) throws ProviderException 
	{
		return (ReportAlert) hibernateProvider.load(ReportAlert.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<ReportAlert> getReportAlerts() throws ProviderException
	{
		String fromClause =
			"from org.efs.openreports.objects.ReportAlert reportAlert order by reportAlert.name ";
	
		return (List<ReportAlert>) hibernateProvider.query(fromClause);
	}

	public ReportAlert insertReportAlert(ReportAlert reportAlert) throws ProviderException
	{
		return (ReportAlert) hibernateProvider.save(reportAlert);
	}

	public void updateReportAlert(ReportAlert reportAlert) throws ProviderException
	{
		hibernateProvider.update(reportAlert);
	}

	public void deleteReportAlert(ReportAlert reportAlert) throws ProviderException
	{
		Session session = hibernateProvider.openSession();
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
			hibernateProvider.rollbackTransaction(tx);
					
			if (he.getCause() != null && he.getCause().getMessage() != null && he.getCause().getMessage().toUpperCase().indexOf("CONSTRAINT") > 0)
			{
				throw new ProviderException(LocalStrings.ERROR_ALERT_DELETION);
			}
				
			log.error("deleteReportAlert", he);			
			throw new ProviderException(LocalStrings.ERROR_SERVERSIDE);
		}
		finally
		{
			hibernateProvider.closeSession(session);
		}		
	}
	
	public ReportUserAlert executeAlert(ReportUserAlert userAlert, boolean includeReportInLog) throws ProviderException
	{
		if (userAlert == null) return null;		 
		
		Connection conn = null;
		PreparedStatement pStmt = null;
		ResultSet rs = null;		
		
		ReportLog alertLog = new ReportLog(userAlert.getUser(), userAlert.getAlert(), new Date());
		if (includeReportInLog) alertLog.setReport(userAlert.getReport());
		
		try
		{
			reportLogProvider.insertReportLog(alertLog);
			
			ReportDataSource dataSource = userAlert.getAlert().getDataSource();
			conn = dataSourceProvider.getConnection(dataSource.getId());
			
			pStmt = conn.prepareStatement(userAlert.getAlert().getQuery());

			rs = pStmt.executeQuery();

			if (!rs.next())
			{
				userAlert.setCount(0);
			}

			userAlert.setCount(rs.getInt(1));
			
			if (userAlert.isTriggered())
			{
				alertLog.setStatus(ReportLog.STATUS_TRIGGERED);
			}
			else
			{
				alertLog.setStatus(ReportLog.STATUS_NOT_TRIGGERED);
			}
			
			alertLog.setMessage("Count: " + userAlert.getCount() + " Condition: "
					+ userAlert.getCondition());
			
			alertLog.setEndTime(new Date());
			reportLogProvider.updateReportLog(alertLog);
		}
		catch (Exception e)
		{
			alertLog.setMessage(e.getMessage());
			alertLog.setStatus(ReportLog.STATUS_FAILURE);
			alertLog.setEndTime(new Date());

			reportLogProvider.updateReportLog(alertLog);
			
			throw new ProviderException(LocalStrings.ERROR_ALERTQUERY_INVALID);
		}
		finally
		{
			try
			{
				if (rs != null) rs.close();
				if (pStmt != null) pStmt.close();
				if (conn != null) conn.close();
			}
			catch (Exception c)
			{
				log.error("Error closing");
			}
		}
		
		return userAlert;
	}	
	
	public void setDataSourceProvider(DataSourceProvider dataSourceProvider)
	{
		this.dataSourceProvider = dataSourceProvider;
	}

	public void setReportLogProvider(ReportLogProvider reportLogProvider)
	{
		this.reportLogProvider = reportLogProvider;
	}
}