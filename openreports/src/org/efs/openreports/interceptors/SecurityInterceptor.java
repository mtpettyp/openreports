/*
 * Copyright (C) 2003 Erik Swenson - erik@oreports.com
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

package org.efs.openreports.interceptors;

import com.opensymphony.xwork2.*;
import com.opensymphony.xwork2.interceptor.Interceptor;

import org.efs.openreports.ORStatics;
import org.efs.openreports.objects.ReportUser;
import org.efs.openreports.util.LocalStrings;

public class SecurityInterceptor implements Interceptor
{	
	private static final long serialVersionUID = -726209768089734484L;
	private boolean loggedIn;	
	private ReportUser user;
	
	public String intercept(ActionInvocation actionInvocation) throws Exception
	{
		user = (ReportUser) actionInvocation.getInvocationContext().getSession().get(
				"user");
		
		if (!isAuthenticated(user))
		{
			ActionSupport action = (ActionSupport) actionInvocation.getAction();
			action.addActionError(action.getText(LocalStrings.ERROR_NOTLOGGEDIN));

			return Action.LOGIN;
		}
		
		if (!isAuthorized(user))
		{
			ActionSupport action = (ActionSupport) actionInvocation.getAction();
			action.addActionError(action.getText(LocalStrings.ERROR_NOTAUTHORIZED));			
			
			return ORStatics.NOT_AUTHORIZED;
		}

		ActionContext.getContext().getValueStack().push(this);

		return actionInvocation.invoke();
	}

	protected boolean isAuthenticated(ReportUser user)
	{
		if (user == null)
		{		
			loggedIn = false;
			return false;
		}

		loggedIn = true;
		return true;
	}

	protected  boolean isAuthorized(ReportUser user)
	{
		return true;
	}
	
	public void destroy()
	{
	}

	public void init()
	{
	}	

	public boolean isLoggedIn()
	{
		return loggedIn;
	}	
	
	public ReportUser getUser()
	{
		return user;
	}
}
