/*
 * Copyright (C) 2003 Erik Swenson - erik@oreports.com
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 *  
 */

package org.efs.openreports.engine;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.util.JRQueryExecuter;

import org.apache.log4j.Logger;
import org.efs.openreports.engine.input.ReportEngineInput;
import org.efs.openreports.engine.output.ChartEngineOutput;
import org.efs.openreports.engine.output.ReportEngineOutput;
import org.efs.openreports.objects.Report;
import org.efs.openreports.objects.ReportChart;
import org.efs.openreports.objects.ReportDataSource;
import org.efs.openreports.objects.chart.CategoryChartValue;
import org.efs.openreports.objects.chart.ChartValue;
import org.efs.openreports.objects.chart.PieChartValue;
import org.efs.openreports.objects.chart.TimeChartValue;
import org.efs.openreports.objects.chart.XYChartValue;
import org.efs.openreports.objects.chart.XYZChartValue;
import org.efs.openreports.providers.DataSourceProvider;
import org.efs.openreports.providers.DirectoryProvider;
import org.efs.openreports.providers.PropertiesProvider;
import org.efs.openreports.providers.ProviderException;
import org.efs.openreports.util.LocalStrings;
import org.efs.openreports.util.ORUtil;
import org.efs.openreports.util.TimeSeriesURLGenerator;
import org.efs.openreports.util.XYURLGenerator;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryAxis3D;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberAxis3D;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.encoders.EncoderUtil;
import org.jfree.chart.encoders.ImageFormat;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.labels.StandardPieToolTipGenerator;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DialShape;
import org.jfree.chart.plot.MeterInterval;
import org.jfree.chart.plot.MeterPlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.RingPlot;
import org.jfree.chart.plot.ThermometerPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.AreaRenderer;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer3D;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.urls.StandardCategoryURLGenerator;
import org.jfree.chart.urls.StandardPieURLGenerator;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;

/**
 * ChartReport ReportEngine implementation. Charts are generated using 
 * the JFreeReport library.
 * 
 * @author Erik Swenson
 * 
 */

public class ChartReportEngine extends ReportEngine
{
	protected static Logger log = Logger.getLogger(ChartReportEngine.class.getName());
	
	public ChartReportEngine(DataSourceProvider dataSourceProvider,
			DirectoryProvider directoryProvider, PropertiesProvider propertiesProvider)
	{
		super(dataSourceProvider,directoryProvider, propertiesProvider);
	}
	
	/**
	 * Generates a ChartEngineOutput from a ReportEngineInput. The output consists 
	 * of a byte[] containing a JPEG image.		
	 */
	public ReportEngineOutput generateReport(ReportEngineInput input)
			throws ProviderException
	{
		ReportChart chart = input.getReport().getReportChart();
		chart.setDescription(ORUtil.parseStringWithParameters(chart.getDescription(), input.getParameters()));
		
		ChartValue[] values = getChartValues(chart, input.getParameters());		
	        
		return createChartOutput(chart, values, input.getReport().isDisplayInline());
	}	

	/**
	 * Executes the Chart query and builds an array of ChartValues from the results.
	 */
	public ChartValue[] getChartValues(ReportChart reportChart, Map parameters)
			throws ProviderException
	{
		Connection conn = null;
		PreparedStatement pStmt = null;
		ResultSet rs = null;

		try
		{
			ReportDataSource dataSource = reportChart.getDataSource();
			conn = dataSourceProvider.getConnection(dataSource.getId());

			// Use JasperReports Query logic to parse parameters in chart
			// queries

			JRDesignQuery query = new JRDesignQuery();
			query.setText(reportChart.getQuery());

			// convert parameters to JRDesignParameters so they can be parsed
			Map jrParameters = ORUtil.buildJRDesignParameters(parameters);

			pStmt = JRQueryExecuter.getStatement(query, jrParameters, parameters, conn);

			rs = pStmt.executeQuery();

			ArrayList<ChartValue> list = new ArrayList<ChartValue>();

			int chartType = reportChart.getChartType();
			if (chartType == ReportChart.BAR_CHART || chartType == ReportChart.AREA_CHART
					|| chartType == ReportChart.STACKED_BAR_CHART)
			{
				while (rs.next())
				{
					CategoryChartValue catValue = new CategoryChartValue();

					catValue.setValue(rs.getDouble(1));
					catValue.setSeries(rs.getString(2));
					catValue.setCategory(rs.getString(3));

					list.add(catValue);
				}
			}
			else if (chartType == ReportChart.PIE_CHART
					|| chartType == ReportChart.RING_CHART)
			{
				while (rs.next())
				{
					PieChartValue pieValue = new PieChartValue();

					pieValue.setValue(rs.getDouble(1));
					pieValue.setKey(rs.getString(2));

					list.add(pieValue);
				}
			}
			else if (chartType == ReportChart.XY_CHART | chartType == ReportChart.XY_AREA_CHART)
			{
				while (rs.next())
				{
					XYChartValue xyValue = new XYChartValue();

					xyValue.setSeries(rs.getString(1));
					xyValue.setValue(rs.getDouble(2));
					xyValue.setYValue(rs.getDouble(3));

					list.add(xyValue);
				}
			}
			else if (chartType == ReportChart.BUBBLE_CHART)
			{
				while (rs.next())
				{
					XYZChartValue xyzValue = new XYZChartValue();

					xyzValue.setSeries(rs.getString(1));
					xyzValue.setValue(rs.getDouble(2));
					xyzValue.setYValue(rs.getDouble(3));
					xyzValue.setZValue(rs.getDouble(4));

					list.add(xyzValue);
				}
			}
			else if (chartType == ReportChart.TIME_CHART | chartType == ReportChart.TIME_AREA_CHART)
			{
				while (rs.next())
				{
					TimeChartValue timeValue = new TimeChartValue();

					timeValue.setSeries(rs.getString(1));
					timeValue.setValue(rs.getDouble(2));
					timeValue.setTime(rs.getTimestamp(3));

					list.add(timeValue);
				}
			}
			else if (chartType == ReportChart.DIAL_CHART
					|| chartType == ReportChart.THERMOMETER_CHART)
			{
				while (rs.next())
				{
					ChartValue chartValue = new ChartValue();
					chartValue.setValue(rs.getDouble(1));					

					list.add(chartValue);
				}
			}

			ChartValue[] values = new ChartValue[list.size()];
			list.toArray(values);

			return values;
		}
		catch (Exception e)
		{
			log.error("getChartValues", e);
			throw new ProviderException(LocalStrings.ERROR_SERVERSIDE);
		}
		finally
		{
			try
			{
				if (rs != null) rs.close();
				if (pStmt != null) pStmt.close();
				if (conn != null) conn.close();
			}
			catch (Exception c)
			{
				log.error("Error closing");
			}
		}
	}	
	
	private static ChartEngineOutput createChartOutput(ReportChart reportChart, ChartValue[] values, boolean displayInline)
	{
		JFreeChart chart = null;

		switch (reportChart.getChartType())
		{
			case ReportChart.BAR_CHART :
				chart = createBarChart(reportChart, values, displayInline);				
				break;
			case ReportChart.PIE_CHART :				
				chart = createPieChart(reportChart, values, displayInline);							
				break;
			case ReportChart.XY_CHART :
				chart = createXYChart(reportChart, values, displayInline);
				break;
			case ReportChart.TIME_CHART :
				chart = createTimeChart(reportChart, values, displayInline);
				break;
			case ReportChart.RING_CHART :
				chart = createRingChart(reportChart, values, displayInline);				
				break;
			case ReportChart.AREA_CHART :
				chart = createAreaChart(reportChart, values, displayInline);
				break;
			case ReportChart.XY_AREA_CHART :
				chart = createXYAreaChart(reportChart, values, displayInline);
				break;
			case ReportChart.TIME_AREA_CHART :
				chart = createTimeAreaChart(reportChart, values, displayInline);
				break;
			case ReportChart.STACKED_BAR_CHART :
				chart = createStackedBarChart(reportChart, values, displayInline);
				break;
			case ReportChart.DIAL_CHART :
				chart = createDialChart(reportChart, values, displayInline);
				break;
			case ReportChart.THERMOMETER_CHART :
				chart = createThermometerChart(reportChart, values, displayInline);
				break;		
		}

		if (chart == null) return null;		
		
		chart.setBackgroundPaint(Color.WHITE);
		
		ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());

		BufferedImage bufferedImage =  chart.createBufferedImage(reportChart.getWidth(), reportChart.getHeight(),info);
		byte[] image = null;
		
		try
		{
			image = EncoderUtil.encode(bufferedImage, ImageFormat.PNG);
		}
		catch(IOException ioe)
		{
			log.warn(ioe);
		}
			
		ChartEngineOutput chartOutput = new ChartEngineOutput();
		chartOutput.setContent(image);	
		chartOutput.setContentType(ReportEngineOutput.CONTENT_TYPE_JPEG);
		chartOutput.setChartRenderingInfo(info);
		chartOutput.setChartValues(values);
		
		return chartOutput;
	}	
	
	public static JFreeChart createBarChart(ReportChart reportChart, ChartValue[] values, boolean displayInline)
	{
		CategoryDataset dataset = createCategoryDataset(values);
		
		CategoryAxis3D categoryAxis = new CategoryAxis3D(reportChart.getXAxisLabel());
		ValueAxis valueAxis = new NumberAxis3D(reportChart.getYAxisLabel());		
		
		BarRenderer3D renderer = new BarRenderer3D();
		renderer.setToolTipGenerator(new StandardCategoryToolTipGenerator());
		
		if (reportChart.getDrillDownReport() != null)
		{
			renderer.setItemURLGenerator(new StandardCategoryURLGenerator(
					"executeReport.action?displayInline=" + displayInline
							+ "&exportType=0&reportId="
							+ reportChart.getDrillDownReport().getId(), "series",
					ReportChart.DRILLDOWN_PARAMETER));
		}
		
		CategoryPlot plot = new CategoryPlot(dataset, categoryAxis, valueAxis, renderer);
		plot.setOrientation(PlotOrientation.VERTICAL);
		if (reportChart.getPlotOrientation() == ReportChart.HORIZONTAL)
		{
			plot.setOrientation(PlotOrientation.HORIZONTAL);
		}
		
		JFreeChart chart = new JFreeChart(reportChart.getTitle(), JFreeChart.DEFAULT_TITLE_FONT, plot, reportChart.isShowLegend());
		
		return chart;
	}	
	
	public static JFreeChart createPieChart(ReportChart reportChart, ChartValue[] values, boolean displayInline)
	{
		PieDataset dataset = createPieDataset(values);		
		
		PiePlot3D plot = new PiePlot3D(dataset);
		plot.setToolTipGenerator(new StandardPieToolTipGenerator());
		
		if (reportChart.getDrillDownReport() != null)
		{
			plot.setURLGenerator(new StandardPieURLGenerator(
					"executeReport.action?displayInline=" + displayInline
							+ "&exportType=0&reportId="
							+ reportChart.getDrillDownReport().getId(),
					ReportChart.DRILLDOWN_PARAMETER, "pieIndex"));
		}
		
		JFreeChart chart = new JFreeChart(reportChart.getTitle(), JFreeChart.DEFAULT_TITLE_FONT, plot, reportChart.isShowLegend());
		
		return chart;
	}	

	public static JFreeChart createRingChart(ReportChart reportChart,
			ChartValue[] values, boolean displayInline)
	{
		PieDataset dataset = createPieDataset(values);

		RingPlot plot = new RingPlot(dataset);
		plot.setToolTipGenerator(new StandardPieToolTipGenerator());

		if (reportChart.getDrillDownReport() != null)
		{
			plot.setURLGenerator(new StandardPieURLGenerator(
					"executeReport.action?displayInline=" + displayInline
							+ "&exportType=0&reportId="
							+ reportChart.getDrillDownReport().getId(),
					ReportChart.DRILLDOWN_PARAMETER, "pieIndex"));
		}

		JFreeChart chart = new JFreeChart(reportChart.getTitle(),
				JFreeChart.DEFAULT_TITLE_FONT, plot, reportChart.isShowLegend());

		return chart;
	}
	
	private static JFreeChart createXYChart(ReportChart reportChart, ChartValue[] values, boolean displayInline)
	{
		XYDataset dataset = createXYDataset(values);
		
		NumberAxis xAxis = new NumberAxis(reportChart.getXAxisLabel());
	    NumberAxis yAxis = new NumberAxis(reportChart.getYAxisLabel());
	    
	    XYItemRenderer renderer = new XYLineAndShapeRenderer(true, false);	
	    renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
	    
	    if (reportChart.getDrillDownReport() != null)
		{
			renderer.setURLGenerator(new XYURLGenerator(
					"executeReport.action?displayInline=" + displayInline
							+ "&exportType=0&reportId="
							+ reportChart.getDrillDownReport().getId(), "series",
					ReportChart.DRILLDOWN_PARAMETER));
		}
		
		XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
		plot.setOrientation(PlotOrientation.VERTICAL);
		
		if (reportChart.getPlotOrientation() == ReportChart.HORIZONTAL)
		{
			plot.setOrientation(PlotOrientation.HORIZONTAL);
		}

		JFreeChart chart = new JFreeChart(reportChart.getTitle(), JFreeChart.DEFAULT_TITLE_FONT, plot, reportChart.isShowLegend());

		return chart;
	}

	private static JFreeChart createTimeChart(ReportChart reportChart, ChartValue[] values, boolean displayInline)
	{
		XYDataset dataset = createTimeDataset(values);
		
		ValueAxis timeAxis = new DateAxis(reportChart.getXAxisLabel());
	    NumberAxis valueAxis = new NumberAxis(reportChart.getYAxisLabel());
	    
	    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, false);
        renderer.setBaseToolTipGenerator(StandardXYToolTipGenerator.getTimeSeriesInstance());
        
        if (reportChart.getDrillDownReport() != null)
		{
			renderer.setURLGenerator(new TimeSeriesURLGenerator(
					"executeReport.action?displayInline=" + displayInline
							+ "&exportType=0&reportId="
							+ reportChart.getDrillDownReport().getId(), "series",
					ReportChart.DRILLDOWN_PARAMETER));
		}       
	     
	    XYPlot plot = new XYPlot(dataset, timeAxis, valueAxis, renderer);	   
		plot.setOrientation(PlotOrientation.VERTICAL);
		
		if (reportChart.getPlotOrientation() == ReportChart.HORIZONTAL)
		{
			plot.setOrientation(PlotOrientation.HORIZONTAL);
		}
		
		JFreeChart chart = new JFreeChart(reportChart.getTitle(), JFreeChart.DEFAULT_TITLE_FONT, plot, reportChart.isShowLegend());

		return chart;
	}	
	
	private static JFreeChart createAreaChart(ReportChart reportChart, ChartValue[] values, boolean displayInline)
	{
		CategoryDataset dataset = createCategoryDataset(values);
		
		CategoryAxis3D categoryAxis = new CategoryAxis3D(reportChart.getXAxisLabel());
		categoryAxis.setCategoryMargin(0.0);
		
		ValueAxis valueAxis = new NumberAxis3D(reportChart.getYAxisLabel());		
		
		AreaRenderer renderer = new AreaRenderer();
		renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
		
		if (reportChart.getDrillDownReport() != null)
		{
			renderer.setBaseItemURLGenerator(new StandardCategoryURLGenerator(
					"executeReport.action?displayInline=" + displayInline
							+ "&exportType=0&reportId="
							+ reportChart.getDrillDownReport().getId(), "series",
					ReportChart.DRILLDOWN_PARAMETER));
		}
		
		CategoryPlot plot = new CategoryPlot(dataset, categoryAxis, valueAxis, renderer);
		plot.setOrientation(PlotOrientation.VERTICAL);
		if (reportChart.getPlotOrientation() == ReportChart.HORIZONTAL)
		{
			plot.setOrientation(PlotOrientation.HORIZONTAL);
		}
		
		JFreeChart chart = new JFreeChart(reportChart.getTitle(), JFreeChart.DEFAULT_TITLE_FONT, plot, reportChart.isShowLegend());

		return chart;
	}	
	
	private static JFreeChart createTimeAreaChart(ReportChart reportChart, ChartValue[] values, boolean displayInline)
	{
		XYDataset dataset = createTimeDataset(values);
		
		ValueAxis timeAxis = new DateAxis(reportChart.getXAxisLabel());
	    NumberAxis valueAxis = new NumberAxis(reportChart.getYAxisLabel());
	    
	    XYAreaRenderer renderer = new XYAreaRenderer();
        renderer.setBaseToolTipGenerator(StandardXYToolTipGenerator.getTimeSeriesInstance());
        
        if (reportChart.getDrillDownReport() != null)
		{
			renderer.setURLGenerator(new TimeSeriesURLGenerator(
					"executeReport.action?displayInline=" + displayInline
							+ "&exportType=0&reportId="
							+ reportChart.getDrillDownReport().getId(), "series",
					ReportChart.DRILLDOWN_PARAMETER));
		}       
	     
	    XYPlot plot = new XYPlot(dataset, timeAxis, valueAxis, renderer);	   
		plot.setOrientation(PlotOrientation.VERTICAL);
		
		if (reportChart.getPlotOrientation() == ReportChart.HORIZONTAL)
		{
			plot.setOrientation(PlotOrientation.HORIZONTAL);
		}
		
		JFreeChart chart = new JFreeChart(reportChart.getTitle(), JFreeChart.DEFAULT_TITLE_FONT, plot, reportChart.isShowLegend());

		return chart;
	}	
	
	private static JFreeChart createXYAreaChart(ReportChart reportChart, ChartValue[] values, boolean displayInline)
	{
		XYDataset dataset = createXYDataset(values);
		
		NumberAxis xAxis = new NumberAxis(reportChart.getXAxisLabel());
	    xAxis.setAutoRangeIncludesZero(false);
	   	    
	    NumberAxis yAxis = new NumberAxis(reportChart.getYAxisLabel());
	     
	    XYAreaRenderer renderer = new XYAreaRenderer();
        renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
        
        if (reportChart.getDrillDownReport() != null)
		{
			renderer.setURLGenerator(new XYURLGenerator(
					"executeReport.action?displayInline=" + displayInline
							+ "&exportType=0&reportId="
							+ reportChart.getDrillDownReport().getId(), "series",
					ReportChart.DRILLDOWN_PARAMETER));
		}       
	        
	    XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
		plot.setForegroundAlpha(0.5F);
		plot.setOrientation(PlotOrientation.VERTICAL);

		if (reportChart.getPlotOrientation() == ReportChart.HORIZONTAL)
		{
			plot.setOrientation(PlotOrientation.HORIZONTAL);
		}

		JFreeChart chart = new JFreeChart(reportChart.getTitle(),
				JFreeChart.DEFAULT_TITLE_FONT, plot, reportChart.isShowLegend());

		return chart;
	}
	
	private static JFreeChart createStackedBarChart(ReportChart reportChart,
			ChartValue[] values, boolean displayInline)
	{
		CategoryDataset dataset = createCategoryDataset(values);

		CategoryAxis categoryAxis = new CategoryAxis3D(reportChart.getXAxisLabel());
		ValueAxis valueAxis = new NumberAxis3D(reportChart.getYAxisLabel());

		CategoryItemRenderer renderer = new StackedBarRenderer3D();
		renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());

		if (reportChart.getDrillDownReport() != null)
		{
			renderer.setBaseItemURLGenerator(new StandardCategoryURLGenerator(
					"executeReport.action?displayInline=" + displayInline
							+ "&exportType=0&reportId="
							+ reportChart.getDrillDownReport().getId(), "series",
					ReportChart.DRILLDOWN_PARAMETER));
		}
		
		CategoryPlot plot = new CategoryPlot(dataset, categoryAxis, valueAxis, renderer);
		plot.setOrientation(PlotOrientation.VERTICAL);

		if (reportChart.getPlotOrientation() == ReportChart.HORIZONTAL)
		{
			plot.setOrientation(PlotOrientation.HORIZONTAL);
		}

		JFreeChart chart = new JFreeChart(reportChart.getTitle(),
				JFreeChart.DEFAULT_TITLE_FONT, plot, reportChart.isShowLegend());

		return chart;
	}
	
	private static JFreeChart createDialChart(ReportChart reportChart,
			ChartValue[] values, boolean displayInline)
	{
		DefaultValueDataset dataset = createDefaultValueDataset(values);

		MeterPlot plot = new MeterPlot(dataset);
		plot.setRange(new Range(0, 60));
		plot.addInterval(new MeterInterval("Normal", new Range(0.0, 35.0),
				Color.lightGray, new BasicStroke(2.0f), new Color(0, 255, 0, 64)));
		plot.addInterval(new MeterInterval("Warning", new Range(35.0, 50.0),
				Color.lightGray, new BasicStroke(2.0f), new Color(255, 255, 0, 64)));
		plot.addInterval(new MeterInterval("Critical", new Range(50.0, 60.0),
				Color.lightGray, new BasicStroke(2.0f), new Color(255, 0, 0, 128)));
		plot.setNeedlePaint(Color.darkGray);
		plot.setDialBackgroundPaint(Color.white);
		plot.setDialOutlinePaint(Color.gray);
		plot.setDialShape(DialShape.CHORD);
		plot.setMeterAngle(260);
		plot.setTickLabelsVisible(true);
		plot.setTickLabelFont(new Font("Dialog", Font.BOLD, 10));
		plot.setTickLabelPaint(Color.darkGray);
		plot.setTickSize(5.0);
		plot.setTickPaint(Color.lightGray);
		plot.setValuePaint(Color.black);
		plot.setValueFont(new Font("Dialog", Font.BOLD, 14));

		JFreeChart chart = new JFreeChart(reportChart.getTitle(),
				JFreeChart.DEFAULT_TITLE_FONT, plot, reportChart.isShowLegend());

		return chart;
	}
	
	private static JFreeChart createThermometerChart(ReportChart reportChart,
			ChartValue[] values, boolean displayInline)
	{
		DefaultValueDataset dataset = createDefaultValueDataset(values);

		ThermometerPlot plot = new ThermometerPlot(dataset);

		plot.setInsets(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
		plot.setPadding(new RectangleInsets(10.0, 10.0, 10.0, 10.0));
		plot.setThermometerStroke(new BasicStroke(2.0f));
		plot.setThermometerPaint(Color.lightGray);
		plot.setUnits(ThermometerPlot.UNITS_FAHRENHEIT);

		JFreeChart chart = new JFreeChart(reportChart.getTitle(),
				JFreeChart.DEFAULT_TITLE_FONT, plot, reportChart.isShowLegend());

		return chart;
	}		

	private static CategoryDataset createCategoryDataset(ChartValue[] values)
	{
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for (int i = 0; i < values.length; i++)
		{
			CategoryChartValue value = (CategoryChartValue) values[i];
			dataset.addValue(value.getValue(), value.getSeries(), value.getCategory());
		}

		return dataset;
	}

	private static PieDataset createPieDataset(ChartValue[] values)
	{
		DefaultPieDataset dataset = new DefaultPieDataset();

		for (int i = 0; i < values.length; i++)
		{
			PieChartValue value = (PieChartValue) values[i];
			dataset.setValue(value.getKey(), value.getValue());
		}

		return dataset;
	}

	private static XYDataset createXYDataset(ChartValue[] values)
	{
		XYSeries series = null;
		XYSeriesCollection seriesCollection = new XYSeriesCollection();

		for (int i = 0; i < values.length; i++)
		{
			XYChartValue value = (XYChartValue) values[i];

			if (series == null || !series.getKey().equals(value.getSeries()))
			{
				if (series != null)
				{
					seriesCollection.addSeries(series);
				}

				series = new XYSeries(value.getSeries());
			}

			series.add(value.getValue(), value.getYValue());
		}

		seriesCollection.addSeries(series);

		return seriesCollection;
	}	

	private static XYDataset createTimeDataset(ChartValue[] values)
	{
		TimeSeries series = null;
		TimeSeriesCollection seriesCollection = new TimeSeriesCollection();

		for (int i = 0; i < values.length; i++)
		{
			TimeChartValue value = (TimeChartValue) values[i];

			if (series == null || !series.getKey().equals(value.getSeries()))
			{
				if (series != null)
				{
					seriesCollection.addSeries(series);
				}

				series = new TimeSeries(value.getSeries(), Second.class);
			}

			series.add(new Second(value.getTime()), value.getValue());
		}

		seriesCollection.addSeries(series);

		return seriesCollection;
	}
	
	private static DefaultValueDataset createDefaultValueDataset(ChartValue[] values)
	{
		DefaultValueDataset dataset = new DefaultValueDataset(values[0].getValue());
		return dataset;
	}

	public List buildParameterList(Report report) throws ProviderException
	{
		throw new ProviderException("ChartReportEngine: buildParameterList not implemented.");
	}		
}