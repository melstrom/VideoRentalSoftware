package accounts;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import store.VRSConnection;

/**
 *
 * @author Mitch
 */
public final class Employee extends Account
{
    // instance fields
    private int position;

    // constants
    public static final int EMPLOYEE = 0;
    public static final int MANAGER = 1;

    // constructors

    /**
     * This constructor makes an Employee that is not known by the database.
     * After this object is constructed, it can be added to the database with the
     * add method.
     * @param name
     * @param address
     * @param postalCode
     * @param phoneNumber
     * @param status
     * @param position
     */
    public Employee(String name, String address, String postalCode,
            String phoneNumber, int position)
    {
        super(name, address, postalCode, phoneNumber);
        this.position = position;
    }



    Employee(int employeeID) throws SQLException
    {
        VRSConnection conn = VRSConnection.getInstance();
        String[] tables = { "Employees_" };
        String condition = "Employees_.employee_ID = " + employeeID;
        ResultSet rs = conn.select(tables, null, condition, null);
        if (!rs.next())
        {
            throw new SQLException("No matching record found for employee ID "
                    + employeeID);
        }
        int columnIndex = 2; // employee_ID is index 1, I want the next one
        name = rs.getString(columnIndex++);
        address = rs.getString(columnIndex++);
        postalCode = rs.getString(columnIndex++);
        phoneNumber = rs.getString(columnIndex++);
        status = rs.getInt(columnIndex++);
        position = rs.getInt(columnIndex++);
        accountID = employeeID;
    }

    // public methods

        public int getPosition()
    {
        return position;
    }


    // package methods
    @Override
    /**
     * Adds the information of the Employee to the database.
     * @pre the Employee must not already exist in the database (assigned an
     * employee_ID
     * @pre All fields must be filled in
     * @return the new employee ID
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

        String table = "Employees_";
        String[] columns = {"name_", "address_", "postalCode_", "phoneNumber_",
            "status_", "position_"
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
        values[intIndex++] = ""+position;
        
        String[] params = {name, address, postalCode, phoneNumber};


        VRSConnection conn = VRSConnection.getInstance();
        conn.insert(table, columns, values, params);

        Connection jdbcConn = conn.getConnection();
        String query = "SELECT MAX(employee_ID) FROM Employees_";
        Statement stat = jdbcConn.createStatement();
        ResultSet rs = stat.executeQuery(query);

        rs.next();
        int newID = rs.getInt(1);
        accountID = newID;
        return newID;

    }

    @Override
    /**
     * Sets a new name for the Employee in the database
     */
    void setName(String name) throws SQLException
    {

        if (name == null)
        {
            throw new NullPointerException("You must provide a name");
        }
        String table = "Employees_";
        String[] setToValue = {"name_ = \'?\'"};
        String condition = "employee_ID = " + accountID;
        String[] params = { name };
        VRSConnection conn = VRSConnection.getInstance();
        conn.update(table, setToValue, condition, params);
        this.name = name;
    }

    @Override
    /**
     * Sets a new address for the Employee in the database
     */
    void setAddress(String address) throws SQLException {
        if (address == null)
        {
            throw new NullPointerException("You must provide an address");
        }
        String table = "Employees_";
        String[] setToValue = {"address_ = \'?\'"};
        String condition = "employee_ID = " + accountID;
        String[] params = { address };
        VRSConnection conn = VRSConnection.getInstance();
        conn.update(table, setToValue, condition, params);
        this.address = address;
    }

    @Override
    /**
     * Sets a new postal code for the Employee in the database
     */
    void setPostalCode(String postalCode) throws SQLException {


        if (postalCode == null)
        {
            throw new NullPointerException("You must provide a postal code");
        }
        String table = "Employees_";
        String[] setToValue = {"postalCode = \'?\'"};
        String condition = "employee_ID = " + accountID;
        String[] params = { postalCode };
        VRSConnection conn = VRSConnection.getInstance();
        conn.update(table, setToValue, condition, params);
        this.postalCode = postalCode;
    }

    @Override
    /**
     * Sets a new phone number for the Employee in the database
     */
    void setPhoneNumber(String phoneNumber) throws SQLException {
        if (phoneNumber == null)
        {
            throw new NullPointerException("You must provide a phone number");
        }
        String table = "Employees_";
        String[] setToValue = {"phone_Number = \'?\'"};
        String condition = "employee_ID = " + accountID;
        String[] params = { phoneNumber };
        VRSConnection conn = VRSConnection.getInstance();
        conn.update(table, setToValue, condition, params);
        this.phoneNumber = phoneNumber;
    }

    @Override
    /**
     * Sets a new status for the Employee in the database
     */
    void setStatus(int status) throws SQLException {
        if (status != Account.ACTIVE && status != Account.INACTIVE)
        {
            throw new NullPointerException("You must provide a valid status");
        }
        String table = "Employees_";
        String[] setToValue = {"status_ = "+status};
        String condition = "employee_ID = " + accountID;
        VRSConnection conn = VRSConnection.getInstance();
        conn.update(table, setToValue, condition, null);
        this.status = status;
    }




    /**
     * Sets a new position for the Employee in the database
     */
    void setPosition(int position) throws SQLException
    {
        if (position != EMPLOYEE && position != MANAGER)
        {
            throw new NullPointerException("You must provide a valid position");
        }
        String table = "Employees_";
        String[] setToValue = {"position_ = "+position};
        String condition = "employee_ID = " + accountID;
        VRSConnection conn = VRSConnection.getInstance();
        conn.update(table, setToValue, condition, null);
        this.position = position;
    }

}
