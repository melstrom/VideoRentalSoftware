package pos;
/**
*	Penalty calculation class
*	
*	@auther mattp
*	@version 1.0 March 22, 2011
*	@version 1.1 
*		-changed setPenaltyPerDay() so it takes a double now
*		-removed getName(), getTitle() and redundant stuff like that 		
*		-fixed getPrice() so it returns a value in cents
*/
import inventory.RentalMovie;
public class Penalty
{
	/**
                Constructor with 2 parameters
		Initialize a penalty 
		@param numberOfDays the number of days the input movie is overdue
		@param movie the movie that is overdue
	*/
	public Penalty(int numberOfDays, RentalMovie movie)
	{
		this.numberOfDays = numberOfDays;
		this.movie = movie;
		setPenaltyPerDay(1);
		calculate();
	}
	/**
		Set penalty per day 
		@param penaltyPerDay 
	*/
	private void setPenaltyPerDay(double penaltyPerDay)
	{
		this.penaltyPerDay = penaltyPerDay;
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
		Gets overdue fee in cents of this item
		@return the price of this item in cents.
	*/	
	public int getPrice()
	{
		return (int)(getOverdueFee()*100);
	}

	private RentalMovie movie;
	private double overdueFee;
	private int numberOfDays;
	private double penaltyPerDay;
}