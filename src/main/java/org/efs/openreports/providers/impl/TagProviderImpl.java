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

package org.efs.openreports.providers.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;
import org.efs.openreports.objects.ORTag;
import org.efs.openreports.providers.HibernateProvider;
import org.efs.openreports.providers.ProviderException;
import org.efs.openreports.providers.TagProvider;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Restrictions;

/**
 * The <tt>TagProviderImpl</tt> class uses the Hibernate based <tt>TagPersistenceProvider</tt>
 * to persist tags to a database.  
 */

public class TagProviderImpl implements TagProvider
{
	protected static Logger log = Logger.getLogger(TagProviderImpl.class.getName());
	
	private HibernateProvider hibernateProvider;	
	
	public TagProviderImpl(HibernateProvider hibernateProvider) throws ProviderException
	{		
        this.hibernateProvider = hibernateProvider;
        log.info("TagProviderImpl created");
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
    
    public List getTaggedObjects(String[] tags, Class objectClass, String tagType) throws ProviderException
	{
        ArrayList<Object> objects = new ArrayList<Object>();     
		Session session = null;        
		
		try
		{            
			session = hibernateProvider.openSession();
			
			Criteria criteria = session.createCriteria(ORTag.class);
			criteria.add(Restrictions.in("tag", tags));
            criteria.add(Restrictions.eq("tagType", tagType));
                        
            if (objectClass != null)
            {
                criteria.add(Restrictions.eq("objectClass", objectClass.getName()));                
            }
			
            Iterator iterator = criteria.list().iterator();
            while (iterator.hasNext())
            {
                ORTag tag = (ORTag) iterator.next();
                
                if (objectClass != null)
                {
                    objects.add(hibernateProvider.load(objectClass, tag.getObjectId()));  
                }
                else
                {
                    objects.add(hibernateProvider.load(Class.forName(tag.getObjectClass()), tag.getObjectId()));  
                }
            }            
            
            return objects;            
		}
		catch (Exception he)
		{
			throw new ProviderException(he);
		}
		finally
		{
			hibernateProvider.closeSession(session);
		}
	}
    
    public String getTagsForObject(Integer objectId, Class objectClass, String tagType) throws ProviderException
    {
        Session session = null;
        
        try
        {
            session = hibernateProvider.openSession();
            
            Criteria criteria = session.createCriteria(ORTag.class);
            criteria.add(Restrictions.eq("objectId", objectId));
            criteria.add(Restrictions.eq("objectClass", objectClass.getName()));
            criteria.add(Restrictions.eq("tagType", tagType));
            
            return formatTags(criteria.list());
        }
        catch (HibernateException he)
        {
            throw new ProviderException(he);
        }
        finally
        {
            hibernateProvider.closeSession(session);
        }
    }
    
    public String getTagList(Class objectClass, String tagType) throws ProviderException
    {        
        StringBuffer queryString = new StringBuffer();
        queryString.append("select distinct orTag.tag from org.efs.openreports.objects.ORTag orTag ");
        queryString.append("where orTag.tagType = ? " );
        if (objectClass != null) queryString.append("and orTag.objectClass = ? " );
        queryString.append("order by orTag.tag ");
        
        Session session = hibernateProvider.openSession();
        
        try
        {
            Query query = session.createQuery(queryString.toString());
            query.setCacheable(true);
            query.setString(0, tagType);
            
            if (objectClass != null) query.setString(1, objectClass.getName());            
            
            return formatTags(query.list());
        }
        catch (HibernateException he)
        {           
            throw new ProviderException(he);
        }
        finally
        {
            hibernateProvider.closeSession(session);
        }      
    }   
    
	public void setTags(Integer objectId, Class objectClass, String tagString, String tagType) throws ProviderException
	{
		Session session = hibernateProvider.openSession();
		Transaction tx = null;

		try
		{
			tx = session.beginTransaction();			
			
			//delete all tags for the given userId, objectId and objectType
			session
					.createQuery(
							"DELETE org.efs.openreports.objects.ORTag orTag where orTag.objectId = ? and orTag.objectClass = ? and orTag.tagType = ? ")
					.setInteger(0, objectId.intValue()).setString(1, objectClass.getName()).setString(2, tagType).executeUpdate();
				
			tx.commit();
			tx = session.beginTransaction();			
			
			String[] tags = parseTags(tagString);
			
			//insert new tags;
            for (int i=0; i < tags.length; i++)
            {
                ORTag tag = new ORTag();                
                tag.setObjectId(objectId);
                tag.setObjectClass(objectClass.getCanonicalName());
                tag.setTag(tags[i]);
                tag.setTagType(tagType);
                
                hibernateProvider.save(tag);
            }			
			
			tx.commit();
		}
		catch (HibernateException he)
		{
			hibernateProvider.rollbackTransaction(tx);

			he.printStackTrace();

			throw new ProviderException(he.getMessage());
		}
		finally
		{
			hibernateProvider.closeSession(session);
		}
	}
}