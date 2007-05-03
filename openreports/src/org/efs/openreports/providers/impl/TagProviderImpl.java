/*
 * Copyright (C) 2007 Erik Swenson - erik@oreports.com
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 *  
 */

package org.efs.openreports.providers.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;
import org.efs.openreports.objects.ORTag;
import org.efs.openreports.providers.ProviderException;
import org.efs.openreports.providers.TagProvider;
import org.efs.openreports.providers.persistence.TagPersistenceProvider;

/**
 * The <tt>TagProviderImpl</tt> class uses the Hibernate based <tt>TagPersistenceProvider</tt>
 * to persist tags to a database. 
 *  
 */

public class TagProviderImpl implements TagProvider
{
	protected static Logger log = Logger.getLogger(TagProviderImpl.class.getName());
	
	private TagPersistenceProvider tagPersistenceProvider;	
	
	public TagProviderImpl() throws ProviderException
	{		
        tagPersistenceProvider = new TagPersistenceProvider();
        log.info("TagProviderImpl created");
	}

    public List getTaggedObjects(String[] tags, Class objectClass) throws ProviderException 
    {
        return tagPersistenceProvider.getTaggedObjects(tags, objectClass);
    }   

    public String getTagsForObject(Integer objectId, Class objectClass) throws ProviderException 
    {
        List tags = tagPersistenceProvider.getTagsForObject(objectId, objectClass);    
        return formatTags(tags);
    }    

    public void setTags(Integer objectId, Class objectClass, String tags) throws ProviderException 
    {        
        tagPersistenceProvider.setTags(objectId, objectClass.getName(), parseTags(tags));        
    }
    
    public String getTagList(Class objectClass) throws ProviderException
    {
        List tags = tagPersistenceProvider.getTagList(objectClass); 
        return formatTags(tags);
    }   
    
    public String formatTags(List tags) 
    {             
        StringBuffer tagString = new StringBuffer();
        
        Iterator iterator = tags.iterator();
        while (iterator.hasNext())
        {
            Object tag = iterator.next();
            
            if (tag instanceof ORTag)
            {               
                tagString.append(((ORTag)tag).getTag() + ",");
            }
            else
            {
                tagString.append("\"" + tag + "\",");
            }
        }   
        
        if (tagString.length() == 0) return null;
        
        return tagString.substring(0, tagString.length() -1);
    }
    
    public String[] parseTags(String tags) 
    {   
        ArrayList<String> tagList  = new ArrayList<String>();
        
        StringTokenizer tokenizer = new StringTokenizer(tags, " \t;,|");        
        while (tokenizer.hasMoreTokens())
        {
            tagList.add(tokenizer.nextToken().trim());           
        }
        
        String[] tagArray = new String[tagList.size()];
        tagList.toArray(tagArray);
        
        return tagArray;
    }
}