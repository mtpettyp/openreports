/*
 * Copyright (C) 2003 Erik Swenson - erik@oreports.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 */

package org.efs.openreports;

public interface ORStatics
{
	// session keys   
	public static final String REPORT = "report";
	public static final String REPORT_SCHEDULE = "reportSchedule";
	public static final String REPORT_USER = "user";
	public static final String REPORT_GROUP = "group";
	public static final String REPORT_PARAMETERS = "reportParameters";
	public static final String REPORT_LOG = "reportLog";
	public static final String EXPORT_TYPE = "exportType";
	public static final String JASPERPRINT = "jasperPrint";	
	public static final String IMAGES_MAP = "IMAGES_MAP";	
	public static final String QUERY_REPORT_RESULTS = "query_report_results";
	public static final String QUERY_REPORT_PROPERTIES = "query_report_properties";
	public static final String JXLS_REPORT_RESULTS = "results";
    public static final String BREADCRUMBS = "breadcrumbs";   
	
	// action names
	public static final String QUERY_REPORT_ACTION = "queryReport";
    public static final String QUERY_REPORT_TEMPLATE_ACTION = "queryReportTemplate";
	public static final String CHART_REPORT_ACTION = "chartReport";
	public static final String JXLSREPORT_ACTION = "jxlsReport";
	public static final String JFREEREPORT_RESULT = "jfreeReport";	
    public static final String JPIVOT_ACTION = "jpivot";
	public static final String SCHEDULE_REPORT_ACTION = "reportSchedule";
	public static final String NOT_AUTHORIZED = "notauthorized";	
	public static final String DASHBOARD_ACTION = "dashboard";
	public static final String PROMPT_PARAMETERS_ACTION = "reportDetail";
	public static final String GENERATED_REPORTS_ACTION = "generatedReports";
	
	// standard report parameter names
	public static final String USER_ID = "OPENREPORTS_USER_ID";
	public static final String USER_NAME = "OPENREPORTS_USER_NAME";
	public static final String EXTERNAL_ID = "OPENREPORTS_USER_EXTERNALID";
	public static final String IMAGE_DIR = "OPENREPORTS_IMAGE_DIR";
	public static final String REPORT_DIR = "OPENREPORTS_REPORT_DIR";
	public static final String EXPORT_TYPE_PARAM = "OPENREPORTS_EXPORT_TYPE";		

	public static final String[] EXPORT_TYPES = new String[] { "PDF", "XLS", "HTML", "CSV",
			"IMAGE", "RFT", "TEXT", "EXCEL" };	
	
	// roles
	public static final String OPENREPORTS_ROLE = "OPENREPORTS_ROLE";
	public static final String SCHEDULER_ROLE = "SCHEDULER_ROLE";
	public static final String ROOT_ADMIN_ROLE = "ROOT_ADMIN_ROLE";
	public static final String DATASOURCE_ADMIN_ROLE = "DATASOURCE_ADMIN_ROLE";
	public static final String REPORT_ADMIN_ROLE = "REPORT_ADMIN_ROLE";
	public static final String PARAMETER_ADMIN_ROLE = "PARAMETER_ADMIN_ROLE";
	public static final String USER_ADMIN_ROLE = "USER_ADMIN_ROLE";
	public static final String GROUP_ADMIN_ROLE = "GROUP_ADMIN_ROLE";
	public static final String CHART_ADMIN_ROLE = "CHART_ADMIN_ROLE";
	public static final String LOG_VIEWER_ROLE = "LOG_VIEWER_ROLE";
	public static final String ALERT_ADMIN_ROLE = "ALERT_ADMIN_ROLE";
	public static final String ALERT_USER_ROLE = "ALERT_USER_ROLE";	
	public static final String DASHBOARD_ROLE = "DASHBOARD_ROLE";
	public static final String UPLOAD_ROLE = "UPLOAD_ROLE";
	public static final String SCHEDULER_ADMIN_ROLE = "SCHEDULER_ADMIN_ROLE";
	public static final String ADVANCED_SCHEDULER_ROLE = "ADVANCED_SCHEDULER_ROLE";
	
	public static final String[] ADMIN_ROLES = new String[] { ROOT_ADMIN_ROLE,
			DATASOURCE_ADMIN_ROLE, REPORT_ADMIN_ROLE, PARAMETER_ADMIN_ROLE,
			USER_ADMIN_ROLE, GROUP_ADMIN_ROLE, CHART_ADMIN_ROLE, ALERT_ADMIN_ROLE,
			SCHEDULER_ADMIN_ROLE };
	
}
