package pos;

/**
	An object representing a payment.
*/

public class Payment
{
	//private int requiredAmount;
	private int collectedAmount;
	private String paymentMethod;
	private boolean verified;
//	public boolean isVerified()
//	public void setVerified()
	//@param requiredAmount the amount required in cents for the transaction.
	
	/**
		Constructs a Cash payment with attributes required and collected payment.
		
		@param collectedAmount the amount the customer is paying in cents.
		@param paymentMethod the method the customer is paying with.
	*/
	public Payment(int collectedAmount, String paymentMethod)
	{
		this.collectedAmount = collectedAmount;
		this.paymentMethod = paymentMethod;
	}
	
	/**
		Gets the amount the payment was in cents.
		@return the amount in cents.
	*/
	public int getAmount()
	{
		return collectedAmount;
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
