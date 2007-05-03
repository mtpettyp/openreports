/*
 * Copyright (C) 2002 Erik Swenson - erik@oreports.com
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.efs.openreports.objects.Report;
import org.efs.openreports.providers.DirectoryProvider;
import org.efs.openreports.providers.ProviderException;
import org.efs.openreports.providers.ReportProvider;
import org.efs.openreports.providers.persistence.ReportPersistenceProvider;

public class ReportProviderImpl	implements ReportProvider
{
	protected static Logger log = Logger.getLogger(ReportProviderImpl.class.getName());
	
	private ReportPersistenceProvider reportPersistenceProvider;

	private DirectoryProvider directoryProvider;
	
	public ReportProviderImpl(DirectoryProvider directoryProvider) throws ProviderException
	{
		this.directoryProvider = directoryProvider;
		init();
	}
	
	protected void init() throws ProviderException
	{
		reportPersistenceProvider = new ReportPersistenceProvider();
		log.info("ReportProviderImpl created");
	}

	public List getReportFileNames() throws ProviderException
	{
		File file = new File(directoryProvider.getReportDirectory());
		
		if (!file.exists())
		{
			throw new ProviderException(
					"BaseDirectory Invalid: Set baseDirectory in openreports.properties to full path "
							+ " of directory containing your JasperReports files ");
		}
		
		File[] files = file.listFiles();

		ArrayList<String> fileNames = new ArrayList<String>(files.length);
		for (int i = 0; i < files.length; i++)
		{
			if (files[i].getName().endsWith(".jasper")
					|| files[i].getName().endsWith(".rptdesign")
					|| files[i].getName().endsWith(".vm")
					|| files[i].getName().endsWith(".xml")
					|| files[i].getName().endsWith(".xls"))
			{
				fileNames.add(files[i].getName());
			}
		}

		return fileNames;
	}

	public Report getReport(Integer id) throws ProviderException
	{
		return reportPersistenceProvider.getReport(id);
	}
	
	public Report getReport(String reportName) throws ProviderException
	{
		return reportPersistenceProvider.getReport(reportName);
	}


	public List getReports() throws ProviderException
	{
		return reportPersistenceProvider.getReports();
	}

	public Report insertReport(Report report) throws ProviderException
	{
		return reportPersistenceProvider.insertReport(report);
	}

	public void updateReport(Report report) throws ProviderException
	{
		reportPersistenceProvider.updateReport(report);
	}

	public void deleteReport(Report report) throws ProviderException
	{
		reportPersistenceProvider.deleteReport(report);
	}

	public void setDirectoryProvider(DirectoryProvider directoryProvider)
	{
		this.directoryProvider = directoryProvider;
	}

}