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

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

import org.apache.log4j.Logger;
import org.efs.openreports.objects.*;
import org.efs.openreports.providers.*;
import org.efs.openreports.util.*;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.util.JRQueryExecuter;

public class ParameterProviderImpl implements ParameterProvider
{
	protected static Logger log =
		Logger.getLogger(ParameterProviderImpl.class.getName());	
	
	private DataSourceProvider dataSourceProvider;
	private DateProvider dateProvider;
	private HibernateProvider hibernateProvider;
	
	public ParameterProviderImpl(DataSourceProvider dataSourceProvider, DateProvider dateProvider, HibernateProvider hibernateProvider) throws ProviderException
	{
		this.dataSourceProvider = dataSourceProvider;
		this.dateProvider = dateProvider;
		this.hibernateProvider = hibernateProvider;
		
		log.info("Created");
	}		

	public ReportParameterValue[] getParamValues(
		ReportParameter reportParameter,
		Map<String,Object> parameters)
		throws ProviderException
	{
		if (reportParameter.getType().equals(ReportParameter.QUERY_PARAM))
		{
			return getParamValuesFromDataSource(reportParameter, parameters);
		}
		else if (reportParameter.getType().equals(ReportParameter.LIST_PARAM))
		{
			return parseListValues(reportParameter);
		}
		else if (reportParameter.getType().equals(ReportParameter.BOOLEAN_PARAM))
		{
			// default to Yes/No 
			if (reportParameter.getData() == null || reportParameter.getData().indexOf("|") == -1)
			{
				reportParameter.setData("true:Yes|false:No");
			}
			
			return parseListValues(reportParameter);
		}

		throw new ProviderException(
			reportParameter.getName()
				+ ": param-type "
				+ reportParameter.getType()
				+ " not supported!");
	}
	
	protected ReportParameterValue[] parseListValues(ReportParameter reportParameter)
		throws ProviderException
	{
		StringTokenizer st =
			new StringTokenizer(reportParameter.getData(), "|");

		ReportParameterValue[] values =
			new ReportParameterValue[st.countTokens()];

		int index = 0;
		while (st.hasMoreTokens())
		{			
			String token = st.nextToken();
			String id = token;
			String description = token;
			
			StringTokenizer paramValue = new StringTokenizer(token,":");			
			if (paramValue.countTokens() == 2)
			{
				id = paramValue.nextToken();
				description = paramValue.nextToken();
			}
				
			try
			{
				if (reportParameter.getClassName().equals("java.lang.Integer"))
				{
					values[index] =
						new ReportParameterValue(Integer.valueOf(id), description);
				}
				else if (
					reportParameter.getClassName().equals("java.lang.Double"))
				{
					values[index] =
						new ReportParameterValue(Double.valueOf(id), description);
				}
				else if (
					reportParameter.getClassName().equals("java.lang.Long"))
				{
					values[index] =
						new ReportParameterValue(Long.valueOf(id), description);
				}
				else if (
					reportParameter.getClassName().equals(
						"java.math.BigDecimal"))
				{
					values[index] =
						new ReportParameterValue(new BigDecimal(id), description);
				}
				else
				{
					values[index] = new ReportParameterValue(id, description);
				}
			}
			catch (Exception e)
			{
				throw new ProviderException(
					reportParameter.getData()
						+ " contains invalid "
						+ reportParameter.getClassName());
			}

			index++;
		}

		return values;
	}

	protected ReportParameterValue[] getParamValuesFromDataSource(
		ReportParameter param,
		Map<String,Object> parameters)
		throws ProviderException
	{
		Connection conn = null;
		PreparedStatement pStmt = null;
		ResultSet rs = null;

		try
		{
			ReportDataSource dataSource = param.getDataSource();
			conn = dataSourceProvider.getConnection(dataSource.getId());			

			if (parameters == null || parameters.isEmpty())
			{
				pStmt = conn.prepareStatement(param.getData());				
			}
			else
			{
				// Use JasperReports Query logic to parse parameters in chart
				// queries

				JRDesignQuery query = new JRDesignQuery();
				query.setText(param.getData());

				// convert parameters to JRDesignParameters so they can be
				// parsed
				Map<String,JRDesignParameter> jrParameters = ORUtil.buildJRDesignParameters(parameters);

				pStmt =
					JRQueryExecuter.getStatement(
						query,
						jrParameters,
						parameters,
						conn);
			}
			
			rs = pStmt.executeQuery();

			ResultSetMetaData rsMetaData = rs.getMetaData();

			boolean multipleColumns = false;
			if (rsMetaData.getColumnCount() > 1)
				multipleColumns = true;

			ArrayList<ReportParameterValue> v = new ArrayList<ReportParameterValue>();

			while (rs.next())
			{
				ReportParameterValue value = new ReportParameterValue();

				if (param.getClassName().equals("java.lang.String"))
				{
					value.setId(rs.getString(1));
				}
				else if (param.getClassName().equals("java.lang.Double"))
				{
					value.setId(new Double(rs.getDouble(1)));
				}
				else if (param.getClassName().equals("java.lang.Integer"))
				{
					value.setId(new Integer(rs.getInt(1)));
				}
				else if (param.getClassName().equals("java.lang.Long"))
				{
					value.setId(new Long(rs.getLong(1)));
				}
				else if (param.getClassName().equals("java.math.BigDecimal"))
				{
					value.setId(rs.getBigDecimal(1));
				}
				else if (param.getClassName().equals("java.util.Date"))
				{
					value.setId(rs.getDate(1));
				}
				else if (param.getClassName().equals("java.sql.Date"))
				{
					value.setId(rs.getDate(1));
				}
				else if (param.getClassName().equals("java.sql.Timestamp"))
				{
					value.setId(rs.getTimestamp(1));
				}

				if (multipleColumns)
				{
					value.setDescription(rs.getString(2));
				}

				v.add(value);
			}

			rs.close();

			ReportParameterValue[] values = new ReportParameterValue[v.size()];
			v.toArray(values);

			return values;
		}
		catch (Exception e)
		{
			throw new ProviderException(
				"Error retreiving param values from database: "
					+ e.getMessage());
		}
		finally
		{
			try
			{
				if (pStmt != null)
					pStmt.close();
				if (conn != null)
					conn.close();
			}
			catch (Exception c)
			{
				log.error("Error closing");
			}
		}
	}

	protected void validateParameter(ReportParameter parameter, String value)
		throws ProviderException
	{
		try
		{			
			if (parameter.getClassName().equals("java.lang.Double"))
			{
				new Double(value);
			}
			else if (parameter.getClassName().equals("java.lang.Integer"))
			{
				new Integer(value);
			}
			else if (parameter.getClassName().equals("java.lang.Long"))
			{
				new Long(value);
			}
			else if (parameter.getClassName().equals("java.math.BigDecimal"))
			{
				new BigDecimal(value);
			}
			else if (
				parameter.getClassName().equals("java.util.Date")
					|| parameter.getClassName().equals("java.sql.Date")
					|| parameter.getClassName().equals("java.sql.Timestamp"))
			{
				try
				{
					dateProvider.parseDate(value);
				}
				catch (Exception e)
				{
					throw new ProviderException(e.getMessage());
				}
			}
		}
		catch (Exception e)
		{
			throw new ProviderException(
				parameter.getDescription() + " Invalid: " + e.getMessage());
		}		
	}

	protected Object parseParameter(ReportParameter parameter, String value)
		throws ProviderException
	{
		try
		{
			if (parameter.getClassName().equals("java.lang.String"))
			{
				return value;
			}
			else if (parameter.getClassName().equals("java.lang.Double"))
			{
				return new Double(value);
			}
			else if (parameter.getClassName().equals("java.lang.Integer"))
			{
				return new Integer(value);
			}
			else if (parameter.getClassName().equals("java.lang.Long"))
			{
				return new Long(value);
			}
			else if (parameter.getClassName().equals("java.math.BigDecimal"))
			{
				return new BigDecimal(value);
			}
			else if (parameter.getClassName().equals("java.util.Date"))
			{
				return dateProvider.parseDate(value);
			}
			else if (parameter.getClassName().equals("java.sql.Date"))
			{
				return dateProvider.parseDate(value);
			}
			else if (parameter.getClassName().equals("java.sql.Timestamp"))
			{
				long time = dateProvider.parseDate(value).getTime();
				return new Timestamp(time);
			}
			else if (parameter.getClassName().equals("java.lang.Boolean"))
			{
				return new Boolean(value);
			}
		}
		catch (Exception e)
		{
			throw new ProviderException(
				parameter.getName() + " Invalid: " + e.getMessage());
		}

		throw new ProviderException(
			parameter.getName() + " currently unsupported!");
	}

	public List<ReportParameter> getReportParameters(Report report, String type)
		throws ProviderException
	{
		List<ReportParameter> parameters = new ArrayList<ReportParameter>();

		List<ReportParameterMap> allParameters = report.getParameters();

		if (allParameters != null)
		{
			Iterator<ReportParameterMap> iterator = allParameters.iterator();

			while (iterator.hasNext())
			{
				ReportParameterMap rpMap = iterator.next();

				if (rpMap.getReportParameter().getType().equals(type))
				{
					parameters.add(rpMap.getReportParameter());
				}
			}
		}

		return parameters;
	}	

	public void loadReportParameterValues(
		List<ReportParameterMap> reportParameters,
		Map<String,Object> parameters)
		throws ProviderException
	{
		for (int i = 0; i < reportParameters.size(); i++)
		{
			ReportParameterMap rpMap = reportParameters.get(i);
			ReportParameter rp = rpMap.getReportParameter();

			try
			{
				if (rp.getType().equals(ReportParameter.LIST_PARAM)
						|| rp.getType().equals(ReportParameter.QUERY_PARAM)
						|| rp.getType().equals(ReportParameter.BOOLEAN_PARAM))
				{
					if (rp.getValues() == null) // only load once...
					{
						log.debug("loading parameter values: " + rp.getName());
						rp.setValues(getParamValues(rp, parameters));
					}
				}
			}
			catch (Exception e)
			{
				log.error("Error loading parameter values: " + rp.getName());
				throw new ProviderException(
					"loadReportParameterValues: " + e.getMessage());
			}
		}
	}

	public Map<String,Object> getReportParametersMap(List<ReportParameterMap> reportParameters, Map<String,Object> origParameters)	throws ProviderException
	{
		Map<String,Object> map = new HashMap<String,Object>();

		// if multiple selections, use origParameters; otherwise, use
		// parameters
		Map<String,Object> parameters = new SingleValueMap(origParameters);

		Iterator<ReportParameterMap> iterator = reportParameters.iterator();
		while (iterator.hasNext())
		{
			ReportParameterMap rpMap = iterator.next();

			ReportParameter reportParameter = rpMap.getReportParameter();

			// if we are expecting multiple selections, convert the array of
			// parameters
			// into a comma-delimited string so that it can be used in the SQL
			// where
			// clause. Otherwise, just expect a simple String
			if (reportParameter.isMultipleSelect())
			{
				Object[] values =
					(String[]) origParameters.get(reportParameter.getName());				

				// load up the StringBuffer with the multiple selections.
				String s = buildMultipleSelectString(reportParameter, values);

				// save the parameter into the Map
				if (rpMap.isRequired() || s.length() > 0)
				{
					map.put(reportParameter.getName(), s);
					
					log.debug(
							reportParameter.getName()
								+ " => "
								+ map.get(reportParameter.getName()));
				}				
			}
			else
			{
				String value =
					(String) parameters.get(reportParameter.getName());
				
				if (value != null)
				{
					try
					{
						Object object = parseParameter(reportParameter, value);
						
						if (rpMap.isRequired() || value.length() > 0)
						{
							map.put(reportParameter.getName(), object);
						}
					}
					catch (Exception e)
					{
						log.warn(
							"Error setting parameter: "
								+ reportParameter.getName()
								+ " : "
								+ e.getMessage());
					}
				}
			}
		}		
		
		// always pass drilldown chart parameter to reports if it exists
		
		String value = (String) parameters.get(ReportChart.DRILLDOWN_PARAMETER);
		
		if (value != null) map.put(ReportChart.DRILLDOWN_PARAMETER, parameters
				.get(ReportChart.DRILLDOWN_PARAMETER));
		
		//
		
		return map;
	}

	private String buildMultipleSelectString(
		ReportParameter reportParameter,
		Object[] values)
	{
		StringBuffer sb = new StringBuffer();
		
		if (values == null || values.length < 1 || values[0].equals("")) return "";

		for (int j = 0; j < values.length; j++)
		{
			String value = "";

			if (values[j] instanceof ReportParameterValue)
			{
				value = ((ReportParameterValue) values[j]).getId().toString();
			}
			else
			{
				value = (String) values[j];
			}

			if (j > 0)
			{
				sb.append(",");
			}
			
			if (reportParameter.getClassName().equals("java.lang.String"))
			{
				sb.append("'" + value + "'");
			}
			else
			{
				sb.append(value);
			}
		}

		return sb.toString();
	}

	public boolean validateParameters(List<ReportParameterMap> reportParameters, Map<String,Object> parameters)
		throws ProviderException
	{
		parameters = new SingleValueMap(parameters);

		if (reportParameters != null && reportParameters.size() > 0)
		{
			Iterator<ReportParameterMap> iterator = reportParameters.iterator();
			while (iterator.hasNext())
			{
				ReportParameterMap rpMap = iterator.next();

				ReportParameter param = rpMap.getReportParameter();

				if (!parameters.containsKey(param.getName())
					&& rpMap.isRequired())
				{
					throw new ProviderException(LocalStrings.ERROR_PARAMETER_REQUIRED);
				}

				if (param.getType().equals(ReportParameter.TEXT_PARAM)
					|| param.getType().equals(ReportParameter.DATE_PARAM))
				{
					String value = (String) parameters.get(param.getName());

					if (value != null && value.length() > 0)
					{
						try
						{
							validateParameter(param, value);
						}
						catch (Exception e)
						{
							throw new ProviderException(e.getMessage());
						}
					}
					else if (rpMap.isRequired())
					{
						throw new ProviderException(LocalStrings.ERROR_PARAMETER_REQUIRED);
					}
				}
			}
		}

		return true;
	}

	public List<ReportParameter> getAvailableParameters(Report report) throws ProviderException
	{
		List<ReportParameter> parameters = getReportParameters();

		Iterator<ReportParameter> iterator = parameters.iterator();
		while (iterator.hasNext())
		{
			ReportParameter rp = iterator.next();

			Iterator<ReportParameterMap> reportIterator = report.getParameters().iterator();
			while (reportIterator.hasNext())
			{
				ReportParameterMap rpMap = reportIterator.next();

				if (rp.getId().equals(rpMap.getReportParameter().getId()))
				{
					parameters.remove(rp);
					iterator = parameters.iterator();
				}
			}
		}

		return parameters;
	}	

	public ReportParameter getReportParameter(Integer id)
		throws ProviderException
	{
		return (ReportParameter) hibernateProvider.load(ReportParameter.class, id);
	}
	
	public ReportParameter getReportParameter(String name) throws ProviderException
	{
		Session session = null;
		
		try
		{
			session = hibernateProvider.openSession();
			
			Criteria criteria = session.createCriteria(ReportParameter.class);
			criteria.add(Restrictions.eq("name", name));
			
			return (ReportParameter) criteria.uniqueResult();
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
	public List<ReportParameter> getReportParameters() throws ProviderException
	{
		String fromClause =
			"from org.efs.openreports.objects.ReportParameter reportParameter order by reportParameter.name ";
		
		return (List<ReportParameter>) hibernateProvider.query(fromClause);
	}
	
	public ReportParameter insertReportParameter(ReportParameter reportParameter)
		throws ProviderException
	{
		return (ReportParameter) hibernateProvider.save(reportParameter);
	}
	
	public void updateReportParameter(ReportParameter reportParameter)
		throws ProviderException
	{
		hibernateProvider.update(reportParameter);
	}
	
	public void deleteReportParameter(ReportParameter reportParameter)
		throws ProviderException
	{
		try
		{
			hibernateProvider.delete(reportParameter);
		}
		catch (ConstraintException ce)
		{
			throw new ProviderException(LocalStrings.ERROR_PARAMETER_DELETION);
		}
	}

	public void setDataSourceProvider(DataSourceProvider dataSourceProvider)
	{
		this.dataSourceProvider = dataSourceProvider;
	}

	public void setDateProvider(DateProvider dateProvider)
	{
		this.dateProvider = dateProvider;
	}

}