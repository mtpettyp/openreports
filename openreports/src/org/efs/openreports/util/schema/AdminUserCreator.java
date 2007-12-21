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

package org.efs.openreports.util.schema;

import org.apache.xbean.spring.context.FileSystemXmlApplicationContext;
import org.efs.openreports.objects.ReportUser;
import org.efs.openreports.providers.UserProvider;

public class AdminUserCreator 
{
	public static void main(String[] args) 
	{
		ReportUser user = new ReportUser();
		user.setName(args[0]);
		user.setPassword(args[1]);
		user.setEmail(args[2]);
		user.setRootAdmin(true);
		
		try 
		{
			FileSystemXmlApplicationContext appContext = new FileSystemXmlApplicationContext(
					"database/spring/applicationContext.xml");

			UserProvider userProvider = (UserProvider) appContext.getBean("userProvider", UserProvider.class);
			userProvider.insertUser(user);			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
