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

package org.efs.openreports.objects;

import java.io.Serializable;

public class ReportChart implements Serializable
{	
	private static final long serialVersionUID = 7406441909199255551L;
	
	public static final int BAR_CHART = 0;
	public static final int PIE_CHART = 1;
	public static final int XY_CHART = 2;
	public static final int TIME_CHART = 3;
	public static final int RING_CHART = 4;	
	public static final int AREA_CHART = 5;
	public static final int XY_AREA_CHART = 6;	
	public static final int TIME_AREA_CHART = 7;
	public static final int STACKED_BAR_CHART = 8;
	public static final int DIAL_CHART = 9;
	public static final int THERMOMETER_CHART = 10;
	public static final int BUBBLE_CHART = 11;	
	public static final int XY_BAR_CHART = 13;
	public static final int STACKED_XY_BAR_CHART = 14;
	public static final int TIME_BAR_CHART = 15;
	public static final int STACKED_TIME_BAR_CHART = 16;
	
	public static final int HORIZONTAL = 1;
	public static final int VERTICAL = 2;
	
	public static final String DRILLDOWN_PARAMETER = "DrillDown";
	
	private Integer id;
	private String name;
	private String description;
	private String query;	
	
	private int chartType;
	
	private int width = 400;
	private int height = 400;
	
	private String xAxisLabel;
	private String yAxisLabel;
	
	private boolean showLegend;	
	private boolean showTitle;
	private boolean showValues;
	private int plotOrientation;

	private ReportDataSource dataSource;
	
	private Report drillDownReport;
	
	private ReportChart overlayChart;

	public ReportChart()
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

	public String getTitle()
	{
		if (showTitle) return description;
		return null;
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

	public int compareTo(Object object)
	{
		ReportChart reportChart = (ReportChart) object;
		return name.compareTo(reportChart.getName());
	}

	public ReportDataSource getDataSource()
	{
		return dataSource;
	}

	public void setDataSource(ReportDataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	public String getQuery()
	{
		return query;
	}

	public void setQuery(String query)
	{
		this.query = query;
	}
		
	public int getChartType()
	{
		return chartType;
	}

	public void setChartType(int chartType)
	{
		this.chartType = chartType;
	}	
	
	public int getHeight()
	{
		return height;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}

	public int getWidth()
	{
		return width;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}

	public String getXAxisLabel()
	{
		return xAxisLabel;
	}

	public void setXAxisLabel(String axisLabel)
	{
		xAxisLabel = axisLabel;
	}

	public String getYAxisLabel()
	{
		return yAxisLabel;
	}

	public void setYAxisLabel(String axisLabel)
	{
		yAxisLabel = axisLabel;
	}	

	public boolean isShowLegend()
	{
		return showLegend;
	}

	public void setShowLegend(Boolean showLegend)
	{
		if (showLegend == null) showLegend = new Boolean(false);
		this.showLegend = showLegend.booleanValue();
	}
	
	public boolean isShowTitle()
	{
		return showTitle;
	}

	public void setShowTitle(Boolean showTitle)
	{
		if (showTitle == null) showTitle = new Boolean(false);
		this.showTitle = showTitle.booleanValue();
	}		

	public int getPlotOrientation()
	{
		return plotOrientation;
	}

	public void setPlotOrientation(Integer plotOrientation)
	{
		if (plotOrientation == null) plotOrientation = new Integer(VERTICAL);
		this.plotOrientation = plotOrientation.intValue();
	}	

	public Report getDrillDownReport()
	{
		return drillDownReport;
	}

	public void setDrillDownReport(Report drillDownReport)
	{
		this.drillDownReport = drillDownReport;
	}

	public boolean isShowValues()
	{
		return showValues;
	}

	public void setShowValues(Boolean showValues)
	{
		if (showValues == null) showValues = new Boolean(false);
		this.showValues = showValues.booleanValue();
	}	
	
	public ReportChart getOverlayChart()
	{
		return overlayChart;
	}
	
	public void setOverlayChart(ReportChart overlayChart)
	{
		this.overlayChart = overlayChart;
	}
}
