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

import java.util.List;

import org.apache.log4j.Logger;
import org.efs.openreports.objects.ReportLog;
import org.efs.openreports.providers.ProviderException;
import org.efs.openreports.providers.ReportLogProvider;
import org.efs.openreports.providers.persistence.ReportLogPersistenceProvider;

public class ReportLogProviderImpl implements ReportLogProvider
{
	protected static Logger log =
		Logger.getLogger(ReportLogProviderImpl.class.getName());
	
	private ReportLogPersistenceProvider reportLogPersistenceProvider;	

	public ReportLogProviderImpl() throws ProviderException
	{
		reportLogPersistenceProvider = new ReportLogPersistenceProvider();

		log.info("ReportLogProviderImpl created");
	}

	public ReportLog getReportLog(Integer id) throws ProviderException
	{
		return reportLogPersistenceProvider.getReportLog(id);
	}

	public List getReportLogs(String status, Integer userId, Integer reportId, Integer alertId, int maxRows) throws ProviderException
	{
		return reportLogPersistenceProvider.getReportLogs(status, userId, reportId, alertId, maxRows);
	}

	public ReportLog insertReportLog(ReportLog reportLog)
		throws ProviderException
	{
		return reportLogPersistenceProvider.insertReportLog(reportLog);
	}

	public void updateReportLog(ReportLog reportLog) throws ProviderException
	{
		reportLogPersistenceProvider.updateReportLog(reportLog);
	}

	public void deleteReportLog(ReportLog reportLog) throws ProviderException
	{
		reportLogPersistenceProvider.deleteReportLog(reportLog);
	}
	
	public List getTopReportsByUser() throws ProviderException
	{
		return reportLogPersistenceProvider.getTopReportsByUser();
	}
	
	public List getTopReports() throws ProviderException
	{
		return reportLogPersistenceProvider.getTopReports();
	}

	public List getTopFailures() throws ProviderException
	{
		return reportLogPersistenceProvider.getTopFailures();		
	}
	
	public List getTopEmptyReports() throws ProviderException
	{
		return reportLogPersistenceProvider.getTopEmptyReports();		
	}	
	
	public List getTopReportsForPeriod(int daysBack) throws ProviderException
	{
		return reportLogPersistenceProvider.getTopReportsForPeriod(daysBack);
	}
	
	public List getTopAlertsByUser() throws ProviderException
	{
		return reportLogPersistenceProvider.getTopAlertsByUser();
	}
	
	public List getTopAlerts() throws ProviderException
	{
		return reportLogPersistenceProvider.getTopAlerts();
	}
	
	public List getTopTriggeredAlerts() throws ProviderException
	{
		return reportLogPersistenceProvider.getTopTriggeredAlerts();
	}
	
	public List getTopNotTriggeredAlerts() throws ProviderException
	{
		return reportLogPersistenceProvider.getTopNotTriggeredAlerts();
	}
}