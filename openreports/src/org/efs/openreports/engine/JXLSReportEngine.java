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

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jxls.transformer.XLSTransformer;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.efs.openreports.ORStatics;
import org.efs.openreports.engine.input.ReportEngineInput;
import org.efs.openreports.engine.output.QueryEngineOutput;
import org.efs.openreports.engine.output.ReportEngineOutput;
import org.efs.openreports.objects.Report;
import org.efs.openreports.providers.DataSourceProvider;
import org.efs.openreports.providers.DirectoryProvider;
import org.efs.openreports.providers.PropertiesProvider;
import org.efs.openreports.providers.ProviderException;

/**
 * Generates a ReportEngineOutput from a ReportEngineInput. The output consists
 * of a byte[] containing an XLS spreadsheet.
 * 
 * @author Erik Swenson
 * 
 */

public class JXLSReportEngine extends ReportEngine
{
	protected static Logger log = Logger.getLogger(JXLSReportEngine.class);
	
	public JXLSReportEngine(DataSourceProvider dataSourceProvider,
			DirectoryProvider directoryProvider, PropertiesProvider propertiesProvider)
	{
		super(dataSourceProvider,directoryProvider, propertiesProvider);
	}
	
	public ReportEngineOutput generateReport(ReportEngineInput input)
			throws ProviderException
	{
		Connection conn = null;

		try
		{
			Report report = input.getReport();
						
			// create new HashMap to send to JXLS in order to maintain original map of parameters
			Map<String,Object> jxlsReportMap = new HashMap<String,Object>(input.getParameters());
			
			if (report.getQuery() != null && report.getQuery().trim().length() > 0)
			{
				QueryReportEngine queryEngine = new QueryReportEngine(dataSourceProvider, directoryProvider, propertiesProvider);				
				
				// set ExportType to null so QueryReportEngine just returns a list of results
				input.setExportType(null);
				
				QueryEngineOutput output = (QueryEngineOutput) queryEngine
						.generateReport(input);		
				
				jxlsReportMap.put(ORStatics.JXLS_REPORT_RESULTS, output.getResults());
			}
			else
			{
				conn = dataSourceProvider.getConnection(report.getDataSource().getId());
				JXLSReportManagerImpl rm = new JXLSReportManagerImpl(conn, jxlsReportMap, dataSourceProvider);
				jxlsReportMap.put("rm", rm);
			}

			FileInputStream template = new FileInputStream(directoryProvider
					.getReportDirectory()
					+ report.getFile());

			XLSTransformer transformer = new XLSTransformer();
			HSSFWorkbook workbook = transformer.transformXLS(template, jxlsReportMap);			

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			workbook.write(out);
			
			ReportEngineOutput output = new ReportEngineOutput();
			output.setContent(out.toByteArray());
			output.setContentType(ReportEngineOutput.CONTENT_TYPE_XLS);
			
			return output;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new ProviderException(e);
		}
		finally
		{
			try
			{
				if (conn != null) conn.close();
			}
			catch (Exception c)
			{
				log.error("Error closing");
			}
		}
	}
	
	public List buildParameterList(Report report) throws ProviderException
	{
		throw new ProviderException("JXLSReportEngine: buildParameterList not implemented.");
	}		
}