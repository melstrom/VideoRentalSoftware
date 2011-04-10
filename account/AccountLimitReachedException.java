/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
