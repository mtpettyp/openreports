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

package org.efs.openreports.util;

import org.apache.log4j.Logger;
import org.displaytag.decorator.TableDecorator;
import org.efs.openreports.objects.ORTag;
import org.efs.openreports.objects.Report;
import org.efs.openreports.objects.ReportGroup;
import org.efs.openreports.objects.ReportUser;
import org.efs.openreports.providers.ProviderException;
import org.efs.openreports.providers.TagProvider;
import org.efs.openreports.providers.impl.TagProviderImpl;

/**
 * The <tt>SearchResultsTableDecorator</tt> class is a DisplayTag <tt>TableDecorator</tt>
 * used to populate the <tt>Action</tt> and <tt>Tags</tt> columns of a DisplayTag table.
 * 
 * These columns are generated based on the class of the current row object. Currently only
 * <tt>Report</tt>, <tt>ReportGroup</tt>, and <tt>ReportUser</tt> classes are supported.
 */

public class SearchResultsTableDecorator extends TableDecorator   
{  
	protected static Logger log = Logger.getLogger(TagProviderImpl.class.getName());
	
	private TagProvider tagProvider;	
	
    /**
     * Generates the appropriate link to the edit action of the current row object
     * 
     * @return a link
     */
    public String getAction() 
    {
        Object object = getCurrentRowObject();

        if (object instanceof Report)
        {
            Report report = (Report)object;
            return "<a href=\"editReport.action?command=edit&amp;id=" + report.getId() + "\">Edit Report</a>";
        }
        else if (object instanceof ReportGroup) 
        {
            ReportGroup reportGroup = (ReportGroup)object;
            return "<a href=\"editGroup.action?command=edit&amp;id=" + reportGroup.getId() + "\">Edit Group</a>";
        }
        else if (object instanceof ReportUser) 
        {
            ReportUser reportUser = (ReportUser)object;
            return "<a href=\"editUser.action?command=edit&amp;id=" + reportUser.getId() + "\">Edit User</a>";
        }

        return "";
    }   
    
    public String getTags()
    {
    	Object object = getCurrentRowObject();
    	
    	Integer id = new Integer(-1);
    	
    	 if (object instanceof Report)
         {
             id = ((Report)object).getId();
            
         }
         else if (object instanceof ReportGroup) 
         {
        	 id = ((ReportGroup)object).getId();
         }
         else if (object instanceof ReportUser) 
         {
        	 id = ((ReportUser)object).getId();
         }
    	 
    	 try
    	 {
    		 String tagString = tagProvider.getTagsForObject(id, object.getClass(), ORTag.TAG_TYPE_UI);
    		 String[] tags = tagProvider.parseTags(tagString);
    		 
    		 tagString = "";
    		 
    		 for (int i=0; i < tags.length; i++)
    		 {
    			 tagString += "<a href=\"search.action?search=" + tags[i] + "\">" + tags[i] + "</a>,";    			 		
    		 }
    		
    		 return tagString.substring(0, tagString.length() - 2);
    	 }
    	 catch(ProviderException pe)
    	 {
    		 log.warn(pe);    		 
    		 return "";
    	 }    	
    }

	public void setTagProvider(TagProvider tagProvider) 
	{
		this.tagProvider = tagProvider;
	}    
}
