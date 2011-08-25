package sales;

/**
 *
 * @author melstrom
 */
public class CheckOutControl
{
    private String paymentMethod; //Includes cash, debit, and credit
    private Invoice invoice;

    /**
     *
     * @param SKU
     */
    public void addItem (int SKU)
    {
        //inquire db about item. Is it a discount or an inventory item? Let it = item
        invoice.addItem(item);
    }

    /**
     *
     * @param amount
     */
    public void addFine (int amount)
    {
        //inquire db about fine
        invoice.addItem(Fine);
    }

    /**
     *
     */
    public void removeItem ()
    {
        invoice.removeItem();
    }

    /**
     *
     * @param customerID
     */
    public void createNewInvoice (int customerID)
    {
        Invoice invoice = new Invoice (customerID);
    }

    /**
     *
     * @param paymentMethod
     * @return
     */
    public int checkout (String paymentMethod, int paymentAmount)
    {
        if (paymentMethod.equals("Cash"))
        {
            int change; //placeholder initialization
            change = paymentAmount - invoice.getTotal();
            if (change <= 0)
            {
                //Throw not enough $ exception
            }
            return change;
        }
        return 0;
    }
}