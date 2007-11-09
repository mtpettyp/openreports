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

package org.efs.openreports.services.util;

import java.util.ArrayList;

import org.efs.openreports.ReportConstants.ExportType;
import org.efs.openreports.objects.DeliveredReport;
import org.efs.openreports.objects.Report;
import org.efs.openreports.objects.ReportParameterMap;
import org.efs.openreports.objects.ReportUser;
import org.efs.openreports.objects.ReportUserAlert;
import org.efs.openreports.services.info.AlertInfo;
import org.efs.openreports.services.info.DeliveredReportInfo;
import org.efs.openreports.services.info.ParameterInfo;
import org.efs.openreports.services.info.ReportInfo;
import org.efs.openreports.services.info.UserInfo;

/**
 * Provides static methods for converting standard OpenReports objects
 * to info objects.
 * 
 * @author Erik Swenson
 */

public class Converter
{
	public static ReportInfo convertToReportInfo(Report report)
	{		
		ReportInfo reportInfo = new ReportInfo();	
		reportInfo.setId(report.getId());
		reportInfo.setName(report.getName());
		reportInfo.setDescription(report.getDescription());
		reportInfo.setCsvExportEnabled(report.isCsvExportEnabled());
		reportInfo.setExcelExportEnabled(report.isExcelExportEnabled());
		reportInfo.setHtmlExportEnabled(report.isHtmlExportEnabled());
		reportInfo.setPdfExportEnabled(report.isPdfExportEnabled());
		reportInfo.setRtfExportEnabled(report.isRtfExportEnabled());
		reportInfo.setTextExportEnabled(report.isTextExportEnabled());
		reportInfo.setXlsExportEnabled(report.isXlsExportEnabled());
		reportInfo.setHidden(report.isHidden());
		
		if (report.isJXLSReport())
		{
			reportInfo.setDefaultExportType(ExportType.XLS);
		}
		else if (report.isHtmlExportEnabled() || report.isQueryReport() || report.isChartReport())
		{
			reportInfo.setDefaultExportType(ExportType.HTML);
		}
		else
		{
			reportInfo.setDefaultExportType(ExportType.PDF);
		}
		
		if (report.getParameters() != null && report.getParameters().size() > 0)
		{
			ArrayList<ReportParameterMap> reportParameters = new ArrayList<ReportParameterMap>(report.getParameters());
			
			ParameterInfo[] parameters = new ParameterInfo[reportParameters.size()];
			for (int x = 0; x < reportParameters.size(); x++)
			{
				ReportParameterMap reportParameter = (ReportParameterMap) reportParameters.get(x);
				
				ParameterInfo parameter = new ParameterInfo();
				parameter.setId(reportParameter.getReportParameter().getId());
				parameter.setName(reportParameter.getReportParameter().getName());
				parameter.setDescription(reportParameter.getReportParameter().getDescription());
				
				parameters[x] = parameter;
			}
			
			reportInfo.setParameters(parameters);
		}
							
		return reportInfo;
	}
	
	public static UserInfo convertToUserInfo(ReportUser reportUser)
	{
		UserInfo userInfo = new UserInfo();
		userInfo.setId(reportUser.getId());
		userInfo.setName(reportUser.getName());
		userInfo.setPassword(reportUser.getPassword());
		userInfo.setEmailAddress(reportUser.getEmail());
		userInfo.setExternalId(reportUser.getExternalId());
		
		return userInfo;
	}
	
	public static AlertInfo convertToAlertInfo(ReportUserAlert userAlert)
	{
		AlertInfo alertInfo = new AlertInfo();
		alertInfo = new AlertInfo();
		alertInfo.setAlertName(userAlert.getAlert().getName());
		alertInfo.setCondition(userAlert.getCondition());
		alertInfo.setCount(userAlert.getCount());		
		alertInfo.setTriggered(userAlert.isTriggered());
        
        if (userAlert.getReport() != null)
        {
            alertInfo.setReportName(userAlert.getReport().getName());
        }
		
		return alertInfo;
	}
	
	public static DeliveredReportInfo convertToDeliveredReportInfo(DeliveredReport report)
	{
		DeliveredReportInfo info = new DeliveredReportInfo();
		info.setReportDescription(report.getReportDescription());
		info.setReportFileName(report.getReportFileName());
		info.setReportName(report.getReportName());
		info.setRunDate(report.getRunDate());
		info.setUserName(report.getUserName());
        info.setDeliveryMethod(report.getDeliveryMethod());
        info.setRequestId(report.getRequestId());
		
		return info;
	}
    
    public static DeliveredReport convertToDeliveredReport(DeliveredReportInfo info)
    {
        DeliveredReport deliveredReport = new DeliveredReport();
        deliveredReport.setReportDescription(info.getReportDescription());
        deliveredReport.setReportFileName(info.getReportFileName());
        deliveredReport.setReportName(info.getReportName());
        deliveredReport.setRunDate(info.getRunDate());
        deliveredReport.setUserName(info.getUserName());
        deliveredReport.setDeliveryMethod(info.getDeliveryMethod());
        deliveredReport.setRequestId(info.getRequestId());
        
        return deliveredReport;
    }
}
