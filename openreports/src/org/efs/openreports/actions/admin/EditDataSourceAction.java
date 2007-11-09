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

import com.opensymphony.xwork2.ActionSupport;

import org.apache.log4j.Logger;

import org.efs.openreports.objects.ReportDataSource;
import org.efs.openreports.providers.DataSourceProvider;

public class EditDataSourceAction extends ActionSupport  
{	
	private static final long serialVersionUID = 688082544181279177L;

	protected static Logger log = Logger.getLogger(EditDataSourceAction.class);
	
	private String command;
	private String dataSource;

	private boolean submitOk;	
	private boolean submitDuplicate;

	private int id;
	private String name;
	private String driver;
	private String url;
	private String userName;
	private String password;
	private int maxIdle;
	private int maxActive;
	private long maxWait;
	private String validationQuery;
	private boolean jndi;	
	
	private DataSourceProvider dataSourceProvider;	

	@Override
	public String execute()
	{
		try
		{
			ReportDataSource reportDataSource = null;

			if (command.equals("edit"))
			{
				reportDataSource =
					dataSourceProvider.getDataSource(new Integer(id));
			}
			else
			{
				reportDataSource = new ReportDataSource();
			}

			if (command.equals("edit") && !submitOk && !submitDuplicate)
			{
				id = reportDataSource.getId().intValue();
				name = reportDataSource.getName();
				driver = reportDataSource.getDriverClassName();
				url = reportDataSource.getUrl();
				userName = reportDataSource.getUsername();
				password = reportDataSource.getPassword();
				maxIdle = reportDataSource.getMaxIdle();
				maxActive = reportDataSource.getMaxActive();
				validationQuery = reportDataSource.getValidationQuery();
				jndi = reportDataSource.isJndi();
				maxWait = reportDataSource.getMaxWait();
			}

			if (!submitOk && !submitDuplicate) return INPUT;
			
			   if (submitDuplicate)
	            {
	            	command = "add";
	            	reportDataSource.setId(null);
	            	
	            	if (reportDataSource.getName().equals(name))
	            	{
	            		name = "Copy of ".concat(name);
	            	}
	            }

			reportDataSource.setName(name);
			reportDataSource.setDriverClassName(driver);
			reportDataSource.setUrl(url);
			reportDataSource.setUsername(userName);
			reportDataSource.setPassword(password);
			reportDataSource.setMaxIdle(maxIdle);
			reportDataSource.setMaxActive(maxActive);
			reportDataSource.setJndi(jndi);
			reportDataSource.setMaxWait(maxWait);			
			
			if (validationQuery == null || validationQuery.length() < 1)
			{
				reportDataSource.setValidationQuery(null);
			}
			else
			{
				reportDataSource.setValidationQuery(validationQuery);
			}

			if (command.equals("edit"))
			{
				dataSourceProvider.updateDataSource(reportDataSource);
			}

			if (command.equals("add"))
			{
				reportDataSource =
					dataSourceProvider.insertDataSource(reportDataSource);
                
                id = reportDataSource.getId().intValue();
			}

			return SUCCESS;
		}
		catch (Exception e)
		{				
			addActionError(getText(e.getMessage()));
			return INPUT;
		}
	}

	public String getCommand()
	{
		return command;
	}

	public String getDataSource()
	{
		return dataSource;
	}

	public void setCommand(String command)
	{
		this.command = command;
	}

	public void setDataSource(String dataSource)
	{
		this.dataSource = dataSource;
	}

	public String getDriver()
	{
		return driver;
	}

	public int getMaxActive()
	{
		return maxActive;
	}

	public int getMaxIdle()
	{
		return maxIdle;
	}

	public String getPassword()
	{
		return password;
	}

	public String getUrl()
	{
		return url;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setDriver(String driver)
	{
		this.driver = driver;
	}

	public void setMaxActive(int maxActive)
	{
		this.maxActive = maxActive;
	}

	public void setMaxIdle(int maxIdle)
	{
		this.maxIdle = maxIdle;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public void setSubmitOk(String submitOk)
	{
		if (submitOk != null) this.submitOk = true;
	}	
		
	public void setSubmitDuplicate(String submitDuplicate)
	{
		if (submitDuplicate != null) this.submitDuplicate = true;
	}

	public int getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getValidationQuery()
	{
		return validationQuery;
	}

	public void setValidationQuery(String validationQuery)
	{
		this.validationQuery = validationQuery;
	}

	public boolean isJndi()
	{
		return jndi;
	}

	public void setJndi(boolean jndi)
	{
		this.jndi = jndi;
	}

	public void setDataSourceProvider(DataSourceProvider dataSourceProvider)
	{
		this.dataSourceProvider = dataSourceProvider;
	}

	public long getMaxWait()
	{
		return maxWait;
	}

	public void setMaxWait(long maxWait)
	{
		this.maxWait = maxWait;
	}

}