package account;

/**
 * This exception indicates that the maximum number of accounts is reached
 * @author Mitch
 */
public class AccountLimitReachedException extends Exception{

    public AccountLimitReachedException(){}
    public AccountLimitReachedException(String msg)
    {
        super(msg);
    }
}
