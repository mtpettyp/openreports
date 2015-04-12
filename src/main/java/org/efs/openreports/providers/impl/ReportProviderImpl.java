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
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.log4j.Logger;
import org.efs.openreports.objects.Report;
import org.efs.openreports.objects.ReportGroup;
import org.efs.openreports.objects.ReportLog;
import org.efs.openreports.objects.ReportTemplate;
import org.efs.openreports.providers.DirectoryProvider;
import org.efs.openreports.providers.HibernateProvider;
import org.efs.openreports.providers.ProviderException;
import org.efs.openreports.providers.ReportProvider;
import org.efs.openreports.util.LocalStrings;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

public class ReportProviderImpl	implements ReportProvider
{
	protected static Logger log = Logger.getLogger(ReportProviderImpl.class.getName());
		
	private DirectoryProvider directoryProvider;
	private HibernateProvider hibernateProvider;
	
	public ReportProviderImpl(DirectoryProvider directoryProvider, HibernateProvider hibernateProvider) throws ProviderException
	{
		this.directoryProvider = directoryProvider;
		this.hibernateProvider = hibernateProvider;
		
		log.info("ReportProviderImpl created");
	}	

	public List<String> getReportFileNames() throws ProviderException
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
                    || files[i].getName().endsWith(".ftl")
					|| files[i].getName().endsWith(".xls"))
			{
				fileNames.add(files[i].getName());
			}
		}
		
		Collections.sort(fileNames);

		return fileNames;
	}
	
	public List<ReportTemplate> getReportTemplates() throws ProviderException
	{	
		List<String> fileNames =  getReportFileNames();
		
		ArrayList<ReportTemplate> reportTemplates = new ArrayList<ReportTemplate>();
		for (String fileName : fileNames)
		{			
			ReportTemplate template = getReportTemplate(fileName);
		    reportTemplates.add(template);
		}
		
		return reportTemplates;
	}
	
	public ReportTemplate getReportTemplate(String templateName) throws ProviderException
	{		
		File reportDirectory = new File(directoryProvider.getReportDirectory());		
		FileFilter templateFilter = new WildcardFileFilter(templateName + "*");
	    File[] templateFiles = reportDirectory.listFiles(templateFilter);
	    
	    String[] revisions = new String[templateFiles.length];
	    for (int i=0; i <  templateFiles.length; i++)
	    {
	    	revisions[i] = templateFiles[i].getName();
	    }
	    
	    Arrays.sort(revisions);
	    
	    ReportTemplate template = new ReportTemplate(templateName);
	    template.setRevisions(revisions);		 
		
		return template;
	}

	public Report getReport(Integer id) throws ProviderException
	{
		return (Report) hibernateProvider.load(Report.class, id);
	}

	public Report getReport(String name) throws ProviderException
	{
		Session session = null;
		
		try
		{
			session = hibernateProvider.openSession();
			
			Criteria criteria = session.createCriteria(Report.class);
			criteria.add(Restrictions.eq("name", name));
			
			return (Report) criteria.uniqueResult();
		}
		catch (HibernateException he)
		{
			throw new ProviderException(he);
		}
		finally
		{
			hibernateProvider.closeSession(session);
		}
	}
	 
	
	@SuppressWarnings("unchecked")
	public List<Report> getReports() throws ProviderException
	{
		String fromClause =
			"from org.efs.openreports.objects.Report report order by report.name ";
		
		return (List<Report>) hibernateProvider.query(fromClause);
	}

	public Report insertReport(Report report) throws ProviderException
	{
		return (Report) hibernateProvider.save(report);
	}

	public void updateReport(Report report) throws ProviderException
	{
		hibernateProvider.update(report);
	}

	public void deleteReport(Report report) throws ProviderException
	{
		Session session = hibernateProvider.openSession();
		Transaction tx = null;

		try
		{
			tx = session.beginTransaction();
			
			//delete report			
			session.delete(report);		
			
			//delete report log entries for report
			Iterator<?> iterator =  session
				.createQuery(
					"from  org.efs.openreports.objects.ReportLog reportLog where reportLog.report.id = ? ")
				.setInteger(0, report.getId().intValue()).iterate();
					
			while(iterator.hasNext())
			{
				ReportLog reportLog = (ReportLog) iterator.next();		 	
				session.delete(reportLog);
			}		
			
			//remove report from groups
			iterator =  session
				.createQuery(
						"from org.efs.openreports.objects.ReportGroup reportGroup").iterate();
						
			 while(iterator.hasNext())
			 {
			 	ReportGroup reportGroup = (ReportGroup) iterator.next();
			 	
			 	List<Report> reports = reportGroup.getReports();			 	
			 	if (reports.contains(report))
			 	{
			 		reports.remove(report);
			 	}
			 }	
			
			tx.commit();
		}
		catch (HibernateException he)
		{
			hibernateProvider.rollbackTransaction(tx);
						
			if (he.getCause() != null && he.getCause().getMessage() != null && he.getCause().getMessage().toUpperCase().indexOf("CONSTRAINT") > 0)
			{
				throw new ProviderException(LocalStrings.ERROR_REPORT_DELETION);
			}
			
			log.error("deleteReport", he);			
			throw new ProviderException(LocalStrings.ERROR_SERVERSIDE);
		}
		finally
		{
			hibernateProvider.closeSession(session);
		}
	}

}