package com.tyrcho.dataaccess;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/** 
 * Builds prepared statements to an Oracle SQL request.
 * @author MDA 
 */
public class PreparedStatementFactory
{
   protected static String moduleName = PreparedStatementFactory.class.getName();
   private static Logger logger = Logger.getLogger(moduleName);
   protected static String lineSeparator = System.getProperty("line.separator");

   /**
    * @param valuesMap the values to insert : key=column, value=value
    */
   public static PreparedStatement buildInsertStatement(
		String tableName,
        Map valuesMap,
      	Connection connection)
    throws SQLException
	{
   	    StringBuffer columns=new StringBuffer();
   	    StringBuffer values=new StringBuffer();   
   	    columns.append(" (");   
   	    values.append(" VALUES (");   
		//to preserve the order of values
		List columnNames=new ArrayList(valuesMap.size());
	   	for (Iterator i=valuesMap.keySet().iterator(); i.hasNext(); )
	   	{
	   		String columnName = (String) i.next();
	   		columnNames.add(columnName);
	   		columns.append(columnName);
	   		values.append(" ? ");
	   		if (i.hasNext())
	   		{
	   			columns.append(", ");
	   			values.append(", ");
	   		}
	   	}
   	    columns.append(") ");   
   	    values.append(") ");   
   	    StringBuffer sql=new StringBuffer();   
   	    sql.append("INSERT INTO ");
   	    sql.append(tableName);
   	    sql.append(lineSeparator);
   	    sql.append(columns.toString());
   	    sql.append(lineSeparator);
   	    sql.append(values.toString());
   	    debug("insert sql"+lineSeparator+sql);	   	
		PreparedStatement statement=connection.prepareStatement(sql.toString());		
		int position=1;
	   	for (Iterator i=columnNames.iterator(); i.hasNext(); )
	   	{
	   		String columnName = (String) i.next();
	   		Object value=valuesMap.get(columnName);
	   		setArgument(value, position, statement);
	   		position++;
	   	}
		return statement;
	}
	
	
   /**
    * @param whereMap the values to use in the where clause : key=column, value=value
    */
	public static PreparedStatement buildDeleteStatement(
		String tableName,
        Map whereMap,
      	Connection connection)
    throws SQLException
	{
   	    StringBuffer where=new StringBuffer();   
   	    where.append(" WHERE ");   
		//to preserve the order of values
		List columnNames=new ArrayList(whereMap.size());
	   	for (Iterator i=whereMap.keySet().iterator(); i.hasNext(); )
	   	{
	   		String columnName = (String) i.next();
	   		columnNames.add(columnName);
	   		where.append(columnName);
	   		where.append("= ?");
	   		if (i.hasNext())
	   		{
	   			where.append(" AND ");
	   		}
	   	}
   	    StringBuffer sql=new StringBuffer();   
   	    sql.append("DELETE FROM ");
   	    sql.append(tableName);
   	    sql.append(lineSeparator);
   	    sql.append(where.toString());
   	    debug("delete sql"+lineSeparator+sql);	   	
		PreparedStatement statement=connection.prepareStatement(sql.toString());		
		int position=1;
	   	for (Iterator i=columnNames.iterator(); i.hasNext(); )
	   	{
	   		String columnName = (String) i.next();
	   		Object value=whereMap.get(columnName);
	   		setArgument(value, position, statement);
	   		position++;
	   	}
		return statement;
	}
		
   /**
    * @param whereMap the values to use in the where clause : key=column, value=value
    */
	public static PreparedStatement buildLockStatement(
		String tableName,
        Map whereMap,
      	Connection connection)
    throws SQLException
	{
   	    StringBuffer where=new StringBuffer();   
   	    where.append(" WHERE ");   
		//to preserve the order of values
		List columnNames=new ArrayList(whereMap.size());
	   	for (Iterator i=whereMap.keySet().iterator(); i.hasNext(); )
	   	{
	   		String columnName = (String) i.next();
	   		columnNames.add(columnName);
	   		where.append(columnName);
	   		where.append("= ?");
	   		if (i.hasNext())
	   		{
	   			where.append(" AND ");
	   		}
	   	}
   	    where.append(") ");   
   	    StringBuffer sql=new StringBuffer();   
   	    sql.append("SELECT 1 FROM ");
   	    sql.append(tableName);
   	    sql.append(lineSeparator);
   	    sql.append(where.toString());
   	    sql.append(lineSeparator);
   	    sql.append(" FOR UPDATE NOWAIT ");
   	    debug("lock sql :"+lineSeparator+sql);	   	
		PreparedStatement statement=connection.prepareStatement(sql.toString());		
		int position=1;
	   	for (Iterator i=columnNames.iterator(); i.hasNext(); )
	   	{
	   		String columnName = (String) i.next();
	   		Object value=whereMap.get(columnName);
	   		setArgument(value, position, statement);
	   		position++;
	   	}
		return statement;
	}
		
   /**
    * @param whereMap the values to use in the where clause : key=column, value=value
    * @param valuesMap the values to set : key=column, value=value
    */
	public static PreparedStatement buildUpdateStatement(
		String tableName,
        Map valuesMap,
        Map whereMap,
      	Connection connection)
    throws SQLException
	{
   	    StringBuffer set=new StringBuffer();   
   	    set.append(" SET ");   
		//to preserve the order of values
		List valuesColumnNames=new ArrayList(valuesMap.size());
	   	for (Iterator i=valuesMap.keySet().iterator(); i.hasNext(); )
	   	{
	   		String columnName = (String) i.next();
	   		valuesColumnNames.add(columnName);
	   		set.append(columnName);
	   		set.append(" = ? ");
	   		if (i.hasNext())
	   		{
	   			set.append(", ");
	   		}
	   	}

   	    StringBuffer where=new StringBuffer();   
 	    where.append(" WHERE ");   
		//to preserve the order of values
		List whereColumnNames=new ArrayList(whereMap.size());
	   	for (Iterator i=whereMap.keySet().iterator(); i.hasNext(); )
	   	{
	   		String columnName = (String) i.next();
	   		whereColumnNames.add(columnName);
	   		where.append(columnName);
	   		where.append(" = ? ");
	   		if (i.hasNext())
	   		{
	   			where.append(" AND ");
	   		}
	   	}
   	    
   	    StringBuffer sql=new StringBuffer();   
   	    sql.append("UPDATE ");
   	    sql.append(tableName);
   	    sql.append(lineSeparator);
   	    sql.append(set.toString());
   	    sql.append(lineSeparator);
   	    sql.append(where.toString());
   	    sql.append(lineSeparator);
   	    debug("update sql"+lineSeparator+sql);   	    	   	
		PreparedStatement statement=connection.prepareStatement(sql.toString());	
			
		int position=1;
	   	for (Iterator i=valuesColumnNames.iterator(); i.hasNext(); )
	   	{
	   		String columnName = (String) i.next();
	   		Object value=valuesMap.get(columnName);
	   		setArgument(value, position, statement);
	   		position++;
	   	}
	   	
	   	for (Iterator i=whereColumnNames.iterator(); i.hasNext(); )
	   	{
	   		String columnName = (String) i.next();
	   		Object value=whereMap.get(columnName);
	   		setArgument(value, position, statement);
	   		position++;
	   	}
		return statement;
	}
		
   public static void setArgument(Object arg, int pos, PreparedStatement statement)
   throws SQLException
   {
      if (arg == null)
      {
         statement.setNull(pos, java.sql.Types.VARCHAR);
      }
      else if (arg instanceof String)
      {
         statement.setString(pos, (String) arg);
      }
      else if (arg instanceof Boolean)
      {
         statement.setBoolean(pos, ((Boolean) arg).booleanValue());
      }
      else if (arg instanceof Byte)
      {
         statement.setByte(pos, ((Byte) arg).byteValue());
      }
      else if (arg instanceof java.sql.Date)
      {
         statement.setDate(pos, ((java.sql.Date) arg));
      }
//      else if (arg instanceof java.util.Date)
//      {
//         statement.setTimestamp(pos, dbUtil.convertDateToTimestamp((java.util.Date)arg));
//      }
      else if (arg instanceof Float)
      {
         statement.setFloat(pos, ((Float) arg).floatValue());
      }
      else if (arg instanceof Integer)
      {
         statement.setInt(pos, ((Integer) arg).intValue());
      }
      else if (arg instanceof Long)
      {
         statement.setLong(pos, ((Long) arg).longValue());
      }
      else if (arg instanceof Time)
      {
         statement.setTime(pos, ((Time) arg));
      }
      else if (arg instanceof Timestamp)
      {
         statement.setTimestamp(pos, ((Timestamp) arg));
      }
      else if (arg instanceof BigInteger)
      {
         statement.setBigDecimal(pos, new BigDecimal((BigInteger)arg));
      }
      else
      {
         statement.setObject(pos, arg);
      }
   }
   
  
   protected static void debug(String message)
   {
      logger.info(message);
   }

   protected static void info(String message)
   {
       logger.info(message);
   }

   protected static void warn(String message)
   {
       logger.info(message);
   }

   protected static void info(String message, String messageId)
   {
       logger.info(message);
   }

   protected static void warn(String message, String messageId)
   {
       logger.info(message);
   }

   protected static void warn(String message, Throwable exception)
   {
       logger.info(message);
   }

   protected static void debug(String message, String messageId)
   {
       logger.info(message);
   }
}
