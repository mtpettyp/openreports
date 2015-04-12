/*
 * Copyright (C) 2004 Erik Swenson - erik@oreports.com
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

package org.efs.openreports.actions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.efs.openreports.ORStatics;
import org.efs.openreports.objects.Report;
import org.efs.openreports.objects.ReportUser;
import org.efs.openreports.util.DisplayProperty;

import com.opensymphony.xwork2.ActionContext;

public class QueryReportResultAction extends DisplayTagAction		
{	
	private static final long serialVersionUID = -6173123870225020481L;

	protected static Logger log = Logger.getLogger(QueryReportResultAction.class);	

	private List results;
	private DisplayProperty[] properties;

	private Report report;
	private Map reportParameters;	

	public String execute()
	{
		ReportUser user = (ReportUser) ActionContext.getContext().getSession().get(
				ORStatics.REPORT_USER);

		report = (Report) ActionContext.getContext().getSession().get(ORStatics.REPORT);		
		reportParameters = getReportParameterMap(user);
		
		results = (List) ActionContext.getContext().getSession().get(ORStatics.QUERY_REPORT_RESULTS);		
		properties = (DisplayProperty[]) ActionContext.getContext().getSession().get(ORStatics.QUERY_REPORT_PROPERTIES);	

        if (report.getFile() != null && report.getFile().endsWith(".ftl"))
        {
            return ORStatics.QUERY_REPORT_TEMPLATE_ACTION;
        }
        
		return SUCCESS;
	}	

	@SuppressWarnings("unchecked")
	protected Map getReportParameterMap(ReportUser user)
	{
		Map<String,Object> reportParameters = new HashMap<String,Object>();

		if (ActionContext.getContext().getSession().get(ORStatics.REPORT_PARAMETERS) != null)
		{
			reportParameters = (Map) ActionContext.getContext().getSession().get(
					ORStatics.REPORT_PARAMETERS);
		}

		// add standard report parameters
		reportParameters.put(ORStatics.USER_ID, user.getId());
		reportParameters.put(ORStatics.EXTERNAL_ID, user.getExternalId());
		reportParameters.put(ORStatics.USER_NAME, user.getName());

		return reportParameters;
	}	

	public List getResults()
	{
		return results;
	}

	public DisplayProperty[] getProperties()
	{
		return properties;
	}

	public Report getReport()
	{
		return report;
	}

	public Map getReportParameters()
	{
		return reportParameters;
	}	
}