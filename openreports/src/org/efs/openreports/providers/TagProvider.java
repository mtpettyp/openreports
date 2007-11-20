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

package org.efs.openreports.providers;

import java.util.List;

public interface TagProvider
{    
    /** 
     * Parses a <tt>String</tt> of tags and assigns them to the object specified by the
     * given objectId and objectClass
     *  
     * @param objectId - <tt>Integer</tt> unique identifier of the object
     * @param objectClass - <tt>Class</tt> of the object
     * @param tags - <tt>String</tt> containing a list of tags
     * @param tagType - <tt>String</tt> specifying the type of tag  
     * @throws ProviderException
     */
    public void setTags(Integer objectId, Class objectClass, String tags, String tagType) throws ProviderException;
	
    
    /**
     * Returns a formatted <tt>String</tt> of all the tags assigned to the object specified
     * by the given objectId and objectClass
     * 
     * @param objectId - <tt>Integer</tt> unique identifier of the object
     * @param objectClass - <tt>Class</tt> of the object 
     * @param tagType - <tt>String</tt> specifying the type of tag   
     * @return a comma separated <tt>String</tt> of tags 
     * @throws ProviderException
     */
    public String getTagsForObject(Integer objectId, Class objectClass, String tagType) throws ProviderException;	
    
    
    /**
     * Returns a <tt>List</tt> of all objects of the objectClass that have been assigned one of
     * the tags specified. If objectClass is <tt>null</tt>, all classes will be included. 
     *  
     * @param tags
     * @param objectClass - <tt>Class</tt> of the object  
     * @param tagType - <tt>String</tt> specifying the type of tag   
     * @return a <tt>List</tt> of objects
     * @throws ProviderException
     */
    public List getTaggedObjects(String[] tags, Class objectClass, String tagType) throws ProviderException;
    
    /** 
     * Returns a <tt>String</tt> of all the unique tags assigned to objects of the objectClass or, 
     * if objectClass is <tt>null</tt>, all classes.
     * 
     * @param objectClass - <tt>Class</tt> of the object  
     * @param tagType - <tt>String</tt> specifying the type of tag   
     * @return a formatted <tt>String</tt> of unique tags 
     * @throws ProviderException
     */
    public String getTagList(Class objectClass, String tagType) throws ProviderException;    
    
    public String[] parseTags(String tags);
    public String formatTags(List tags); 
}