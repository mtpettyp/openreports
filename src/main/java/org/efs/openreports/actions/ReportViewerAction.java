/*
 * Copyright (C) 2004 Erik Swenson - erik@oreports.com
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

package org.efs.openreports.actions;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import org.apache.log4j.Logger;
import org.efs.openreports.ORStatics;
import org.efs.openreports.objects.Report;
import org.jfree.chart.encoders.SunPNGEncoderAdapter;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;

public class ReportViewerAction extends ActionSupport
{	
	private static final long serialVersionUID = 6910790405397971123L;

	protected static Logger log = Logger.getLogger(ReportViewerAction.class);

	private int pageIndex = 1;
	private int pageCount = 0;
	private float zoom = 1.0f;
	
	private String submitType;
	private Report report;
	
	public String execute()
	{		
		JasperPrint jasperPrint = (JasperPrint) ActionContext.getContext().getSession().get(
				ORStatics.JASPERPRINT);
		
		report = (Report) ActionContext.getContext().getSession().get(ORStatics.REPORT);
		
		if (jasperPrint != null && jasperPrint.getPages() != null)
		{
			pageCount = jasperPrint.getPages().size();
		}

		if (!"image".equals(submitType)) return SUCCESS;
		
		byte[] imageData = null; 
		
		if (jasperPrint != null)
		{
			try
			{
				BufferedImage image = (BufferedImage) JasperPrintManager.printPageToImage(jasperPrint, pageIndex -1, zoom);
				imageData = new SunPNGEncoderAdapter().encode(image);
			}
			catch(Exception e)
			{
				addActionError(e.getMessage());
				log.error(e.toString());
			}
		}			
		
		if (imageData != null)
		{

			HttpServletResponse response = ServletActionContext.getResponse();

			try
			{
				response.setContentLength(imageData.length);
				ServletOutputStream ouputStream = response.getOutputStream();
				ouputStream.write(imageData, 0, imageData.length);
				ouputStream.flush();
				ouputStream.close();
			}
			catch (IOException ioe)
			{
				log.warn(ioe.toString());
			}
		}

		return NONE;
	}
	
	public int getPageIndex()
	{
		return pageIndex;
	}
	
	public void setPageIndex(int pageIndex)
	{
		this.pageIndex = pageIndex;
	}
	
	public float getZoom()
	{
		return zoom;
	}
	
	public void setZoom(float zoom)
	{
		this.zoom = zoom;
	}
	
	public int getPageCount()
	{
		return pageCount;
	}

	public String getSubmitType()
	{
		return submitType;
	}

	public void setSubmitType(String submitType)
	{
		this.submitType = submitType;
	}

	public Report getReport()
	{
		return report;
	}

	public void setReport(Report report)
	{
		this.report = report;
	}	
}