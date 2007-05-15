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

package org.efs.openreports.actions.schedule;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.thoughtworks.xstream.XStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.efs.openreports.ORStatics;
import org.efs.openreports.objects.GeneratedReport;
import org.efs.openreports.objects.ReportUser;
import org.efs.openreports.providers.DirectoryProvider;
import org.efs.openreports.providers.SchedulerProvider;

public class ListScheduledReportsAction	extends ActionSupport
{	
	private static final long serialVersionUID = 3842653664544858888L;

	private List scheduledReports;
	private List<GeneratedReport> generatedReports;
    
    private int refresh;
    
	private SchedulerProvider schedulerProvider;
    private DirectoryProvider directoryProvider;

	public String execute()
	{
		try
		{
			ReportUser reportUser =
				(ReportUser) ActionContext.getContext().getSession().get(
					ORStatics.REPORT_USER);

			scheduledReports =
				schedulerProvider.getScheduledReports(reportUser);
            
			IOFileFilter extensionFilter = FileFilterUtils.suffixFileFilter("xml");
            
            File directory = new File(directoryProvider.getReportGenerationDirectory());

            generatedReports = new ArrayList<GeneratedReport>();

            Iterator iterator = FileUtils.iterateFiles(directory, extensionFilter, null);
            while (iterator.hasNext())
            {
                File file = (File) iterator.next();

                if (FilenameUtils.wildcardMatch(file.getName(), "*" + reportUser.getName() + "*"))
                {
                    XStream xStream = new XStream();
                    xStream.alias("reportGenerationInfo", GeneratedReport.class);

                    FileInputStream inputStream = new FileInputStream(file);
                
                    GeneratedReport info = (GeneratedReport) xStream
                        .fromXML(inputStream);
                
                    generatedReports.add(info);
                
                    inputStream.close();
                }
            }
		}
		catch (Exception e)
		{
			addActionError(getText(e.getMessage()));
			return ERROR;
		}

		return SUCCESS;
	}

	public void setSchedulerProvider(SchedulerProvider schedulerProvider)
	{
		this.schedulerProvider = schedulerProvider;
	}
    
    public void setDirectoryProvider(DirectoryProvider directoryProvider)
    {
        this.directoryProvider = directoryProvider;
    }

	public List getScheduledReports()
	{
		return scheduledReports;
	}
    
    public List getGeneratedReports()
    {
        return generatedReports;
    }

    public int getRefresh()
    {
        return refresh;
    }

    public void setRefresh(int refresh)
    {
        this.refresh = refresh;
    }

}