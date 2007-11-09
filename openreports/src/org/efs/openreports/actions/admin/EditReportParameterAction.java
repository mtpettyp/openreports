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

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import org.apache.log4j.Logger;
import org.efs.openreports.ORStatics;
import org.efs.openreports.objects.*;
import org.efs.openreports.providers.*;
import org.efs.openreports.util.ORUtil;

public class EditReportParameterAction extends ActionSupport 
{	
	private static final long serialVersionUID = 5156762771044903756L;

	protected static Logger log = Logger.getLogger(EditReportParameterAction.class);

	private String command;

	private boolean submitOk;
	private boolean submitValidate;
	private boolean submitDuplicate;

	private int id;
	private String name;
	private String type;
	private String className;
	private String data;
	private int dataSourceId = Integer.MIN_VALUE;
	private String description;
	private boolean required;
	private boolean multipleSelect;
	private String defaultValue;

	private ReportParameter reportParameter;
	private ReportParameter queryParameter;

	private ReportParameterValue[] parameterValues;

	private DataSourceProvider dataSourceProvider;
	private ParameterProvider parameterProvider;

	@Override
	public String execute()
	{
		try
		{
			if (command.equals("edit"))
			{
				reportParameter =
					parameterProvider.getReportParameter(new Integer(id));
			}
			else
			{
				reportParameter = new ReportParameter();
			}

			if (command.equals("edit") && !submitOk && !submitValidate && !submitDuplicate)
			{
				name = reportParameter.getName();
				type = reportParameter.getType();
				className = reportParameter.getClassName();
				data = reportParameter.getData();
				id = reportParameter.getId().intValue();
				if (reportParameter.getDataSource() != null)
				{
					dataSourceId =
						reportParameter.getDataSource().getId().intValue();
				}
				description = reportParameter.getDescription();
				required = reportParameter.isRequired();
				multipleSelect = reportParameter.isMultipleSelect();
				defaultValue = reportParameter.getDefaultValue();
			}

			if (!submitOk && !submitValidate && !submitDuplicate)
				return INPUT;
			
			if (submitDuplicate)
            {
            	command = "add";
            	reportParameter.setId(null);
            	
            	if (reportParameter.getName().equals(name))
            	{
            		name = "Copy of ".concat(name);
            	}
            }

			reportParameter.setName(name);
			reportParameter.setType(type);
			reportParameter.setClassName(className);
			reportParameter.setData(data);			
			reportParameter.setDescription(description);
			reportParameter.setRequired(required);
			reportParameter.setMultipleSelect(multipleSelect);
			reportParameter.setDefaultValue(defaultValue);
			
			if (dataSourceId > -1)
			{
				reportParameter.setDataSource(dataSourceProvider.getDataSource(new Integer(dataSourceId)));
			}
			else
			{
				reportParameter.setDataSource(null);
			}

			if (submitValidate)
			{
				if (type.equals(ReportParameter.LIST_PARAM)
						|| type.equals(ReportParameter.QUERY_PARAM))
				{
					Map<String,Object> map = null;

					if (type.equals(ReportParameter.QUERY_PARAM)
							&& data.toUpperCase().indexOf("$P") > -1)
					{	
						ReportUser reportUser = (ReportUser) ActionContext.getContext().getSession().get(ORStatics.REPORT_USER);						
						map = ORUtil.buildQueryParameterMap(reportUser, data, parameterProvider);						
					}

					parameterValues = parameterProvider.getParamValues(reportParameter, map);
				}

				return INPUT;
			}

			if (command.equals("edit"))
			{
				parameterProvider.updateReportParameter(reportParameter);
			}

			if (command.equals("add"))
			{
				reportParameter = parameterProvider.insertReportParameter(reportParameter);
                id = reportParameter.getId().intValue();
			}

			return SUCCESS;
		}
		catch (Exception e)
		{
			addActionError(getText(e.getMessage()));
			return INPUT;
		}
	}

	public String getCommand()
	{
		return command;
	}

	public void setCommand(String command)
	{
		this.command = command;
	}

	public void setSubmitOk(String submitOk)
	{
		if (submitOk != null) this.submitOk = true;
	}
	
	public void setSubmitValidate(String submitValidate)
	{
		if (submitValidate != null) this.submitValidate = true;
	}
	
	public void setSubmitDuplicate(String submitDuplicate)
	{
		if (submitDuplicate != null) this.submitDuplicate = true;
	}

	public int getDataSourceId()
	{
		return dataSourceId;
	}

	public String getName()
	{
		return name;
	}

	public void setDataSourceId(int dataSourceId)
	{
		this.dataSourceId = dataSourceId;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public ReportParameter getQueryParameter()
	{
		return queryParameter;
	}
	
	public List<ReportDataSource> getDataSources()
	{
		try
		{
			return dataSourceProvider.getDataSources();
		}
		catch (Exception e)
		{
			addActionError(e.getMessage());
			return null;
		}
	}

	public String[] getTypes()
	{
		return ReportParameter.TYPES;
	}

	public String[] getClassNames()
	{
		return ReportParameter.CLASS_NAMES;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getClassName()
	{
		return className;
	}

	public String getData()
	{
		return data;
	}

	public String getType()
	{
		return type;
	}

	public void setClassName(String className)
	{
		this.className = className;
	}

	public void setData(String data)
	{
		this.data = data;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public ReportParameterValue[] getParameterValues()
	{
		return parameterValues;
	}

	public void setParameterValues(ReportParameterValue[] parameterValues)
	{
		this.parameterValues = parameterValues;
	}

	public void setDataSourceProvider(DataSourceProvider dataSourceProvider)
	{
		this.dataSourceProvider = dataSourceProvider;
	}

	public void setParameterProvider(ParameterProvider parameterProvider)
	{
		this.parameterProvider = parameterProvider;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public boolean isRequired()
	{
		return required;
	}

	public void setRequired(boolean required)
	{
		this.required = required;
	}

	public boolean isMultipleSelect()
	{
		return multipleSelect;
	}

	public void setMultipleSelect(boolean multipleSelect)
	{
		this.multipleSelect = multipleSelect;
	}

	public String getDefaultValue() 
	{
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) 
	{
		this.defaultValue = defaultValue;
	}

}