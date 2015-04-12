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
package org.efs.openreports.dispatcher;

import com.opensymphony.xwork2.ActionInvocation;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.views.freemarker.FreemarkerResult;
import org.efs.openreports.actions.QueryReportResultAction;
import org.efs.openreports.objects.Report;
import org.efs.openreports.providers.DirectoryProvider;

/** 
 * QueryReportTemplateResult loads the reports FreeMarker template from the report directory.
 * 
 * @author erik@oreports.com
 */
public class QueryReportTemplateResult extends FreemarkerResult
{        
    private static final long serialVersionUID = -8316005191338172951L;    
    private static Logger log = Logger.getLogger(QueryReportTemplateResult.class);
    
    private static final String CFG_SERLVET_CONTEXT_KEY = "queryReportConfiguration";
    
    private DirectoryProvider directoryProvider;
    
    @Override     
    protected Configuration getConfiguration() throws TemplateException 
    {        
    	Configuration cfg = (Configuration) ServletActionContext.getServletContext().getAttribute(CFG_SERLVET_CONTEXT_KEY);
    	
    	if (cfg == null)
    	{
    		cfg = (Configuration) super.getConfiguration().clone();
    		
    		try
    		{
    			cfg.setDirectoryForTemplateLoading(new File(directoryProvider.getReportDirectory()));
    		}
    		catch(Exception e)
    		{
    			log.error("QueryReport Template Loader Exception", e);
    		}
    		
    		ServletActionContext.getServletContext().setAttribute(CFG_SERLVET_CONTEXT_KEY, cfg); 
    		log.info("QueryReport Template FreeMarker Configuration created.");
    	}       
        
        return cfg;
    }

    @Override
    public void doExecute(String location, ActionInvocation invocation) throws IOException, TemplateException 
    {        
        QueryReportResultAction queryReportAction = (QueryReportResultAction) invocation.getAction();
        Report report = queryReportAction.getReport();            
        
        super.doExecute(report.getFile(), invocation);
    }
    
    public void setDirectoryProvider(DirectoryProvider directoryProvider) 
    {
        this.directoryProvider = directoryProvider;
    }    
}
