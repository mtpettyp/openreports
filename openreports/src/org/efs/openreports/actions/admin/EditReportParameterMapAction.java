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

package org.efs.openreports.actions.admin;

import java.util.*;

import com.opensymphony.xwork2.ActionSupport;

import org.apache.log4j.Logger;
import org.efs.openreports.engine.ReportEngine;
import org.efs.openreports.engine.ReportEngineHelper;
import org.efs.openreports.objects.*;
import org.efs.openreports.providers.*;

public class EditReportParameterMapAction extends ActionSupport
{	
	private static final long serialVersionUID = -5228104770995110195L;

	protected static Logger log = Logger.getLogger(EditReportParameterMapAction.class);

	private int id;
	private int reportParameterId;

	private int sortOrder;
	private int step;
	private boolean required;

	private String submitAdd;
	private String submitUpdate;
	private String submitDelete;
    private String submitLoad;
    private String submitLoadAndCreate;

	private String command;

	private Report report;

	private ReportProvider reportProvider;
	private ParameterProvider parameterProvider;
    private DirectoryProvider directoryProvider;
    private DataSourceProvider dataSourceProvider;
    private PropertiesProvider propertiesProvider;

	@Override
	public String execute()
	{
		try
		{
			report = reportProvider.getReport(new Integer(id));		

			if (submitAdd != null)
			{
				ReportParameter param =
					parameterProvider.getReportParameter(
						new Integer(reportParameterId));

				ReportParameterMap rpMap = new ReportParameterMap();
				rpMap.setReport(report);
				rpMap.setReportParameter(param);
				rpMap.setRequired(param.isRequired());
				rpMap.setSortOrder(0);
				rpMap.setStep(0);
				
				report.getParameters().add(rpMap);

				reportProvider.updateReport(report);
			}

			if (submitUpdate != null)
			{
				ReportParameterMap rpMap =
					report.getReportParameterMap(
						new Integer(reportParameterId));

				rpMap.setRequired(required);
				rpMap.setSortOrder(sortOrder);
				rpMap.setStep(step);

				reportProvider.updateReport(report);
			}

			if (submitDelete != null)
			{
				ReportParameterMap rpMap =
					report.getReportParameterMap(
						new Integer(reportParameterId));			

				report.getParameters().remove(rpMap);

				reportProvider.updateReport(report);
			}
            
            if (submitLoad != null || submitLoadAndCreate != null)
            {
                ReportEngine engine = ReportEngineHelper.getReportEngine(report,
                        dataSourceProvider, directoryProvider, propertiesProvider);     
                                
                List<ReportParameter> parameters = engine.buildParameterList(report);                    
                for (int i=0; i < parameters.size(); i++)
                {
                    ReportParameter designParameter = parameters.get(i);
                    
                    ReportParameter param =
                        parameterProvider.getReportParameter(designParameter.getName());

                    // if null, create and assign a new parameter
                    if (param == null && submitLoadAndCreate != null)
                    {
                        param = parameterProvider.insertReportParameter(designParameter);                       
                        log.info("Parameter Created: " + param.getName());
                    }                   
                    
                    if (param != null)
                    {
                        ReportParameterMap rpMap = new ReportParameterMap();
                        rpMap.setReport(report);
                        rpMap.setReportParameter(param);
                        rpMap.setRequired(param.isRequired());
                        rpMap.setSortOrder(0);
                        rpMap.setStep(0);
                    
                        report.getParameters().add(rpMap);
                    }
                }
                
                reportProvider.updateReport(report);
            }

			return INPUT;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			addActionError(e.toString());
			return INPUT;
		}
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public List<ReportParameter> getReportParameters()
	{
		try
		{
			return parameterProvider.getAvailableParameters(report);
		}
		catch (Exception e)
		{
			addActionError(e.toString());
			return null;
		}
	}

	public List<ReportParameterMap> getParametersInReport()
	{
		List<ReportParameterMap> list = report.getParameters();
		Collections.sort(list);
		
		return list;
	}

	public void setReportProvider(ReportProvider reportProvider)
	{
		this.reportProvider = reportProvider;
	}

	public void setParameterProvider(ParameterProvider parameterProvider)
	{
		this.parameterProvider = parameterProvider;
	}

	public Report getReport()
	{
		return report;
	}

	public void setReport(Report report)
	{
		this.report = report;
	}

	public int getReportParameterId()
	{
		return reportParameterId;
	}

	public void setReportParameterId(int reportParameterId)
	{
		this.reportParameterId = reportParameterId;
	}

	public String getCommand()
	{
		return command;
	}

	public void setCommand(String command)
	{
		this.command = command;
	}

	public String getSubmitAdd()
	{
		return submitAdd;
	}

	public void setSubmitAdd(String submitAdd)
	{
		this.submitAdd = submitAdd;
	}

	public String getSubmitUpdate()
	{
		return submitUpdate;
	}

	public void setSubmitUpdate(String submitUpdate)
	{
		this.submitUpdate = submitUpdate;
	}

	public String getSubmitDelete()
	{
		return submitDelete;
	}

	public void setSubmitDelete(String submitDelete)
	{
		this.submitDelete = submitDelete;
	}

    public String getSubmitLoad()
    {
        return submitLoad;
    }

    public void setSubmitLoad(String submitLoad)
    {
        this.submitLoad = submitLoad;
    }
    
    public String getSubmitLoadAndCreate()
    {
        return submitLoadAndCreate;
    }

    public void setSubmitLoadAndCreate(String submitLoadAndCreate)
    {
        this.submitLoadAndCreate = submitLoadAndCreate;
    }
    
	public boolean isRequired()
	{
		return required;
	}

	public void setRequired(boolean required)
	{
		this.required = required;
	}

	public int getSortOrder()
	{
		return sortOrder;
	}

	public void setSortOrder(int sortOrder)
	{
		this.sortOrder = sortOrder;
	}

	public int getStep()
	{
		return step;
	}

	public void setStep(int step)
	{
		this.step = step;
	}

    public void setDirectoryProvider(DirectoryProvider directoryProvider)
    {
        this.directoryProvider = directoryProvider;
    }

    public void setDataSourceProvider(DataSourceProvider dataSourceProvider)
    {
        this.dataSourceProvider = dataSourceProvider;
    }

    public void setPropertiesProvider(PropertiesProvider propertiesProvider)
    {
        this.propertiesProvider = propertiesProvider;
    }

}