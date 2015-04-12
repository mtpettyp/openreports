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

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.efs.openreports.ReportConstants.ExportType;
import org.efs.openreports.engine.output.JasperReportEngineOutput;
import org.efs.openreports.engine.output.ReportEngineOutput;
import org.efs.openreports.objects.DeliveredReport;
import org.efs.openreports.objects.MailMessage;
import org.efs.openreports.objects.ReportSchedule;
import org.efs.openreports.objects.ReportUser;
import org.efs.openreports.providers.MailProvider;
import org.efs.openreports.providers.ProviderException;
import org.efs.openreports.util.ByteArrayDataSource;


public class EMailDeliveryMethod implements DeliveryMethod 
{
    protected static Logger log = Logger.getLogger(EMailDeliveryMethod.class.getName());
    
    private MailProvider mailProvider;
    
    public void deliverReport(ReportSchedule reportSchedule, ReportEngineOutput reportOutput) throws DeliveryException 
    {
        ArrayList<ByteArrayDataSource> htmlImageDataSources = new ArrayList<ByteArrayDataSource>();
        
        ByteArrayDataSource byteArrayDataSource = exportReport(reportOutput, reportSchedule, htmlImageDataSources);

        MailMessage mail = new MailMessage();               
        mail.setByteArrayDataSource(byteArrayDataSource);
        mail.addHtmlImageDataSources(htmlImageDataSources);          
        mail.setSender(reportSchedule.getUser().getEmail());
        mail.parseRecipients(reportSchedule.getRecipients());
        mail.setBounceAddress(reportSchedule.getDeliveryReturnAddress());
        
        if (reportSchedule.getScheduleDescription() != null && reportSchedule.getScheduleDescription().trim().length() > 0)
        {
            mail.setSubject(reportSchedule.getScheduleDescription());
        }
        else
        {
            mail.setSubject(reportSchedule.getReport().getName());
        }
        
        if (reportSchedule.getExportType() != ExportType.HTML.getCode())
        {
            mail.setText(reportSchedule.getReport().getName() + ": Generated on " + new Date());
        }

        try
        {
            mailProvider.sendMail(mail);
        }
        catch(ProviderException pe)
        {
            throw new DeliveryException(pe);
        }
        
        log.debug(byteArrayDataSource.getName() + " sent to: " + mail.formatRecipients(";"));        
    }
    
    public byte[] getDeliveredReport(DeliveredReport deliveredReport) throws DeliveryException 
    {        
        throw new DeliveryException("Method getDeliveredReport not implemented by EMailDeliveryMethod");
    }

    public DeliveredReport[] getDeliveredReports(ReportUser user) throws DeliveryException 
    {        
        throw new DeliveryException("Method getDeliveredReports not implemented by EMailDeliveryMethod");
    }
    
    protected ByteArrayDataSource exportReport(ReportEngineOutput reportOutput, ReportSchedule reportSchedule,
            ArrayList<ByteArrayDataSource> htmlImageDataSources)
    {       
        String reportName = StringUtils.deleteWhitespace(reportSchedule.getReport().getName());

        ByteArrayDataSource byteArrayDataSource = new ByteArrayDataSource(reportOutput.getContent(), reportOutput.getContentType());
        byteArrayDataSource.setName(reportName + reportOutput.getContentExtension());
        
        if (reportSchedule.getExportType() == ExportType.HTML.getCode()
                && reportSchedule.getReport().isJasperReport())
        {
            Map imagesMap = ((JasperReportEngineOutput) reportOutput).getImagesMap();

            for (Iterator entryIter = imagesMap.entrySet().iterator(); entryIter
                    .hasNext();)
            {
                Map.Entry entry = (Map.Entry) entryIter.next();

                ByteArrayDataSource imageDataSource = new ByteArrayDataSource(
                        (byte[]) entry.getValue(), getImageContentType((byte[]) entry
                                .getValue()));

                imageDataSource.setName((String) entry.getKey());

                htmlImageDataSources.add(imageDataSource);
            }
        }

        return byteArrayDataSource;
    }
    
    /**
     * Try to figure out the image type from its bytes.
     */
    private String getImageContentType(byte[] bytes)
    {
        String header = new String(bytes, 0, (bytes.length > 100) ? 100 : bytes.length);
        if (header.startsWith("GIF"))
        {
            return "image/gif";
        }

        if (header.startsWith("BM"))
        {
            return "image/bmp";
        }

        if (header.indexOf("JFIF") >= 0)
        {
            return "image/jpeg";
        }

        if (header.indexOf("PNG") >= 0)
        {
            return "image/png";
        }

        // We are out of guesses, so just guess tiff
        return "image/tiff";
    }
    
    public void setMailProvider(MailProvider mailProvider) 
    {
        this.mailProvider = mailProvider;
    }   
}
