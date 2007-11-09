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

package org.efs.openreports.actions.admin;

import org.efs.openreports.objects.ReportAlert;
import org.efs.openreports.providers.AlertProvider;

public class DeleteAlertAction extends DeleteAction
{	
    private static final long serialVersionUID = -8254096109570893632L;
    
    private AlertProvider alertProvider;

	@Override
	public String execute()
	{
		try
		{
			ReportAlert reportAlert =
				alertProvider.getReportAlert(new Integer(id));

			name = reportAlert.getName();
			description = reportAlert.getDescription();

			if (!submitDelete && !submitCancel)
			{
				return INPUT;
			}

			if (submitDelete)
			{
				alertProvider.deleteReportAlert(reportAlert);
			}
		}
		catch (Exception e)
		{
			addActionError(getText(e.getMessage()));
			return INPUT;
		}

		return SUCCESS;
	}

	public void setAlertProvider(AlertProvider alertProvider)
	{
		this.alertProvider = alertProvider;
	}

}