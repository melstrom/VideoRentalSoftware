// temp stub class made by peter to test Transaction class

public class TransactionItem
{
	private String type;
	private String name;
	private int price;
	
	public TransactionItem(String type, String name, int price)
	{
		this.type = type;
		this.name = name;
		this.price = price;
	}
	
	public String getType()
	{
		return type;
	}
	public String getName()
	{
		return name;
	}
	public int getPrice() // price in cents
	{
		return price;
	}
	public String toString()
	{
		return "Type: " + type + ", Name: " + name + ", Price: " + price + ".";
	}
	
}
