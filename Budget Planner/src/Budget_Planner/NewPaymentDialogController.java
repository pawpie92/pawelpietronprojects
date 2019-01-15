package Budget_Planner;

import Budget_Planner.Model.DataConstants;
import Budget_Planner.Model.DataSource;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.time.LocalDate;

public class NewPaymentDialogController {


    @FXML
    private TextField amount;

    @FXML
    private DatePicker date;

    @FXML
    private ComboBox<String> comboTitle;

    public void initializeTitleList(){
        comboTitle.setEditable(true);
        comboTitle.getItems().addAll(DataSource.getInstance().querryTittlesPayments());
    }


    public void addPaymentRecord(String account){
        String inTitle = comboTitle.getSelectionModel().getSelectedItem();
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
