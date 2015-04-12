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
package org.efs.openreports.delivery;

import org.efs.openreports.engine.output.ReportEngineOutput;
import org.efs.openreports.objects.DeliveredReport;
import org.efs.openreports.objects.ReportSchedule;
import org.efs.openreports.objects.ReportUser;

public interface DeliveryMethod 
{    
    public void deliverReport(ReportSchedule reportSchedule, ReportEngineOutput reportOutput) throws DeliveryException;
    
    public DeliveredReport[] getDeliveredReports(ReportUser user) throws DeliveryException;
    
    public byte[] getDeliveredReport(DeliveredReport deliveredReport) throws DeliveryException;        
}
