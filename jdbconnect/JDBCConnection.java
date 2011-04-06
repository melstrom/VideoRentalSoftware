package jdbconnection;
import java.sql.*;

class JDBCConnection {

    //this is the connection variable that will make the connection to MySql DB
   private Connection conn = null;
   private Statement st =null;

   //Constructor that calls getConnection to create the connection.

   public JDBCConnection() {
      getJDBCConnection();
  }


    // makes the connection.
  public void getJDBCConnection(){
    try{
      //Load the JDBC driver.
      Class.forName("com.mysql.jdbc.Driver");
      //connection to the database.
      String url = "jdbc:mysql://174.132.159.251:3306/kpoirier_CPSC2301?user=kpoirier_User&password=foobar";
      conn = DriverManager.getConnection(url);
    }
    catch (Exception e){System.err.println(e.getMessage());}
  }

  public Statement createStatement(){
      try{
        st = conn.createStatement();
      }
      catch(Exception egg){System.err.println(egg.getMessage());}
      return st;
  }

  public Statement prepareStatement(){
      try{
          st = conn.createStatement();
      }
      catch(Exception egg){System.err.println(egg.getMessage());}
      return st;
  }
}