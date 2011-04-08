/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jdbconnection;
import java.sql.*;
import java.util.Scanner;

/**
 *
 * @author Kristan
 */
public class connectionExamples {

    /**
     * @param args the command line arguments
     */
    private static JDBCConnection DBConn = null;
    private static String fname = "";
    private static String pnum = "";
    private static String address = "";

    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        connectToDb();
        getInput();
        SelectTest();
        DBConn.closeConnection();
    }
    public static void connectToDb(){
        try{
            DBConn = new JDBCConnection();
            }
        catch (Exception e){System.err.println(e.getMessage());
        }
    }
    public static void getInput(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter first name:");
        fname = sc.next();
        System.out.println("Enter phoneNumber:");
        pnum = sc.next();
        System.out.println("Enter address:");
        address = sc.next();
    }
    public static void InsertTest() throws Exception{
        Statement st = DBConn.createStatement();
        try{
        st.executeUpdate("insert into Customer (FirstName,PhoneNumber,address)value('" + fname + "','" + pnum + "','" + address + "');");
        }
        catch(Exception egg){}
    }

    public static void SelectTest(){

        String query = "SELECT * FROM Customer";
        try
        {
          Statement st = DBConn.createStatement();
          ResultSet rs = st.executeQuery(query);
          while (rs.next())
          {
            String s = rs.getString("FirstName");
            //float n = rs.getFloat("PRICE");
            System.out.println(s + " ");
          }
        }
        catch (SQLException ex)
        {
          System.err.println(ex.getMessage());
        }
    }

}
