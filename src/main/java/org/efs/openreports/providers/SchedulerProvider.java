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

import org.efs.openreports.objects.ReportSchedule;
import org.efs.openreports.objects.ReportUser;

public interface SchedulerProvider
{
	public void scheduleReport(ReportSchedule reportSchedule) throws ProviderException;

	public ReportSchedule getScheduledReport(ReportUser reportUser, String name)
			throws ProviderException;

	public List<ReportSchedule> getScheduledReports(ReportUser reportUser) throws ProviderException;

	public void deleteScheduledReport(ReportUser reportUser, String name)
			throws ProviderException;

	public void pauseScheduledReport(ReportUser reportUser, String name)
			throws ProviderException;

	public void resumeScheduledReport(ReportUser reportUser, String name)
			throws ProviderException;
}