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
package org.efs.openreports.interceptors;

import com.opensymphony.xwork2.*;
import com.opensymphony.xwork2.interceptor.Interceptor;

import org.efs.openreports.ORStatics;

public class BreadcrumbsInterceptor implements Interceptor
{	   
	private static final long serialVersionUID = 2622675137395370397L;
	
	private static String[] MAIN_MENU_ITEMS = new String[] {"dashboard", "reportAdmin", "reportGroup", "listScheduledReports", "userAdmin"};
    
	@SuppressWarnings("unchecked")
	public String intercept(ActionInvocation actionInvocation) throws Exception
	{        
        String breadcrumbs = (String) actionInvocation.getInvocationContext().getSession().get(ORStatics.BREADCRUMBS);
        
		String actionName = actionInvocation.getInvocationContext().getName(); 
        
        // if the action is a main menu action, reset breadcrumbs
        for (int i=0; i < MAIN_MENU_ITEMS.length; i++)
        {
            if (actionName.equals(MAIN_MENU_ITEMS[i]))
            {               
                breadcrumbs = "";
                break;
            }
        }
        
        breadcrumbs += actionName + ".";               
        
        actionInvocation.getInvocationContext().getSession().put(ORStatics.BREADCRUMBS, breadcrumbs);

		return actionInvocation.invoke();
	}	    
   
	public void destroy()
	{
	}

	public void init()
	{
	}		
}
