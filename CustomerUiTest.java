//import java.util.Date;
import ui.UiController;
import ui.util.UiMode;

/**
 *
 * @author alby
 */
public class CustomerUiTest {


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        UiController UIC = new UiController();
        UIC.setMode(UiMode.Customer); //UiMode customer and Employee
        UIC.startUI();
    }

}

