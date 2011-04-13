package reports;

import java.util.Date;
import java.sql.*;
import java.util.ArrayList;

/**
  This is an entity object of the ReportPackage.
  It does not provide any setter operations, instead, every attribute is set up
  during the construction of this object.
 
  The construction will access the invoice and item table in the database.
  The construction has four part of operation.
 
  1) collect every invoice in the invoice table with the time period specified.
  The invoicID  and tax rate of these records will be stored into different
  arraylists based on their payment method (by cash, by debit, or by credit).
 
  2) With the arraylist of invoiceID and tax rate for payment method cash, access
  the item table, and find out the items that is associate with the invoiceID.
  Based on the content in the type attribute, the corresponding attribute of this
  object will change. Eg, if an item record is:
  +--------+---------------+-------+--------+---------+----------+----------+-----------+
  | itemID | type          | price | SaleID | promoID | catagory | rentalID | invoiceID |
  +--------+---------------+-------+--------+---------+----------+----------+-----------+
  |    001 | rental        |  100  |   NULL |  NULL   | NULL     |   011111 |       111 |
  +--------+---------------+-------+--------+---------+----------+----------+-----------+
  The relative attribute of this object rentalIncome, incomeTax, cashCredit will
  be incremented.
 
  3) Same as 2) for payment method debit.
 
  4) Same as 2) for payment method credit.
 
  The code is getting a bit messy, but I hope the documentation inside the code
  body helps understanding.
  
  @author kevin
 */
public class SaleReport
{
    /**
     *  This is the constructor of this class. Every attribute of this class is
     * set here. As mentioned above, there will be no setter methods.
     *
     * @post all the attributes will be set.
     * @param Date startDate the starting date of the report.
     * @param Date endDate the ending date of the report.
     * @throws SQLException database error.
     */
    public SaleReport(Date startDate, Date endDate)
            throws SQLException
    {
        this.startDate = startDate;
        this.endDate = endDate;

        JDBCConnection conn = new JDBCConnection();
        conn.getConnction();
        try
        {
            PreparedStatement stat = conn.prepareStatement("SELECT * FROM invoice"
                    + "WHERE dateTime >= " + startDate.toString() + " AND "
                    + "dateTime <= " + endDate.toString() + ";");
            boolean hasResult = stat.execute();
            if(hasResult)
            {
                ResultSet results = stat.getResultSet();

                //prepare the arraylist to store all the invoices that is paid by
                //each payment method
                ArrayList<String> cashInvoiceIDs = new ArrayList<String>();
                ArrayList<String> debitInvoiceIDs = new ArrayList<String>();
                ArrayList<String> creditInvoiceIDs = new ArrayList<String>();

                //Stroing the tax rate in arraylist at here will be more efficient
                //than going back to invoice table every time.
                ArrayList<String> cashInvoiceTaxRate = new ArrayList<String>();
                ArrayList<String> debitInvoiceTaxRate = new ArrayList<String>();
                ArrayList<String> creditInvoiceTaxRate = new ArrayList<String>();

                while(results.next())
                {
                    if(results.getString(2).trim().toLowerCase().equals("cash"))
                    {
                        cashInvoiceIDs.add(results.getString(1));
                            //storing the invoiceID that is paid by cash.
                        cashInvoiceTaxRate.add(results.getString(7));
                            //storing the tax rate of this invoice.
                    }
                    else if(results.getString(2).trim().toLowerCase().equals("debit"))
                    {
                        debitInvoiceIDs.add(results.getString(1));
                        debitInvoiceTaxRate.add(results.getString(7));
                    }
                    else
                    {
                        creditInvoiceIDs.add(results.getString(1));
                        creditInvoiceTaxRate.add(results.getString(7));
                    }
                }

                //With the invoiceIDs for different payment method, and the tax
                //rate of each of them, analyze each record in the item table.
                analyzeCashInvoices(cashInvoiceIDs, cashInvoiceTaxRate);
                analyzeDebitInvoices(debitInvoiceIDs, debitInvoiceTaxRate);
                analyzeCreditInvoices(creditInvoiceIDs, creditInvoiceTaxRate);

                //finally, when all the invoices are analyzed, update the totalIncome
                //and disRefTotal. (The incomeTax and returnedTax is analyzed in
                //the ananlyze<>() method.)
                totalIncome = rentalsIncome + salesIncome + penalty;
                discRefTotal = refund + discount;
                stat.close();
            }
        }
        finally
        {
            conn.close();
        }
    }

    /**
     * With the invoiceID and the tax rate of each invoice provided, this method
     * will update the value of rentalsIncome, salesIncome, penalty, incomeTax,
     * discount, refund, returnedTax, and at the same time, cashCredit and
     * cashDebit.
     * @post the attributes mentioned above will be updated.
     * @param ArrayList<String> cashInvoiceIDs all the invoiceIDs that is paid by
     * cash
     * @param ArrayList<String> cashInvoiceTaxRate the tax rates for each invoice
     * that is paid by cash
     * @throws SQLException database error.
     */
    private void analyzeCashInvoices(ArrayList<String> cashInvoiceIDs,
            ArrayList<String> cashInvoiceTaxRate)
            throws SQLException
    {
        JDBCConnection conn = new JDBCConnection();
        conn.getConnection();
        try
        {
            //prepare the SQL command
            String command = "SELCT * FROM item WHERE invoiceID = ";
            int i = 0;
            for(; i < cashInvoiceIDs.size()-1; i++)
                command += cashInvoiceIDs.get(i) + " OR invoiceID = ";
            command += cashInvoiceIDs.get(++i) + ";";
            //execute it.
            PreparedStatement stat = conn.prepareStatement(command);
            boolean hasResult = stat.execute();
            if(hasResult)
            {
                ResultSet results = stat.getResultSet();

                while(results.next())
                {
                    if(results.getString(2).trim().toLowerCase().equals("return"))
                        //the second field of the item table is the "type" attribute
                        //the "type" attribute indicate the what kind of transaction item
                        //it is.
                    {
                        //refund will be incremented by the price of this item
                        this.refund += results.getInt(3);
                            // the third field of the item table is the price of the item
                        //cashDebit will be incremented, becasue the amount of money
                        //is given back to the customer by cash.
                        this.cashDebit += results.getInt(3);
                        //The tax that the customer paid before also needed to
                        //be returned to the customer.
                        this.returnedTax += results.getInt(3) * (Integer.parseInt(cashInvoiceTaxRate.get(cashInvoiceIDs.indexOf(results.getString(8))))/100);
                            //the 8th field of the item table is the invoiceID. I need to get the invoiceID to find the index of the tax rate inside the arraylist
                            //cashInvoiceTaxRate. And because I can't create ArrayList<int>, I use ArrayList<String> instead. As a result, I need to parse it into
                            //int again.
                    }
                    else if(results.getString(2).trim().toLowerCase().equals("penalty"))
                    {
                        this.penalty += results.getInt(3);
                        this.cashCredit += results.getInt(3);
                        this.incomeTax += results.getInt(3) * (Integer.parseInt(cashInvoiceTaxRate.get(cashInvoiceIDs.indexOf(results.getString(8))))/100);
                    }
                    else if(results.getString(2).trim().toLowerCase().equals("promo"))
                    {
                        this.discount += results.getInt(3);
                        this.cashDebit += results.getInt(3);
                    }
                    else if(results.getString(2).trim().toLowerCase().equals("rental"))
                    {
                        this.rentalsIncome += results.getInt(3);
                        this.cashCredit += results.getInt(3);
                        this.incomeTax += results.getInt(3) * (Integer.parseInt(cashInvoiceTaxRate.get(cashInvoiceIDs.indexOf(results.getString(8))))/100);
                    }
                    else if(results.getString(2).trim().toLowerCase().equals("sale"))
                    {
                        this.salesIncome += results.getInt(3);
                        this.cashCredit += results.getInt(3);
                        this.incomeTax += results.getInt(3) * (Integer.parseInt(cashInvoiceTaxRate.get(cashInvoiceIDs.indexOf(results.getString(8))))/100);
                    }
                }
            }
            stat.close();
        }
        finally
        {
            conn.close();
        }
    }

    /**
     * This method use the same mechanism of
     * analyzeCashInvoices(ArrayList<String> cashInvoiceIDs,ArrayList<String> cashInvoiceTaxRate),
     * but it is for payment method debit.
     * @post rentalsIncome, salesIncome, penalty, incomeTax, discount, refund,
     * returnedTax, and debitCredit, debitDebit will be updated.
     * @param ArrayList<String> debitInvoiceIDs all the invoiceIDs that is paid by
     * debit
     * @param ArrayList<String> debitInvoiceTaxRate the tax rates for each invoice
     * that is paid by debit
     * @throws SQLException database error.
     */
    private void analyzeDebitInvoices(ArrayList<String> debitInvoiceIDs,
            ArrayList<String> debitInvoiceTaxRate)
            throws SQLException
    {
        JDBCConnection conn = new JDBCConnection();
        conn.getConnection();
        try
        {
            String command = "SELCT * FROM item WHERE invoiceID = ";
            int i = 0;
            for(; i < debitInvoiceIDs.size()-1; i++)
                command += debitInvoiceIDs.get(i) + " OR invoiceID = ";
            command += debitInvoiceIDs.get(++i) + ";";
            PreparedStatement stat = conn.prepareStatement(command);
            boolean hasResult = stat.execute();
            if(hasResult)
            {
                ResultSet results = stat.getResultSet();

                while(results.next())
                {
                    if(results.getString(2).trim().toLowerCase().equals("return"))
                    {
                        this.refund += results.getInt(3);
                        this.debitDebit += results.getInt(3);
                        this.returnedTax += results.getInt(3) * (Integer.parseInt(debitInvoiceTaxRate.get(debitInvoiceIDs.indexOf(results.getString(8))))/100);
                    }
                    else if(results.getString(2).trim().toLowerCase().equals("penalty"))
                    {
                        this.penalty += results.getInt(3);
                        this.debitCredit += results.getInt(3);
                        this.incomeTax += results.getInt(3) * (Integer.parseInt(debitInvoiceTaxRate.get(debitInvoiceIDs.indexOf(results.getString(8))))/100);
                    }
                    else if(results.getString(2).trim().toLowerCase().equals("promo"))
                    {
                        this.discount += results.getInt(3);
                        this.debitDebit += results.getInt(3);
                    }
                    else if(results.getString(2).trim().toLowerCase().equals("rental"))
                    {
                        this.rentalsIncome += results.getInt(3);
                        this.debitCredit += results.getInt(3);
                        this.incomeTax += results.getInt(3) * (Integer.parseInt(debitInvoiceTaxRate.get(debitInvoiceIDs.indexOf(results.getString(8))))/100);
                    }
                    else if(results.getString(2).trim().toLowerCase().equals("sale"))
                    {
                        this.salesIncome += results.getInt(3);
                        this.debitCredit += results.getInt(3);
                        this.incomeTax += results.getInt(3) * (Integer.parseInt(debitInvoiceTaxRate.get(debitInvoiceIDs.indexOf(results.getString(8))))/100);
                    }
                }
            }
            stat.close();
        }
        finally
        {
            conn.close();
        }
    }

    /**
     * This method use the same mechanism of
     * analyzeCashInvoices(ArrayList<String> cashInvoiceIDs,ArrayList<String> cashInvoiceTaxRate),
     * but it is for payment method debit.
     * @post rentalsIncome, salesIncome, penalty, incomeTax, discount, refund,
     * returnedTax, and creditCredit, creditDebit will be updated.
     * @param ArrayList<String> creditInvoiceIDs all the invoiceIDs that is paid by
     * credit
     * @param ArrayList<String> creditInvoiceTaxRate the tax rates for each invoice
     * that is paid by credit
     * @throws SQLException database error.
     */
    private void analyzeCreditInvoices(ArrayList<String> creditInvoiceIDs,
            ArrayList<String> creditInvoiceTaxRate)
            throws SQLException
    {
        JDBCConnection conn = new JDBCConnection();
        conn.getConnection();
        try
        {
            String command = "SELCT * FROM item WHERE invoiceID = ";
            int i = 0;
            for(; i < creditInvoiceIDs.size()-1; i++)
                command += creditInvoiceIDs.get(i) + " OR invoiceID = ";
            command += creditInvoiceIDs.get(++i) + ";";
            PreparedStatement stat = conn.prepareStatement(command);
            boolean hasResult = stat.execute();
            if(hasResult)
            {
                ResultSet results = stat.getResultSet();

                while(results.next())
                {
                    if(results.getString(2).trim().toLowerCase().equals("return"))
                    {
                        this.refund += results.getInt(3);
                        this.creditDebit += results.getInt(3);
                        this.returnedTax += results.getInt(3) * (Integer.parseInt(creditInvoiceTaxRate.get(creditInvoiceIDs.indexOf(results.getString(8))))/100);
                    }
                    else if(results.getString(2).trim().toLowerCase().equals("penalty"))
                    {
                        this.penalty += results.getInt(3);
                        this.creditCredit += results.getInt(3);
                        this.incomeTax += results.getInt(3) * (Integer.parseInt(creditInvoiceTaxRate.get(creditInvoiceIDs.indexOf(results.getString(8))))/100);
                    }
                    else if(results.getString(2).trim().toLowerCase().equals("promo"))
                    {
                        this.discount += results.getInt(3);
                        this.creditDebit += results.getInt(3);
                    }
                    else if(results.getString(2).trim().toLowerCase().equals("rental"))
                    {
                        this.rentalsIncome += results.getInt(3);
                        this.creditCredit += results.getInt(3);
                        this.incomeTax += results.getInt(3) * (Integer.parseInt(creditInvoiceTaxRate.get(creditInvoiceIDs.indexOf(results.getString(8))))/100);
                    }
                    else if(results.getString(2).trim().toLowerCase().equals("sale"))
                    {
                        this.salesIncome += results.getInt(3);
                        this.creditCredit += results.getInt(3);
                        this.incomeTax += results.getInt(3) * (Integer.parseInt(creditInvoiceTaxRate.get(creditInvoiceIDs.indexOf(results.getString(8))))/100);
                    }
                }
            }
            stat.close();
        }
        finally
        {
            conn.close();
        }
    }

    /**
     * Ordinary getter method for startDate
     * @return Date the starting date of the report object
     */
    public Date getStartDate()
    {
        return this.startDate;
    }

    /**
     * Ordinary getter method for endDate
     * @return Date the ending date of the report object
     */
    public Date getEndDate()
    {
        return this.endDate;
    }

    /**
     * Ordinary getter method for rentalsIncome
     * @return double the rental income
     */
    public double getRentalsIncome()
    {
        return ((double)this.rentalsIncome)/100;
    }

    /**
     * Ordinary getter method for salesIncome
     * @return double the sales income
     */
    public double getSalesIncome()
    {
        return ((double)this.salesIncome)/100;
    }

    /**
     * Ordinary getter method for penalty
     * @return double the penalty
     */
    public double getPenalty()
    {
        return ((double)this.penalty)/100;
    }

    /**
     * Ordinary getter method for totalIncome
     * @return double the total income from rentals, sales, and penalty during the
     * timer period of the report.
     */
    public double getTotalIncome()
    {
        return ((double)this.totalIncome)/100;
    }

    /**
     * Ordinary getter method for incomeTax
     * @return the total income that collect from rentals, sales, and penalty.
     */
    public double getIncomeTax()
    {
        return ((double)this.incomeTax)/100;
    }

    /**
     * Ordinary getter method for discount
     * @return double the amount of discount that given to the customer during the
     * timer period of the report.
     */
    public double getDiscount()
    {
        return ((double)this.discount)/100;
    }

    /**
     * Ordinary getter method for refund
     * @return double the total amount of money of refunds during the timer period
     * of the report.
     */
    public double getRefund()
    {
        return ((double)this.refund)/100;
    }

    /**
     * Ordinary getter method for discRefTotal
     * @return double the total amount of money that should be deducted from
     * totalIncome
     */
    public double getDiscRefTotal()
    {
        return ((double)this.discRefTotal)/100;
    }

    /**
     * Ordinary getter method for returnedTax
     * @return double the tax that returned to the customer when a refund takes
     * place.
     */
    public double getReturnTax()
    {
        return ((double)this.returnedTax)/100;
    }

    /**
     * Ordinary getter method for cashCredit
     * @return double how much cash the store receive from rentals, sales, and
     * penalty.
     */
    public double getCashCredit()
    {
        return ((double)this.cashCredit)/100;
    }

    /**
     * Ordinary getter method for cashDebit
     * @return double how much cash the store returned to the customer from
     * discounts/promo and refund.
     */
    public double getCashDebit()
    {
        return ((double)this.cashDebit)/100;
    }

    /**
     * Ordinary getter method for debitCredit
     * @return double how much debit the store receive from the rentals, sales, and
     * penalty.
     */
    public double getDebitCredit()
    {
        return ((double)this.debitCredit)/100;
    }

    /**
     * Ordinary getter method for debitDebit
     * @return double how much money the store returnedto the customer from
     * discount/promo and refund via debit
     */
    public double getDebitDebit()
    {
        return ((double)this.debitDebit)/100;
    }

    /**
     * Ordinary getter method for creditCredit
     * @return double how much money the store receive from rentals, sales, and
     * penalty via credit.
     */
    public double getCreditCredit()
    {
        return ((double)this.creditCredit)/100;
    }

    /**
     * Ordinary getter method for creditDebit
     * @return double how much money the store return to the customer via credit.
     */
    public double getCreditDebit()
    {
        return ((double)this.creditDebit)/100;
    }

    /**
     * The revenue is the incomeTotal - discRefTotal. It is the amount of money
     * that the store should get during the time period of the report.Double.parseDouble(this.rentalsIncome)/100
     * @return double the revenue
     */
    public double getRevenue()
    {
        return ((double)(totalIncome - discRefTotal))/100;
    }

    /**
     * The total amount of tax the store should pay the government. It is
     * incomeTax - returnedTax
     * @return double the actual amount of tax should pay the government
     */
    public double getTaxShouldPay()
    {
        return ((double)(incomeTax - returnedTax))/100;
    }

    /**
     * The balance of cash that the store earn during the time period of the
     * report. It is cashCredit - cashDebit.
     * @return double the balance of cash.
     */
    public double getCashBalance()
    {
        return ((double)(cashCredit - cashDebit))/100;
    }

    /**
     * The balance of money that the store earn via debit during the time period
     * of the report. It is debitCredit - debitDebit.
     * @return double the balance of debit
     */
    public double getDebitBalance()
    {
        return ((double)(debitCredit - debitDebit))/100;
    }

    /**
     * The balance of money that store earn via credit during the time period of
     * the report. It is creditCredit - creditDebit.
     * @return double the balance of credit.
     */
    public double getCreditBalance()
    {
        return ((double)(creditCredit - creditDebit))/100;
    }

    /**
     * The total balance of total amount of money the store has got. It is calculated
     * from cashCredit, cashDebit, debitCredit, debitDebit, creditCredit, and
     * creditDebit. In general this number should be similar to the value returned
     * from getRevenue()
     * @return double the balance of every payment method.
     */
    public double getTotalBalance()
    {
        return ((double)(cashCredit -cashDebit + debitCredit - debitDebit + creditCredit -
                creditDebit))/100;
    }

    private Date startDate;
    private Date endDate;
    private int rentalsIncome = 0;
    private int salesIncome = 0;
    private int penalty = 0;
    private int totalIncome = 0;
    private int incomeTax = 0;
    private int discount = 0;
    private int refund = 0;
    private int discRefTotal = 0;
    private int returnedTax = 0;
    private int cashCredit = 0;
    private int cashDebit = 0;
    private int debitCredit = 0;
    private int debitDebit = 0;
    private int creditCredit = 0;
    private int creditDebit = 0;
}
