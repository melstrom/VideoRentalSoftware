/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sales;

import java.util.ArrayList;

/**
 *
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

    public Invoice (int customerID)
    {

    }

    public int getSubtotal ()
    {
        return subtotal;
    }

    public int getTax ()
    {
        return tax;
    }

    public int getTotal ()
    {
        return total;
    }

    public ArrayList getInvoiceItems ()
    {
        return invoiceItems;
    }

    protected void checkOut ()
    {

    }

    protected void addItem ()
    {

    }

    protected void removeItem ()
    {

    }

    private void calculateSubtotal ()
    {

    }

    private void calculateTax ()
    {

    }

    private void calculateTotal ()
    {

    }

}
