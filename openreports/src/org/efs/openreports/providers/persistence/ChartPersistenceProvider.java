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

package org.efs.openreports.providers.persistence;

import java.util.List;

import org.apache.log4j.Logger;

import org.efs.openreports.objects.ReportChart;
import org.efs.openreports.providers.HibernateProvider;
import org.efs.openreports.providers.ProviderException;
import org.efs.openreports.util.LocalStrings;

public class ChartPersistenceProvider 
{
	protected static Logger log =
		Logger.getLogger(ChartPersistenceProvider.class.getName());

	public ChartPersistenceProvider() throws ProviderException
	{
		super();

		log.info("Created");
	}

	public ReportChart getReportChart(Integer id)
		throws ProviderException
	{
		return (ReportChart) HibernateProvider.load(ReportChart.class, id);
	}

	public List getReportCharts() throws ProviderException
	{
		String fromClause =
			"from org.efs.openreports.objects.ReportChart reportChart order by reportChart.name ";
		
		return HibernateProvider.query(fromClause);
	}

	public ReportChart insertReportChart(ReportChart reportChart)
		throws ProviderException
	{
		return (ReportChart) HibernateProvider.save(reportChart);
	}

	public void updateReportChart(ReportChart reportChart)
		throws ProviderException
	{
		HibernateProvider.update(reportChart);
	}

	public void deleteReportChart(ReportChart reportChart)
		throws ProviderException
	{
		try
		{
			HibernateProvider.delete(reportChart);
		}
		catch (ConstraintException ce)
		{
			throw new ProviderException(LocalStrings.ERROR_CHART_DELETION);
		}
	}
}