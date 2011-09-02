package accounts;
import inventory.Copy;
import inventory.RentedCopy;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import store.VRSConnection;
// bookmark
/**
 * Interface for the Customer object.
 * @author Mitch
 */
public abstract class Customer extends Account
{

    // protected fields
    /**
     * How much the customer owes in fines to the store, in cents
     */
    protected int fines;

    /**
     * The date that the customer was added to the system.
     */
    protected Calendar registrationDate;

    // constructors
    
    public Customer(){}


    /**
     * instantiates the instance fields
     * @param name
     * @param address
     * @param postalCode
     * @param phoneNumber
     */
    public Customer(String name, String address, String postalCode,
            String phoneNumber)
    {
        fines = 0;
        registrationDate = new GregorianCalendar();
    }



    /**
     * Creates a Customer object based on information in the database.
     * @param customerID
     * @throws SQLException
     */
    Customer(int customerID) throws SQLException
    {
        String[] tables = {"Customers_"};
        String condition = "customer_ID = " + customerID;

        int parameterIndex = 2; // parameter 1 is the customer_ID. I want the name first.

        VRSConnection conn = VRSConnection.getInstance();
        ResultSet rs = conn.select(tables, null, condition, null);

        if (!rs.next())
        {
            throw new SQLException("No matching record found for customer ID "+customerID);
        }

        name = rs.getString(parameterIndex++);
        address =  rs.getString(parameterIndex++);
        postalCode = rs.getString(parameterIndex++);
        phoneNumber = rs.getString(parameterIndex++);
        status = rs.getInt(parameterIndex++);
        fines = rs.getInt(parameterIndex++);
        Date sqlDate = rs.getDate(parameterIndex++);
        registrationDate = new GregorianCalendar();
        registrationDate.setTime(sqlDate);

        accountID = customerID;
    }


    // public methods
    /**
     * Finds out how much the Customer owes in cents.
     * @return the customer's fines in cents
     */
    public int getFines()
    {
        return fines;
    }



    /**
     * Gets a list of all movies that the Customer has rented out
     * @return all the movies that Customer has rented out
     */
    abstract public RentedCopy[] getRentals();



    /**
     * Gets the date that the Customer was entered into the system
     * @return the date that the Customer was entered into the system
     */
    public Calendar getRegistrationDate()
    {
        return registrationDate;
    }

    // mutators

    /**
     * Adds to the customer's fine, the amount is in cents.
     * To subtract from the customer's fines do not use negative amounts.
     * Use the removeFine(:int) method.
     * @param cents a non-negative amount of money to add. The unit is cents.
     * @throws SQLException
     * @pre cents must be >= 0
     */
     void addFine(int cents) throws SQLException
     {
         if (cents < 0)
         {
             throw new IllegalArgumentException("You cannot add negative amounts.");
         }
         String table = "Customers_";
         String[] setToValue = { "fines_ = fines_ + " + cents };
         String condition = "customer_ID = " + accountID;

         VRSConnection conn = VRSConnection.getInstance();
         conn.update(table, setToValue, condition, null);
         fines += cents;
     }



    /**
     * Subtracts from the customer's fines, the amount is in cents.
     * To add to the customer's fines, do not use negative amounts.
     * Use the addFine(:int) method.
     * @param cents a non-negative amount of money to remove. The unit is cents.
     * @throws SQLException
     * @pre cents >= 0
     * @post fines >= 0
     */
    void removeFine(int cents) throws SQLException
     {
         if (cents < 0)
         {
             throw new IllegalArgumentException("You cannot add negative amounts.");
         }
         int newFines = fines - cents;
         if (newFines < 0)
         {
             throw new IllegalArgumentException("You cannot remove more fine than the customer owes.");
         }
         String table = "Customers_";
         String[] setToValue = { "fines_ = " + newFines };
         String condition = "customer_ID = " + accountID;

         VRSConnection conn = VRSConnection.getInstance();
         conn.update(table, setToValue, condition, null);
         fines = newFines;
     }



    /**
     * Adds a rental movie to the Customer's account
     * Meant to act as a manual override
     * @param copy
     * @throws SQLException
     */
    abstract void addRental(Copy copy) throws SQLException;



    /**
     * Removes a rented movie from the Customer's account.
     * You should use the getRental method to find out the index of the movie
     * to remove.
     * Meant to be used when the Employee is already looking at a list of
     * rentals and needs to select one to manually check in.
     * For the check-in use case, use removeRental(copy:RentedCopy):void
     * @param rentalIndex
     */
    abstract void removeRental(int rentalIndex) throws SQLException;



    /**
     * Removes a rented movie from the Customer's account.
     * Meant to be used when a RentedCopy object is already instantiated, such
     * as during the check in use case.
     * @param copy
     */
    abstract void removeRental(RentedCopy copy) throws SQLException;



    @Override
    void setName(String name) throws SQLException {
        if (name == null)
        {
            throw new NullPointerException("You must provide a name");
        }
        String table = "Customers_";
        String[] setToValue = {"name_ = \'?\'"};
        String condition = "customer_ID = " + accountID;
        String[] params = { name };
        VRSConnection conn = VRSConnection.getInstance();
        conn.update(table, setToValue, condition, params);
        this.name = name;
    }

    @Override
    /**
     * Sets a new address for the Customer in the database
     */
    void setAddress(String address) throws SQLException {
        if (address == null)
        {
            throw new NullPointerException("You must provide an address");
        }
        String table = "Customers_";
        String[] setToValue = {"address_ = \'?\'"};
        String condition = "customer_ID = " + accountID;
        String[] params = { address };
        VRSConnection conn = VRSConnection.getInstance();
        conn.update(table, setToValue, condition, params);
        this.address = address;
    }

    @Override
    /**
     * Sets a new postal code for the Customer in the database
     */
    void setPostalCode(String postalCode) throws SQLException {


        if (postalCode == null)
        {
            throw new NullPointerException("You must provide a postal code");
        }
        String table = "Customers_";
        String[] setToValue = {"postalCode = \'?\'"};
        String condition = "customer_ID = " + accountID;
        String[] params = { postalCode };
        VRSConnection conn = VRSConnection.getInstance();
        conn.update(table, setToValue, condition, params);
        this.postalCode = postalCode;
    }

    @Override
    /**
     * Sets a new phone number for the Customer in the database
     */
    void setPhoneNumber(String phoneNumber) throws SQLException {
        if (phoneNumber == null)
        {
            throw new NullPointerException("You must provide a phone number");
        }
        String table = "Customers_";
        String[] setToValue = {"phone_Number = \'?\'"};
        String condition = "customer_ID = " + accountID;
        String[] params = { phoneNumber };
        VRSConnection conn = VRSConnection.getInstance();
        conn.update(table, setToValue, condition, params);
        this.phoneNumber = phoneNumber;
    }

    @Override
    /**
     * Sets a new status for the Customer in the database
     */
    void setStatus(int status) throws SQLException {
        if (status != Account.ACTIVE && status != Account.INACTIVE)
        {
            throw new NullPointerException("You must provide a valid status");
        }
        String table = "Customers_";
        String[] setToValue = {"status_ = "+status};
        String condition = "customer_ID = " + accountID;
        VRSConnection conn = VRSConnection.getInstance();
        conn.update(table, setToValue, condition, null);
        this.status = status;
    }

    // package methods
    @Override
    /**
     * Adds the Customer to the database, provided that all fields are filled out
     * and the Customer is not already in the database
     * @return the new customer ID
     */
    int add() throws SQLException
    {
        if (accountID < 0 ||
                name == null ||
                address == null ||
                postalCode == null ||
                phoneNumber == null
        )
        {
            throw new NullPointerException("All fields must have values");
        }

        String table = "Customers_";
        String[] columns = {"name_", "address_", "postalCode_", "phoneNumber_",
            "status_", "fines_", "registration_Date"
        };
        String[] values = new String[columns.length];
        int stringIndex = 0;
        int intIndex = 4;
        while (stringIndex < intIndex)
        {
            values[stringIndex] = "\'?\'";
            stringIndex++;
        }
        values[intIndex++] = ""+status;
        values[intIndex++] = ""+fines;
        values[intIndex++] = "NOW()";

        String[] params = {name, address, postalCode, phoneNumber};


        VRSConnection conn = VRSConnection.getInstance();
        conn.insert(table, columns, values, params);

        Connection jdbcConn = conn.getConnection();
        String query = "SELECT MAX(customer_ID) FROM Customers_";
        Statement stat = jdbcConn.createStatement();
        ResultSet rs = stat.executeQuery(query);

        rs.next();
        int newID = rs.getInt(1);
        return newID;

    }

}
