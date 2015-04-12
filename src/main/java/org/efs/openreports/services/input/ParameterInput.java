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

package org.efs.openreports.services.input;

import java.io.Serializable;

public class ParameterInput implements Serializable
{
    private static final long serialVersionUID = -6620997797656979135L;
    
    private String name;
    private String[] values;
    
    /**
     * Default constructor.
     */
    public ParameterInput() {}

    /**
     * Full constructor.
     * 
     * @param name Name of the parameter
     * @param values parameter values
     */
    public ParameterInput(String name, String... values) {
        this.name = name;
        this.values = values;
    }

    public String getName() 
    {
        return name;
    }
    
    public void setName(String name) 
    {
        this.name = name;
    }
    
    public String[] getValues() 
    {
        return values;
    }
    
    public void setValues(String[] values) 
    {
        this.values = values;
    }
    
    
}
