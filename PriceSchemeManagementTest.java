/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import pos.PriceSchemeManagement;
import java.util.*;
/**
 *
 * @author xliu01
 */
public class PriceSchemeManagementTest
{
    public static void main(String[] args)
            throws Exception
    {
        PriceSchemeManagement priceMan = new PriceSchemeManagement();
        String newForm = "LaseRay";
        String newCat = "Canadian Films";
        String newCatPeriod = "14";

        priceMan.addFormat(newForm);
        priceMan.addCategory(newCat, newCatPeriod);

        ArrayList<String> cats = priceMan.getAllCategories();
        for(int i = 0 ; i < cats.size(); i ++)
        {
            System.out.println("cat: " + cats.get(i));
        }
        ArrayList<String> forms = priceMan.getAllFormats();
        for(int i = 0 ; i < forms.size(); i++ )
        {
            System.out.println("form: "+ forms.get(i));
        }

        priceMan.addPrice(newCat, newForm, 109);
        int newPrice = priceMan.getPrice(newCat, newForm);
        System.out.println(newPrice);
    }
}
