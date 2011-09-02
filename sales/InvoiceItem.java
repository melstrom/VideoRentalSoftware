package sales;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 *
 * @author melstrom
 */
public interface InvoiceItem
{
    public String getName();
    public int getPrice()
            throws NotBoundException, MalformedURLException, RemoteException;
    public void checkout();
}
