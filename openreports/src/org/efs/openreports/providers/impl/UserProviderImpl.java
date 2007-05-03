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

package org.efs.openreports.providers.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.efs.openreports.objects.ReportUser;
import org.efs.openreports.providers.ProviderException;
import org.efs.openreports.providers.UserProvider;
import org.efs.openreports.providers.persistence.UserPersistenceProvider;

public class UserProviderImpl implements UserProvider
{
	protected static Logger log =
		Logger.getLogger(UserProviderImpl.class.getName());
	
	private UserPersistenceProvider userPersistenceProvider;

	public UserProviderImpl() throws ProviderException
	{
		userPersistenceProvider = new UserPersistenceProvider();

		log.info("UserProviderImpl created");
	}

	public ReportUser getUser(String name) throws ProviderException
	{
		return userPersistenceProvider.getUser(name);
	}

	public ReportUser getUser(Integer id) throws ProviderException
	{
		return userPersistenceProvider.getUser(id);
	}

	public List getUsers() throws ProviderException
	{
		return userPersistenceProvider.getUsers();
	}

	public ReportUser insertUser(ReportUser user) throws ProviderException
	{
		return userPersistenceProvider.insertUser(user);
	}

	public void updateUser(ReportUser user) throws ProviderException
	{
		userPersistenceProvider.updateUser(user);
	}

	public void deleteUser(ReportUser user) throws ProviderException
	{
		userPersistenceProvider.deleteUser(user);
	}	
}