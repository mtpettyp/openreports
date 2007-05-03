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

package org.efs.openreports.providers;

import java.util.List;

import org.efs.openreports.objects.ReportLog;

public interface ReportLogProvider
{
	public ReportLog getReportLog(Integer id) throws ProviderException;
	public List getReportLogs(String status, Integer userId, Integer reportId, Integer alertId, int maxRows) throws ProviderException;
	public ReportLog insertReportLog(ReportLog reportLog)	throws ProviderException;
	public void updateReportLog(ReportLog reportLog) throws ProviderException;
	public void deleteReportLog(ReportLog reportLog) throws ProviderException;	
	
	public List getTopReportsByUser() throws ProviderException;
	public List getTopReports() throws ProviderException;
	public List getTopFailures() throws ProviderException;
	public List getTopEmptyReports() throws ProviderException;
	public List getTopReportsForPeriod(int daysBack) throws ProviderException;	
	
	public List getTopAlertsByUser() throws ProviderException;	
	public List getTopAlerts() throws ProviderException;	
	public List getTopTriggeredAlerts() throws ProviderException;	
	public List getTopNotTriggeredAlerts() throws ProviderException;
}