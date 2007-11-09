/*
 * Copyright (C) 2005 Erik Swenson - erik@oreports.com
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

import java.util.Locale;
import org.displaytag.decorator.TableDecorator;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.LocaleProvider;
import com.opensymphony.xwork2.TextProvider;
import com.opensymphony.xwork2.TextProviderFactory;

/*
 * Workaround because org.displaytag.tags.ColumnTag no longer
 * has a setValue method...
 */
public class HRefColumnDecorator extends TableDecorator implements LocaleProvider
{		
    private final transient TextProvider textProvider;
    
    public HRefColumnDecorator()
    {
        textProvider = new TextProviderFactory().createInstance(getClass(), this);
    }
    
    public Locale getLocale()
    {       
        return ActionContext.getContext().getLocale();
    }	
	
	public Object getRemoveLink()
	{
		return textProvider.getText(LocalStrings.LINK_DELETE);		
	}	
	
	public Object getUpdateLink()
	{
		return textProvider.getText(LocalStrings.LINK_UPDATE);	
	}
	
	public Object getAddToGroupLink()
	{
		return textProvider.getText(LocalStrings.LINK_GROUPS);	
	}
	
	public Object getUsersLink()
	{
		return textProvider.getText(LocalStrings.LINK_USERS);	
	}	
}
