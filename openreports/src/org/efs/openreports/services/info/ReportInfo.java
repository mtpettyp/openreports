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

package org.efs.openreports.services.info;

import java.io.Serializable;

/**
 * ReportInfo object. 
 *  
 * @author Erik Swenson
 * @see ParameterInfo
 * 
 */

public class ReportInfo implements Serializable
{	
	private static final long serialVersionUID = -529487598274539409L;
	
	private Integer id;
	private String name;
	private String description;	
	private ParameterInfo[] parameters;
	private boolean pdfExportEnabled;
	private boolean htmlExportEnabled;
	private boolean csvExportEnabled;
	private boolean xlsExportEnabled;
	private boolean rtfExportEnabled;
	private boolean textExportEnabled;
	private boolean excelExportEnabled;		
	private boolean hidden;
	private int defaultExportType;

	public ReportInfo()
	{
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String toString()
	{
		return name;
	}

	public String getDescription()
	{
		return description;
	}	

	public Integer getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}	

	public void setName(String name)
	{
		this.name = name;
	}

	public ParameterInfo[] getParameters()
	{
		return parameters;
	}	

	public void setParameters(ParameterInfo[] parameters)
	{
		this.parameters = parameters;
	}	

	public boolean isCsvExportEnabled()
	{
		return csvExportEnabled;
	}

	public void setCsvExportEnabled(boolean csvExportEnabled)
	{
		this.csvExportEnabled = csvExportEnabled;
	}

	public boolean isHtmlExportEnabled()
	{
		return htmlExportEnabled;
	}

	public void setHtmlExportEnabled(boolean htmlExportEnabled)
	{
		this.htmlExportEnabled = htmlExportEnabled;
	}

	public boolean isPdfExportEnabled()
	{
		return pdfExportEnabled;
	}

	public void setPdfExportEnabled(boolean pdfExportEnabled)
	{
		this.pdfExportEnabled = pdfExportEnabled;
	}

	public boolean isXlsExportEnabled()
	{
		return xlsExportEnabled;
	}

	public void setXlsExportEnabled(boolean xlsExportEnabled)
	{
		this.xlsExportEnabled = xlsExportEnabled;
	}	

	public boolean isExcelExportEnabled()
	{
		return excelExportEnabled;
	}

	public void setExcelExportEnabled(boolean excelExportEnabled)
	{		
		this.excelExportEnabled = excelExportEnabled;
	}

	public boolean isRtfExportEnabled()
	{
		return rtfExportEnabled;
	}

	public void setRtfExportEnabled(boolean rtfExportEnabled)
	{		
		this.rtfExportEnabled = rtfExportEnabled;
	}

	public boolean isTextExportEnabled()
	{
		return textExportEnabled;
	}

	public void setTextExportEnabled(boolean textExportEnabled)
	{		
		this.textExportEnabled = textExportEnabled;
	}

	public boolean isHidden()
	{
		return hidden;
	}

	public void setHidden(boolean hidden)
	{
		this.hidden = hidden;
	}

	public int getDefaultExportType()
	{
		return defaultExportType;
	}

	public void setDefaultExportType(int defaultExportType)
	{
		this.defaultExportType = defaultExportType;
	}
}