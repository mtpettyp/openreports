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
package org.efs.openreports.actions.admin;

import com.opensymphony.xwork2.ActionSupport;

import org.efs.openreports.providers.ProviderException;
import org.efs.openreports.providers.TagProvider;

public class ReportAdminAction extends ActionSupport  
{
	private static final long serialVersionUID = 1339590512207608587L;

	private String tags;

	private TagProvider tagProvider;	

	public String execute()
	{
		try
		{
			tags = tagProvider.getTagList(null);
		}
		catch (ProviderException pe)
		{
			addActionError(pe.toString());			
		}

		return SUCCESS;
	}
    
    public String getTags() 
    {
        return tags;
    }    
    
    public void setTagProvider(TagProvider tagProvider) 
    {
        this.tagProvider = tagProvider;
    }
}