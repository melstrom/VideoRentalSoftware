package pos;

/**
	this is a temporary stub object so that unit testing of TransactionManager can be done.
	@author Peter
*/

public class TempInventoryItem implements TransactionItem
{
	public String getType() { return "video rental"; }
	public String getName() { return "Batman"; }
	public int getPrice() { return 320; }
	public String getBarcode() { return "00101023"; }
	public boolean updateItemInfoAtCheckOut(int invoiceID)
	{
		// do sql insert/update stuff here for the item when the item is checked out
		// UPDATE table SET attrib=value WHERE attrib=constriant;
		return true;
	}
}
