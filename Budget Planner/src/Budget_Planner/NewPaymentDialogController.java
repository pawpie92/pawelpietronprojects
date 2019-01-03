package Budget_Planner;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class NewPaymentDialogController {

    @FXML
    private TextField title;

    @FXML
    private TextField ammount;

    @FXML
    private DatePicker date;

    public void addPaymentRecord(){
        String inTitle = title.getText().trim();
        if(inTitle.isEmpty())
            inTitle = "Empty Record";
        double inAmmount;
        if(ammount.getText().isEmpty())
            inAmmount = 0.0;
        else
            inAmmount = Double.parseDouble(ammount.getText());
        LocalDate inDate;
        if(date.getValue() == null)
            inDate = LocalDate.now();
        else
            inDate = date.getValue();
        System.out.println("Payment record " + inTitle + ", " + inAmmount + ", " + inDate + " has been entered to database");
    }
}
