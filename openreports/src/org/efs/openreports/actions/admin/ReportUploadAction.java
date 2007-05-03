/*
 * Copyright (C) 2002 Erik Swenson - erik@oreports.com
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

import java.io.File;
import java.util.List;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.design.JRJdtCompiler;
import net.sf.jasperreports.engine.util.JRProperties;

import org.apache.log4j.Logger;
import org.efs.openreports.providers.ProviderException;
import org.efs.openreports.providers.ReportProvider;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import com.opensymphony.xwork2.ActionSupport;

public class ReportUploadAction extends ActionSupport  
{	
	private static final long serialVersionUID = 3832737720793888800L;

	protected static Logger log = Logger.getLogger(ReportUploadAction.class);

	private ReportProvider reportProvider;

	private List reportFileNames;
    private String uploadedFileName;

	private String command;

	public String execute()
	{
		try
		{
			if ((command != null) && (command.equals("upload")))
			{
				MultiPartRequestWrapper multiWrapper = (MultiPartRequestWrapper) ServletActionContext.getRequest();

				File[] files = multiWrapper.getFiles("file");
				if (files.length > 0 && files[0] != null)
				{
					File reportUploadFile = files[0];
                    uploadedFileName = reportUploadFile.getName();
					String reportUploadFileName = reportUploadFile.getAbsolutePath();
					
                    if ((reportUploadFileName.endsWith(".xml")) || (reportUploadFileName.endsWith(".jrxml")))
					{
						try
						{
							System.setProperty(JRProperties.COMPILER_CLASS, JRJdtCompiler.class.getName());
							JRProperties.setProperty(JRProperties.COMPILER_CLASS, JRJdtCompiler.class.getName());
								
							JasperCompileManager.compileReportToFile(reportUploadFileName);
						}
						catch (Exception e)
						{
							if (e.toString().indexOf("groovy") > -1)
							{
							    try
								{
									System.setProperty(JRProperties.COMPILER_CLASS,"net.sf.jasperreports.compilers.JRGroovyCompiler" );
									JRProperties.setProperty(JRProperties.COMPILER_CLASS,"net.sf.jasperreports.compilers.JRGroovyCompiler");
					                   
					                JasperCompileManager.compileReportToFile(reportUploadFileName);
								}
								catch(Exception ex)
								{
									log.error("Failed to compile report: " + reportUploadFileName, e);
									addActionError("Failed to compile report: " + e.toString());
								}
							}
							else
							{								
								log.error("Failed to compile report: " + reportUploadFileName, e);
								addActionError("Failed to compile report: " + e.toString());
							}
						}
					}
				}
                else
                {
                    addActionError("Invalid File.");
                }
			}

			reportFileNames = reportProvider.getReportFileNames();
		}
		catch (ProviderException pe)
		{
			addActionError(pe.getMessage());
		}       

		return SUCCESS;
	}	
	
    public String getUploadedFileName() 
    {
        return uploadedFileName;
    }

    public List getReportFileNames()
	{
		return reportFileNames;
	}

	public void setReportProvider(ReportProvider reportProvider)
	{
		this.reportProvider = reportProvider;
	}

	public String getCommand()
	{
		return command;
	}

	public void setCommand(String command)
	{
		this.command = command;
	}

}