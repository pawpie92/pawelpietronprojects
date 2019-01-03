package Budget_Planner;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.Optional;

public class Controller {

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    public void showNewPaymentDialog(){
        Dialog<ButtonType> dialog = new Dialog();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Add New Payment");
        dialog.setHeaderText("Use this field to add new payment");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("newPaymentDialog.fxml"));
        try{
            dialog.getDialogPane().setContent(loader.load());
        }
        catch(IOException e){
            System.err.println(e.getMessage());
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            NewPaymentDialogController controller = loader.getController();
            controller.addPaymentRecord();
        }
    }

    @FXML
    public void showNewDepositDialog(){
        Dialog<ButtonType> dialog = new Dialog();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Add New Deposit");
        dialog.setHeaderText("Use this field to add new deposit");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("newDepositDialog.fxml"));
        try{
            dialog.getDialogPane().setContent(loader.load());
        }
        catch(IOException e){
            System.err.println(e.getMessage());
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            NewDepositDialogController controller = loader.getController();
            controller.addDepositRecord();
        }
    }

}
