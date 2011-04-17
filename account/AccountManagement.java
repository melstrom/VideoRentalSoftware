package account;

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
 *
 * April 11
 * -added incomplete SQL statements; needs revision, see TODOs
 * -most methods are missing proper queries
 *
 * April 14
 * First pass: SQL queries for createEmployee
 * First pass: SQL queries for createCustomer
 * Completed implementation for generateNewID
 * Added helper methods
 *
 * Moved all unused methods to bottom
 * Changed name from generateBarcode to generateNewID
 */
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import jdbconnection.JDBCConnection;

public class AccountManagement
{

    private Account account;
    private Connection connection;
    private Statement statement;

    /**
     * Default constructor with no parameters
     */
    public AccountManagement() throws SQLException, ClassNotFoundException
    {
        setupConnection();
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
    public void createEmployee(int employeeID, String position, String Fname, String Lname, Address address, String phoneNum)
            throws SQLException, java.lang.Exception
    {
        int accountID = this.generateNewAccountID();
        int addressID = this.generateAddressID();
        account = new Employee(position, employeeID, Fname, Lname, address, phoneNum);
        try
        {
            String addressInsert = this.createAddressInsertSQL(address, addressID);
            String accountInsert = this.createAccountInsertSQL(accountID, addressID, Fname, Lname, phoneNum);
            String employeeInsert = "INSERT INTO employee (employeeID, accountID, position) value("
                    + employeeID + ","
                    + accountID + ",'"
                    + position + "')";
            statement.executeUpdate(addressInsert);
            statement.executeUpdate(accountInsert);
            statement.executeUpdate(employeeInsert);
        }
        finally
        {
            connection.close();
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
     * @throws SQLException
     * @throws java.lang.Exception
     */

    //TODO: Fix exception handling and documentation
    public void createCustomer(int customerID, String DL, String Fname, String Lname, Address address, String phoneNum)
            throws SQLException, java.lang.Exception
    {
        int accountID = this.generateNewAccountID();
        int addressID = this.generateAddressID();
        account = new Customer(DL, customerID, Fname, Lname, address, phoneNum);
        try
        {
            String addressInsert = this.createAddressInsertSQL(address, addressID);
            String accountInsert = this.createAccountInsertSQL(accountID, addressID, Fname, Lname, phoneNum);

            String customerInsert = "INSERT INTO customer (customerID, accountID, driversLicense, penalty) value("
                    + customerID + ","
                    + accountID + ",'"
                    + DL + "',"
                    + "0";
            statement.executeUpdate(addressInsert);
            statement.executeUpdate(accountInsert);
            statement.executeUpdate(customerInsert);
        }
        finally
        {
            connection.close();
        }
    }
    
    /**
     * Edits personal information with 4 attributes
     * @param Fname the first name of the user
     * @param Lname the last name of the user
     * @param address the address of the user
     * @param phoneNum the phone number of the user
     */
        public void editPersonalInfo(String Fname, String Lname, Address address, String phoneNum)
    {
        account.setPersonalInfo(Fname, Lname, address, phoneNum);
    }

    /**
     * Edits personal information with 5 attributes
     * @param DL the driver license id of the customer
     * @param Fname the first name of the user
     * @param Lname the last name of the user
     * @param address the address of the user
     * @param phoneNum the phone number of the user
     */
    public void editPersonalInfo(String DL, String Fname, String Lname, Address address, String phoneNum)
    {
        Customer customer = (Customer) account;
        customer.setPersonalInfo(Fname, Lname, address, phoneNum);
        customer.setDL(DL);
        account = customer;
    }

    /**
     * Generates a new employeeID or customerID from the database for a employee or customer
     * @param accountType The account type to generate a new ID for (employee or customer)
     * @return A new ID for a customer or employee
     * @throws SQLException
     */
    public int generateNewID(String accountType) throws SQLException
    {
        String table = accountType;
        String column = accountType + "ID";
        String SQL = "SELECT " + column + " FROM " + table;
        ResultSet rs = statement.executeQuery(SQL);
        rs.last();
        int LastID = rs.getInt(column);
        int newID = LastID + 1;
        return newID;
    }
    
    /**
     * Promote an employee to manager
     */
    public void promoteEmployee(int employeeID) throws SQLException, AlreadyManagerException
    {
        String table = "employee";
        String column = "position";
        String query = "SELECT " + column + " FROM " + table + " WHERE employeeID = " + employeeID;
        ResultSet rs = statement.executeQuery(query);
        if (rs.next()==!false)
        {
            String result = rs.getString(1);
            if (!result.equals("manager"))
            {
                throw new AlreadyManagerException("The employee is already a manager");
            }
        String SQL = "UPDATE " + table + " SET " + column + " = 'Manager' WHERE employeeID = " + employeeID;
        statement.executeUpdate(SQL);
        }
    }

    /**
     * Demote a manager to employee
     */
    public void demoteManager(int employeeID) throws SQLException, NotManagerException
    {
        String table = "employee";
        String column = "position";
        String query = "SELECT " + column + " FROM " + table + " WHERE employeeID = " + employeeID;
        ResultSet rs = statement.executeQuery(query);
        if (rs.next()==!false)
        {
            String result = rs.getString(1);
            if (result.equals("Employee"))
            {
                throw new NotManagerException("The employee is not a manager");
            }
        }
    }

    /**
     * Generates a new accountID from the database for a new account
     * @return The next accountID for the address table
     * @throws SQLException
     */
    private int generateNewAccountID() throws SQLException
    {
        String table = "account";
        String column = "accountID";
        String SQL = "SELECT " + column + " FROM " + table;
        ResultSet rs = statement.executeQuery(SQL);
        rs.last();
        int LastAccountID = rs.getInt(column);
        int newAccountID = LastAccountID + 1;
        return newAccountID;
    }

    /**
     * Generates a new addressID from the database for a new address
     * @return The next addressID for the address table
     * @throws SQLException
     */
    private int generateAddressID () throws SQLException
    {
        String table = "address";
        String column = "addressID";
        String SQL = "SELECT " + column + " FROM " + table;
        ResultSet rs = statement.executeQuery(SQL);
        rs.last();
        int LastAccountID = rs.getInt(column);
        int newAccountID = LastAccountID + 1;
        return newAccountID;
    }

    /**
     * Prepares a SQL statement to insert a new account
     * @param accountID accountID generated from generateNewAccountID
     * @param addressID addressID generated from generateAddressID
     * @param Fname The first name
     * @param Lname The last name
     * @return
     */
    private String createAccountInsertSQL (int accountID, int addressID, String Fname, String Lname, String phoneNum)
    {
            String SQL = "INSERT INTO account (accountID, addressID, firstName, lastName, phoneNum) value("
            + accountID + ","
            + addressID + ",'"
            + Fname + "','"
            + Lname + "','"
            + phoneNum + "')";
            return SQL;
    }

    /**
     *
     * @param address
     * @param addressID
     * @return
     * @throws SQLException
     */
    private String createAddressInsertSQL (Address address, int addressID) throws SQLException
    {
        String SQL = "INSERT INTO address (addressID, houseNumber, streetName, city, province, country, postalCode) value ("
        + addressID + ","
        + address.getHouseNumber() + ",'"
        + address.getStreetName() + "','"
        + address.getCity() + "','"
        + address.getProvince() + "','"
        + address.getCountry() + "','"
        + address.getPostalCode() + "')";
        return SQL;
    }

        /**
     * Sets up the database connection for the class
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private void setupConnection() throws SQLException, ClassNotFoundException
    {
        connection = JDBCConnection.getJDBCConnection();
        statement = connection.createStatement();
    }
}

//    /**
//     * Check if an account id already exists in the database
//     * @param ID an account ID
//     * @param userType the type of user (employee/customer)
//     * @return boolean
//     * @throws SQLException
//     */
//    private boolean isDuplicatedID(int ID, String accountType)
//            throws SQLException, ClassNotFoundException, java.lang.Exception
//    {
//        String table = accountType;
//        String column = accountType;
//        int constraint = ID;
//        String query = "SELECT " + column + "ID FROM " + table + " WHERE " + column + "ID = " + constraint;
//        Statement statement = connection.createStatement();
//        boolean result = statement.execute(query);
//        return result;
//    }
//    /**
//     * Set log in information
//     * @param loginID the log in ID/username of the account
//     * @param password the password of the account
//     */
//    public void setLogin(String loginID, String password)
//    {
//        account.setLogin(loginID, password);
//    }
//    /**
//     * Change the status of the account (Active/Inactive)
//     */
//    public void changeStatus()
//    {
//        account.changeStatus();
//    }



//    /**
//     * Edit an existing account
//     * @param aAccount the account
//     * @param accountType the type of the account
//     */
//    public void editAccount(Object aAccount, String accountType)
//    {
//        if (accountType.equals("employee"))
//        {
//            Employee employee = (Employee) aAccount;
//            account = employee;
//        }
//
//        else if (accountType.equals("customer"))
//        {
//            Customer customer = (Customer) aAccount;
//            account = customer;
//        }
//    }