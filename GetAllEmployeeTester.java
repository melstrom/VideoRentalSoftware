/* sample output

$ java GetAllEmployeeTester
Employee first name: Homer
Employee first name: Ned

*/

import pos.SQLhelper;
import account.Employee;
import java.util.ArrayList;
/**
	Testing method (getAllEmployees() added to SQLhelper.java).
*/
public class GetAllEmployeeTester
{
	public static void main(String[] args)
	{
		SQLhelper myHelper = new SQLhelper();
		ArrayList<Employee> allOfThem = null;
		
		try
		{
			allOfThem = myHelper.getAllEmployees();
		}
		catch (Exception e) { System.out.println("some sql db connection error happened."); }
		
		int counter;
		
		for (counter = 0; counter < allOfThem.size(); counter++)
		{
			System.out.println("Employee first name: " + allOfThem.get(counter).getFname());
		}
		
	}
}
