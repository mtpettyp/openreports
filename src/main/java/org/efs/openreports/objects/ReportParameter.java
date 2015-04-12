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

package org.efs.openreports.objects;

import java.io.Serializable;

public class ReportParameter implements Serializable
{
	private static final long serialVersionUID = 667082979233371385l;

	public static String[] TYPES =
		new String[] { "Date", "List", "Query", "Text", "SubReport","Boolean" };

	private static final String STRING = "java.lang.String";
	private static final String DOUBLE = "java.lang.Double";
	private static final String INTEGER = "java.lang.Integer";
	private static final String LONG = "java.lang.Long";
	private static final String BIGDECIMAL = "java.math.BigDecimal";
	private static final String DATE = "java.util.Date";
	private static final String SQL_DATE = "java.sql.Date";
	private static final String TIMESTAMP = "java.sql.Timestamp";	
	private static final String BOOLEAN = "java.lang.Boolean";
	
	public static String[] CLASS_NAMES =
		new String[] {
			STRING,
			DOUBLE,
			INTEGER,
			LONG,
			BIGDECIMAL,
			DATE,
			SQL_DATE,
			TIMESTAMP,
			BOOLEAN};

	public static final String QUERY_PARAM = "Query";
	public static final String LIST_PARAM = "List";
	public static final String TEXT_PARAM = "Text";
	public static final String DATE_PARAM = "Date";
	public static final String SUBREPORT_PARAM = "SubReport";
	public static final String BOOLEAN_PARAM = "Boolean";
	
	private Integer id;
	private String name;
	private String type;
	private String className;
	private ReportDataSource dataSource;
	private String data;
	private ReportParameterValue[] values;
	private String description;
	private boolean required;
	private boolean multipleSelect;
	private String defaultValue;

	public ReportDataSource getDataSource()
	{
		return dataSource;
	}

	public void setDataSource(ReportDataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	public ReportParameter()
	{
	}

	public String getName()
	{
		return name;
	}

	public String getType()
	{
		return type;
	}

	public String getClassName()
	{
		return className;
	}

	public String getData()
	{
		return data;
	}

	public ReportParameterValue[] getValues()
	{
		return values;
	}

	public void setValues(ReportParameterValue[] values)
	{
		this.values = values;
	}

	public void setClassName(String className)
	{
		this.className = className;
	}

	public void setData(String data)
	{
		this.data = data;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
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

	public void setMultipleSelect(boolean multiple)
	{
		this.multipleSelect = multiple;
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