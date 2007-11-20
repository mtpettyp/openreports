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

import java.io.Serializable;

/**
 * The <tt>ORTag</tt> class is used by OpenReports to assign a tag to an object. 
 * Each ORTag instance will have a unique identifier and a <tt>String</tt> tag
 * and is assigned to the specified objectId and objectClass 
 */

public class ORTag implements Serializable
{	    
    private static final long serialVersionUID = 6070621685826795761L;
    
    public static final String TAG_TYPE_SYSTEM = "SYSTEM";
    public static final String TAG_TYPE_UI = "UI";
    
    private Integer id;    
    private Integer objectId;    
	private String objectClass;
	private String tag;
    private String tagType;

	public ORTag()
	{
	}
    
    public Integer getId() 
    {
        return id;
    }
    
    public void setId(Integer id) 
    {
        this.id = id;
    }
    
    public Integer getObjectId() 
    {
        return objectId;
    }
    
    public void setObjectId(Integer objectId) 
    {
        this.objectId = objectId;
    }        
    
    public String getObjectClass() 
    {
        return objectClass;
    }
    
    public void setObjectClass(String objectClass) 
    {
        this.objectClass = objectClass;
    }  

    public String getTag() 
    {
        return tag;
    }
    
    public void setTag(String tag)
    {
        this.tag = tag;
    }
    
    public String getTagType() 
    {
        return tagType;
    }
    
    public void setTagType(String tagType)
    {
        this.tagType = tagType;
    }    
}