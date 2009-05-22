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
package org.efs.openreports.util.displaytag;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

import org.displaytag.localization.I18nResourceProvider;
import org.displaytag.localization.LocaleResolver;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.LocaleProvider;
import com.opensymphony.xwork2.TextProvider;
import com.opensymphony.xwork2.TextProviderFactory;

public class DisplayTagI18nStruts2Adapter implements LocaleResolver, I18nResourceProvider, LocaleProvider
{ 
	private final transient TextProvider textProvider;
	
    public DisplayTagI18nStruts2Adapter()
    {
        textProvider = new TextProviderFactory().createInstance(getClass(), this);
    }
    
    public Locale resolveLocale(HttpServletRequest request) 
    {    	
        return ActionContext.getContext().getLocale();
    }
   
    public Locale getLocale()
    {    	
        return ActionContext.getContext().getLocale();
    }
    
    public String getResource(String resourceKey, String defaultValue, Tag tag, PageContext pageContext)
    { 
    	return textProvider.getText(resourceKey, defaultValue);      
    }
}
