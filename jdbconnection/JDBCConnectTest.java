package jdbconnection;

//package com.devdaily.sqlprocessortests;

import java.sql.*;

public class JDBCConnectTest
{
  Connection conn;

  public static void main(String[] args)
  {
    JDBCConnectTest catfood = new JDBCConnectTest();
    catfood.doSelectTest();
    System.out.println("DEBUG: end of main()");
  }
 
  public JDBCConnectTest()
  {
    System.out.println("DEBUG: begin JDCConnectTest() constructor");
    try
    {
      Class.forName("com.mysql.jdbc.Driver").newInstance();
      String url = "jdbc:mysql://174.132.159.251/kpoirier_CPSC2301";
      conn = DriverManager.getConnection(url, "kpoirier_User", "foobar");
      doTests();
      System.out.println("DEBUG: before conn.close()");
      conn.close();
      System.out.println("DEBUG: after conn.close()");
    }
    catch (ClassNotFoundException ex) {System.err.println(ex.getMessage());}
    catch (IllegalAccessException ex) {System.err.println(ex.getMessage());}
    catch (InstantiationException ex) {System.err.println(ex.getMessage());}
    catch (SQLException ex)           {System.err.println(ex.getMessage());}
  }

  private void doTests()
  {
    doSelectTest();

    doInsertTest();  doSelectTest();
    //doUpdateTest();  doSelectTest();
    //doDeleteTest();  doSelectTest();
  }

  private void doSelectTest()
  {
    System.out.println("[OUTPUT FROM SELECT]");
    String query = "SELECT * FROM Customer";
    try
    {
      Statement st = conn.createStatement();
      ResultSet rs = st.executeQuery(query);
      while (rs.next())
      {
        String s = rs.getString("address");
        //float n = rs.getFloat("PRICE");
        System.out.println(s + "   ");
      }
    }
    catch (SQLException ex)
    {
      System.err.println(ex.getMessage());
    }
  }

  private void doInsertTest()
  {
    System.out.print("\n[Performing INSERT] ... ");
    try
    {
      Statement st = conn.createStatement();
      st.executeUpdate("INSERT INTO COFFEES " +
                       "VALUES ('BREAKFAST BLEND', 200, 7.99, 0, 0)");
    }
    catch (SQLException ex)
    {
      System.err.println(ex.getMessage());
    }
  }

  private void doUpdateTest()
  {
    System.out.print("\n[Performing UPDATE] ... ");
    try
    {
      Statement st = conn.createStatement();
      st.executeUpdate("UPDATE COFFEES SET PRICE=4.99 WHERE COF_NAME='BREAKFAST BLEND'");
    }
    catch (SQLException ex)
    {
      System.err.println(ex.getMessage());
    }
  }

  private void doDeleteTest()
  {
    System.out.print("\n[Performing DELETE] ... ");
    try
    {
      Statement st = conn.createStatement();
      st.executeUpdate("DELETE FROM COFFEES WHERE COF_NAME='BREAKFAST BLEND'");
    }
    catch (SQLException ex)
    {
      System.err.println(ex.getMessage());
    }
  }
}
