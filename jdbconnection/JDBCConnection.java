package jdbconnection;
import java.sql.*;

/**
 * This class gets a connection to our database for the caller.  It is the
 * caller's responsibility to close the connection when he is done.
 * @author Kristan
 *
 * 4 April: Mitch
 * changed getJDBCConnection to a static method
 * added a synonym method to make calling easier
 * added a closeConnection() method so that the database connection can be
 * released on command
 * removed all catch blocks since they are not dealing with errors at this level
 * changed the class to be public
 *
 * The way to use this class is to create a new JDBCConnection object
 * ie JDBCConnection connection = new JDBCConnection();
 *
 */
public class JDBCConnection {

    //this is the connection variable that will make the connection to MySql DB
   private Connection conn = null;
   private Statement st =null;
   public static final int COLUMNS = 0;
   public static final int VALUES = 1;

   //Constructor that calls getConnection to create the connection.

   public JDBCConnection()
   throws SQLException, ClassNotFoundException{
      this.conn = getJDBCConnection();
  }



   /**
    * This method closes the database connection.  If an instance of
    * JDBCConnection is created, this closeConnection() method MUST be called
    * when the user is done with the connection.  It is preferable for you to
    * put this method in a finally block.
    * @throws SQLException
    */
   public void closeConnection()
           throws SQLException
    {
       if (conn != null)
       {
           conn.close();
       }
       conn = null;
   }


    // makes the connection.
   /**
    * Returns a connection the database.  It is the caller's responsibility to
    * close this connection when he is finished.
    * Synonym for getConnection()
    * @return a connection to the database
    * @throws SQLException if a connection to the database cannot be made
    * @throws ClassNotFoundException if the driver cannot be found
    */
  public static Connection getJDBCConnection()
      throws SQLException, ClassNotFoundException{
      //Load the JDBC driver.
      Class.forName("com.mysql.jdbc.Driver");
      //connection to the database.
      String url = "jdbc:mysql://174.132.159.251:3306/kpoirier_CPSC2301?user=kpoirier_User&password=foobar";
      return DriverManager.getConnection(url);
    
  }


 /**
* Returns a connection the database.  It is the caller's responsibility to
* close this connection when he is finished.
* Synonym for getJDBCConnection()
* @return a connection to the database
* @throws SQLException if a connection to the database cannot be made
* @throws ClassNotFoundException if the driver cannot be found
*/
public static Connection getConnection()
      throws SQLException, ClassNotFoundException
{
  return getJDBCConnection();
}



/**
 *
 * @return
 * @throws SQLException
 */
  public Statement createStatement()
    throws SQLException{
      //try{
        st = conn.createStatement();
     // }
     // catch(Exception egg){System.err.println(egg.getMessage());}
      return st;
  }




  /**
   *
   * @return
   * @throws SQLException
   */
 
  public PreparedStatement prepareStatement(String command)
    throws SQLException{
        return conn.prepareStatement(command);
  }
  
    /**
   *
   * @return
   * @throws SQLException
   */
  /*public Statement prepareStatement(String command)
    throws SQLException{
      //try{
          st = conn.prepareStatement(command);
     // }
      //catch(Exception egg){System.err.println(egg.getMessage());}
      return st;
  }*/

  
  
    /**
     * This method generates a simple SQL query of the form
     * SELECT ___ FROM ____ WHERE ____
     * Both tableName and columnName will be filtered for ' characters,
     * but it is the responsibility of the caller to make sure that constraint
     * does not contain any characters that will escape the SQL statement.
     * @param tableName the name of the table you are querying
     * @param columnName the name of the column that you are selecting the 
     * results from
     * @param constraint the condition that narrows your results
     * @return a string containing a valid SQL query
     * @pre tableName must not be null
     * @throws NullPointerException if tableName is null
     */
    public static String makeQuery(String tableName, String columnName, String constraint)
    {
        String query = "";
        
        if (columnName == null)
        {
            query = "SELECT * ";
        }
        else
        {
            query =  "SELECT " + columnName.replaceAll("'", "") + " ";
        }
        
        query = query + "FROM "+tableName.replaceAll("'","")+" ";
        
        if (constraint != null)
        {
            query = query + "WHERE "+constraint;
        }
        return query;
    }



    /**
     * This method makes an insert query of the form
     * INSERT INTO tableName (information[COLUMNS][0], information[COLUMNS][1],
     * ..., information[COLUMNS][n])
     * VALUES (information[VALUES][0], information[VALUES][1],
     * ..., information[VALUES][n])
     * @param tableName
     * @param information
     * @return
     * @pre parameters cannot be null
     * @throws NullPointerException if any information is null
     */
    public static String makeInsert(String tableName, String[][] information)
    {
        String query = "INSERT INTO " + tableName + " ";
        query = query + "(" + information[COLUMNS][0];

        for (int i = 1; i < information[COLUMNS].length; i++)
        {
            query += ", ";
            query += information[COLUMNS][i];
        }

        query += ") ";

        query = query + "VALUES ('" + information[VALUES][0]+"'";
        for (int j = 1; j < information[VALUES].length; j++)
        {
            query += ", ";
            if (information[VALUES][j] == null)
            {
                query = query + "?";
            }
            else
            {
                query = query + "'"+ information[VALUES][j] + "'";
            }
        }

        query += ")";

        return query;
    }



    /**
     * creates an update query.
     * The form is UPDATE tableName SET column = blah WHERE blah = blah
     * @pre no parameter may be null
     * @param tableName
     * @param set
     * @param constraint
     * @return
     */
    public static String makeUpdate(String tableName, String set, String constraint)
    {
        String update = "UPDATE "+tableName+" "+
                "SET "+set+" "+
                "WHERE "+constraint;
        return update;
    }



    /**
     * This method gets the results of an SQL query that you provide
     * @param query a SELECT FROM WHERE query
     * @return the results of the query
     * @throws SQLException if a connection to the database cannot be made
     * @throws ClassNotFoundException if the Driver cannot be found
     *
     *
     */
    public ResultSet getResults(String query) throws SQLException, ClassNotFoundException
    {
        //System.out.println(query); // TESTING
        ResultSet result = null;
        Statement statement = conn.createStatement();
        //Statement statement = conn.prepareStatement(query);
        result = statement.executeQuery(query);
        return result;
    }
    
    

    /**
     * This method uses PreparedStatement to get a ResultSet.  A query should
     * be of form SELECT * FROM tableName WHERE condition = ?
     * where there can be as many ? as you require, so long as the number of them
     * is given.  An array holding the Strings you want to replace the ? with
     * should also be provided.
     * @param query The SQL query with a number of parameters to be filled in
     * @param numParameters the number of ?s to be filled in
     * @param parameters an array of the strings to replace the ?s with
     * @return the results of the sql query
     * @throws SQLException if a connection to the database cannot be made
     */
    public ResultSet getResults(String query, int numParameters,
            String[] parameters) throws SQLException, ClassNotFoundException
    {
        ResultSet result = null;
        PreparedStatement statement = conn.prepareStatement(query);
        for (int parameterIndex = 1; parameterIndex <= numParameters; parameterIndex++)
        {
            statement.setString(parameterIndex, parameters[parameterIndex - 1]);
        }
        result = statement.executeQuery();
        return result;
    }



    /**
     * This method executes a query that changes the database.
     * It can be either an INSERT, UPDATE, or DELETE query.
     * @param query
     * @return
     * @throws Exception
     */
    public int update(String query) throws SQLException, ClassNotFoundException
    {
        int linesChanged = 0;
        Statement statement = conn.prepareStatement(query);
        linesChanged = statement.executeUpdate(query);
        
        return linesChanged;
    }



    /**
     * This method executes a query that chagnse the database.
     * It can be either an INSERT, UPDATE, or DELETE query.
     * It uses PreparedStatement to prevent escaping SQL statements with '
     * @param query the update query
     * @param numParameters the number of ?s to fill in
     * @param parameters what to fill the ?s in with
     * @return the number of lines changed
     * @throws SQLException if the database cannot be found
     */
    public int update(String query, int numParameters, String[] parameters)
            throws SQLException, ClassNotFoundException
    {
       
        int linesChanged = 0;
        PreparedStatement statement = conn.prepareStatement(query);
        for (int parameterIndex = 1; parameterIndex <= numParameters; parameterIndex++)
        {
            statement.setString(parameterIndex, parameters[parameterIndex - 1]);
        }
        linesChanged = statement.executeUpdate();

        return linesChanged;
    }



    /**
     * This method is used to re-open a database connection if you have closed it
     * @throws Exception
     */
    public void openConnection() throws Exception
    {
        conn = getConnection();
    }
  
}



