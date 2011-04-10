
/**
	A Credit/Debit card payment terminal. This is the machine the customer swipes their card in and then enters their pin.
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
	*/
	public Boolean verify(int cardNumber, int amountInCents, int pin)
	{
		return true;
	}
}
