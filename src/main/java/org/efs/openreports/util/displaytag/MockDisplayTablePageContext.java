/*
 * Copyright (C) 2009 Erik Swenson - erik@oreports.com
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

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.VariableResolver;

import mondrian.tui.MockHttpServletRequest;
import mondrian.tui.MockHttpServletResponse;
import mondrian.tui.MockServletContext;

import org.displaytag.tags.TableTag;
import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

/**
 * Mock PageContext that implements the methods and sets the attributes required to use the
 * DisplayTag exporting code outside of an actual servlet context.
 * 
 * @author eswenson@oreports.com
 *
 */
public class MockDisplayTablePageContext extends PageContext 
{	
	public static final String EXPORT_TYPE_CSV = "1";
	public static final String EXPORT_TYPE_XLS = "2";
	public static final String EXPORT_TYPE_PDF = "5";
	
	private MockHttpServletRequest request = new MockHttpServletRequest();
	private MockHttpServletResponse response = new MockHttpServletResponse();	
	private MockServletContext servletContext = new MockServletContext();
	
	public MockDisplayTablePageContext(String exportType, ApplicationContext applicationContext) 
	{	
		request.setupAddParameter(new ParamEncoder(null).encodeParameterName(TableTagParameters.PARAMETER_EXPORTTYPE), exportType);
	
		HashMap<String,String> map = new HashMap<String,String>();
		map.put(TableTagParameters.BEAN_BUFFER, "placeholder");
		
		request.setAttribute(TableTag.FILTER_CONTENT_OVERRIDE_BODY, map);	
		
		servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, applicationContext);
	}

	@Override
	public ServletRequest getRequest() 
	{		
		return request;		
	}

	@Override
	public ServletResponse getResponse() 
	{
		return response;
	}
	
	@Override
	public Object findAttribute(String attributeName) 
	{
		return request.getAttribute(attributeName);
	}
	
	@Override
	public ServletContext getServletContext() 
	{		
		return servletContext;
	}
	
	@Override
	public void forward(String arg0) throws ServletException, IOException {}

	@Override
	public Exception getException() 
	{		
		return null;
	}

	@Override
	public Object getPage() 
	{		
		return null;
	}	

	@Override
	public ServletConfig getServletConfig() 
	{		
		return null;
	}	

	@Override
	public HttpSession getSession() 
	{		
		return null;
	}

	@Override
	public void handlePageException(Exception arg0) throws ServletException, IOException {}
	
	@Override
	public void handlePageException(Throwable arg0) throws ServletException, IOException {}

	@Override
	public void include(String arg0, boolean arg1) throws ServletException,	IOException {}

	@Override
	public void include(String arg0) throws ServletException, IOException {}

	@Override
	public void initialize(Servlet arg0, ServletRequest arg1, ServletResponse arg2, String arg3, boolean arg4, int arg5, boolean arg6) throws IOException, IllegalStateException, IllegalArgumentException {}

	@Override
	public void release() {}	

	@Override
	public Object getAttribute(String arg0, int arg1) 
	{		
		return null;
	}

	@Override
	public Object getAttribute(String arg0)
	{		
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Enumeration getAttributeNamesInScope(int arg0) 
	{
		return null;
	}

	@Override
	public int getAttributesScope(String arg0)
	{		
		return 0;
	}

	@Override
	public ExpressionEvaluator getExpressionEvaluator() 
	{		
		return null;
	}

	@Override
	public JspWriter getOut()
	{			
		return null;
	}

	@Override
	public VariableResolver getVariableResolver() 
	{		
		return null;
	}

	@Override
	public void removeAttribute(String arg0, int arg1) {}

	@Override
	public void removeAttribute(String arg0) {}

	@Override
	public void setAttribute(String arg0, Object arg1, int arg2) {}

	@Override
	public void setAttribute(String arg0, Object arg1) {}
}
