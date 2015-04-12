/*
 * Copyright (C) 2003 Erik Swenson - erik@oreports.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 */
 
package org.efs.openreports.util.schema;

import java.io.FileWriter;
import java.io.IOException;

import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.*;

public class SchemaGenerator
{
	public static void main(String[] args)
	{
		try
		{
			Configuration cfg = new Configuration().configure();			
			
			String[] scripts =	cfg.generateSchemaCreationScript(new HSQLDialect());	
			writeFile("or_ddl_hsqldb.sql", scripts);
					
			scripts =	cfg.generateSchemaCreationScript(new MySQLDialect());
			writeFile("or_ddl_mysql.sql", scripts);			
			
			scripts =	cfg.generateSchemaCreationScript(new SybaseDialect());
			writeFile("or_ddl_sybase.sql", scripts);			
				
			scripts =	cfg.generateSchemaCreationScript(new DB2Dialect());
			writeFile("or_ddl_db2.sql", scripts);
			
			// hibernate bug?
			cfg = new Configuration().configure();
			
			scripts =	cfg.generateSchemaCreationScript(new PostgreSQLDialect());
			writeFile("or_ddl_postgre.sql", scripts);		
			
			scripts =	cfg.generateSchemaCreationScript(new Oracle9Dialect());
			writeFile("or_ddl_oracle.sql", scripts);		
			
			//hibernate bug?
			cfg = new Configuration().configure();
			
			scripts =	cfg.generateSchemaCreationScript(new SQLServerDialect());
			writeFile("or_ddl_sqlserver.sql", scripts);
			
			//hibernate bug?
			cfg = new Configuration().configure();
			
			scripts =	cfg.generateSchemaCreationScript(new DerbyDialect());
			writeFile("or_ddl_derby.sql", scripts);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private static void writeFile(String fileName, String[] scripts) throws IOException
	{
		FileWriter writer = new FileWriter(fileName);
		
		for (int i=0; i < scripts.length; i++)
		{
			writer.write(scripts[i] + "\n\n");			
		}
		
		writer.flush();
		writer.close();
	}
}

 