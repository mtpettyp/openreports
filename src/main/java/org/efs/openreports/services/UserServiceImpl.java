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

package org.efs.openreports.services;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.efs.openreports.objects.Report;
import org.efs.openreports.objects.ReportUser;
import org.efs.openreports.providers.ProviderException;
import org.efs.openreports.providers.UserProvider;
import org.efs.openreports.services.info.ReportInfo;
import org.efs.openreports.services.info.UserInfo;
import org.efs.openreports.services.input.UserInput;
import org.efs.openreports.services.util.Converter;

/**
 * UserService implementation using standard OpenReports providers. 
 * 
 * @author Erik Swenson
 */

public class UserServiceImpl implements UserService
{
	private static Logger log = Logger.getLogger(UserServiceImpl.class.getName());
	
	private UserProvider userProvider;	

	public UserServiceImpl()
	{
		log.info("UserService: Started");
	}		

	public void authenticate(UserInput userInput) throws ServiceException
	{		
		try
		{
			ReportUser user = userProvider.getUser(userInput.getUserName(), userInput.getPassword());
			if (user == null)
            {
                throw new ServiceException(ServiceMessages.NOT_AUTHENTICATED);
            }
		}
		catch(ProviderException pe)
		{
			throw new ServiceException(pe);		
		}		
	}
	
	public UserInfo getUserInfo(UserInput userInput) throws ServiceException
	{	
	    authenticate(userInput);        
        
		try
		{   
			ReportUser user = userProvider.getUser(userInput.getUserName(), userInput.getPassword());
			return Converter.convertToUserInfo(user);				
		}
		catch(ProviderException pe)
		{
            throw new ServiceException(pe);			
		}		
	}
	
	public void updateUserInfo(UserInput userInput, UserInfo userInfo) throws ServiceException
	{        
        authenticate(userInput);        
		
		try
		{
			ReportUser user = userProvider.getUser(userInfo.getName(), userInfo.getPassword());
			user.setPassword(userInfo.getPassword());
			user.setEmail(userInfo.getEmailAddress());
			
			userProvider.updateUser(user);
		}
		catch(ProviderException e)
		{
			throw new ServiceException(e);
		}	
	}
	
	public ReportInfo[] getReports(UserInput userInput) throws ServiceException
	{
        authenticate(userInput);       

		ReportInfo[] reports = null;

		try
		{
			ReportUser user = userProvider.getUser(userInput.getUserName(), userInput.getPassword());
			if (user != null && user.getReports() != null)
			{	
				ArrayList<Report> userReports = new ArrayList<Report>(user.getReports());

				reports = new ReportInfo[userReports.size()];
				for (int i = 0; i < userReports.size(); i++)
				{										
					reports[i] = Converter.convertToReportInfo((Report)userReports.get(i));
				}
			}
		}
		catch (ProviderException e)
		{
            throw new ServiceException(e);
		}

		return reports;
	}

	public void setUserProvider(UserProvider userProvider)
	{
		this.userProvider = userProvider;
	}	
}
