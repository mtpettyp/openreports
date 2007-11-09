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

package org.efs.openreports.providers.persistence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.efs.openreports.objects.ORTag;
import org.efs.openreports.providers.HibernateProvider;
import org.efs.openreports.providers.ProviderException;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;

public class TagPersistenceProvider 
{
	protected static Logger log = Logger.getLogger(TagPersistenceProvider.class.getName());

	public TagPersistenceProvider() throws ProviderException
	{
		super();

		log.info("TagPersistenceProvider Created.");
	}	

	public List getTaggedObjects(String[] tags, Class objectClass) throws ProviderException
	{
        ArrayList<Object> objects = new ArrayList<Object>();     
		Session session = null;        
		
		try
		{            
			session = HibernateProvider.openSession();
			
			Criteria criteria = session.createCriteria(ORTag.class);
			criteria.add(Expression.in("tag", tags));
                        
            if (objectClass != null)
            {
                criteria.add(Expression.eq("objectClass", objectClass.getName()));                
            }
			
            Iterator iterator = criteria.list().iterator();
            while (iterator.hasNext())
            {
                ORTag tag = (ORTag) iterator.next();
                
                if (objectClass != null)
                {
                    objects.add(HibernateProvider.load(objectClass, tag.getObjectId()));  
                }
                else
                {
                    objects.add(HibernateProvider.load(Class.forName(tag.getObjectClass()), tag.getObjectId()));  
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
			HibernateProvider.closeSession(session);
		}
	}
    
    public List getTagsForObject(Integer objectId, Class objectClass) throws ProviderException
    {
        Session session = null;
        
        try
        {
            session = HibernateProvider.openSession();
            
            Criteria criteria = session.createCriteria(ORTag.class);
            criteria.add(Expression.eq("objectId", objectId));
            criteria.add(Expression.eq("objectClass", objectClass.getName()));
            
            return criteria.list();
        }
        catch (HibernateException he)
        {
            throw new ProviderException(he);
        }
        finally
        {
            HibernateProvider.closeSession(session);
        }
    }
    
    public List getTagList(Class objectClass) throws ProviderException
    {        
        StringBuffer queryString = new StringBuffer();
        queryString.append("select distinct orTag.tag from org.efs.openreports.objects.ORTag orTag ");        
        if (objectClass != null) queryString.append("where orTag.objectClass = ? " );
        queryString.append("order by orTag.tag ");
        
        Session session = HibernateProvider.openSession();
        
        try
        {
            Query query = session.createQuery(queryString.toString());
            query.setCacheable(true);
            
            if (objectClass != null) query.setString(0, objectClass.getName());
            
            List list = query.list();       

            return list;
        }
        catch (HibernateException he)
        {           
            throw new ProviderException(he);
        }
        finally
        {
            HibernateProvider.closeSession(session);
        }      
    }   
    
	public void setTags(Integer objectId, String objectClass, String[] tags) throws ProviderException
	{
		Session session = HibernateProvider.openSession();
		Transaction tx = null;

		try
		{
			tx = session.beginTransaction();			
			
			//delete all tags for the given userId, objectId and objectType
			session
					.createQuery(
							"DELETE org.efs.openreports.objects.ORTag orTag where orTag.objectId = ? and orTag.objectClass = ? ")
					.setInteger(0, objectId.intValue()).setString(1, objectClass).executeUpdate();
				
			tx.commit();
			tx = session.beginTransaction();			
			
			//insert new tags;
            for (int i=0; i < tags.length; i++)
            {
                ORTag tag = new ORTag();                
                tag.setObjectId(objectId);
                tag.setObjectClass(objectClass);
                tag.setTag(tags[i]);
                
                HibernateProvider.save(tag);
            }			
			
			tx.commit();
		}
		catch (HibernateException he)
		{
			HibernateProvider.rollbackTransaction(tx);

			he.printStackTrace();

			throw new ProviderException(he.getMessage());
		}
		finally
		{
			HibernateProvider.closeSession(session);
		}
	}
}