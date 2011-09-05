
package accounts;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import store.VRSConnection;

/**
 * This class encapsulates all the functions for organizing employees.
 * 
 * @author Mitch
 */
public class EmployeeControl 
{
    // instance field
    Employee employee;
    
    // methods
    
    /**
     * Gets the current employee
     * @return 
     */
    public Employee getEmployee()
    {
        return employee;
    }
    
    
    
    /**
     * Sets the employee to the one with the provided ID and returns it.
     * @param empID the unique employee ID number
     * @return the Employee object
     * @throws SQLException 
     */
    public Employee getEmployee(int empID) throws SQLException
    {
        return new Employee(empID);
    }
    
    
    
    /**
     * Sets the employee to the existing provided employee.
     * @param emp 
     */
    public void setEmployee(Employee emp)
    {
        employee = emp;
    }
    
    
    
    /**
     * Creates a new Employee and returns the object.
     * @param name
     * @param address
     * @param postalCode
     * @param phoneNumber
     * @param position
     * @return
     * @throws SQLException 
     */
    public Employee createEmployee(String name, String address, String postalCode,
            String phoneNumber, int position) throws SQLException
    {
        Employee emp = new Employee(name, address, postalCode, phoneNumber, 
                position);
        return emp;
    }
    
    
    
    /**
     * Edits the current employee to reflect the new data.  No field may be null.
     * @param name
     * @param address
     * @param postalCode
     * @param phoneNmber
     * @param position
     * @return the updated employee
     * @throws SQLException 
     */
    public Employee updateEmployee(String name, String address, String postalCode,
            String phoneNumber, int status, int position) throws SQLException
    {
        boolean hasNull = name == null || address == null || 
                postalCode == null || phoneNumber == null;
        boolean hasBlank = name.length() < 1 || address.length() < 1 ||
                postalCode.length() < 1 || phoneNumber.length() < 1;
        boolean validStatus = status == Account.ACTIVE || status == Account.INACTIVE;
        boolean validPosition = position == Employee.EMPLOYEE ||
                position == Employee.MANAGER;
        if (hasNull || hasBlank || !validStatus || !validPosition)
        {
            throw new IllegalArgumentException("No blank fields allowed");
        }
        if (employee == null)
        {
            throw new NullPointerException("No employee is currently set");
        }
        
        employee.setName(name);
        employee.setAddress(address);
        employee.setPostalCode(postalCode);
        employee.setPhoneNumber(phoneNumber);
        employee.setStatus(status);
        employee.setPosition(position);
        
        return employee;
    }
    
    
    /**
     * Gets a list of all Employees in the store.  If there are no Employees,
     * it returns null.
     * @return
     * @throws SQLException 
     */
    public ArrayList<Employee> listEmployees() throws SQLException
    {
        ArrayList<Employee> emps = new ArrayList<Employee>();
        String table = "Employees_";
        VRSConnection conn = VRSConnection.getInstance();
        ResultSet rs = conn.select(table, null, null, null);
        
        if (!rs.next())
        {
            return null;
        }
        
        do 
        {
            int paramIndex = 1;
            int empID = rs.getInt(paramIndex++);
            String name = rs.getString(paramIndex++);
            String address = rs.getString(paramIndex++);
            String postalCode = rs.getString(paramIndex++);
            String phoneNum = rs.getString(paramIndex++);
            int status = rs.getInt(paramIndex++);
            int position = rs.getInt(paramIndex++);
            Employee tempEmp = new Employee(empID, name, address, postalCode,
                    phoneNum, status, position);
            emps.add(tempEmp);
        }while (rs.next());
        
        return emps;
    }
    
}
