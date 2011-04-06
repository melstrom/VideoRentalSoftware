import java.util.GregorianCalendar;
import java.sql.SQLException;
import java.util.ArrayList;
//import inventory. *;
public class TestMovies
{
	public static void main(String arg[]) throws SQLException
	{
		
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.set(1990, 11, 30);
		
		//SKU, title, actors, director, releaseDate, synopsis
		GeneralMovie movie = new GeneralMovie("a", "b", "c", "d" , calendar, "e");
		//category, price, format, barcode
		IndividualMovie movie1 = new IndividualMovie("f", 1, "g", "h", movie);
		
		movie.getAll();
		movie1.getAll();
		System.out.println(movie1.getCategory()+"\n"
						+movie1.getBarcode()+"\n"
						+movie1.getFormat()+"\n"
						+movie1.getPrice());
		
		RentalMovie movie2 = new RentalMovie("i", "j", movie1);
		movie2.getAll();
		SaleMovie movie3 = new SaleMovie("k", "l", movie1);
		movie3.getAll();
		
		Reservation reserv = new Reservation(10, calendar);
		Reservation reserv1 = new Reservation(11, calendar);
		Reservation reserv2 = new Reservation(12, calendar);
		movie.reservationEnqueue(reserv);
		movie.reservationEnqueue(reserv1);
		movie.reservationEnqueue(reserv2);
		
		ArrayList list = movie.getReservations();
		System.out.println(list.size());
		Integer a = list.size()-1;
		Object[] elements = list.toArray();
		
		for(int i=0; i < elements.length ; i++)        
               System.out.println(elements[i]);
	       
	       System.out.println();
	       movie.reservationDequeue();
		
		for(int i=0; i < elements.length ; i++)        
               System.out.println(elements[i]);
	}
}