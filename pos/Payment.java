package pos;

/**
	An object representing a payment.
*/

public class Payment
{
	private int amount;
	private String paymentMethod;
//	boolean verified;
//	public boolean isVerified()
//	public void setVerified()
	
	/**
		Constructs a payment with attributes amount and paymentMethod.
		@param amount the amount the customer is paying in cents (example: with cash, a $20 would be 2000 and with CC or debit it is exact amount in cents).
		@param paymentMethod the method the customer is paying with.
	*/
	public Payment(int amount, String paymentMethod)
	{
		this.amount = amount;
		this.paymentMethod = paymentMethod;
	}
	
	/**
		Gets the amount the payment was in cents.
		@return the amount in cents.
	*/
	public int getAmount()
	{
		return amount;
	}
	
	/**
		Gets the payment method.
		@return the payment method as a string.
	*/
	public String getPaymentMethod()
	{
		return paymentMethod;
	}


}
