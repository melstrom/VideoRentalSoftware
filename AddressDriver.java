/**
	The purpose of this program is to do unit testing on the Address class; status: success.
	@author Peter
*/
/* sample output
$ java AddressDriver
The address A is: 123 49th Ave., Vancouver, B.C., Canada, V6V 2Y2
The number of rows in address table is: 6
INSERTING (123 49th Ave., Vancouver, B.C., Canada, V6V 2Y2)INTO THE DB NOW.
The number of rows in address table is: 7
The address B is: 123 49th Ave., Vancouver, B.C., Canada, V6V 2Y2
The address B is now: 123 49th Ave., Vancouver, B.C., USA, V6V 2Y2
$ 



// after inserting into the db, here is the info in the address table

mysql> select * from address;

+-----------+-------------+------------+-----------+----------+---------+------------+
| addressID | houseNumber | streetName | city      | province | country | postalCode |
+-----------+-------------+------------+-----------+----------+---------+------------+
|         1 |         123 | Main       | Vancouver | B.C.     | Canada  | V6V 1V1    | 
|         2 |         666 | Fraser St  | Vancouver | B.C.     | Canada  | V6V 2V2    | 
|         3 |         432 | Knight St  | Vancouver | B.C.     | Canada  | V5V 2V2    | 
|         4 |         123 | 49th Ave.  | Vancouver | B.C.     | V6V 2Y2 | V6V 2Y2    | 
|         5 |         123 | 49th Ave.  | Vancouver | B.C.     | Canada  | V6V 2Y2    | 
|         6 |         123 | 49th Ave.  | Vancouver | B.C.     | USA     | V6V 2Y2    | 
|         7 |         123 | 49th Ave.  | Vancouver | B.C.     | USA     | V6V 2Y2    | 
+-----------+-------------+------------+-----------+----------+---------+------------+
7 rows in set (0.53 sec)

*/
import account.Address;
import pos.SQLhelper;
import java.lang.ClassNotFoundException;
import java.sql.SQLException;

public class AddressDriver
{
	public static void main(String[] args) throws Throwable
	{
		Address addressA = null;
		Address addressB = null;
		SQLhelper mySQLhelper = new SQLhelper();
		
		int addressID = 0;
		int houseNumber = 123;
		String streetName = "49th Ave.";
		String city = "Vancouver";
		String province = "B.C.";
		String country = "Canada";
		String postalCode = "V6V 2Y2";
		
		addressA = new Address(houseNumber, streetName, city, province, country, postalCode);
		System.out.println("The address A is: " + addressA);
		
		// try inserting info into database
		System.out.println("The number of rows in address table is: " + mySQLhelper.getTotalNumberOfRows("address", "addressID"));
		System.out.println("INSERTING (" + addressA + ")INTO THE DB NOW.");
		try
		{
			addressID = addressA.saveInDB();
		}
		catch (Exception e) { System.out.println("Error happened."); }
		System.out.println("The number of rows in address table is: " + mySQLhelper.getTotalNumberOfRows("address", "addressID"));
		
		
		
		
		// try to get a Address object from its primary key
		try
		{
			addressB = mySQLhelper.getAddress(addressID);
		}
		catch (Exception e) { System.out.println("Error happened."); }
		
		System.out.println("The address B is: " + addressB);
		
		// trying updating info
		addressB.updateCountry("USA");
		
		System.out.println("The address B is now: " + addressB);
		
	}
}
