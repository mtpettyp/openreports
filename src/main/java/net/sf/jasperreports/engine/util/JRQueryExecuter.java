/**
 * mconner, 2009-07-14: What's Going on Here?
 * This class, JRQueryExecuter, was the sole inhabitant of a jar, jasperreports-query-1.1.0.jar, that was included
 * in the initial openreports-pro-3.1.0.   However, I wasn't able to find a jar or zip published by jasper or
 * sourceforge for the jar, or even one that included the source (i.e. jasperreports-1.1.0.jar). However, there were
 * several sources of this file online.  I grabbed one of those, and rather than hope that it matched, I removed
 * the jar and added it to the openreports source tree, so we have source that matches the actual bytecode.  In
 * reality, we should not be using this class at all, but should use some other manner for parsing the query, but I
 * at this point, I added this so we could diagnose another problem.
 *
 * TODO: eliminate the need for this class, use more current jasper query execution.
 */
package net.sf.jasperreports.engine.util;

/*
 * ============================================================================ GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library. Copyright (C) 2001-2005 JasperSoft Corporation
 * http://www.jaspersoft.com
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to
 * the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.
 *
 * JasperSoft Corporation 185, Berry Street, Suite 6200 San Francisco CA 94107 http://www.jaspersoft.com
 */

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRQuery;
import net.sf.jasperreports.engine.JRQueryChunk;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRQueryExecuter.java,v 1.7 2005/06/27 07:16:53 teodord Exp $
 */
public class JRQueryExecuter {

    /**
      *
      */
    private JRQuery query = null;
    private Map parametersMap = new HashMap();
    private Map parameterValues = null;
    private String queryString = "";
    private List parameterNames = new ArrayList();

    /**
      *
      */
    protected JRQueryExecuter( JRQuery query, Map parameters, Map values ) {
        this.query = query;
        this.parametersMap = parameters;
        this.parameterValues = values;

        this.parseQuery();
    }

    /**
      *
      */
    public static PreparedStatement getStatement( JRQuery query, Map parameters, Map values, Connection conn )
            throws JRException {
        PreparedStatement pstmt = null;

        if( conn != null ) {
            JRQueryExecuter queryExecuter = new JRQueryExecuter( query, parameters, values );

            pstmt = queryExecuter.getStatement( conn );
        }

        return pstmt;
    }

    /**
      *
      */
    private void parseQuery() {
        queryString = "";
        parameterNames = new ArrayList();

        if( query != null ) {
            JRQueryChunk[] chunks = query.getChunks();
            if( chunks != null && chunks.length > 0 ) {
                StringBuffer sbuffer = new StringBuffer();
                JRQueryChunk chunk = null;
                for( int i = 0; i < chunks.length; i++ ) {
                    chunk = chunks[i];
                    switch( chunk.getType() ) {
                    case JRQueryChunk.TYPE_PARAMETER_CLAUSE: {
                        String parameterName = chunk.getText();
                        Object parameterValue = parameterValues.get( parameterName );
                        sbuffer.append( String.valueOf( parameterValue ) );
                        // parameterNames.add(parameterName);
                        break;
                    }
                    case JRQueryChunk.TYPE_PARAMETER: {
                        sbuffer.append( "?" );
                        parameterNames.add( chunk.getText() );
                        break;
                    }
                    case JRQueryChunk.TYPE_TEXT:
                    default: {
                        sbuffer.append( chunk.getText() );
                        break;
                    }
                    }
                }

                queryString = sbuffer.toString();
            }
        }
    }

    /**
      *
      */
    private PreparedStatement getStatement( Connection conn ) throws JRException {
        PreparedStatement pstmt = null;

        if( queryString != null && queryString.trim().length() > 0 ) {
            try {
                pstmt = conn.prepareStatement( queryString );

                if( parameterNames != null && parameterNames.size() > 0 ) {
                    JRParameter parameter = null;
                    String parameterName = null;
                    Class<?> clazz = null;
                    Object parameterValue = null;
                    for( int i = 0; i < parameterNames.size(); i++ ) {
                        parameterName = (String) parameterNames.get( i );
                        parameter = (JRParameter) parametersMap.get( parameterName );
                        if ( parameter == null ) {
                                throw new JRException( "Parameter '" + parameterName + "' found in query not defined on report." );
                        }

                        clazz = getBaseType( parameter.getValueClass(), parameterName );
                        // FIXMEparameterValue = jrParameter.getValue();
                        parameterValue = parameterValues.get( parameterName );
                        parameterValue = getSingleValue( parameterValue, parameterName );

                        if( clazz.equals( java.lang.Object.class ) ) {
                            if( parameterValue == null ) {
                                pstmt.setNull( i + 1, Types.JAVA_OBJECT );
                            } else {
                                pstmt.setObject( i + 1, parameterValue );
                            }
                        } else if( clazz.equals( java.lang.Boolean.class ) ) {
                            if( parameterValue == null ) {
                                pstmt.setNull( i + 1, Types.BIT );
                            } else {
                                pstmt.setBoolean( i + 1, ( (Boolean) parameterValue ).booleanValue() );
                            }
                        } else if( clazz.equals( java.lang.Byte.class ) ) {
                            if( parameterValue == null ) {
                                pstmt.setNull( i + 1, Types.TINYINT );
                            } else {
                                pstmt.setByte( i + 1, ( (Byte) parameterValue ).byteValue() );
                            }
                        } else if( clazz.equals( java.lang.Double.class ) ) {
                            if( parameterValue == null ) {
                                pstmt.setNull( i + 1, Types.DOUBLE );
                            } else {
                                pstmt.setDouble( i + 1, ( (Double) parameterValue ).doubleValue() );
                            }
                        } else if( clazz.equals( java.lang.Float.class ) ) {
                            if( parameterValue == null ) {
                                pstmt.setNull( i + 1, Types.FLOAT );
                            } else {
                                pstmt.setFloat( i + 1, ( (Float) parameterValue ).floatValue() );
                            }
                        } else if( clazz.equals( java.lang.Integer.class ) ) {
                            if( parameterValue == null ) {
                                pstmt.setNull( i + 1, Types.INTEGER );
                            } else {
                                pstmt.setInt( i + 1, ( (Integer) parameterValue ).intValue() );
                            }
                        } else if( clazz.equals( java.lang.Long.class ) ) {
                            if( parameterValue == null ) {
                                pstmt.setNull( i + 1, Types.BIGINT );
                            } else {
                                pstmt.setLong( i + 1, ( (Long) parameterValue ).longValue() );
                            }
                        } else if( clazz.equals( java.lang.Short.class ) ) {
                            if( parameterValue == null ) {
                                pstmt.setNull( i + 1, Types.SMALLINT );
                            } else {
                                pstmt.setShort( i + 1, ( (Short) parameterValue ).shortValue() );
                            }
                        } else if( clazz.equals( java.math.BigDecimal.class ) ) {
                            if( parameterValue == null ) {
                                pstmt.setNull( i + 1, Types.DECIMAL );
                            } else {
                                pstmt.setBigDecimal( i + 1, (BigDecimal) parameterValue );
                            }
                        } else if( clazz.equals( java.lang.String.class ) ) {
                            if( parameterValue == null ) {
                                pstmt.setNull( i + 1, Types.VARCHAR );
                            } else {
                                pstmt.setString( i + 1, parameterValue.toString() );
                            }
                        } else if( clazz.equals( java.util.Date.class ) ) {
                            if( parameterValue == null ) {
                                pstmt.setNull( i + 1, Types.DATE );
                            } else {
                                pstmt
                                        .setDate( i + 1, new java.sql.Date( ( (java.util.Date) parameterValue )
                                                .getTime() ) );
                            }
                        } else if( clazz.equals( java.sql.Timestamp.class ) ) {
                            if( parameterValue == null ) {
                                pstmt.setNull( i + 1, Types.TIMESTAMP );
                            } else {
                                pstmt.setTimestamp( i + 1, (java.sql.Timestamp) parameterValue );
                            }
                        } else if( clazz.equals( java.sql.Time.class ) ) {
                            if( parameterValue == null ) {
                                pstmt.setNull( i + 1, Types.TIME );
                            } else {
                                pstmt.setTime( i + 1, (java.sql.Time) parameterValue );
                            }
                        } else {
                            throw new JRException( "Parameter type not supported in query : " + parameterName
                                    + " class " + clazz.getName() );
                        }
                    }
                }
            } catch( SQLException e ) {
                throw new JRException( "Error preparing statement for executing the report query : " + "\n\n"
                        + queryString + "\n\n", e );
            }
        }

        return pstmt;
    }



    public static Object getSingleValue( Object value, String description ) {

        if( value == null ) {
            return null;
        }

        if( value.getClass().isArray() ) {
            Object[] array = (Object[]) value;
            switch( array.length ) {
            case 0:
                return null;
            case 1:
                return ( array[0] == null ) ? null : array[0].toString();
            default:
                throw new IllegalStateException( "more than one (" + array.length + ") value for " + description );
            }
        } else {
            return value;
        }
    }

    public static Class<?> getBaseType( Class<?> clazz, String description ) {

        if( clazz == null ) {
            return null;
        }

        if( clazz.isArray() ) {
            return clazz.getComponentType();
        } else {
            return clazz;
        }
    }



}