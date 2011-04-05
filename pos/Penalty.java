package pos;
/**
*	Penalty calculation class
*	
*	@auther mattp
*	@version 1.0 March 22, 2011
*/
public class Penalty implements TransactionItem
{
	/**
                Constructor with 2 parameters
		Initialize a penalty record
		@param numberOfDays the number of days an item is overdue
		@param movie the movie that is overdue
	*/
	public Penalty(int numberOfDays, RentalMovie movie)
	{
		setPenaltyPerDay(movie.getMediaType());
		this.numberOfDays = numberOfDays;
		calculate();
		movieName = movie.getTitle();
	}
	/**
		Set penalty per day according to media type
		@param mediaType the media type of the item
	*/
	private void setPenaltyPerDay(char mediaType)
	{
		switch(mediaType){
			case 'd': penaltyPerDay = 1; break;
			case 'v': penaltyPerDay = 1;	break;
			case 'b': penaltyPerDay = 1; break;
			default:	penaltyPerDay = 1; break;
		}
	}
	/**
		Get overdue fee
		@return overdueFee the total fee that is overdue
	*/
	public double getOverdueFee()
	{
		return overdueFee;
	}
	/**
		Calculate total overdue fees
	*/
	private void calculate()
	{
		overdueFee = penaltyPerDay * numberOfDays;
	}
	
	/**
		Gets the price in cents of this item (aka the over due fee amount)
		@return the price of this item in cents.
	*/	
	public int getPrice()
	{
		return (int)getOverdueFee();
	}
	/**
		Gets the name of the movie this penalty is for.
		@return the name of the movie this penalty is for
	*/
	public String getName()
	{
		return movieName;
	}
	/**
		Gets the type the item is (example: Promo, Rental movie, Penalty, SaleMovie).
		@return the type this item is (Penalty).
	*/
	public String getType()
	{
		return type;
	}
	
	private String type = "Penalty";
	private String movieName;
	private double overdueFee;
	private int numberOfDays;
	private double penaltyPerDay;
}