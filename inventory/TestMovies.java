import java.util.GregorianCalendar;
import java.sql.SQLException;
import java.util.ArrayList;
//import inventory. *;
public class TestMovies
{
	public static void main(String arg[]) throws SQLException
	{
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.set(2011, 2, 31);
	
		//SKU, title, actors, director, releaseDate, synopsis
		GeneralMovie movie = new GeneralMovie("a", "b", "c", "d" , calendar, "e");
		//category, price, format, barcode
		IndividualMovie movie1 = new IndividualMovie("f", 1, "g", "h", movie);
		
		String attributes = movie.getAll();
		
		System.out.println("[1] "+attributes);
		
		attributes = movie1.getAll();
		
		System.out.println("[2] "+attributes);
		System.out.println("[3] "+movie1.getCategory()+" "
						+movie1.getBarcode()+" "
						+movie1.getFormat()+" "
						+movie1.getPrice());
		
		RentalMovie movie2 = new RentalMovie("i", "j", movie1);
		
		System.out.println("[3.1]"+movie2.getCondition());
		SaleMovie movie3 = new SaleMovie("k", "l", movie1);
		System.out.println("[3.2]"+movie3.getCondition());
		attributes = movie2.getAll();
		System.out.println("[4] "+attributes);
		attributes = movie3.getAll();
		System.out.println("[5] "+attributes);
		Reservation reserv = new Reservation(10, calendar);
		Reservation reserv1 = new Reservation(11, calendar);
		Reservation reserv2 = new Reservation(12, calendar);
		movie.reservationEnqueue(reserv);
		movie.reservationEnqueue(reserv1);
		movie.reservationEnqueue(reserv2);
		
		ArrayList list = movie.getReservations();
		System.out.println("[6]" +list.size());
		Integer a = list.size()-1;
		Object[] elements = list.toArray();
		
		Reservation r  = (Reservation)elements[0]; 
	       int num = r.getAccountID();
	       int date = r.getDate().get(r.getDate().DATE);
	       int month = r.getDate().get(r.getDate().MONTH);
		
		for(int i=0; i < elements.length ; i++)        
               System.out.println(elements[i]);
		
		movie.reservationDequeue();

	       list = movie.getReservations();
		Object[] elements1 = list.toArray();
	       
		GregorianCalendar calendar1 = new GregorianCalendar();
		calendar1.set(1999, 5, 7);
	       
		r.setAccountID(99);

		num = r.getAccountID();
		int year = r.getDate().get(r.getDate().YEAR);
		date = r.getDate().get(r.getDate().DATE);
		System.out.println("+++"+num+" "+date +" "+month+" "+year);
		for(int i=0; i < elements1.length ; i++)        
               System.out.println(elements1[i]);
	}

}