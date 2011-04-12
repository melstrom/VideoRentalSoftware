/**
	The purpose of this program is to do unit testing of SQLhelper's method of inserting into the videoInfo table
*/
/* sample output

*/

import pos.SQLhelper;


public class Tester
{

	public static void main(String[] args) throws Exception
	{
		SQLhelper myHelper = new SQLhelper();
		myHelper.insertVideoInfoTable();
		
	}
}