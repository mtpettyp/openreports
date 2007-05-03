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

package org.efs.openreports.services.info;

import java.io.Serializable;

/**
 * UserInfo object. 
 *  
 * @author Erik Swenson
 * 
 */

public class UserInfo implements Serializable
{	
	private static final long serialVersionUID = 2720389107519743681L;
	
	private Integer id;
	private String name;
	private String password;
	private String emailAddress;
	private String externalId;
	
	public UserInfo()
	{
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String toString()
	{
		return name;
	}	

	public Integer getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}	

	public void setName(String name)
	{
		this.name = name;
	}

	public String getEmailAddress()
	{
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress)
	{
		this.emailAddress = emailAddress;
	}

	public String getExternalId()
	{
		return externalId;
	}

	public void setExternalId(String externalId)
	{
		this.externalId = externalId;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}
	
	
}