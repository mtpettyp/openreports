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
import org.efs.openreports.objects.ReportChart;
import org.efs.openreports.providers.ChartProvider;
import org.efs.openreports.providers.ProviderException;
import org.efs.openreports.providers.persistence.ChartPersistenceProvider;

public class ChartProviderImpl
	implements ChartProvider
{
	protected static Logger log =
		Logger.getLogger(ChartProviderImpl.class.getName());

	private ChartPersistenceProvider chartPersistenceProvider;
	
	public ChartProviderImpl() throws ProviderException
	{		
		chartPersistenceProvider = new ChartPersistenceProvider();
		log.info("Created");
	}

	public ReportChart getReportChart(Integer id) throws ProviderException
	{
		return chartPersistenceProvider.getReportChart(id);
	}

	public List getReportCharts() throws ProviderException
	{
		return chartPersistenceProvider.getReportCharts();
	}

	public ReportChart insertReportChart(ReportChart reportChart)
		throws ProviderException
	{
		return chartPersistenceProvider.insertReportChart(reportChart);
	}

	public void updateReportChart(ReportChart reportChart)
		throws ProviderException
	{
		chartPersistenceProvider.updateReportChart(reportChart);
	}

	public void deleteReportChart(ReportChart reportChart)
		throws ProviderException
	{
		chartPersistenceProvider.deleteReportChart(reportChart);
	}

}