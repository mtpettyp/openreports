/*  
 * Copyright (C) 2006 by Open Source Software Solutions, LLC and Contributors
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
 * Original Author	:	Lisa Shields 	- brillobaby@gmail.com 
 * Contributor(s)	:	Erik Swenson 	- erik@oreports.com
 * 
 */

package org.efs.openreports.engine;

import org.efs.openreports.providers.DataSourceProvider;
import org.efs.openreports.providers.ProviderException;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Extension of the Jxls ReportManagerImpl class,
 * making it Open Reports DataSourceProvider aware.  This means 
 * JXLS reporting templates can create multiple datasource
 * reports in a single workbook.
 *
 * The standard JXLS xls template tag construct looks like this:
 * 	<jx:forEach items="${rm.exec("select email, jobtitle from employees")}" var="x">
 *
 * The underlaying ReportManagerImpl provides a single exec function,
 * with the following signature:
 *                     	rm.exec(String sql)
 * 
 * This class extends the above standard capability, without changing it, by adding
 * the following additional Report Manager exec functions:
 *
 * 			rm.exec(String datasourceName, String sql)
 * 			rm.exec(int datasourceId, String sql)
 *
 * These two additional methods support template constructs as below, enabling
 * multiple datasources (or parameter-driven datasource selection) for
 * JXLS reports in Open Reports:
 *
 * 	<jx:forEach items="${rm.exec("OpenReports Sample Data", "select email, jobtitle from employees")}" var="x">
 * 	<jx:forEach items="${rm.exec(3, "select email, jobtitle from employees")}" var="x">
 *	
 */
public class JXLSReportManagerImpl extends net.sf.jxls.report.ReportManagerImpl {
	
	protected static Logger log = Logger.getLogger(JXLSReportManagerImpl.class);

	DataSourceProvider dataSourceProvider = null;

    public JXLSReportManagerImpl(Connection connection, Map beans, DataSourceProvider dataSourceProvider) throws SQLException {
	    super( connection, beans );
	    this.dataSourceProvider = dataSourceProvider;
    }
    
    // This method allows Open Reports JXLs templates to contain ReportManager
    // invocations of the form rm.exec(String datasourceName, String sql), where
    // datasourceName can match any report_datasource.NAME configured in the Open Reports Application.
    public List exec(String datasourceName, String sql) throws SQLException, ProviderException {
	    List results = null;
	    log.debug("JXLS exec invoked for datasourceName=[" + datasourceName +"]");
	    try {		    
		    results = exec(dataSourceProvider.getDataSource(datasourceName).getId(), sql);
	    } catch( Exception e ) {
		    String msg = "Unable to complete query against datasourceName=[" + datasourceName +"]." +
		              " Please ensure the datasourceName in your xls template is valid." +
			      "  Caught exception is " + e.getClass().getName() + ", message is " + e.getMessage();      
		    log.error(msg);
		    throw new ProviderException(msg);  
	    }
	    return results;
    }
    
   // This new method allows Open Reports JXLs templates to contain ReportManager
    // invocations of the form rm.exec(int datasourceId, String sql), where
    // datasourceId can match any report_datasource.DATASOURCE_ID configured in the Open Reports Application.
    public List exec(Integer datasourceId, String sql) throws SQLException, ProviderException {
	    Connection defaultConnection=super.getConnection();
	    List list = null;
	    try {
		    super.setConnection(dataSourceProvider.getConnection(datasourceId));
		    log.debug("JXLS exec sql=[" + sql + "] against datasourceId=[" + datasourceId +"]");
		    list = super.exec(sql); // invokes net.sf.jxls.report.ReportManagerImpl.exec function
	    } catch( Exception e ) {
		    String msg = "Unable to execute sql=[" + sql + "] against datasourceId=[" + datasourceId +"]." +
		              " Please ensure the dataSourceId and SQL in your xls template are valid." +
			      "  Caught exception is " + e.getClass().getName() + ", message is " + e.getMessage();      
		    log.error(msg);		     
		    throw new ProviderException(msg);
	    } finally {
	    	super.setConnection(defaultConnection);
	    }
	    return list;
    }  
    
   
}
