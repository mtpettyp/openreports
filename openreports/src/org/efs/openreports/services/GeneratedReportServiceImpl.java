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

package org.efs.openreports.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.log4j.Logger;
import org.efs.openreports.objects.GeneratedReport;
import org.efs.openreports.providers.DirectoryProvider;
import org.efs.openreports.services.info.GeneratedReportInfo;
import org.efs.openreports.services.input.UserInput;
import org.efs.openreports.services.util.Converter;

import com.thoughtworks.xstream.XStream;

/**
 * GeneratedReportService implementation using standard OpenReports providers. 
 * 
 * @author Erik Swenson
 */

public class GeneratedReportServiceImpl implements GeneratedReportService
{
	private static Logger log = Logger.getLogger(GeneratedReportServiceImpl.class.getName());
	
	private DirectoryProvider directoryProvider;
    private UserService userService;

	public GeneratedReportServiceImpl()
	{
		log.info("GeneratedReportService: Started");
	}		
	
	public GeneratedReportInfo[] getGeneratedReports(UserInput user)
	{
        boolean authenticated = userService.authenticate(user);
        if (!authenticated)
        {
            log.warn("Not Authenticated: " + user.getUserName());
            return null;
        }
        
		IOFileFilter extensionFilter = FileFilterUtils.suffixFileFilter("xml");
		
		File directory = new File(directoryProvider.getReportGenerationDirectory());

		ArrayList<GeneratedReportInfo> generatedReports = new ArrayList<GeneratedReportInfo>();

		Iterator iterator = FileUtils.iterateFiles(directory, extensionFilter, null);
		while (iterator.hasNext())
		{
			File file = (File) iterator.next();

			if (FilenameUtils.wildcardMatch(file.getName(), "*" + user.getUserName() + "*"))
			{
				XStream xStream = new XStream();
				xStream.alias("reportGenerationInfo", GeneratedReport.class);
				
				try
				{
					FileInputStream inputStream = new FileInputStream(file);
			
					GeneratedReport report = (GeneratedReport) xStream
						.fromXML(inputStream);
					
					GeneratedReportInfo info = Converter.convertToGeneratedReportInfo(report);
			
					generatedReports.add(info);
			
					inputStream.close();
				}
				catch(IOException io)
				{
					log.warn(io.toString());
				}
			}
		}	
		
		GeneratedReportInfo[] info = new GeneratedReportInfo[generatedReports.size()];
		generatedReports.toArray(info);
		
		return info;
	}
	
	public void setDirectoryProvider(DirectoryProvider directoryProvider)
	{
		this.directoryProvider = directoryProvider;
	}
    
    public void setUserService(UserService userService) 
    {
        this.userService = userService;
    }	
}
