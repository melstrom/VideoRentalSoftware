package store;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class acts as the database connection for each terminal.
 * It also has methods for generating simple SQL queries.  If you have a
 * complex query that the methods do not provide for, this class provides a
 * sql.Connection to the database as well.
 *
 * Mitch will write the SQL methods.
 * Kevin will write the constructor and actual connection code.
 *
 * @author Mitch
 */
public class VRSConnection
{
    // instance fields
    private Connection conn;
    private static VRSConnection instance;

    // static initialization
    static
    {
        try
        {
            instance = new VRSConnection();
        }
        catch (Exception e)
        {
            throw new ExceptionInInitializerError(e);
        }
    }

    // constructor
    private VRSConnection() throws SQLException
    {
        // needs implementation
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // public methods
    /**
     * Returns a reference to the Singleton connection
     * @return a reference to the connection
     */
    public static VRSConnection getInstance()
    {
        return instance;
    }



    /**
     * Returns a reference to the database connection.
     * Used for making complex SQL queries not provided for in this class's
     * methods.
     * Do not close this connection.
     * @return a reference to the database connection
     */
    public Connection getConnection()
    {
        return conn;
    }



    /**
     * Re-opens a connection in case it is lost
     */
    public void open() throws SQLException
    {
        // not implemented
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    
    /**
     * Closes the connection.
     * Only use this method when the program is exiting, whether
     * normally or abnormally
     * @throws SQLException
     */
    public void close() throws SQLException
    {
        conn.close();
    }
    
    // public SQL methods

    /**
     * This method makes an SQL SELECT query.  The only required parameter is
     * tables.  The rest may be null.
     * You can tag things on to the end of the query e.g. ORDER BY
     * If part of your SQL statement is a string or char variable, you should
     * include it in your statement as a ? character, and put the variable
     * into the params array.
     * e.g.
     * String input = Scanner.getLine();
     * vrsconn.select({"Customers_"}, null, "name_ = ?", { input });
     *
     * rather than
     *
     * String input = Scanner.getLine();
     * vrsconn.select({"Customers_"}, null, "name_ = \'" + input + "\'", null);
     * @param tables the table(s) that you are selecting results from
     * @param columns the column(s) that you want.  If you want them all this
     * can be null
     * @param condition the condition that your results must fulfill.  If you
     * want everything, you may leave this as null.  You can add things that are
     * not strictly conditions, such as ORDER BY commands, but keep in mind that
     * if this parameter is not null, it will always have a WHERE clause.
     * @param params the variables that may appear as ? in the sql statement.
     * @return all the results of your query
     */
    public ResultSet select(String[] tables, String[] columns, String condition,
            String[] params) throws SQLException
    {
        String select;
        String from;
        String where;

        if (columns == null || columns.length < 1)
        {
            select = "SELECT *";
        }
        else
        {
            select = "SELECT " + arrayToCSL(columns);
        }

        if (tables == null || tables.length < 1)
        {
            throw new IllegalArgumentException("You must provide tables to query");
        }
        else
        {
            from = "FROM " + arrayToCSL(tables);
        }

        if (condition == null)
        {
            where = "";
        }
        else
        {
            where = "WHERE " + condition;
        }

        String query = select + ' ' + from + ' ' + where;
        PreparedStatement stat = prepareStatement(query, params);
        ResultSet rs = stat.executeQuery();
        return rs;
    }



    /**
     * This method performs an update query on a single table.
     * The first two parameters are required.  The others may be null if they
     * are not relevant.
     *
     * If part of your SQL statement is a string or char variable, you should
     * include it in your statement as a ? character, and put the variable
     * into the params array.
     * e.g.
     * String input = Scanner.getLine();
     * vrsconn.select({"Customers_"}, null, "name_ = ?", { input });
     *
     * rather than
     *
     * String input = Scanner.getLine();
     * vrsconn.select({"Customers_"}, null, "name_ = \'" + input + "\'", null);
     *
     * @param table the table that you want to update
     * @param setToValue the columns to update, and the value that you want to
     * update it to i.e. setToValue = { "Customers.name = \'Harry Potter\'" }
     * @param condition The condition that the value must meet in order to be
     * updated i.e. condition = Customers.name = "\'Snape\'"
     * the WHERE is inserted for you.  If you want to change all rows in the
     * table then use null for this parameter
     * @param params the variables that may appear as ? in the sql statement.
     * @return the number of rows changed
     */
    public int update(String table, String[] setToValue, String condition, 
            String[] params) throws SQLException
    {
        String update;
        String set;
        String where;

        if (table == null || table.length() < 1)
        {
            throw new IllegalArgumentException("You must provide a table.");
        }
        update = "UPDATE " + table;

        if (setToValue == null || setToValue.length < 1)
        {
            throw new IllegalArgumentException("You must provide columns to update");
        }
        set = "SET " + arrayToCSL(setToValue);

        if (condition == null)
        {
            where = "";
        }
        else
        {
            where = "WHERE " + condition;
        }

        String query = update + ' ' + set + ' ' + where;
        PreparedStatement stat = prepareStatement(query, params);
        int rowsChanged = stat.executeUpdate();
        
        return rowsChanged;
    }



    /**
     * Deletes rows from a single table.
     * The table name is required, but the condition may be null if you wish
     * to delete everything in the table.
     *
     * @param table the table to delete from
     * @param condition the condition that the row must meet in order to be
     * deleted
     * @param params the variable ?s to fill in
     * @return the number of rows deleted
     */
    public int delete(String table, String condition, String[] params) throws SQLException
    {
       String delete;
       String where;

       if (table == null || table.length() < 1)
       {
           throw new IllegalArgumentException("You must provide a table");
       }
       delete = "DELETE FROM " + table;

       if (condition == null)
       {
           where = "";
       }
       else
       {
           where = "WHERE " + condition;
       }

       String query = delete + ' ' + where;
       PreparedStatement stat = prepareStatement(query, params);
       int rowsDeleted = stat.executeUpdate();
       return rowsDeleted;
    }



    /**
     * Inserts a row into a single pre-existing table.
     *
     * Specifying columns is optional.  If you don't want to specify that columns
     * use null for that parameter.  If you use null you must include values for
     * every column, in the correct order.  See ER diagram for details.
     *
     * @param table the table you want to insert values into
     * @param columns the columns that you want to insert values into. This must
     * match the number and order of the values parameter.  It may be null.
     * @param values the values you want to insert into the columns.  If the
     * columns parameter is null then you must include all the values for every
     * column in the right order
     * @param params variables to fill the ?s in the sql query
     * @return the number of rows inserted
     */
    public int insert(String table, String[] columns, String[] values,
            String[] params) throws SQLException
    {
        String insert;
        String columnsSQL;
        String valuesSQL;

        if (table == null)
        {
            throw new IllegalArgumentException("You must provide a table.");
        }
        insert = "INSERT INTO " + table;

        if (columns == null || columns.length < 1)
        {
            columnsSQL = "";
        }
        else
        {
            columnsSQL = "(" + arrayToCSL(columns) + ")";
        }

        if (values == null || values.length < 1)
        {
            throw new IllegalArgumentException("You must provide values to insert.");
        }
        valuesSQL = "(" + arrayToCSL(values) + ")";

        String query = insert + ' ' + columnsSQL + ' ' + valuesSQL;

        PreparedStatement stat = prepareStatement(query, params);
        int numInserted = stat.executeUpdate();
        return numInserted;
    }



    // private methods
    
    /**
     * Array to comma separated list.
     * This method takes an array and compiles each element into a single String,
     * separated with commas.
     * This is intended to be the opposite of String.split()
     * however if there are commas in the elements of the array, using split on
     * the result of this method will not return the original array
     * @param array the array to combine
     * @return the elements of the array as a comma separated list
     */
    private String arrayToCSL(String[] array)
    {
        if (array == null || array.length < 1)
        {
            throw new IllegalArgumentException("You must provide a non-empty array");
        }
        String list;
        list = array[0];
        for (int i = 1; i < array.length; i++)
        {
            list = list + ", " + array[i];
        }
        return list;
    }
    
    
    
    /**
     * This method takes an sql statement and prepares it by filling in any
     * variable ?s with the proper parameter.
     * This is to guard against SQL injections.  This should only be used when the
     * variable is a String.
     * If there are no parameters, you may pass the array params as null.
     * @param sql the sql statement to prepare
     * @param params the parameters to fill in.  The order and number must match the order
     * the ?s appear in the sql statement.  This parameter may be null if there
     * is nothing to fill in.
     * @return the statement with all variables filled by appropriate parameters
     */
    private PreparedStatement prepareStatement(String sql, String[] params) throws SQLException
    {
        PreparedStatement stat = conn.prepareStatement(sql);
        if (params == null || params.length < 1)
        {
            return stat;
        }
        int arrayIndex = 0;
        int parameterIndex = 1;
        while (parameterIndex <= params.length)
        {
            stat.setString(parameterIndex, params[arrayIndex]);
            arrayIndex++;
            parameterIndex++;
        }
        return stat;
    }
}
