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
package org.efs.openreports;

public class ReportConstants 
{
	/**
	 * Report delivery methods. Name is used to map a delivery method to a Spring bean identifier using
	 * the following format: name + "DeliveryMethod"
	 */
	public enum DeliveryMethod {EMAIL("email"), FILE("fileSystem"), FAX("fax"), FTP("ftp"), PRINTER("printer"), DOCUMENT_REPOSITORY("documentRepository");
	
		private final String name;

		DeliveryMethod(String name) 
		{
			this.name = name;
		}

		public String getName() 
		{
			return name;
		}
        
        public static DeliveryMethod findByName(String name) {
            for (DeliveryMethod value : DeliveryMethod.values()) {
                if (value.getName().equalsIgnoreCase(name)) {
                    return value;
                }
            }
            return null;
        }
	}
	
	/**
	 * Report export types. int code is used to support legacy export types persisted or serialized
	 * as integers in ReportSchedule and ReportLog objects
	 */
	public enum ExportType {PDF(0), XLS(1), HTML(2), CSV(3), IMAGE(4), RTF(5), TEXT(6), EXCEL(7), HTML_EMBEDDED(8);

		private final int code;

		ExportType(int code) 
		{
			this.code = code;
		}

		public int getCode() 
		{
			return code;
		}

		public static ExportType findByCode(int code) 
		{
			for (ExportType exportType : ExportType.values()) 
			{
				if (exportType.getCode() == code) return exportType;
			}
			
			return null;
		}
	}

	/**
	 * Report schedule types. int code is used to support legacy schedule types serialized as
	 * integers in ReportSchedule objects.
	 */
	public enum ScheduleType {ONCE(0), DAILY(1), WEEKLY(2), MONTHLY(3), WEEKDAYS(4), HOURLY(5), CRON(6);

		private final int code;

		ScheduleType(int code) 
		{
			this.code = code;
		}

		public int getCode() 
		{
			return code;
		}

		public static ScheduleType findByCode(int code) 
		{
			for (ScheduleType scheduleType : ScheduleType.values()) 
			{
				if (scheduleType.getCode() == code)	return scheduleType;
			}
			
			return null;
		}
	}
	
	/**
	 * AM/PM indicator for scheduled request. The code corresponds to the AM and PM constants 
	 * in the java.util.Calendar class. 
	 */
	public enum ScheduleAmPm {AM(0), PM(1);
		
		private final int code;

		ScheduleAmPm(int code) 
		{
			this.code = code;
		}

		public int getCode() 
		{
			return code;
		}
		
		public static ScheduleAmPm findByCode(int code) 
		{
			for (ScheduleAmPm scheduleAmPm : ScheduleAmPm.values()) 
			{
				if (scheduleAmPm.getCode() == code) return scheduleAmPm;
			}
			
			return null;
		}		
	}
	
	public enum Status {SUCCESS, FAILURE, INVALID};	
}
