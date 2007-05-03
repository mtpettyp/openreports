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

package org.efs.openreports.actions.admin;

import org.efs.openreports.objects.ReportUser;
import org.efs.openreports.providers.UserProvider;

public class DeleteUserAction extends DeleteAction  
{	
	private static final long serialVersionUID = 8105838790172793555L;
	
	private UserProvider userProvider;

	public String execute()
	{
		try
		{
			ReportUser reportUser = userProvider.getUser(new Integer(id));

			name = reportUser.getName();

			if (!submitDelete && !submitCancel)
			{
				return INPUT;
			}

			if (submitDelete)
			{
				userProvider.deleteUser(reportUser);
			}
		}
		catch (Exception e)
		{
			addActionError(e.getMessage());
			return INPUT;
		}

		return SUCCESS;
	}

	public void setUserProvider(UserProvider userProvider)
	{
		this.userProvider = userProvider;
	}

}