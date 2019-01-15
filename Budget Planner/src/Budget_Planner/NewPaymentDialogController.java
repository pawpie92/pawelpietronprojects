package Budget_Planner;

import Budget_Planner.Model.DataConstants;
import Budget_Planner.Model.DataSource;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.time.LocalDate;

public class NewPaymentDialogController {

    @FXML
    private TextField title;

    @FXML
    private TextField amount;

    @FXML
    private DatePicker date;



    public void addPaymentRecord(String account){
        title.getText().replaceAll(".","\\.");
        String inTitle = title.getText().trim();
        double inAmount = Double.parseDouble(amount.getText().replaceAll(",","\\."));
        LocalDate inDate = date.getValue();

        if(inTitle.isEmpty() || amount.getText().isEmpty())
            return;
        if(date.getValue() == null)
            inDate = LocalDate.now();
        try
        {

            DataSource.getInstance().addTransaction(inTitle, inAmount, inDate, DataConstants.PAYMENT , account);
        }
        catch (SQLException e){
            System.err.println("Payment insertion failure: " + e.getMessage());
        }
    }
}
