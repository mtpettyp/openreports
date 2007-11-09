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

package org.efs.openreports.providers;

import java.util.List;

import org.efs.openreports.objects.ReportGroup;
import org.efs.openreports.objects.ReportUser;

public interface UserProvider
{
	public ReportUser getUser(String name) throws ProviderException;
	public ReportUser getUser(Integer id) throws ProviderException;
	public List<ReportUser> getUsers() throws ProviderException;
	public ReportUser insertUser(ReportUser user)
		throws ProviderException;
	public void updateUser(ReportUser user) throws ProviderException;
	public void deleteUser(ReportUser user) throws ProviderException;
	public List<ReportUser> getUsersForGroup(ReportGroup reportGroup) throws ProviderException;
}