/*
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
package org.efs.openreports.delivery;

import com.thoughtworks.xstream.XStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.efs.openreports.engine.output.ReportEngineOutput;
import org.efs.openreports.objects.DeliveredReport;
import org.efs.openreports.objects.MailMessage;
import org.efs.openreports.objects.Report;
import org.efs.openreports.objects.ReportSchedule;
import org.efs.openreports.objects.ReportUser;
import org.efs.openreports.providers.DirectoryProvider;
import org.efs.openreports.providers.MailProvider;
import org.efs.openreports.providers.ProviderException;

public class FileSystemDeliveryMethod implements DeliveryMethod
{  
    protected static Logger log = Logger.getLogger(FileSystemDeliveryMethod.class.getName());
    
    private MailProvider mailProvider;
    private DirectoryProvider directoryProvider;
    
    public void deliverReport(ReportSchedule reportSchedule, ReportEngineOutput reportOutput) throws DeliveryException 
    {
        Report report = reportSchedule.getReport();
        ReportUser user = reportSchedule.getUser();
        
        Date runDate = new Date();
        
        String fileName = runDate.getTime() + "-"
                + StringUtils.deleteWhitespace(user.getName()) + "-"
                + StringUtils.deleteWhitespace(report.getName());                       
        
        try
        {
            FileOutputStream file = new FileOutputStream(directoryProvider
                .getReportGenerationDirectory()
                + fileName + reportOutput.getContentExtension());
            
            file.write(reportOutput.getContent());  
            file.flush();
            file.close();
        }
        catch(IOException ioe)
        {
            throw new DeliveryException(ioe);
        }        
        
        DeliveredReport info = new DeliveredReport();             
        info.setParameters(reportSchedule.getReportParameters());
        info.setReportDescription(reportSchedule.getScheduleDescription());
        info.setReportName(report.getName());
        info.setReportFileName(fileName + reportOutput.getContentExtension());
        info.setRunDate(runDate);
        info.setUserName(user.getName());
        info.setDeliveryMethod("fileSystemDeliveryMethod");
        
        try
        {
            FileOutputStream file = new FileOutputStream(directoryProvider.getReportGenerationDirectory() + fileName + ".xml");

            XStream xStream = new XStream();
            xStream.alias("reportGenerationInfo", DeliveredReport.class);
            xStream.toXML(info, file);
        
            file.flush();
            file.close();
        }
        catch(IOException ioe)
        {
            throw new DeliveryException(ioe);
        }   
        
        MailMessage mail = new MailMessage();               
        mail.setSender(user.getEmail());
        mail.parseRecipients(reportSchedule.getRecipients());
        mail.setText(report.getName() + ": Generated on " + new Date());
        mail.setBounceAddress(reportSchedule.getDeliveryReturnAddress());
        
        if (reportSchedule.getScheduleDescription() != null && reportSchedule.getScheduleDescription().trim().length() > 0)
        {
            mail.setSubject(reportSchedule.getScheduleDescription());
        }
        else
        {
            mail.setSubject(reportSchedule.getReport().getName());
        }      
        
        try
        {
            mailProvider.sendMail(mail);    
        }
        catch(ProviderException pe)
        {
            throw new DeliveryException(pe);
        }   
        
        log.debug(report.getName() + " written to: " + fileName);
    }
   
    public DeliveredReport[] getDeliveredReports(ReportUser user) throws DeliveryException
    {        
        IOFileFilter extensionFilter = FileFilterUtils.suffixFileFilter("xml");
        
        File directory = new File(directoryProvider.getReportGenerationDirectory());

        ArrayList<DeliveredReport> deliveredReports = new ArrayList<DeliveredReport>();

        Iterator iterator = FileUtils.iterateFiles(directory, extensionFilter, null);
        while (iterator.hasNext())
        {
            File file = (File) iterator.next();

            if (FilenameUtils.wildcardMatch(file.getName(), "*" + user.getName() + "*"))
            {
                XStream xStream = new XStream();
                xStream.alias("reportGenerationInfo", DeliveredReport.class);
                
                try
                {
                    FileInputStream inputStream = new FileInputStream(file);
            
                    DeliveredReport report = (DeliveredReport) xStream.fromXML(inputStream);                    
            
                    deliveredReports.add(report);
            
                    inputStream.close();
                }
                catch(IOException io)
                {
                    log.warn(io.toString());
                }
            }
        }   
        
        DeliveredReport[] reports = new DeliveredReport[deliveredReports.size()];
        deliveredReports.toArray(reports);
        
        return reports;
    }    

    public byte[] getDeliveredReport(DeliveredReport deliveredReport) throws DeliveryException
    {
        try
        {
            File file = new File(directoryProvider.getReportGenerationDirectory() + deliveredReport.getReportFileName());        
            return FileUtils.readFileToByteArray(file);
        }
        catch(IOException ioe)
        {
            throw new DeliveryException(ioe);
        }        
    }

    public void setDirectoryProvider(DirectoryProvider directoryProvider)
    {
        this.directoryProvider = directoryProvider;
    }
    
    public void setMailProvider(MailProvider mailProvider) 
    {
        this.mailProvider = mailProvider;
    }
}
