package info.daviot.dataaccess;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/** 
 * Lowest level callable statement to an Oracle Stored Procedure.
 * 
 * @author MDA 
 * @version NP (2nd iteration)
 *
 * change history
 * @author VA	change setArgument for java.util.Date
 */
public class CallableStoredProcedure
{
   private CallableStatement callableStatement;
   private int[] outTypes;
   private Object[] inArgs;
   private boolean isFunction;
   private Connection connection;
   protected String moduleName = this.getClass().getName();
   private Logger logger = Logger.getLogger(moduleName);
   private String name;

   /**
    * Constructs the callable PL/SQL procedure.
    * Note that all input arguments must be before 
    * all output arguments in the PL/SQL.
    * The prototype is like <pre>result=procname(in1, in2, ..., out1, out2, ...)</pre>.
    * 
    * @param procedureName the name of the procedure fully qualified with package name
    * @param inArgs input arguments as objects
    * @param outTypes Types of the out parameters
    * @param isFunction true if this must be called as a function,
    *                   false if this must be called as a procedure
    * @param connection Connection used to create the callableStatement
    * @throws SQLException if the creation or initialization of the statement fail
    */
   public CallableStoredProcedure(
         String procedureName,
         Object[] inArgs,
         int[] outTypes,
         boolean isFunction,
         Connection connection)
   throws SQLException
   {   	  
   	  this.outTypes=outTypes;
   	  this.isFunction=isFunction;
   	  this.inArgs=inArgs;
   	  this.connection=connection;
   	  setName(procedureName);
   	  if (isFunction)
   	  {
   	  	initAsFunction();
   	  } 
   	  else 
   	  {
   	  	initAsProcedure();
   	  }
   }
   
   // isFunction=true : out0=proc(in0, in1, ..., out1, out2 ...)
   private void initAsFunction() throws SQLException
   {
      StringBuffer questionMarks = new StringBuffer();
      int parametersCount=inArgs.length + outTypes.length - 1;
      for (int i = 0; i < parametersCount; i++)
      {
         if (i > 0)
            questionMarks.append(",");
         questionMarks.append("?");
      }

      callableStatement =
         connection.prepareCall(
               "{ ? = call " 
               + name
               + " ("
               + questionMarks.toString()
               + ") }");

	  callableStatement.registerOutParameter(1, outTypes[0]);
      for (int i = 1; i < outTypes.length; i++)
      {
         callableStatement.registerOutParameter(1+inArgs.length+i, outTypes[i]);
      }

      for (int i = 0; i < inArgs.length; i++)  
      {
         setArgument(inArgs[i], i+2);
      }   
   }
      
   // isFunction=false : call proc(in0, in1, ..., out0, out1 ...)
   private void initAsProcedure() throws SQLException
   {
      StringBuffer questionMarks = new StringBuffer();
      int parametersCount=inArgs.length + outTypes.length;
      for (int i = 0; i < parametersCount; i++)
      {
         if (i > 0)
            questionMarks.append(",");
         questionMarks.append("?");
      }

      callableStatement =
         connection.prepareCall(
               "{ call "
               + name
               + " ("
               + questionMarks.toString()
               + ") }");

      for (int i = 0; i < outTypes.length; i++) 
      {
         callableStatement.registerOutParameter(inArgs.length+i+1, outTypes[i]);
      }

      for (int i = 0; i < inArgs.length; i++)  
      {
         setArgument(inArgs[i], i+1);
      } 
   }
   
   public String toString()
   {
  	  StringBuffer description = new StringBuffer();
      if (isFunction)
   	  {
   	  	 description.append("? = ");
   	  	 description.append(name);
   	  	 description.append("(");
   	  	 for (int i=0; i<inArgs.length; i++)
   	  	 {
   	  	 	if (i>0)
   	  	 	{
   	  	 		description.append(", ");
   	  	 	}
   	  	 	description.append(inArgs[i]);
   	  	 }
   	  	 for (int i=0; i<outTypes.length-1; i++)
   	  	 {
   	  	 	description.append(", ?");
   	  	 }
   	  	 description.append(")");
   	  }
   	  else
   	  {
   	  	 description.append("call ");
   	  	 description.append(name);
   	  	 description.append("(");
   	  	 for (int i=0; i<inArgs.length; i++)
   	  	 {
   	  	 	if (i>0)
   	  	 	{
   	  	 		description.append(", ");
   	  	 	}
   	  	 	description.append(inArgs[i]);
   	  	 }
   	  	 for (int i=0; i<outTypes.length; i++)
   	  	 {
   	  	 	description.append(", ?");
   	  	 }
   	  	 description.append(")");
   	  }
   	  return description.toString();
   }

   /** 
    * Calls the stored procedure and returns the result/out paramaters.
    * The vector returned will contain result, out1, out2, ...
    * 
    * @return Vector with result/out parameters
    * @throws SQLException if the statement fails
    */
   public List getResult() throws SQLException
   {
      callableStatement.execute();
      List returnValues = new LinkedList();
      if (isFunction) 
      {
      	  returnValues.add(getReturnValue(outTypes[0], 1));
	      for (int i = 1; i < outTypes.length; i++)
	         returnValues.add(getReturnValue(outTypes[i], 1+inArgs.length+i));
      }
      else
      {
	      for (int i = 0; i < outTypes.length; i++) 
	      {
	         returnValues.add(getReturnValue(outTypes[i], inArgs.length+i+1));
	      }
      }
      return returnValues;
   }
   
   /** 
    * Closes the inner statement.
    */
   public void close()
   {
   	  try 
   	  {
 	      callableStatement.close();
   	  }
   	  catch (SQLException e)
   	  {
   	  	  warn("Could not close statement", e);
   	  }
   }

   private Object getReturnValue(int oracleType, int pos) throws SQLException
   {
      switch (oracleType)
      {
         case Types.VARCHAR :
            {
               return callableStatement.getString(pos);
            }
         case Types.BIT :
            {
               return new Boolean(callableStatement.getBoolean(pos));
            }
         case Types.DATE :
            {
               return callableStatement.getDate(pos);
            }
         case Types.FLOAT :
            {
               return new Float(callableStatement.getFloat(pos));
            }
         case Types.INTEGER :
            {
               return new Long(callableStatement.getLong(pos));
            }
         case Types.TIME :
            {
               return callableStatement.getTime(pos);
            }
         case Types.TIMESTAMP :
            {
               return callableStatement.getTimestamp(pos);
            }
//         case Types.CURSOR :
//            {
//               return callableStatement.getObject(pos);
//            }
        case Types.BIGINT :
            {
               return callableStatement.getBigDecimal(pos);
            }
         case Types.ARRAY :
            {
               return callableStatement.getArray(pos);
            }
         default :
            {
               return callableStatement.getObject(pos);
            }
      }
   }

   private void setArgument(Object arg, int pos) throws SQLException
   {
   	  PreparedStatementFactory.setArgument(arg, pos, callableStatement);
   	  /*
      if (arg == null)
      {
         callableStatement.setNull(pos, java.sql.Types.VARCHAR);
      }
      else if (arg instanceof String)
      {
         callableStatement.setString(pos, (String) arg);
      }
      else if (arg instanceof Boolean)
      {
         callableStatement.setBoolean(pos, ((Boolean) arg).booleanValue());
      }
      else if (arg instanceof Byte)
      {
         callableStatement.setByte(pos, ((Byte) arg).byteValue());
      }
      else if (arg instanceof java.sql.Date)
      {
         callableStatement.setDate(pos, ((java.sql.Date) arg));
      }
      else if (arg instanceof java.util.Date)
      {
         callableStatement.setTimestamp(pos, dbUtil.convertDateToTimestamp((java.util.Date)arg));
      }
      else if (arg instanceof Float)
      {
         callableStatement.setFloat(pos, ((Float) arg).floatValue());
      }
      else if (arg instanceof Integer)
      {
         callableStatement.setInt(pos, ((Integer) arg).intValue());
      }
      else if (arg instanceof Long)
      {
         callableStatement.setLong(pos, ((Long) arg).longValue());
      }
      else if (arg instanceof Time)
      {
         callableStatement.setTime(pos, ((Time) arg));
      }
      else if (arg instanceof Timestamp)
      {
         callableStatement.setTimestamp(pos, ((Timestamp) arg));
      }
      else if (arg instanceof BigInteger)
      {
         callableStatement.setBigDecimal(pos, new BigDecimal((BigInteger)arg));
      }
      else
      {
         callableStatement.setObject(pos, arg);
      }
      */
   }
   
   	protected void warn(String message, Throwable exception)
	{
		logger.info(message);
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