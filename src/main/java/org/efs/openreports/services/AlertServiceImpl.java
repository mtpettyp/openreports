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
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.efs.openreports.objects.ReportUser;
import org.efs.openreports.objects.ReportUserAlert;
import org.efs.openreports.providers.AlertProvider;
import org.efs.openreports.providers.ProviderException;
import org.efs.openreports.providers.UserProvider;
import org.efs.openreports.services.info.AlertInfo;
import org.efs.openreports.services.input.UserInput;
import org.efs.openreports.services.util.Converter;

/**
 * AlertService implementation using standard OpenReports providers. 
 * 
 * @author Erik Swenson
 */

public class AlertServiceImpl implements AlertService
{
	private static Logger log = Logger.getLogger(AlertServiceImpl.class.getName());

	private AlertProvider alertProvider;
	private UserProvider userProvider;
    private UserService userService;

	public AlertServiceImpl()
	{
		log.info("AlertService: Started");
	}		
	
	public AlertInfo[] getAlerts(UserInput userInput) throws ServiceException
	{
        userService.authenticate(userInput);        
        
		ReportUser user = null;
		
		try
		{
			user = userProvider.getUser(userInput.getUserName(), userInput.getPassword());
		}
		catch(ProviderException pe)
		{
			throw new ServiceException(pe);
		}		
		
		ArrayList<AlertInfo> alerts = new ArrayList<AlertInfo>();
		
		Iterator<ReportUserAlert> iterator = user.getAlerts().iterator();	
		while(iterator.hasNext())
		{			
			ReportUserAlert userAlert = iterator.next();				
			userAlert.setUser(user);
			
			try
			{
				userAlert = alertProvider.executeAlert(userAlert, false);			

				alerts.add(Converter.convertToAlertInfo(userAlert));
			}
			catch (ProviderException e)
			{
				AlertInfo alertInfo = new AlertInfo();
				alertInfo.setMessage(e.toString());
				
				alerts.add(alertInfo);
			}
		}	
		
		AlertInfo[] alertInfos = new AlertInfo[alerts.size()];
		alerts.toArray(alertInfos);
		
		return alertInfos;		
	}
	
	public void setAlertProvider(AlertProvider alertProvider)
	{
		this.alertProvider = alertProvider;
	}

	public void setUserProvider(UserProvider userProvider)
	{
		this.userProvider = userProvider;
	}
    
    public void setUserService(UserService userService) 
    {
        this.userService = userService;
    }	
}
