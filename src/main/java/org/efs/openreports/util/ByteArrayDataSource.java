/*
 * Copyright (C) 2003 Erik Swenson - erik@oreports.com
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *  
 */

package org.efs.openreports.util;

import java.io.*;

import javax.activation.DataSource;

public class ByteArrayDataSource implements DataSource
{
	private byte[] data;
	private String type;
	private String name;

	public ByteArrayDataSource(byte[] data, String type)
	{
		this.type = type;
		this.data = data;
	}

	public InputStream getInputStream() throws IOException
	{
		if (data == null)
			throw new IOException("No data.");

		return new ByteArrayInputStream(data);
	}

	public OutputStream getOutputStream() throws IOException
	{
		throw new IOException("Not supported.");
	}

	public String getContentType()
	{
		return type;
	}

	public void setContentType(String type)
	{
		this.type = type;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

}
