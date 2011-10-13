package info.daviot.dataaccess;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


public class DatabaseUtilities
{
    protected static String moduleName = DatabaseUtilities.class.getName();
    private static Logger logger = Logger.getLogger(moduleName);

    //   /**
    //    * Executes a stored procedure with simple return types
    //    * (no Cursor/Resultset).
    //    * 
    //    * @param procedureName the name of the procedure fully qualified with package name
    //    * @param inArgs input arguments as objects
    //    * @param outTypes OracleTypes of the out parameters 
    //    * (by default error code and error message are added to these parameters)
    //    * @param isFunction true if it must be called as a function,
    //    *                   false if it must be called as a procedure
    //    * @param transactionId the id of the transaction to use
    //    * @return the result and out parameters
    //    * @throws DatabaseFailureException if execution of the statement fails
    //    * @throws NoDataFoundException if the statement returns an error 20001
    //    */
    //   public Vector getResultVector(
    //      String procedureName,
    //      Object[] inArgs,
    //      int[] outTypes,
    //      boolean isFunction,
    //      Connection connection)
    //      throws DatabaseFailureException, NoDataFoundException
    //   {
    //      
    //      int outParametersCount = outTypes.length;
    //      int[] fullOutTypes = new int[outParametersCount + 2];
    //      int errorCodePosition = outParametersCount;
    //      int errorMessagePosition = outParametersCount + 1;
    //      for (int i = 0; i < outParametersCount; i++)
    //         fullOutTypes[i] = outTypes[i];
    //      fullOutTypes[errorCodePosition] = Types.DECIMAL;
    //      fullOutTypes[errorMessagePosition] = Types.VARCHAR;
    //      CallableStoredProcedure callableStoredProcedure;
    //      try
    //         {
    //         callableStoredProcedure =
    //            new CallableStoredProcedure(
    //               procedureName,
    //               inArgs,
    //               fullOutTypes,
    //               isFunction,
    //               connection);
    //         debug(
    //            "CallableStoredProcedure " + callableStoredProcedure + " succesfully created.");
    //      }
    //      catch (SQLException e)
    //         {
    //         throw new DatabaseFailureException(
    //            "Could not create the CallableStoredProcedure for " + procedureName,
    //            moduleName,
    //            "",
    //            null,
    //            e);
    //      }
    //      Vector result = getCallResult(callableStoredProcedure);
    //      callableStoredProcedure.close();
    //      return result;
    //   }
    //
    //   /**
    //    * Executes a stored procedure with no return type except standard error code and message.
    //    * 
    //    * @param procedureName the name of the procedure fully qualified with package name
    //    * @param inArgs input arguments as objects
    //    * @param transactionId the id of the transaction to use
    //    * @throws DatabaseFailureException if execution of the statement fails
    //    */
    //   public void executeProcedure(
    //      String procedureName,
    //      Object[] inArgs,
    //      Connection connection)
    //      throws DatabaseFailureException
    //   {
    //      
    //      int[] fullOutTypes = new int[2];
    //      fullOutTypes[0] = Types.DECIMAL;
    //      fullOutTypes[1] = Types.VARCHAR;
    //      CallableStoredProcedure callableStoredProcedure;
    //      try
    //         {
    //         callableStoredProcedure =
    //            new CallableStoredProcedure(
    //               procedureName,
    //               inArgs,
    //               fullOutTypes,
    //               false,
    //               connection);
    //         debug(
    //            "CallableStoredProcedure " + callableStoredProcedure + " succesfully created.");
    //         Vector result = getCallResult(callableStoredProcedure);
    //         callableStoredProcedure.close();
    //      }
    //      catch (SQLException e)
    //         {
    //         throw new DatabaseFailureException(
    //            "Could not create the CallableStoredProcedure for " + procedureName,
    //            moduleName,
    //            "",
    //            null,
    //            e);
    //      }
    //      catch (NoDataFoundException e)
    //         {
    //         throw new DatabaseFailureException(
    //            "Unexpected NoDataFoundException for " + procedureName,
    //            moduleName,
    //            "",
    //            null,
    //            e);
    //      }
    //   }
    //
    //   /**
    //    * Executes a stored procedure which returns a Cursor/Resultset.
    //    * 
    //    * @param procedureName the name of the procedure fully qualified with package name
    //    * @param inArgs input arguments as objects
    //    * @param outTypes OracleTypes of the Resultset 
    //    * @param isFunction true if it must be called as a function,
    //    *                   false if it must be called as a procedure
    //    * @param transactionId the id of the transaction to use
    //    * @return the resultset as a Vector of Vectors
    //    * @throws DatabaseFailureException if execution of the statement fails
    //    * @throws NoDataFoundException if the statement returns an error 20001
    //    */
    //   public Vector getResultsetVector(
    //      String procedureName,
    //      Object[] inArgs,
    //      int[] outTypes,
    //      boolean isFunction,
    //      Connection connection)
    //      throws DatabaseFailureException, NoDataFoundException
    //   {
    //      
    //      int[] fullOutTypes = new int[3];
    //      fullOutTypes[0] = OracleTypes.CURSOR;
    //      fullOutTypes[1] = Types.DECIMAL;
    //      fullOutTypes[2] = Types.VARCHAR;
    //      CallableStoredProcedure callableStoredProcedure;
    //      try
    //         {
    //         callableStoredProcedure =
    //            new CallableStoredProcedure(
    //               procedureName,
    //               inArgs,
    //               fullOutTypes,
    //               isFunction,
    //               connection);
    //         debug(
    //            "CallableStoredProcedure " + callableStoredProcedure + " succesfully created.");
    //      }
    //      catch (SQLException e)
    //         {
    //         throw new DatabaseFailureException(
    //            "Could not create the CallableStoredProcedure for " + procedureName,
    //            moduleName,
    //            "",
    //            null,
    //            e);
    //      }
    //      Vector result = getCallResult(callableStoredProcedure);
    //      ResultSet resultSet = (ResultSet) result.get(0);
    //      Vector fullResultSet = new Vector();
    //      try
    //         {
    //         while (resultSet.next())
    //            {
    //            Vector resultItem = new Vector();
    //            for (int i = 0; i < outTypes.length; i++)
    //               resultItem.add(getResultsetValue(resultSet, outTypes, i));
    //            fullResultSet.add(resultItem);
    //         }
    //         resultSet.close();
    //      }
    //      catch (SQLException e)
    //         {
    //         throw new DatabaseFailureException(
    //            "Unexpected SQLException when processing resultset from " + procedureName,
    //            moduleName,
    //            "",
    //            null,
    //            e);
    //      }
    //      callableStoredProcedure.close();
    //      if(fullResultSet.size()==0)
    //      {
    //      	 throw new NoDataFoundException("No data returned for "+callableStoredProcedure,
    //      	 	moduleName, 
    //      	 	"", 
    //      	 	null);
    //      }
    //      return fullResultSet;
    //   }
    //
    //   /**
    //    * Executes a stored procedure which returns a Cursor/Resultset.
    //    * 
    //    * @param procedureName the name of the procedure fully qualified with package name
    //    * @param inArgs input arguments as objects
    //    * @param isFunction true if it must be called as a function,
    //    *                   false if it must be called as a procedure
    //    * @param transactionId the id of the transaction to use
    //    * @return the resultset as a Collection of Maps indexed with the field names
    //    * @throws DatabaseFailureException if execution of the statement fails
    //    * @throws NoDataFoundException if the statement returns an error 20001
    //    */
    //   public Collection getResultsetMaps(
    //      String procedureName,
    //      Object[] inArgs,
    //      boolean isFunction,
    //      Connection connection)
    //      throws DatabaseFailureException, NoDataFoundException
    //   {
    //      
    //      int[] fullOutTypes = new int[3];
    //      fullOutTypes[0] = OracleTypes.CURSOR;
    //      fullOutTypes[1] = Types.DECIMAL;
    //      fullOutTypes[2] = Types.VARCHAR;
    //      CallableStoredProcedure callableStoredProcedure;
    //      try
    //         {
    //         callableStoredProcedure =
    //            new CallableStoredProcedure(
    //               procedureName,
    //               inArgs,
    //               fullOutTypes,
    //               isFunction,
    //               connection);
    //         debug(
    //            "CallableStoredProcedure " + callableStoredProcedure + " succesfully created.");
    //      }
    //      catch (SQLException e)
    //         {
    //         throw new DatabaseFailureException(
    //            "Could not create the CallableStoredProcedure for " + procedureName,
    //            moduleName,
    //            "",
    //            null,
    //            e);
    //      }
    //      Vector result = getCallResult(callableStoredProcedure);
    //      ResultSet resultSet = (ResultSet) result.get(0);
    //      Collection fullResultSet=convertResultSetToMaps(resultSet, procedureName);
    //      debug("CallableStoredProcedure " 
    //      		+ callableStoredProcedure 
    //      		+ " returned :"
    //      		+ lineSeparator
    //      		+ fullResultSet);
    //      callableStoredProcedure.close();
    //      if (fullResultSet.size()==0)
    //      {
    //      	 throw new NoDataFoundException("No data returned for "+callableStoredProcedure,
    //      	 	moduleName, 
    //      	 	"", 
    //      	 	null);
    //      }
    //      return fullResultSet;
    //   }
    //
    //   /**
    //    * Executes a stored procedure which returns a Table of "cursors".
    //    * 
    //    * @param procedureName the name of the procedure fully qualified with package name
    //    * @param inArgs input arguments as objects
    //    * @param isFunction true if it must be called as a function,
    //    *                   false if it must be called as a procedure
    //    * @param transactionId the id of the transaction to use
    //    * @return the resultset as a Collection of Maps indexed with the field names
    //    * @throws DatabaseFailureException if execution of the statement fails
    //    * @throws NoDataFoundException if the statement returns an error 20001
    //    * @deprecated not correctyl implemented yet
    //    */
    //   public Collection getTableMaps(
    //      String procedureName,
    //      Object[] inArgs,
    //      boolean isFunction,
    //      Connection connection)
    //      throws DatabaseFailureException, NoDataFoundException
    //   {
    //      
    //      int[] fullOutTypes = new int[3];
    //      fullOutTypes[0] = OracleTypes.ARRAY;
    //      fullOutTypes[1] = Types.DECIMAL;
    //      fullOutTypes[2] = Types.VARCHAR;
    //      CallableStoredProcedure callableStoredProcedure;
    //      try
    //         {
    //         callableStoredProcedure =
    //            new CallableStoredProcedure(
    //               procedureName,
    //               inArgs,
    //               fullOutTypes,
    //               isFunction,
    //               connection);
    //         debug(
    //            "CallableStoredProcedure " + callableStoredProcedure + " succesfully created.");
    //      }
    //      catch (SQLException e)
    //         {
    //         throw new DatabaseFailureException(
    //            "Could not create the CallableStoredProcedure for " + procedureName,
    //            moduleName,
    //            "",
    //            null,
    //            e);
    //      }
    //      Vector result = getCallResult(callableStoredProcedure);
    //      Array resultSet = (Array) result.get(0);
    //      Collection fullResultSet=convertArrayToMaps(resultSet, procedureName);
    //      debug("CallableStoredProcedure " 
    //      		+ callableStoredProcedure 
    //      		+ " returned :"
    //      		+ lineSeparator
    //      		+ fullResultSet);
    //
    //      callableStoredProcedure.close();
    //      return fullResultSet;
    //   }
    private static Object getResultsetValue(ResultSet resultSet, int[] outTypes,
        int position) throws DatabaseFailureException
    {
        int type = outTypes[position];
        int oraclePosition = position + 1;

        try
        {
            switch (type)
            {
            case Types.VARCHAR:
                return resultSet.getString(oraclePosition);

            case Types.BIT:
                return new Boolean(resultSet.getBoolean(oraclePosition));

            case Types.DATE:
                return resultSet.getDate(oraclePosition);

            case Types.FLOAT:
                return new Float(resultSet.getFloat(oraclePosition));

            case Types.INTEGER:
                return new Long(resultSet.getLong(oraclePosition));

            case Types.TIME:
                return resultSet.getTime(oraclePosition);

            case Types.TIMESTAMP:
                return resultSet.getTimestamp(oraclePosition);

            //            case Types.CURSOR :
            //               {
            //                  return resultSet.getObject(oraclePosition);
            //               }
            default:
                return resultSet.getObject(oraclePosition);
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseFailureException("Could not get value from resultset",
                moduleName, "", null, e);
        }
    }

    //   /**
    //    * Executes a stored procedure 
    //    * and returns the result and out parameters.
    //    * 
    //    * @param callableStoredProcedure the initialized CallableStoredProcedure
    //    * @return the result and out parameters
    //    * (you have to close the CallableStoredProcedure after reading this)
    //    * @throws DatabaseFailureException if execution of the statement fails
    //    * @throws NoDataFoundException if the statement returns an error 20001
    //    */
    //   protected Vector getCallResult(CallableStoredProcedure callableStoredProcedure)
    //      throws DatabaseFailureException, NoDataFoundException
    //   {
    //      try
    //         {
    //         Vector result = callableStoredProcedure.getResult();
    //         BigDecimal errorCodeLong = (BigDecimal) result.get(result.size() - 2);
    //         long errorCode;
    //         if (errorCodeLong == null)
    //            {
    //            errorCode = 0;
    //         }
    //         else
    //            {
    //            errorCode = errorCodeLong.longValue();
    //         }
    //         String errorMessage = (String) result.get(result.size() - 1);
    //         if (errorMessage == null)
    //            {
    //            errorMessage = "";
    //         }
    //         if (errorCode == NO_DATA_FOUND_CODE)
    //            {
    //            callableStoredProcedure.close();
    //            throw new NoDataFoundException(
    //               "No data found for "
    //                  + callableStoredProcedure.getName()
    //                  + " : "
    //                  + lineSeparator
    //                  + errorMessage,
    //               moduleName,
    //               "",
    //               null);
    //         }
    //         else if (errorCode < 0)
    //            {
    //            callableStoredProcedure.close();
    //            throw new DatabaseFailureException(
    //               "Error code "
    //                  + errorCode
    //                  + " in "
    //                  + callableStoredProcedure.getName()
    //                  + " : "
    //                  + lineSeparator
    //                  + errorMessage,
    //               moduleName,
    //               null,
    //               null,
    //               new SQLException(errorMessage, null, (int)errorCode));
    //         }
    //         else
    //            {
    //            return result;
    //         }
    //      }
    //      catch (SQLException se)
    //         {
    //         String error =
    //            "Unexpected SQLException when calling " + callableStoredProcedure.getName();
    //         warn(error, se);
    //         callableStoredProcedure.close();
    //         throw new DatabaseFailureException(
    //            error,
    //            moduleName,
    //            "Check the CallableStatement",
    //            null,
    //            se);
    //      }
    //   }

    /**
     * Fetches the record from the table by doing a SELECT * FROM tableName WHERE
     * keyColumnName=sequenceId.
     *
     * @param tableName name of the table
     * @param keyColumnName name of the primary key column
     * @param sequenceId primary key of the record
     * @param transactionId identifier of the transaction
     * @return the record in the form of a Map with key=columnName, value=java Object
     * @throws DatabaseFailureException if the request cannot be performed
     * @throws NoDataFoundException if the key does not exist
     */
    public static Map fetchRecord(String tableName, String keyColumnName,
        int sequenceId, Connection connection)
        throws NoDataFoundException, DatabaseFailureException
    {
        String sqlSelect = "SELECT * FROM " + tableName + " WHERE " +
            keyColumnName + " = ";

        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlSelect +
                    "?");
            preparedStatement.setInt(1, sequenceId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.first())
            {
                Map result = convertResultSetToMap(resultSet, sqlSelect);
                resultSet.close();
                preparedStatement.close();

                return result;
            }
            else
            {
                throw new NoDataFoundException("No data for " + sqlSelect +
                    sequenceId, moduleName, "", null);
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseFailureException("SQLException when performing " +
                sqlSelect + sequenceId, moduleName, "", null, e);
        }
    }

    private static Map convertResultSetToMap(ResultSet resultSet, String context)
        throws SQLException, DatabaseFailureException
    {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int numberOfColumns = metaData.getColumnCount();
        HashMap result = new HashMap();

        for (int i = 1; i <= numberOfColumns; i++)
        {
            String columnName = metaData.getColumnName(i);
            Object value = resultSet.getObject(i);

            //Recursively process nested resultsets
            if (value instanceof ResultSet)
            {
                value = convertResultSetToMaps((ResultSet) value, context);
            }

            result.put(columnName, value);
        }

        return result;
    }

    private static Collection convertArrayToMaps(Array array, String context)
        throws DatabaseFailureException
    {
        Collection maps = new ArrayList();

        try
        {
            ResultSet arrayResultSet = array.getResultSet();

            while (arrayResultSet.next())
            {
                ResultSet data = (ResultSet) arrayResultSet.getObject(2);
                maps.add(convertResultSetToMap(data, context));
            }

            return maps;
        }
        catch (SQLException e)
        {
            throw new DatabaseFailureException(
                "Unexpected SQLException when processing resultset from " +
                context, moduleName, "", null, e);
        }
    }

    private static Collection convertResultSetToMaps(ResultSet resultSet,
        String context) throws DatabaseFailureException
    {
        Collection maps = new ArrayList();

        try
        {
            while (resultSet.next())
            {
                maps.add(convertResultSetToMap(resultSet, context));
            }

            resultSet.close();

            return maps;
        }
        catch (SQLException e)
        {
            throw new DatabaseFailureException(
                "Unexpected SQLException when processing resultset from " +
                context, moduleName, "", null, e);
        }
    }

    //used to convert Long from SQL to Integer
    protected static Integer integerValue(Object columnValue)
    {
        Long longValue = (Long) columnValue;

        return new Integer(longValue.intValue());
    }

    /**
     * deletes the rows from the database.
     *
     * @param tableName SQL table
     * @param whereMap SQL map containing columns and values for WHERE clause
     * @param transactionId reference to the transaction
     * @throws DatabaseFailureException when the request cannot be performed
     */
    public static void deleteRows(String tableName, Map whereMap, Connection connection)
        throws DatabaseFailureException
    {
        PreparedStatement statement = null;

        try
        {
            statement = PreparedStatementFactory.buildDeleteStatement(tableName,
                    whereMap, connection);
            statement.executeUpdate();
            statement.close();
        }
        catch (SQLException e)
        {
            throw new DatabaseFailureException("SQLException in deleteRows(" +
                tableName + ", " + whereMap + ")", moduleName, "", null, e);
        }
    }

    /**
     * updates the values in the database.
     *
     * @param tableName SQL table
     * @param setMap map containing columns and values for SET clause
     * @param whereMAP SQL map containing columns and values for WHERE clause
     * @param transactionId reference to the transaction
     * @throws DatabaseFailureException when the request cannot be performed
     */
    public static void updateValues(String tableName, Map setMap, Map whereMap,
        Connection connection) throws DatabaseFailureException
    {
        PreparedStatement statement = null;

        try
        {
            statement = PreparedStatementFactory.buildUpdateStatement(tableName,
                    setMap, whereMap, connection);
            statement.executeUpdate();
            statement.close();
        }
        catch (SQLException e)
        {
            throw new DatabaseFailureException("SQLException in updateValues(" +
                tableName + ", " + setMap + ", " + whereMap + ")", moduleName,
                "", null, e);
        }
    }

    /**
     * insert the values in the database.
     *
     * @param tableName SQL table
     * @param values columns and values to insert
     * @param transactionId reference to the transaction
     * @throws DatabaseFailureException when the request cannot be performed
     */
    public static void insertValues(String tableName, Map values, Connection connection)
        throws DatabaseFailureException
    {
        PreparedStatement statement = null;

        try
        {
            statement = PreparedStatementFactory.buildInsertStatement(tableName,
                    values, connection);
            statement.executeUpdate();
            statement.close();
        }
        catch (SQLException e)
        {
            throw new DatabaseFailureException("SQLException in insertValues(" +
                tableName + ", " + values + ")", moduleName, "", null, e);
        }
    }

    /**
     * Locks matching rows.
     *
     * @param tableName SQL table
     * @param whereMAP SQL map containing columns and values for WHERE clause
     * @param transactionId reference to the transaction
     * @throws DatabaseFailureException when the request cannot be performed
     */
    public static void lockRows(String tableName, Map whereMap, Connection connection)
        throws DatabaseFailureException, RecordAlreadyLockedException
    {
        PreparedStatement statement = null;

        try
        {
            statement = PreparedStatementFactory.buildLockStatement(tableName,
                    whereMap, connection);
            statement.executeQuery();
            statement.close();
        }
        catch (SQLException e)
        {
            if (e.getErrorCode() == -54)
            {
                throw new RecordAlreadyLockedException("Unkown",
                    RecordAlreadyLockedException.DEFAULT_SEVERITY, moduleName,
                    null, null, e);
            }
            else
            {
                throw new DatabaseFailureException("SQLException in lockRows(" +
                    tableName + ", " + whereMap + ")", moduleName, "", null, e);
            }
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
