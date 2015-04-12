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

package org.efs.openreports.actions.admin;

import java.util.*;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.efs.openreports.ORStatics;
import org.efs.openreports.objects.*;
import org.efs.openreports.providers.*;

public class EditUserAlertsAction extends ActionSupport implements SessionAware
{	
    private static final long serialVersionUID = -164042143249798706L;

    protected static Logger log = Logger.getLogger(EditUserAlertsAction.class);

    private Map<Object,Object> session;
    
	private int id;
	private int alertId;
	private int reportId;

	private int alertLimit;
	private String alertOperator;	

	private String submitAdd;
	private String submitUpdate;
	private String submitDelete;

	private String command;

	private ReportUser reportUser;	

	private UserProvider userProvider;
	private AlertProvider alertProvider;
	private ReportProvider reportProvider;

	@Override
	public String execute()
	{
		try
		{
			reportUser = userProvider.getUser(new Integer(id));					

			if (submitAdd != null)
			{
				ReportAlert alert = alertProvider.getReportAlert(new Integer(alertId));
				
				ReportUserAlert userAlert = new ReportUserAlert();
				userAlert.setAlert(alert);
				userAlert.setUser(reportUser);			
				userAlert.setLimit(alertLimit);
				userAlert.setOperator(alertOperator);
				
				if (reportId > 0)
				{
					userAlert.setReport(reportProvider.getReport(new Integer(reportId)));
				}
				else
				{
					userAlert.setReport(null);
				}
			
				
				reportUser.getAlerts().add(userAlert);				

				userProvider.updateUser(reportUser);
			}

			if (submitUpdate != null)
			{				
				ReportUserAlert userAlert = reportUser.getAlerts().get(alertId -1);				
				userAlert.setLimit(alertLimit);
				userAlert.setOperator(alertOperator);
				
				if (reportId > 0)
				{
					userAlert.setReport(reportProvider.getReport(new Integer(reportId)));
				}
				else
				{
					userAlert.setReport(null);
				}

				userProvider.updateUser(reportUser);
			}

			if (submitDelete != null)
			{				
				ReportUserAlert userAlert = reportUser.getAlerts().get(alertId -1);		

				reportUser.getAlerts().remove(userAlert);

				userProvider.updateUser(reportUser);
			}
			
			if (submitAdd != null || submitUpdate != null || submitDelete != null)
			{
				ReportUser currentUser = (ReportUser) ActionContext.getContext().getSession().get(ORStatics.REPORT_USER);
				if (currentUser.getId().equals(reportUser.getId()))
				{
					session.put(ORStatics.REPORT_USER, reportUser);
				}
			}

			return INPUT;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			addActionError(e.toString());
			return INPUT;
		}
	}
    
    @SuppressWarnings("unchecked")
    public void setSession(Map session) {
        this.session = session;
    }

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public List<ReportAlert> getAlerts()
	{
		try
		{
			return alertProvider.getReportAlerts();
		}
		catch (Exception e)
		{
			addActionError(e.toString());
			return null;
		}
	}

	public List<ReportUserAlert> getUserAlerts()
	{		
		return reportUser.getAlerts();
	}

	public int getAlertId()
	{
		return alertId;
	}

	public void setAlertId(int alertId)
	{
		this.alertId = alertId;
	}

	public int getAlertLimit()
	{
		return alertLimit;
	}

	public void setAlertLimit(int alertLimit)
	{
		this.alertLimit = alertLimit;
	}

	public String getAlertOperator()
	{
		return alertOperator;
	}

	public void setAlertOperator(String alertOperator)
	{
		this.alertOperator = alertOperator;
	}

	public String getCommand()
	{
		return command;
	}

	public void setCommand(String command)
	{
		this.command = command;
	}

	public String getSubmitAdd()
	{
		return submitAdd;
	}

	public void setSubmitAdd(String submitAdd)
	{
		this.submitAdd = submitAdd;
	}

	public String getSubmitDelete()
	{
		return submitDelete;
	}

	public void setSubmitDelete(String submitDelete)
	{
		this.submitDelete = submitDelete;
	}

	public String getSubmitUpdate()
	{
		return submitUpdate;
	}

	public void setSubmitUpdate(String submitUpdate)
	{
		this.submitUpdate = submitUpdate;
	}

	public ReportUser getReportUser()
	{
		return reportUser;
	}

	public void setAlertProvider(AlertProvider alertProvider)
	{
		this.alertProvider = alertProvider;
	}

	public void setUserProvider(UserProvider userProvider)
	{
		this.userProvider = userProvider;
	}
	
	public String[] getOperators()
	{
		return new String[] {ReportAlert.OPERATOR_EQUAL, ReportAlert.OPERATOR_GREATER, ReportAlert.OPERATOR_LESS};
	}

	public void setReportProvider(ReportProvider reportProvider)
	{
		this.reportProvider = reportProvider;
	}	

	public int getReportId()
	{
		return reportId;
	}

	public void setReportId(int reportId)
	{
		this.reportId = reportId;
	}
}