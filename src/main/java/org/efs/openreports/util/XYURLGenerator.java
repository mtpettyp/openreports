/*
 * Copyright (C) 2007 Erik Swenson - erik@oreports.com
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

package org.efs.openreports.util;

import org.jfree.chart.urls.StandardXYURLGenerator;
import org.jfree.data.xy.XYDataset;

public class XYURLGenerator extends StandardXYURLGenerator
{	
	private static final long serialVersionUID = 636190210691457205L;
	
	protected String prefix;
	protected String seriesParameterName;
	protected String itemParameterName;
    
	public XYURLGenerator(String prefix, String seriesParameterName, String itemParameterName)
    {
		super(prefix, seriesParameterName, itemParameterName);
		
		this.prefix = prefix;
		this.itemParameterName = itemParameterName;
		this.seriesParameterName = seriesParameterName;		
    }
	
	public String generateURL(XYDataset dataset, int series, int item)
    {
        String url = prefix;
        boolean firstParameter = url.indexOf("?") == -1;
        
        url = url + (firstParameter ? "?" : "&amp;");
        url = url + seriesParameterName + "=" + dataset.getSeriesKey(series) + "&amp;" + itemParameterName + "=" + dataset.getXValue(series, item);
        
        return url;
    }
}
