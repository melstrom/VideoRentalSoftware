
/**
	A Credit/Debit card payment terminal. This is the machine the customer swipes their card in and then enters their pin.
	@author Peter
*/
public class CardPaymentMachine
{
	/**
		Creates a credit/debit card payment machine.
	*/
	public CardPaymentMachine()
	{//empty constructor, nothing to init
	}
	
	/**
		Verifies a payment.
		@param cardNumber the number on the credit card.
		@param amountInCents the amount of the payment in cents.
		@param pin the customer's pin, aka their password.
		@return true if the pin was verified and the account has enough money
	*/
	public Boolean verify(int cardNumber, int amountInCents, int pin)
	{
		return true;
	}
}