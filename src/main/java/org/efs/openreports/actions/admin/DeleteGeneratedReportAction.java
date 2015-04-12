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

import java.io.File;
import java.io.FileInputStream;

import org.efs.openreports.objects.DeliveredReport;
import org.efs.openreports.providers.DateProvider;
import org.efs.openreports.providers.DirectoryProvider;

import com.thoughtworks.xstream.XStream;

public class DeleteGeneratedReportAction extends DeleteAction 		
{	
    private static final long serialVersionUID = -3946731095408147356L;
    
    private String runDate;
	private String reportName;
	private String description;
	private String fileName;
	
	private DateProvider dateProvider;
	private DirectoryProvider directoryProvider;
	
	public String execute()
	{
		try
		{			
			if (!submitDelete && !submitCancel)
			{				
				XStream xStream = new XStream();
				xStream.alias("reportGenerationInfo", DeliveredReport.class);

				String xmlFileName = fileName.substring(0, fileName.indexOf(".")) + ".xml";
				System.out.println(xmlFileName);
				
				File file = new File(directoryProvider.getReportGenerationDirectory() + xmlFileName);
				FileInputStream inputStream = new FileInputStream(file);
			
				DeliveredReport info = (DeliveredReport) xStream
					.fromXML(inputStream);				
				
				runDate = dateProvider.formatDate(info.getRunDate());					
				reportName = info.getReportName();	
				description = info.getReportDescription();
				
				inputStream.close();
				
				return INPUT;
			}

			if (submitDelete)
			{
				fileName = directoryProvider.getReportGenerationDirectory() + fileName;
				
				File file = new File(fileName);				
				file.delete();
				
				fileName = fileName.substring(0, fileName.indexOf(".")) + ".xml";
								
				file = new File(fileName);
				file.delete();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			addActionError(e.getMessage());
			return INPUT;
		}

		return SUCCESS;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public String getReportName()
	{
		return reportName;
	}

	public String getRunDate()
	{
		return runDate;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDateProvider(DateProvider dateProvider)
	{
		this.dateProvider = dateProvider;
	}

	public void setDirectoryProvider(DirectoryProvider directoryProvider)
	{
		this.directoryProvider = directoryProvider;
	}	
}