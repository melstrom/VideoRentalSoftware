import java.util.GregorianCalendar;
import java.sql.SQLException;
import inventory. *;
public class TestMovies
{
	public static void main(String arg[]) throws SQLException
	{
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.set(1990, 11, 30);
		
		//System.out.println(calendar.get(calendar.MONTH));
		GeneralMovie movie = new GeneralMovie("a", "b", "c", "d" , calendar, "e");
		IndividualMovie movie1 = new IndividualMovie("f", 1, "g", "h", movie);
		//String set = movie1.getAll();
		//System.out.println(set);
		
		movie1.getAll();
	}
}