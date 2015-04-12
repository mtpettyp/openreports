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

import java.util.List;

import org.apache.log4j.Logger;

import org.efs.openreports.actions.DisplayTagAction;
import org.efs.openreports.providers.ProviderException;
import org.efs.openreports.providers.UserProvider;

public class ListUsersAction extends DisplayTagAction  
{
	private static final long serialVersionUID = 4967474231626079678L;

	protected static Logger log = Logger.getLogger(ListUsersAction.class);

	private List users;

	private UserProvider userProvider;

	public List getUsers()
	{
		return users;
	}

	public String execute()
	{
		try
		{
			users = userProvider.getUsers();
		}
		catch (ProviderException pe)
		{
			addActionError(pe.getMessage());
			return ERROR;
		}

		return SUCCESS;
	}

	public void setUserProvider(UserProvider userProvider)
	{
		this.userProvider = userProvider;
	}

}