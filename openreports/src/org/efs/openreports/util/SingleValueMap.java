/*
 * WebWork, Web Application Framework
 *
 * Distributable under Apache license.
 * See terms of license at opensource.org
 */
package org.efs.openreports.util;

import java.io.Serializable;
import java.util.*;

/**
 * A Map that can be used to wrap a map whose values are object arrays.
 * This wrapper will then allow one to access only the first object of those arrays.
 *
 * A common usage is to use this wrap the Map received through the ParameterAware interface.
 *
 * @see ParameterAware
 * @author Rickard �berg (rickard@middleware-company.com)
 */
public class SingleValueMap implements Map<String,Object>, Serializable
{  
	private static final long serialVersionUID = -6002408173914149493L;
	
	private Map<String,Object> m;	        // Backing Map

   public SingleValueMap(Map<String,Object> m)
   {
      if (m == null)
         throw new NullPointerException();
      this.m = m;
   }

   public int size()
   {
      return m.size();
   }

   public boolean isEmpty()
   {
      return m.isEmpty();
   }

   public boolean containsKey(Object key)
   {
      return m.containsKey(key);
   }

   public boolean containsValue(Object value)
   {
      return m.containsValue(value);
   }

   public Object get(Object key)
   {
      Object[] value = (Object[])m.get(key);
      return value == null ? null : value[0];
   }

   public Object put(String key, Object value)
   {
      Object[] val = (Object[])m.put(key, new Object[] { value });
      return val == null ? null : val[0];
   }

   public Object remove(Object key)
   {
      Object[] val = (Object[])m.remove(key);
      return val == null ? null : val[0];
   }

   @SuppressWarnings("unchecked")
   public void putAll(Map map)
   {
      throw new UnsupportedOperationException();
   }

   public void clear()
   {
      m.clear();
   }   

   public Set<String> keySet()
   {
      return m.keySet();
   }

   @SuppressWarnings("unchecked")
   public Set entrySet()
   {      
      return m.entrySet();
   }

   public Collection<Object> values()
   {
      Collection<Object> vals = m.values();
      Collection<Object> realVals = new ArrayList<Object>(vals.size());
      for (Iterator<Object> iterator = vals.iterator(); iterator.hasNext();)
      {
         Object o = iterator.next();
         if (o != null)
         {
            realVals.add(((Object[])o)[0]);
         }
      }

      return realVals;
   }
}
