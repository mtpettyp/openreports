/*
 * Copyright (C) 2005 Erik Swenson - erik@oreports.com
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

import com.opensymphony.xwork2.ActionSupport;
import java.io.File;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.efs.openreports.objects.ORProperty;
import org.efs.openreports.providers.DateProvider;
import org.efs.openreports.providers.DirectoryProvider;
import org.efs.openreports.providers.MailProvider;
import org.efs.openreports.providers.PropertiesProvider;
import org.efs.openreports.util.LocalStrings;

public class EditPropertiesAction extends ActionSupport 
{	
	private static final long serialVersionUID = -2577286917721890875L;

	protected static Logger log = Logger.getLogger(EditPropertiesAction.class);

	private String submitType;

	private String dateFormat;
	private String baseDirectory;
	private String tempDirectory;
	private String mailHost;
	private boolean mailAuthenticatorUsed;
	private String mailUser;
	private String mailPassword;	
	private String maxRows;
	private String reportGenerationDirectory;
	
	private int numberOfFiles;
	private String directorySize;
    
    private String xmlaUri;
    private String xmlaDataSource;
    private String xmlaCatalog;
    
	private PropertiesProvider propertiesProvider;
	private DateProvider dateProvider;
	private DirectoryProvider directoryProvider;
	private MailProvider mailProvider;
	
	public String execute()
	{
		try
		{
			if (submitType == null)
			{						
				dateFormat = dateProvider.getDateFormat().toPattern();				
				baseDirectory = directoryProvider.getReportDirectory();	
				tempDirectory = directoryProvider.getTempDirectory();
				reportGenerationDirectory = directoryProvider.getReportGenerationDirectory();

				ORProperty property = propertiesProvider.getProperty(ORProperty.MAIL_SMTP_HOST);
				if (property != null) mailHost = property.getValue();

				property = propertiesProvider.getProperty(ORProperty.MAIL_SMTP_AUTH);
				if (property != null) mailAuthenticatorUsed = new Boolean(property.getValue()).booleanValue();

				property = propertiesProvider.getProperty(ORProperty.MAIL_AUTH_USER);
				if (property != null) mailUser = property.getValue();

				property = propertiesProvider.getProperty(ORProperty.MAIL_AUTH_PASSWORD);
				if (property != null) mailPassword = property.getValue();							
				
				property = propertiesProvider.getProperty(ORProperty.QUERYREPORT_MAXROWS);
				if (property != null) maxRows = property.getValue();
                
                property = propertiesProvider.getProperty(ORProperty.XMLA_CATALOG);
                if (property != null) xmlaCatalog = property.getValue();    
                
                property = propertiesProvider.getProperty(ORProperty.XMLA_DATASOURCE);
                if (property != null) xmlaDataSource = property.getValue();    
                
                property = propertiesProvider.getProperty(ORProperty.XMLA_URL);
                if (property != null) xmlaUri = property.getValue();    
				
				//
				File tempDirFile = new File(directoryProvider.getTempDirectory());
								
				long size = FileUtils.sizeOfDirectory(tempDirFile);			
				directorySize = FileUtils.byteCountToDisplaySize(size);	
				
				numberOfFiles = tempDirFile.listFiles().length;				
				//
				
				return INPUT;
			}
			
			// validate maxRows is an integer
			try
			{
				Integer.parseInt(maxRows);
			}
			catch(NumberFormatException nfe)
			{
				addActionError(LocalStrings.ERROR_INVALID_MAXROWS);
				return INPUT;
			}

			propertiesProvider.setProperty(ORProperty.BASE_DIRECTORY, baseDirectory);
			if (baseDirectory != null) directoryProvider.setReportDirectory(baseDirectory);
			
			propertiesProvider.setProperty(ORProperty.TEMP_DIRECTORY, tempDirectory);
			if (tempDirectory != null) directoryProvider.setTempDirectory(tempDirectory);
			
			propertiesProvider.setProperty(ORProperty.REPORT_GENERATION_DIRECTORY, reportGenerationDirectory);
			if (tempDirectory != null) directoryProvider.setReportGenerationDirectory(reportGenerationDirectory);
			
			propertiesProvider.setProperty(ORProperty.DATE_FORMAT, dateFormat);
			if (dateFormat != null) dateProvider.setDateFormat(dateFormat);
			
			propertiesProvider.setProperty(ORProperty.MAIL_AUTH_PASSWORD, mailPassword);
			if (mailPassword != null) mailProvider.setPassword(mailPassword);
			
			propertiesProvider.setProperty(ORProperty.MAIL_AUTH_USER, mailUser);
			if (mailUser !=null) mailProvider.setUserName(mailUser);
			
			propertiesProvider.setProperty(ORProperty.MAIL_SMTP_AUTH, String.valueOf(mailAuthenticatorUsed));
			mailProvider.setUseMailAuthenticator(mailAuthenticatorUsed);
			
			propertiesProvider.setProperty(ORProperty.MAIL_SMTP_HOST, mailHost);
			if (mailHost != null) mailProvider.setMailHost(mailHost);			
									
			propertiesProvider.setProperty(ORProperty.QUERYREPORT_MAXROWS, maxRows);
            propertiesProvider.setProperty(ORProperty.XMLA_CATALOG, xmlaCatalog);
            propertiesProvider.setProperty(ORProperty.XMLA_DATASOURCE, xmlaDataSource);
            propertiesProvider.setProperty(ORProperty.XMLA_URL, xmlaUri);            

			return SUCCESS;
		}
		catch (Exception e)
		{
			addActionError(e.toString());
			return INPUT;
		}
	}

	public String getSubmitType()
	{
		return submitType;
	}

	public void setSubmitType(String submitType)
	{
		this.submitType = submitType;
	}

	public String getBaseDirectory()
	{
		return baseDirectory;
	}

	public void setBaseDirectory(String baseDirectory)
	{
		this.baseDirectory = baseDirectory;
	}

	public String getDateFormat()
	{
		return dateFormat;
	}

	public void setDateFormat(String dateFormat)
	{
		this.dateFormat = dateFormat;
	}

	public boolean isMailAuthenticatorUsed()
	{
		return mailAuthenticatorUsed;
	}

	public void setMailAuthenticatorUsed(boolean mailAuthenticatorUsed)
	{
		this.mailAuthenticatorUsed = mailAuthenticatorUsed;
	}

	public String getMailHost()
	{
		return mailHost;
	}

	public void setMailHost(String mailHost)
	{
		this.mailHost = mailHost;
	}

	public String getMailPassword()
	{
		return mailPassword;
	}

	public void setMailPassword(String mailPassword)
	{
		this.mailPassword = mailPassword;
	}

	public String getMailUser()
	{
		return mailUser;
	}

	public void setMailUser(String mailUser)
	{
		this.mailUser = mailUser;
	}	

	public void setPropertiesProvider(PropertiesProvider propertiesProvider)
	{
		this.propertiesProvider = propertiesProvider;
	}

	public void setDateProvider(DateProvider dateProvider)
	{
		this.dateProvider = dateProvider;
	}
	
	public void setDirectoryProvider(DirectoryProvider directoryProvider)
	{
		this.directoryProvider = directoryProvider;
	}

	public void setMailProvider(MailProvider mailProvider)
	{
		this.mailProvider = mailProvider;
	}

	public String getTempDirectory()
	{
		return tempDirectory;
	}

	public void setTempDirectory(String tempDirectory)
	{
		this.tempDirectory = tempDirectory;
	}

	public String getMaxRows()
	{
		return maxRows;
	}

	public void setMaxRows(String maxRows)
	{
		this.maxRows = maxRows;
	}

	public String getDirectorySize()
	{
		return directorySize;
	}

	public int getNumberOfFiles()
	{
		return numberOfFiles;
	}

	public String getReportGenerationDirectory()
	{
		return reportGenerationDirectory;
	}

	public void setReportGenerationDirectory(String reportGenerationDirectory)
	{
		this.reportGenerationDirectory = reportGenerationDirectory;
	}
    
    public String getXmlaCatalog() 
    {
        return xmlaCatalog;
    }
    
    public void setXmlaCatalog(String xmlaCatalog) 
    {
        this.xmlaCatalog = xmlaCatalog;
    }
    
    public String getXmlaDataSource() 
    {
        return xmlaDataSource;
    }
    
    public void setXmlaDataSource(String xmlaDataSource) 
    {
        this.xmlaDataSource = xmlaDataSource;
    }
    
    public String getXmlaUri() 
    {
        return xmlaUri;
    }
    
    public void setXmlaUri(String xmlaUri)
    {
        this.xmlaUri = xmlaUri;
    }
}