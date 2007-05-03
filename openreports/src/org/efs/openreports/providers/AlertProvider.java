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

package org.efs.openreports.providers;

import java.util.List;

import org.efs.openreports.objects.ReportAlert;
import org.efs.openreports.objects.ReportUserAlert;

public interface AlertProvider
{	
	public ReportUserAlert executeAlert(ReportUserAlert reportAlert, boolean includeReportInLog) throws ProviderException;	
		
	public ReportAlert getReportAlert(Integer id) throws ProviderException;

	public List getReportAlerts() throws ProviderException;
	
	public ReportAlert insertReportAlert(ReportAlert reportAlert)
			throws ProviderException;

	public void updateReportAlert(ReportAlert reportAlert) throws ProviderException;

	public void deleteReportAlert(ReportAlert reportAlert) throws ProviderException;
}