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

import org.efs.openreports.actions.DisplayTagAction;
import org.efs.openreports.providers.ParameterProvider;
import org.efs.openreports.providers.ProviderException;

public class ListReportParametersAction extends DisplayTagAction  
{	
	private static final long serialVersionUID = 3093318964618880747L;

	private List parameters;

	private ParameterProvider parameterProvider;

	public List getReportParameters()
	{		
		return parameters;		
	}

	public String execute()
	{
		try
		{			
			parameters = parameterProvider.getReportParameters();
		}
		catch(ProviderException pe)
		{
			addActionError(pe.getMessage());
			return ERROR;	
		}	

		return SUCCESS;
	}

	public void setParameterProvider(ParameterProvider parameterProvider)
	{
		this.parameterProvider = parameterProvider;
	}

}