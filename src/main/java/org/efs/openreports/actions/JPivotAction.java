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
package org.efs.openreports.actions;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import java.util.Map;
import org.apache.log4j.Logger;
import org.efs.openreports.ORStatics;
import org.efs.openreports.objects.ORProperty;
import org.efs.openreports.objects.Report;
import org.efs.openreports.providers.PropertiesProvider;
import org.efs.openreports.util.LocalStrings;
import org.efs.openreports.util.ORUtil;

public class JPivotAction extends ActionSupport 
{   
    protected static Logger log = Logger.getLogger(JPivotAction.class);
    
    private static final long serialVersionUID = 167823208513025513L;
   
    private Report report;
    private String query;
    
    private String xmlaUri;
    private String xmlaDataSource;
    private String xmlaCatalog;
    
    private PropertiesProvider propertiesProvider;  
    
    public String execute() throws Exception 
    {  	
    	Map reportParameters = (Map) ActionContext.getContext().getSession().get(ORStatics.REPORT_PARAMETERS);
         
    	report = (Report) ActionContext.getContext().getSession().get(ORStatics.REPORT);         
        query = ORUtil.parseStringWithParameters(report.getQuery(), reportParameters); 
        
        try
        {        
            xmlaUri = propertiesProvider.getProperty(ORProperty.XMLA_URL).getValue();
            xmlaDataSource = propertiesProvider.getProperty(ORProperty.XMLA_DATASOURCE).getValue();
            xmlaCatalog = propertiesProvider.getProperty(ORProperty.XMLA_CATALOG).getValue();
        }
        catch(Exception e)
        {
            log.error(e);
            addActionError(getText(LocalStrings.ERROR_XMLA_PROPERTIES_INVALID));            
            return ERROR;        
        }
        
        return SUCCESS;
    }    
    
    public String getQuery()
    {
        return query;
    }
    
    public Report getReport() 
    {
        return report;
    }
    
    public String getXmlaCatalog()
    {
        return xmlaCatalog;
    }
    
    public String getXmlaDataSource() 
    {
        return xmlaDataSource;
    }
    
    public String getXmlaUri() 
    {
        return xmlaUri;
    }
    
    public void setPropertiesProvider(PropertiesProvider propertiesProvider) 
    {
        this.propertiesProvider = propertiesProvider;
    }	 
}
