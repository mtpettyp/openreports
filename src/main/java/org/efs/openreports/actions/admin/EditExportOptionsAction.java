/*
 * Copyright (C) 2006 Erik Swenson - erik@oreports.com
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

package org.efs.openreports.actions.admin;

import com.opensymphony.xwork2.ActionSupport;

import org.apache.log4j.Logger;

import org.efs.openreports.objects.Report;
import org.efs.openreports.objects.ReportExportOption;
import org.efs.openreports.providers.ReportProvider;

public class EditExportOptionsAction extends ActionSupport  
{	
	private static final long serialVersionUID = 8085217825617019729L;

	protected static Logger log = Logger.getLogger(EditExportOptionsAction.class);
	
	private String command;
	private String submitType;

	private int id;
	
	private boolean xlsRemoveEmptySpaceBetweenRows;
	private boolean xlsOnePagePerSheet;	
	private boolean xlsAutoDetectCellType;
	private boolean xlsWhitePageBackground;	
	 
	private boolean htmlRemoveEmptySpaceBetweenRows;	
	private boolean htmlWhitePageBackground;
	private boolean htmlUsingImagesToAlign;
	private boolean htmlWrapBreakWord; 
	
	private Report report;
	
	private ReportProvider reportProvider;	

	public String execute()
	{
		try
		{
			report = reportProvider.getReport(new Integer(id));				
			ReportExportOption exportOption = report.getReportExportOption();			

			if (command.equals("edit") && submitType == null)
			{
				id = report.getId().intValue();
				
				xlsRemoveEmptySpaceBetweenRows = exportOption.isXlsRemoveEmptySpaceBetweenRows();
				xlsOnePagePerSheet = exportOption.isXlsOnePagePerSheet();
				xlsAutoDetectCellType = exportOption.isXlsAutoDetectCellType();
				xlsWhitePageBackground = exportOption.isXlsWhitePageBackground();
				
				htmlRemoveEmptySpaceBetweenRows = exportOption.isHtmlRemoveEmptySpaceBetweenRows();
				htmlUsingImagesToAlign = exportOption.isHtmlUsingImagesToAlign();
				htmlWhitePageBackground = exportOption.isHtmlWhitePageBackground();
				htmlWrapBreakWord = exportOption.isHtmlWrapBreakWord();
			}

			if (submitType == null)
				return INPUT;

			
			exportOption.setXlsOnePagePerSheet(xlsOnePagePerSheet);
			exportOption.setXlsRemoveEmptySpaceBetweenRows(xlsRemoveEmptySpaceBetweenRows);
			exportOption.setXlsAutoDetectCellType(xlsAutoDetectCellType);
			exportOption.setXlsWhitePageBackground(xlsWhitePageBackground);
			
			exportOption.setHtmlRemoveEmptySpaceBetweenRows(htmlRemoveEmptySpaceBetweenRows);
			exportOption.setHtmlUsingImagesToAlign(htmlUsingImagesToAlign);
			exportOption.setHtmlWhitePageBackground(htmlWhitePageBackground);
			exportOption.setHtmlWrapBreakWord(htmlWrapBreakWord);
			
			report.setReportExportOption(exportOption);

			reportProvider.updateReport(report);		

			return SUCCESS;
		}
		catch (Exception e)
		{
			log.error(e.toString());
			addActionError(e.getMessage());
			return INPUT;
		}
	}

	public String getCommand()
	{
		return command;
	}
	
	public void setCommand(String command)
	{
		this.command = command;
	}

	public String getSubmitType()
	{
		return submitType;
	}

	public void setSubmitType(String submitType)
	{
		this.submitType = submitType;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}
	
	public void setReportProvider(ReportProvider reportProvider)
	{
		this.reportProvider = reportProvider;
	}	

	public ReportProvider getReportProvider()
	{
		return reportProvider;
	}

	public Report getReport()
	{
		return report;
	}

	public void setReport(Report report)
	{
		this.report = report;
	}

	public boolean isHtmlRemoveEmptySpaceBetweenRows()
	{
		return htmlRemoveEmptySpaceBetweenRows;
	}

	public void setHtmlRemoveEmptySpaceBetweenRows(boolean htmlRemoveEmptySpaceBetweenRows)
	{
		this.htmlRemoveEmptySpaceBetweenRows = htmlRemoveEmptySpaceBetweenRows;
	}

	public boolean isHtmlUsingImagesToAlign()
	{
		return htmlUsingImagesToAlign;
	}

	public void setHtmlUsingImagesToAlign(boolean htmlUsingImagesToAlign)
	{
		this.htmlUsingImagesToAlign = htmlUsingImagesToAlign;
	}

	public boolean isHtmlWhitePageBackground()
	{
		return htmlWhitePageBackground;
	}

	public void setHtmlWhitePageBackground(boolean htmlWhitePageBackground)
	{
		this.htmlWhitePageBackground = htmlWhitePageBackground;
	}

	public boolean isHtmlWrapBreakWord()
	{
		return htmlWrapBreakWord;
	}

	public void setHtmlWrapBreakWord(boolean htmlWrapBreakWord)
	{
		this.htmlWrapBreakWord = htmlWrapBreakWord;
	}

	public boolean isXlsAutoDetectCellType()
	{
		return xlsAutoDetectCellType;
	}

	public void setXlsAutoDetectCellType(boolean xlsAutoDetectCellType)
	{
		this.xlsAutoDetectCellType = xlsAutoDetectCellType;
	}

	public boolean isXlsOnePagePerSheet()
	{
		return xlsOnePagePerSheet;
	}

	public void setXlsOnePagePerSheet(boolean xlsOnePagePerSheet)
	{
		this.xlsOnePagePerSheet = xlsOnePagePerSheet;
	}

	public boolean isXlsRemoveEmptySpaceBetweenRows()
	{
		return xlsRemoveEmptySpaceBetweenRows;
	}

	public void setXlsRemoveEmptySpaceBetweenRows(boolean xlsRemoveEmptySpaceBetweenRows)
	{
		this.xlsRemoveEmptySpaceBetweenRows = xlsRemoveEmptySpaceBetweenRows;
	}

	public boolean isXlsWhitePageBackground()
	{
		return xlsWhitePageBackground;
	}

	public void setXlsWhitePageBackground(boolean xlsWhitePageBackground)
	{
		this.xlsWhitePageBackground = xlsWhitePageBackground;
	}

}