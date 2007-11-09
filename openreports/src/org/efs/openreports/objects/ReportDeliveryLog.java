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
package org.efs.openreports.objects;

import java.util.Date;

public class ReportDeliveryLog 
{
    private Integer id;
    private String deliveryMethod;
    private Date startTime;
    private Date endTime;
    private String status;
    private String message;
    
    public ReportDeliveryLog() {}
    
    public ReportDeliveryLog(String deliveryMethod, Date startTime)
    {
        this.deliveryMethod = deliveryMethod;
        this.startTime = startTime;
    }
    
    public String getDeliveryMethod() 
    {
        return deliveryMethod;
    }
    
    public void setDeliveryMethod(String deliveryMethod) 
    {
        this.deliveryMethod = deliveryMethod;
    }
    
    public Date getEndTime() 
    {
        return endTime;
    }
    
    public void setEndTime(Date endTime) 
    {
        this.endTime = endTime;
    }
    
    public Integer getId() 
    {
        return id;
    }
    
    public void setId(Integer id) 
    {
        this.id = id;
    }
    
    public String getMessage() 
    {
        return message;
    }
    
    public void setMessage(String message) 
    {
        this.message = message;
    }
    
    public Date getStartTime() 
    {
        return startTime;
    }
    
    public void setStartTime(Date startTime) 
    {
        this.startTime = startTime;
    }
    
    public String getStatus() 
    {
        return status;
    }
    
    public void setStatus(String status) 
    {
        this.status = status;
    }    
}
