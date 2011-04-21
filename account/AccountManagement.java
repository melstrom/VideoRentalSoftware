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

/**
 * AccountManagement is charge of all account related tasks, such as creating new
 * customers and employees, editing personal info, and changing the position of employees
 * @author melstrom
 */
public class AccountManagement
{
    private Connection connection;
    private Statement statement;

    /**
     * Default constructor with no parameters
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public AccountManagement() throws SQLException, ClassNotFoundException
    {
        setupConnection();
    }

    /**
     * Creates an employee account
     * @param employeeID The employeeID that is auto-generated for each new employee
     * @param position the position of the employee (Manager/Staff)
     * @param Fname the first name of the user
     * @param Lname the last name of the user
     * @param address the address of the user
     * @param phoneNum the phone number of the user
     * @throws SQLException
     * @throws java.lang.Exception
     */
    public void createEmployee(int employeeID, String position, String Fname, String Lname, Address address, String phoneNum)
            throws SQLException, java.lang.Exception
    {
        int accountID = this.generateNewAccountID();
        int addressID = this.generateAddressID();
        Employee account = new Employee(position, employeeID, Fname, Lname, address, phoneNum);
        String addressInsert = this.createAddressInsertSQL(address, addressID);
        String accountInsert = this.createAccountInsertSQL(accountID, addressID, Fname, Lname, phoneNum);
        String employeeInsert = "INSERT INTO employee (employeeID, accountID, position) values ("
        + "'"+account.getEmployeeID()+"'" + ","
        + "'"+accountID+"'" + ","
        + "'"+account.getPosition()+"'" + ")";
        try
        {
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
     * Creates a customer account
     * @param customerID The customerID that is auto-generated for each new customer
     * @param DL the driver license number of the user
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
        Customer account = new Customer(DL, customerID, Fname, Lname, address, phoneNum);
        String addressInsert = this.createAddressInsertSQL(address, addressID);
        String accountInsert = this.createAccountInsertSQL(accountID, addressID, Fname, Lname, phoneNum);
        String customerInsert = "INSERT INTO customer (customerID, accountID, driversLicense, penalty) values ("
                + "'"+account.getCustomerID()+"'" + ","
                + "'"+accountID+"'" + ","
                + "'"+account.getDL()+"'" + ","
                + "'0')";
        try
        {
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
     * This method takes an employee object, with attributes that differ from
     * the attributes in the database.  It updates the database with this
     * employee information.  The attribute that should not change is the
     * employee's accountID (Java class) which is equivalent to the
     * employeeID in the database.
     * @param employee an Employee object with attributes that we want to save
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static void editPersonalInfo(Employee employee)
            throws SQLException, ClassNotFoundException
    {
        String table = "employee, account, address";
        String set = "employee.position = ?";
        set += ", ";
        set += "account.firstName = ?";
        set += ", ";
        set += "account.lastName = ?";
        set += ", ";
        set += "account.phoneNum = ?";
        set += ", ";
        set += "address.houseNumber = ?";
        set += ", ";
        set += "address.streetName = ?";
        set += ", ";
        set += "address.city = ?";
        set += ", ";
        set += "address.province = ?";
        set += ", ";
        set += "address.country = ?";
        set += ", ";
        set += "address.postalCode = ?";

        String constraint = "employee.accountID = account.accountID";
        constraint += " AND ";
        constraint += "account.addressID = address.addressID";
        constraint += " AND ";
        constraint += "employee.employeeID = ?";

        int numParams = 11;
        Address address = employee.getAddress();
        String[] params = {
            employee.getPosition(),
            employee.getFname(),
            employee.getLname(),
            employee.getPhoneNum(),
            "" + address.getHouseNumber(),
            address.getStreetName(),
            address.getCity(),
            address.getProvince(),
            address.getCountry(),
            address.getPostalCode(),
            "" + employee.getAccountID()
        };
        String updateQuery = JDBCConnection.makeUpdate(table, set, constraint);
        JDBCConnection connection = new JDBCConnection();
        try
        {
            int linesChanged = connection.update(updateQuery, numParams, params);
            if (linesChanged == 0)
            {
                throw new IllegalArgumentException("Could not find that EmployeeID");
            }
            // assert (linesChanged == 1)
        }
        finally
        {
            connection.closeConnection();
        }
    }

    /**
     * This method takes an customer object, with attributes that differ from
     * the attributes in the database.  It updates the database with this
     * customer information.  The attribute that should not change is the
     * customer's accountID (Java class) which is equivalent to the
     * customerID in the database.
     * @param customer an customer object with attributes that we want to save
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static void editPersonalInfo(Customer customer)
            throws SQLException, ClassNotFoundException
    {
        String table = "customer, account, address";
        String set = "customer.driversLicense  = ?";
        set += ", ";
        set += "account.firstName = ?";
        set += ", ";
        set += "account.lastName = ?";
        set += ", ";
        set += "account.phoneNum = ?";
        set += ", ";
        set += "address.houseNumber = ?";
        set += ", ";
        set += "address.streetName = ?";
        set += ", ";
        set += "address.city = ?";
        set += ", ";
        set += "address.province = ?";
        set += ", ";
        set += "address.country = ?";
        set += ", ";
        set += "address.postalCode = ?";

        String constraint = "customer.accountID = account.accountID";
        constraint += " AND ";
        constraint += "account.addressID = address.addressID";
        constraint += " AND ";
        constraint += "customer.customerID = ?";

        int numParams = 11;
        Address address = customer.getAddress();
        String[] params = {
            customer.getDL(),
            customer.getFname(),
            customer.getLname(),
            customer.getPhoneNum(),
            "" + address.getHouseNumber(),
            address.getStreetName(),
            address.getCity(),
            address.getProvince(),
            address.getCountry(),
            address.getPostalCode(),
            "" + customer.getAccountID()
        };
        String updateQuery = JDBCConnection.makeUpdate(table, set, constraint);
        JDBCConnection connection = new JDBCConnection();
        try
        {
            int linesChanged = connection.update(updateQuery, numParams, params);
            if (linesChanged == 0)
            {
                throw new IllegalArgumentException("Could not find that CustomerID");
            }
            // assert (linesChanged == 1)
        }
        finally
        {
            connection.closeConnection();
        }
    }

    /**
     * This method generates a new employee or customer ID.  It finds the 
     * highest previous value of the ID and adds one to it to obtain a new
     * unique ID.  If there are no values in the database, then it automatically
     * starts at the minimum possible value.
     * 
     * @param accountType the type of account.  Must be either employee or customer
     * @return the new ID
     * @throws SQLException if a connection the database cannot be made
     * @throws AccountLimitReachedException if the maximum number of accounts, either
     * customer or employee depending on the accountType, has been reached
     * @throws ClassNotFoundException
     * @throws IllegalArgumentException if the accountType is not customer
     * or employee
     */
    public int generateNewID(String accountType) throws SQLException,
            AccountLimitReachedException, ClassNotFoundException
    {
        String table = accountType;
        String column = accountType + "ID";
        int highestID = getHighestID(table, column);
        int newHighestID = highestID + 1;
        int maxCustomerID = Integer.MAX_VALUE;
        int maxEmployeeID = (int) Math.pow(10, Customer.ID_LENGTH -1) * Customer.ID_START_DIGIT - 1;
        if (accountType.equalsIgnoreCase("customer") && newHighestID > maxCustomerID)
        {
            throw new AccountLimitReachedException("The maximum number of " +
                    "customer accounts has been reached");
        }
        else if (accountType.equalsIgnoreCase("employee") && newHighestID > maxEmployeeID)
        {
            throw new AccountLimitReachedException("The maximum number" +
                    " of employee accounts has been reached");
        }
        return newHighestID;
    }

    /**
     * Promotes an employee to manager
     * @param employeeID The employeeID of the employee being promoted
     * @throws SQLException
     * @throws AlreadyManagerException
     */
    public void promoteEmployee(int employeeID) throws SQLException, AlreadyManagerException
    {
        String table = "employee";
        String column = "position";
        String query = "SELECT " + column + " FROM " + table + " WHERE employeeID = " + employeeID;
        ResultSet rs = statement.executeQuery(query);
        if (rs.next())
        {
            String result = rs.getString(1);
            if (result.equalsIgnoreCase("manager"))
            {
                throw new AlreadyManagerException("The employee is already a manager");
            }
        String SQL = "UPDATE " + table + " SET " + column + " = 'Manager' WHERE employeeID = " + "'"+employeeID+"'";
        statement.executeUpdate(SQL);
        }
    }

    /**
     * Demote a manager to employee
     * @param employeeID The employeeID of the employee being demoted
     * @throws SQLException
     * @throws NotManagerException
     */
    public void demoteManager(int employeeID) throws SQLException, NotManagerException
    {
        String table = "employee";
        String column = "position";
        String query = "SELECT " + column + " FROM " + table + " WHERE employeeID = " + employeeID;
        ResultSet rs = statement.executeQuery(query);
        if (rs.next())
        {
            String result = rs.getString(1);
            if (result.equalsIgnoreCase("Employee"))
            {
                throw new NotManagerException("The employee is not a manager");
            }

            String set = column + " = 'Employee'";
            String constraint = "employeeID = '"+employeeID+"'";
            String updateQuery = JDBCConnection.makeUpdate(table, set, constraint);
            statement.executeUpdate(updateQuery);
        }
    }

    /**
     * This method generates a new account ID.  The account ID is only ever
     * used in the database.
     * @return a new, unique AccountID
     * @throws SQLException
     */
    private int generateNewAccountID() throws SQLException, ClassNotFoundException
    {
        String table = "account";
        String column = "accountID";
        int highestID = getHighestID(table, column);
        return highestID + 1;
    }

    /**
     * This method generates a new address ID.  The address ID is only ever
     * used in the database.
     * @return a new, unique AddressID
     * @throws SQLException
     */
    private int generateAddressID() throws SQLException, ClassNotFoundException
    {
        String table = "address";
        String column = "addressID";
        int highestID = getHighestID(table, column);
        return highestID + 1;
    }

    /**
     * Prepares a SQL statement to insert a new account
     * @param accountID accountID generated from generateNewAccountID
     * @param addressID addressID generated from generateAddressID
     * @param Fname The first name
     * @param Lname The last name
     * @return A SQL statement that inserts a new account when run
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
     * Prepares a SQL statement to insert a new address
     * @param address An address object containing all address information
     * @param addressID addressID generated from generateAddressID
     * @return A SQL statement that inserts a new address when run
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
     * This method finds the highest ID number that currently exists
     * for either an employee or customer or account or address
     * @return the highest ID
     * @throws SQLException
     * @throws IllegalArgumentException if the table is not
     * account, customer, employee, or address
     */
    private int getHighestID(String table, String column)
            throws SQLException, ClassNotFoundException
    {
        String query = JDBCConnection.makeQuery(table, "MAX(" + column + ")", null);

        if (connection.isClosed())
        {
            setupConnection();
        }
        ResultSet result = statement.executeQuery(query);//conn.getResults(query);
        if (result.next() && result.getInt(1) != 0)
        {
                return result.getInt(1);
        }
        else if (table.equalsIgnoreCase("customer"))
        {
            return (int) (Math.pow(10, Customer.ID_LENGTH - 1) * Customer.ID_START_DIGIT);
        }
        else if (table.equalsIgnoreCase("employee"))
        {
            return (int) (Math.pow(10, Employee.ID_LENGTH - 1) * Employee.ID_START_DIGIT);
        }
        else if (table.equalsIgnoreCase("account"))
        {
            return 0;
        }
        else if (table.equalsIgnoreCase("address"))
        {
            return 0;
        }
        else
        {
            throw new IllegalArgumentException("Cannot find the highest ID of" +
                    " "+table);
        }
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