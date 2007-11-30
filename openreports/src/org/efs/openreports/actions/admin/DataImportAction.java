/*
 * Copyright (C) 2007 Erik Swenson - erik@oreports.com
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

package org.efs.openreports.actions.admin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.efs.openreports.objects.Report;
import org.efs.openreports.objects.ReportChart;
import org.efs.openreports.objects.ReportDataSource;
import org.efs.openreports.objects.ReportParameter;
import org.efs.openreports.objects.ReportParameterMap;
import org.efs.openreports.providers.ChartProvider;
import org.efs.openreports.providers.DataSourceProvider;
import org.efs.openreports.providers.ParameterProvider;
import org.efs.openreports.providers.ReportProvider;

import com.opensymphony.xwork2.ActionSupport;
import com.thoughtworks.xstream.XStream;

public class DataImportAction extends ActionSupport  
{	
	protected static Logger log = Logger.getLogger(DataImportAction.class);

	private ReportProvider reportProvider;	
	private DataSourceProvider dataSourceProvider;
	private ChartProvider chartProvider;
	private ParameterProvider parameterProvider;
   
    private File importFile;    
    
	@Override
	public String execute()
	{
		try
		{
			if (importFile != null) 
			{
				List<Report> reports = parseReports(importFile);
				
				for (Report report : reports) 
				{					 
					try
					{		
						if (reportProvider.getReport(report.getName()) != null)
						{
							addActionError("Skipping Existing Report: " + report.getName());
							continue;
						}
						
						log.info("Importing: " + report.getName());
						
						if (report.getDataSource() != null)
						{
							ReportDataSource dataSource = dataSourceProvider.getDataSource(report.getDataSource().getName());
							if (dataSource == null)
							{						
								dataSource = dataSourceProvider.insertDataSource(report.getDataSource());
								log.info("Adding DataSource: " + dataSource.getName());
							}
							report.setDataSource(dataSource);
						}
						
						if (report.getReportChart() != null)
						{
							ReportChart chart = chartProvider.getReportChart(report.getReportChart().getName());
							if (chart == null)
							{
								if (report.getReportChart().getDataSource() != null)
								{
									ReportDataSource dataSource = dataSourceProvider.getDataSource(report.getReportChart().getDataSource().getName());
									if (dataSource == null)
									{						
										dataSource = dataSourceProvider.insertDataSource(report.getReportChart().getDataSource());
										log.info("Adding DataSource: " + dataSource.getName());
									}
									report.getReportChart().setDataSource(dataSource);
								}
								
								if (report.getReportChart().getDrillDownReport() != null)
								{
									Report drillDownReport = reportProvider.getReport(report.getReportChart().getDrillDownReport().getName());
									if (drillDownReport == null)
									{						
										drillDownReport = reportProvider.insertReport(report.getReportChart().getDrillDownReport());
										log.info("Adding Report: " + drillDownReport.getName());
									}
									report.getReportChart().setDrillDownReport(drillDownReport);
								}
								
								if (report.getReportChart().getOverlayChart() != null)
								{
									ReportChart overlayChart = chartProvider.getReportChart(report.getReportChart().getOverlayChart().getName());
									if (overlayChart == null)
									{						
										overlayChart = chartProvider.insertReportChart(report.getReportChart().getOverlayChart());
										log.info("Adding Chart: " + overlayChart.getName());
									}
									report.getReportChart().setOverlayChart(overlayChart);
								}
								
								chart = chartProvider.insertReportChart(report.getReportChart());
								log.info("Adding Chart: " + chart.getName());
							}
							report.setReportChart(chart);
						}
						
						if (report.getParameters() != null)
						{
							List<ReportParameterMap> parameters = report.getParameters();
							List<ReportParameterMap> duplicateParameters = new ArrayList<ReportParameterMap>();
							
							for (ReportParameterMap map: parameters)
							{								
								ReportParameter parameter = parameterProvider.getReportParameter(map.getReportParameter().getName());
								if (parameter == null)
								{
									if (map.getReportParameter().getDataSource() != null)
									{
										ReportDataSource dataSource = dataSourceProvider.getDataSource(map.getReportParameter().getDataSource().getName());
										if (dataSource == null)
										{						
											dataSource = dataSourceProvider.insertDataSource(map.getReportParameter().getDataSource());
											log.info("Adding DataSource: " + dataSource.getName());
										}
										map.getReportParameter().setDataSource(dataSource);
									}
									
									parameter = parameterProvider.insertReportParameter(map.getReportParameter());
									log.info("Adding Parameter: " + parameter.getName());
								}
								
								map.setReportParameter(parameter);									
								duplicateParameters.add(map);
							}
							
							report.setParameters(duplicateParameters);					
						}
						
						reportProvider.insertReport(report);
						
						log.info("Finished Importing: " + report.getName());
						addActionError("Imported Report: " + report.getName());
					}
					catch(Exception e)
					{
						addActionError("Import Error: " + report.getName());
						log.error(report.getName(), e);
					}
				}
			}
		}
		catch (Exception pe)
		{			
			addActionError(pe.toString());
		}       

		return SUCCESS;
	}	    
	
	@SuppressWarnings("unchecked")
	private List<Report> parseReports(File importFile) throws FileNotFoundException
	{
		FileInputStream input = new FileInputStream(importFile);
		
		XStream xStream = new XStream();				
		return (List<Report>) xStream.fromXML(input);
	}

	public File getImportFile() 
	{
		return importFile;
	}

	public void setImportFile(File importFile) 
	{
		this.importFile = importFile;
	}

	public void setReportProvider(ReportProvider reportProvider)
	{
		this.reportProvider = reportProvider;
	}

	public void setDataSourceProvider(DataSourceProvider dataSourceProvider) 
	{
		this.dataSourceProvider = dataSourceProvider;
	}

	public void setChartProvider(ChartProvider chartProvider)
	{
		this.chartProvider = chartProvider;
	}

	public void setParameterProvider(ParameterProvider parameterProvider) 
	{
		this.parameterProvider = parameterProvider;
	}	
}