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
package org.efs.openreports.engine;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.efs.openreports.ReportConstants.ExportType;
import org.efs.openreports.engine.input.ReportEngineInput;
import org.efs.openreports.engine.output.QueryEngineOutput;
import org.efs.openreports.engine.output.ReportEngineOutput;
import org.efs.openreports.objects.Report;
import org.efs.openreports.providers.DataSourceProvider;
import org.efs.openreports.providers.DirectoryProvider;
import org.efs.openreports.providers.PropertiesProvider;
import org.efs.openreports.providers.ProviderException;

public class VelocityReportEngine extends ReportEngine
{
    protected static Logger log = Logger.getLogger(VelocityReportEngine.class);        
    
    public VelocityReportEngine(DataSourceProvider dataSourceProvider,
            DirectoryProvider directoryProvider, PropertiesProvider propertiesProvider)
    {
        super(dataSourceProvider,directoryProvider, propertiesProvider);
    }
    
    public ReportEngineOutput generateReport(ReportEngineInput input) throws ProviderException
    {
        Report report = input.getReport();
        Map parameters = input.getParameters();
        
        try
        {
            Properties properties = new Properties();
            properties.setProperty("file.resource.loader.path", directoryProvider.getReportDirectory());
            properties.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogSystem");
            
            Velocity.init(properties);
           
            VelocityContext context = new VelocityContext();
            context.put( "parameters", parameters);
            context.put("report", report);            
            
            /* 
             * if report has a query, process it like a QueryReport and 
             * put the results in the VelocityContext for use in templates
             */
            if (report.getQuery() != null && report.getQuery().trim().length() > 0)
            {
                QueryReportEngine queryReport = new QueryReportEngine(dataSourceProvider, directoryProvider, propertiesProvider);
                QueryEngineOutput queryOutput = (QueryEngineOutput) queryReport.generateReport(input);
                
                context.put("results", queryOutput.getResults());
                context.put("properties", queryOutput.getProperties());
            }
           
            StringWriter writer = new StringWriter();
            
            Template template = Velocity.getTemplate(report.getFile());           
            template.merge(context, writer);          
            
            ReportEngineOutput output = new ReportEngineOutput();
            output.setContent(writer.toString().getBytes());
            
            if (input.getExportType() == ExportType.HTML)
            {
                output.setContentType(ReportEngineOutput.CONTENT_TYPE_HTML);
            }
            else
            {
                output.setContentType(ReportEngineOutput.CONTENT_TYPE_TEXT);
            }
            
            return output;
        }
        catch(Exception e)
        {
            throw new ProviderException(e);
        }          
    }   
    
    public List buildParameterList(Report report) throws ProviderException
    {
        throw new ProviderException("VelocityReportEngine: buildParameterList not implemented.");
    }       
}