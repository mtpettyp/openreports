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

package org.efs.openreports.providers.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.efs.openreports.objects.Report;
import org.efs.openreports.objects.ReportGroup;
import org.efs.openreports.providers.GroupProvider;
import org.efs.openreports.providers.ProviderException;
import org.efs.openreports.providers.persistence.GroupPersistenceProvider;

public class GroupProviderImpl implements GroupProvider
{
	protected static Logger log =
		Logger.getLogger(GroupProviderImpl.class.getName());

	private GroupPersistenceProvider groupPersistenceProvider;

	public GroupProviderImpl() throws ProviderException
	{
		groupPersistenceProvider = new GroupPersistenceProvider();

		log.info("GroupProviderImpl created");
	}

	public ReportGroup getReportGroup(Integer id) throws ProviderException
	{
		return groupPersistenceProvider.getReportGroup(id);

	}
	
	public List getReportGroups() throws ProviderException
	{
		return groupPersistenceProvider.getReportGroups();
	}
	
	public List getGroupsForReport(Report report) throws ProviderException
	{
		return groupPersistenceProvider.getGroupsForReport(report);
	}

	public ReportGroup insertReportGroup(ReportGroup reportGroup)
		throws ProviderException
	{
		return groupPersistenceProvider.insertReportGroup(reportGroup);
	}

	public void updateReportGroup(ReportGroup reportGroup)
		throws ProviderException
	{
		groupPersistenceProvider.updateReportGroup(reportGroup);
	}

	public void deleteReportGroup(ReportGroup reportGroup)
		throws ProviderException
	{
		groupPersistenceProvider.deleteReportGroup(reportGroup);
	}

}