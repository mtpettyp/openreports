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

package org.efs.openreports.util;

import org.displaytag.decorator.TableDecorator;
import org.efs.openreports.objects.ReportSchedule;

public class ScheduleHRefColumnDecorator extends TableDecorator
{			
	public Object getRemoveLink()
	{
		ReportSchedule reportSchedule = (ReportSchedule) getCurrentRowObject();

		return "<a href=\"deleteScheduledReportByUser.action?scheduleName="
				+ reportSchedule.getScheduleName() + "&amp;userId="
				+ reportSchedule.getUser().getId() + "\">Remove</a>";		
	}	
	
	public Object getUpdateLink()
	{
		ReportSchedule reportSchedule = (ReportSchedule) getCurrentRowObject();
		
		return "<a href=\"reportScheduleByUser.action?scheduleName="
		+ reportSchedule.getScheduleName() + "&amp;userId="
		+ reportSchedule.getUser().getId() + "\">Update</a>";	
	}
	
	public Object getStateLink()
	{
		ReportSchedule reportSchedule = (ReportSchedule) getCurrentRowObject();

		String state = reportSchedule.getScheduleState();		
		if (!state.equals("Normal") && !state.equals("Paused")) return state;
				
		return "<a href=\"changeScheduleState.action?scheduleName="
				+ reportSchedule.getScheduleName() + "&amp;state="
				+ reportSchedule.getScheduleState() + "&amp;userId="
				+ reportSchedule.getUser().getId() + "\">"
				+ reportSchedule.getScheduleState() + "</a>";
	}	
}
