package authentication;
import account.*;
import jdbconnection.JDBCConnection;
import search.*;

import java.sql.*;
import java.io.IOException;
/**
*	
*	
*	@author Kevin
* 	- added method to get username at albert's request 
*/
public class Authentication
{
	public Authentication()
        {}

        public boolean login(String ID, String password)
                throws SQLException, ClassNotFoundException,
                       AuthenticationException, IOException
        {
            if(ID.charAt(0) == '1')
            {
                JDBCConnection conn = new JDBCConnection();
                conn.getConnection();
                try
                {
                    String command = "SELECT account.phoneNum " +
                            "FROM employee, account" +
                            "WHERE employee.accountID = account.accountID " +
                            "AND employee.employeeID = " + ID + ";";
                    PreparedStatement stat = conn.prepareStatement(command);
                    ResultSet result = stat.executeQuery();
                    if(result.next())
                    {
                        String phoneNum = result.getString(1);
                        String expectPassword = phoneNum.substring(phoneNum.length() - 4);
                        if(expectPassword.equals(password))
                        {
                            account = Search.getEmployee(Integer.parseInt(ID));
                            return true;
                        }
                        else
                            return false;
                    }
                    else
                        throw new AuthenticationException("The ID provided cannot be found.");
                }
                finally
                {
                    conn.closeConnection();
                }
            }
            else if(ID.charAt(0) == '5')
            {
                JDBCConnection conn = new JDBCConnection();
                conn.getConnection();
                try
                {
                    String command = "SELECT account.phoneNum " +
                            "FROM customer, account " +
                            "WHERE customer.accountID = account.accountID " +
                            "AND customer.customerID = " + ID + ";";
                    PreparedStatement stat = conn.prepareStatement(command);
                    ResultSet result = stat.executeQuery();
                    if(result.next())
                    {
                        String phoneNum = result.getString(1);
                        String expectPassword = phoneNum.substring(phoneNum.length() - 4);
                        if(expectPassword.equals(password))
                        {
                            account = Search.getCustomer(Integer.parseInt(ID));
                            return true;
                        }
                        else
                            return false;
                    }
                    else
                        throw new AuthenticationException("The ID provided cannot be found.");
                }
                finally
                {
                    conn.closeConnection();
                }
            }
            else
                throw new AuthenticationException("The ID provided cannot be found.");
        }
        
        public TypeOfPerson whoAmI()
        {
            if(account != null)
            {
                if(Integer.toString(account.getAccountID()).charAt(0) == '1')
                {
                    if(((Employee)(account)).getPosition().equals("Manager"))
                        return TypeOfPerson.MANAGER;
                    else
                        return TypeOfPerson.EMPLOYEE;
                }
                else
                    return TypeOfPerson.CUSTOMER;
            }
            else
                return TypeOfPerson.NOT_LOGIN;
        }

        /**
         * User please cancatinate the account object to Customer or Employee
         * @return
         */
        public Account getAccount()
        {
            return this.account;
        }

        public boolean logout()
        {
            if(account !=null)
                return false;
            account = null;
            return true;
        }

        public enum TypeOfPerson{NOT_LOGIN, CUSTOMER, EMPLOYEE, MANAGER};
        private Account account;
}
