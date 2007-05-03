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
import java.util.List;

import org.apache.log4j.Logger;
import org.efs.openreports.objects.ReportAlert;
import org.efs.openreports.objects.ReportDataSource;
import org.efs.openreports.objects.ReportLog;
import org.efs.openreports.objects.ReportUserAlert;
import org.efs.openreports.providers.AlertProvider;
import org.efs.openreports.providers.DataSourceProvider;
import org.efs.openreports.providers.ProviderException;
import org.efs.openreports.providers.ReportLogProvider;
import org.efs.openreports.providers.persistence.AlertPersistenceProvider;
import org.efs.openreports.util.LocalStrings;

public class AlertProviderImpl implements AlertProvider
{
	protected static Logger log =
		Logger.getLogger(AlertProviderImpl.class.getName());

	private AlertPersistenceProvider alertPersistenceProvider;	
	private DataSourceProvider dataSourceProvider;
	private ReportLogProvider reportLogProvider;	
	
	public AlertProviderImpl(DataSourceProvider dataSourceProvider, ReportLogProvider reportLogProvider) throws ProviderException
	{
		this.dataSourceProvider = dataSourceProvider;
		this.reportLogProvider = reportLogProvider;
		init();
	}	
	
	protected void init() throws ProviderException
	{
		alertPersistenceProvider = new AlertPersistenceProvider();
		log.info("Created");
	}	

	public ReportAlert getReportAlert(Integer id) throws ProviderException
	{
		return alertPersistenceProvider.getReportAlert(id);
	}

	public List getReportAlerts() throws ProviderException
	{
		return alertPersistenceProvider.getReportAlerts();
	}

	public ReportAlert insertReportAlert(ReportAlert reportAlert)
		throws ProviderException
	{
		return alertPersistenceProvider.insertReportAlert(reportAlert);
	}

	public void updateReportAlert(ReportAlert reportAlert)
		throws ProviderException
	{
		alertPersistenceProvider.updateReportAlert(reportAlert);
	}

	public void deleteReportAlert(ReportAlert reportAlert)
		throws ProviderException
	{
		alertPersistenceProvider.deleteReportAlert(reportAlert);
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