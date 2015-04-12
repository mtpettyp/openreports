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

package org.efs.openreports.engine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.util.JRQueryExecuter;

import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.RowSetDynaClass;
import org.apache.log4j.Logger;
import org.displaytag.tags.ColumnTag;
import org.displaytag.tags.TableTag;
import org.displaytag.tags.TableTagParameters;
import org.efs.openreports.ReportConstants.ExportType;
import org.efs.openreports.engine.input.ReportEngineInput;
import org.efs.openreports.engine.output.QueryEngineOutput;
import org.efs.openreports.engine.output.ReportEngineOutput;
import org.efs.openreports.objects.ORProperty;
import org.efs.openreports.objects.Report;
import org.efs.openreports.objects.ReportDataSource;
import org.efs.openreports.objects.ReportParameter;
import org.efs.openreports.providers.DataSourceProvider;
import org.efs.openreports.providers.DirectoryProvider;
import org.efs.openreports.providers.PropertiesProvider;
import org.efs.openreports.providers.ProviderException;
import org.efs.openreports.util.DisplayProperty;
import org.efs.openreports.util.ORUtil;
import org.efs.openreports.util.displaytag.MockDisplayTablePageContext;
import org.efs.openreports.util.displaytag.MockDisplayTableTag;

/**
*  QueryReport ReportEngine implementation.
* 
* @author Erik Swenson
* 
*/
public class QueryReportEngine extends ReportEngine
{
	protected static Logger log = Logger.getLogger(QueryReportEngine.class);		
	
	public QueryReportEngine(DataSourceProvider dataSourceProvider,
			DirectoryProvider directoryProvider, PropertiesProvider propertiesProvider)
	{
		super(dataSourceProvider,directoryProvider, propertiesProvider);
	}
	
	public ReportEngineOutput generateReport(ReportEngineInput input) throws ProviderException
	{
		Connection conn = null;
		PreparedStatement pStmt = null;
		ResultSet rs = null;

		try
		{
			Report report = input.getReport();
			Map<String,Object> parameters = input.getParameters();
			
			ReportDataSource dataSource = report.getDataSource();
			conn = dataSourceProvider.getConnection(dataSource.getId());

			if (parameters == null || parameters.isEmpty())
			{
				pStmt = conn.prepareStatement(report.getQuery());
			}
			else
			{
				// Use JasperReports Query logic to parse parameters in chart
				// queries

				JRDesignQuery query = new JRDesignQuery();
				query.setText(report.getQuery());

				// convert parameters to JRDesignParameters so they can be
				// parsed
				Map<String,JRDesignParameter> jrParameters = ORUtil.buildJRDesignParameters(parameters);

				pStmt = JRQueryExecuter.getStatement(query, jrParameters, parameters,
						conn);
			}

			ORProperty maxRows = propertiesProvider
					.getProperty(ORProperty.QUERYREPORT_MAXROWS);
			if (maxRows != null && maxRows.getValue() != null)
			{
				pStmt.setMaxRows(Integer.parseInt(maxRows.getValue()));
			}

			rs = pStmt.executeQuery();

			RowSetDynaClass rowSetDynaClass = new RowSetDynaClass(rs);

			List<?> results = rowSetDynaClass.getRows();

			DynaProperty[] dynaProperties = rowSetDynaClass.getDynaProperties();

			DisplayProperty[] properties = new DisplayProperty[dynaProperties.length];
			for (int i = 0; i < dynaProperties.length; i++)
			{
				properties[i] = new DisplayProperty(dynaProperties[i].getName(),
						dynaProperties[i].getType().getName());
			}

			rs.close();
			
			QueryEngineOutput output = new QueryEngineOutput();
			
			if (input.getExportType() == null) 
			{
				output.setResults(results);
				output.setProperties(properties);
			}
			else 
			{
				// Use DisplayTag to generate scheduled QueryReports
				MockDisplayTablePageContext pageContext = null;				
				
				if (input.getExportType() == ExportType.CSV) 
				{
					pageContext = new MockDisplayTablePageContext(MockDisplayTablePageContext.EXPORT_TYPE_CSV, applicationContext);
					output.setContentType(ReportEngineOutput.CONTENT_TYPE_CSV);
					
				}
				else if (input.getExportType() == ExportType.XLS)
				{
					pageContext = new MockDisplayTablePageContext(MockDisplayTablePageContext.EXPORT_TYPE_XLS, applicationContext);
					output.setContentType(ReportEngineOutput.CONTENT_TYPE_XLS);
				}
				else
				{
					pageContext = new MockDisplayTablePageContext(MockDisplayTablePageContext.EXPORT_TYPE_PDF, applicationContext);
					output.setContentType(ReportEngineOutput.CONTENT_TYPE_PDF);
				}
					
				// create tag 
				MockDisplayTableTag displayTag = new MockDisplayTableTag();
				displayTag.setPageContext(pageContext);
				displayTag.setName(results);			
				displayTag.doStartTag();
					
				// add columns
				for (int i = 0; i < properties.length; i++)
				{
					ColumnTag column = new ColumnTag();					
					column.setParent(displayTag);
					column.setProperty(properties[i].getName());
					column.setTitle(properties[i].getDisplayName());
					column.setDecorator(properties[i].getDecorator());
					column.setPageContext(pageContext);	
					column.doStartTag();
					column.doEndTag();
				}
				
				// call doAfterBody for each row in the results
				for (int i= 0; i < results.size(); i++)
				{
					displayTag.doAfterBody();	
				}				
				
				// call doEndTag to perform the export
				displayTag.doEndTag();
				
				HashMap<?,?> map = (HashMap<?,?>) pageContext.getRequest().getAttribute(TableTag.FILTER_CONTENT_OVERRIDE_BODY);
				Object content = map.get(TableTagParameters.BEAN_BODY);
				
				if (content instanceof String)
				{
					output.setContent(((String)content).getBytes());
				}
				else
				{
					output.setContent((byte[])content);
				}
			}
			
			return output;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new ProviderException("Error executing report query: " + e.getMessage());			
		}
		finally
		{
			try
			{
				if (pStmt != null) pStmt.close();
				if (conn != null) conn.close();
			}
			catch (Exception c)
			{
				log.error("Error closing");
			}
		}
	}	
	
	public List<ReportParameter> buildParameterList(Report report) throws ProviderException
	{
		throw new ProviderException("QueryReportEngine: buildParameterList not implemented.");
	}		
}