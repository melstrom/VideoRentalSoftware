package account;
//TODO: most methods are missing proper queries

/**
 * Account Manager class
 * 
 * @author mattp
 * @version 1.0 April 3, 2011
 * @version 1.1 April 4
 *      -added isDuplicatedID()
 * @version 1.2 
 *	-added barcodeGenerator (incomplete)
 *	-added JDBCConnetion package
 *	-fixed demote(), promote() and editAccount()
 */
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import jdbconnection.JDBCConnection;

public class AccountManagement
{

    /**
     * Default constructor with no parameters
     */
    public AccountManagement() throws SQLException, ClassNotFoundException
    {
        JDBC = new JDBCConnection();
        //connection = JDBCConnection.getJDBCConnection();
    }

    /**
     * Create an employee account
     * @param position the position of the employee (Manager/Staff)
     * @param accountID the account ID of the account
     * @param Fname the first name of the user
     * @param Lname the last name of the user
     * @param address the address of the user
     * @param phoneNum the phone number of the user
     */
    public void createEmployee(String position, int accountID, String Fname, String Lname, String address, String phoneNum)
            throws SQLException, java.lang.Exception
    {
        if (!isDuplicatedID(accountID, "employee"))
        {
            account = new Employee(position, accountID, Fname, Lname, address, phoneNum);
            try
            {
                st = JDBC.createStatement();
                //TODO: Write the correct insert - consult Kristan
                String SQL = "INSERT INTO employee ()value();";
                st.executeUpdate(SQL);
            } finally
            {
                JDBC.closeConnection();
            }
        }
    }

    /**
     * Create a customer account
     * @param DL the driver license number of the user
     * @param accountID the accountID of the account
     * @param Fname the first name of the user
     * @param Lname the last name of the user
     * @param address the address of the user
     * @param phoneNum the phone number of the user
     */
    public void createCustomer(String DL, int accountID, String Fname, String Lname, String address, String phoneNum)
            throws SQLException, java.lang.Exception
    {
        if (!isDuplicatedID(accountID, "customer"))
        {
            account = new Customer(generateBarcode(), DL, accountID, Fname, Lname, address, phoneNum);
            try
            {
                st = JDBC.createStatement();
                //TODO: Write the correct insert - consult Kristan
                String SQL = "INSERT INTO customer ()value();";
                st.executeUpdate(SQL);
            } finally
            {
                JDBC.closeConnection();
            }
        }
    }

    /**
     * Generate membership barcode
     * @return barcode
     */
    private String generateBarcode() throws SQLException
    {
        //TODO: implement logic; find last ID and assign next
        //SELECT account FROM customer/employee
        //ResultSet rs = st.execute(SQL);
        //rs.last();
        //LastID = rs.getString(1) 1=columnIndex
        //Convert into INT, +1, convert to string
        String barcode = "101";
        return barcode;
    }

    /**
     * Edit an existing account
     * @param aAccount the account
     * @param accountType the type of the account
     */
    public void editAccount(Object aAccount, String accountType)
    {
        if (accountType.equals("employee"))
        {
            Employee employee = (Employee) aAccount;
            account = employee;
        } else if (accountType.equals("customer"))
        {
            Customer customer = (Customer) aAccount;
            account = customer;
        }
    }

    /**
     * Check if an account id already exists in the database
     * @param ID an account ID
     * @param userType the type of user (employee/customer)
     * @return boolean
     * @throws SQLException 
     */
    private boolean isDuplicatedID(int ID, String accountType)
            throws SQLException, ClassNotFoundException, java.lang.Exception
    {
        //TODO: generate correct query
        String table = accountType;
        String column = accountType;
        int constraint = ID;
        String query = "SELECT " + column + " FROM " + table + " WHERE " + accountType + "ID = " + constraint;
        Statement statement = connection.createStatement();
        boolean result = statement.execute(query);
        return result;
    }

    /**
     * Set log in information
     * @param loginID the log in ID/username of the account
     * @param password the password of the account
     */
    public void setLogin(String loginID, String password)
    {
        account.setLogin(loginID, password);
    }

    /**
     * Set personal information with 4 attributes
     * @param Fname the first name of the user
     * @param Lname the last name of the user
     * @param address the address of the user
     * @param phoneNum the phone number of the user
     */
    public void setPersonalInfo(String Fname, String Lname, String address, String phoneNum)
    {
        account.setPersonalInfo(Fname, Lname, address, phoneNum);
    }

    /**
     * Set personal information with 5 attributes
     * @param DL the driver license id of the customer
     * @param Fname the first name of the user
     * @param Lname the last name of the user
     * @param address the address of the user
     * @param phoneNum the phone number of the user
     */
    public void setPersonalInfo(String DL, String Fname, String Lname, String address, String phoneNum)
    {
        Customer customer = (Customer) account;
        customer.setPersonalInfo(Fname, Lname, address, phoneNum);
        customer.setDL(DL);
        account = customer;
    }

    /**
     * Change the status of the account (Active/Inactive)
     */
    public void changeStatus()
    {
        account.changeStatus();
    }

    /**
     * Promote an employee to manager
     */
    public void promoteEmployee()
    {
        Employee employee = (Employee) account;
        employee.setPosition("Manager");
        account = employee;
    }

    /**
     * Demote a manager to employee
     */
    public void demoteManager()
    {
        Employee employee = (Employee) account;
        employee.setPosition("Employee");
        account = employee;
    }
    private Account account;
    private JDBCConnection JDBC;
    private Connection connection;
    private Statement st;
}
