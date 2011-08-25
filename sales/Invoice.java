package sales;

import java.util.ArrayList;
import accounts.AccountControl;
import accounts.Customer;

/**
 * The invoice class contains within itself all the information
 * needed to produce a complete invoice.
 *
 * It will also be in charge of communicating its information with the database
 * for tax and accounting purposes.
 * @author melstrom
 */
public class Invoice
{
    private ArrayList invoiceItems;
    private int subtotal;
    private int tax;
    private int total;
    private String paymentMethod;
    private Customer customer;
    private int totalRental;
    private int totalFines;
    private int totalDiscounts;
    private int totalRefunds;
    private int totalSales;

    /**
     * Constructor for the Invoice class
     * @param customerID The customerID entered by the employee required to
     * create a new invoice.
     */
    public Invoice (int customerID)
    {
        AccountControl ac = new AccountControl();
        customer = ac.getCustomer (int customerID);
    }

    /**
     *
     * @return Returns the subtotal of the current invoice
     */
    public int getSubtotal ()
    {
        return subtotal;
    }

    /**
     *
     * @return Returns the tax amount of the current invoice
     */
    public int getTax ()
    {
        return tax;
    }

    /**
     *
     * @return Returns the total of the current invoice
     */
    public int getTotal ()
    {
        return total;
    }

    /**
     *
     * @return Returns the list of the current items in the invoice
     */
    public ArrayList getInvoiceItems ()
    {
        return invoiceItems;
    }

    /**
     *
     */
    protected void checkOut ()
    {

    }

    /**
     *
     */
    protected void addItem (InvoiceItem item)
    {
        invoiceItems.add(item);
    }

    /**
     *
     */
    protected void removeItem ()
    {

    }

    /**
     *
     */
    private void calculateSubtotal (int amount)
    {
        subtotal =+ amount;
    }

    /**
     *
     */
    private void calculateTax (int amount)
    {
        tax =+ amount;
    }

    /**
     *
     */
    private void calculateTotal ()
    {
        total =+ total;
    }
}