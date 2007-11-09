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

import java.io.File;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import mondrian.xmla.DataSourcesConfig;
import mondrian.xmla.XmlaServlet;
import mondrian.xmla.impl.DefaultXmlaServlet;
import org.efs.openreports.providers.DirectoryProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class XMLADispatcher extends DefaultXmlaServlet
{     
    private static final long serialVersionUID = 1L;
    
    private DirectoryProvider directoryProvider;
    
    public void init(ServletConfig servletConfig) throws ServletException
    {       
        ApplicationContext appContext = WebApplicationContextUtils
            .getRequiredWebApplicationContext(servletConfig.getServletContext());
    
        directoryProvider = (DirectoryProvider) appContext.getBean("directoryProvider", DirectoryProvider.class);
       
        super.init(servletConfig);        
    }
    
    protected DataSourcesConfig.DataSources makeDataSources(ServletConfig servletConfig) 
    {
        File file = new File(directoryProvider.getReportDirectory() + XmlaServlet.DEFAULT_DATASOURCE_FILE);
        
        try
        {
            return parseDataSourcesUrl(file.toURL());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
