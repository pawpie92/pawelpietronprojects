package Budget_Planner;

import Budget_Planner.Model.DataConstants;
import Budget_Planner.Model.DataSource;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.time.LocalDate;

public class NewDepositDialogController {

    @FXML
    private TextField title;

    @FXML
    private TextField amount;

    @FXML
    private DatePicker date;

    public void addDepositRecord(){
        title.getText().replaceAll(".","\\.");
        String inTitle = title.getText().trim();
        double inAmount = Double.parseDouble(amount.getText().replaceAll(",","\\."));
        LocalDate inDate = date.getValue();


        if(inTitle.isEmpty() || amount.getText().isEmpty() || date.getValue() == null)
            return;
        try
        {
            DataSource.getInstance().addTransaction(inTitle, inAmount, inDate, DataConstants.DEPOSIT);
        }
        catch (SQLException e){
            System.err.println("Payment insertion failure: " + e.getMessage());
        }
    }
}
