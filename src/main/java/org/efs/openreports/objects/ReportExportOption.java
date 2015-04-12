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

package org.efs.openreports.objects;

import java.io.Serializable;

public class ReportExportOption implements Serializable
{	
	private static final long serialVersionUID = -8911923598920065987L;
	
	private Integer id;
	
	private boolean xlsRemoveEmptySpaceBetweenRows;
	private boolean xlsOnePagePerSheet;	
	private boolean xlsAutoDetectCellType;
	private boolean xlsWhitePageBackground;	
	 
	private boolean htmlRemoveEmptySpaceBetweenRows;	
	private boolean htmlWhitePageBackground;
	private boolean htmlUsingImagesToAlign;
	private boolean htmlWrapBreakWord; 
 	
	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}	

	public boolean isHtmlRemoveEmptySpaceBetweenRows()
	{
		return htmlRemoveEmptySpaceBetweenRows;
	}

	public void setHtmlRemoveEmptySpaceBetweenRows(boolean htmlRemoveEmptySpaceBetweenRows)
	{
		this.htmlRemoveEmptySpaceBetweenRows = htmlRemoveEmptySpaceBetweenRows;
	}

	public boolean isXlsRemoveEmptySpaceBetweenRows()
	{
		return xlsRemoveEmptySpaceBetweenRows;
	}

	public void setXlsRemoveEmptySpaceBetweenRows(boolean xlsRemoveEmptySpaceBetweenRows)
	{
		this.xlsRemoveEmptySpaceBetweenRows = xlsRemoveEmptySpaceBetweenRows;
	}

	public boolean isXlsOnePagePerSheet()
	{
		return xlsOnePagePerSheet;
	}

	public void setXlsOnePagePerSheet(boolean xlsOnePagePerSheet)
	{
		this.xlsOnePagePerSheet = xlsOnePagePerSheet;
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

	public boolean isXlsWhitePageBackground()
	{
		return xlsWhitePageBackground;
	}

	public void setXlsWhitePageBackground(boolean xlsWhitePageBackground)
	{
		this.xlsWhitePageBackground = xlsWhitePageBackground;
	}
}