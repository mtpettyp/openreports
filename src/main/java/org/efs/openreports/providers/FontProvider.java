/*
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
 *
 */

package org.efs.openreports.providers;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;

import com.lowagie.text.FontFactory;

/**
 * Registers fonts for use in BIRT
 *
 */
public class FontProvider implements DisposableBean
{
    protected static Logger log = Logger.getLogger(FontProvider.class);

    public FontProvider(String fontDirectories)
    {
        if (!StringUtils.isBlank(fontDirectories))
        {
            try
            {
                for (String directory : fontDirectories.split(";"))
                {
                    FontFactory.registerDirectory(directory);
                }

                log.info("Font directories registered");
            }
            catch (Exception e)
            {
                log.error("Error loading font directories", e);
            }
        }
    }

    public void destroy()
    {
    }
}
