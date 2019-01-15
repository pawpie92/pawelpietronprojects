package Budget_Planner;

import Budget_Planner.Model.Account;
import Budget_Planner.Model.DataSource;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.Optional;

public class LoginUI {

    @FXML
    private TextField loginInput;

    @FXML
    private PasswordField passwordInput;


    public Account loginToBudget(){
        if(loginInput.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Please enter login.");
            alert.setHeaderText("Please enter login.");
            alert.showAndWait();
            return null;
        }
        if(passwordInput.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Please enter password.");
            alert.setHeaderText("Please enter password.");
            alert.showAndWait();
            return null;
        }

        if(DataSource.getInstance().accountLookup(loginInput.getText()) == null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Account Does not Exist");
            alert.setHeaderText("Account Does not Exist.");
            alert.setContentText("Please, try again or create a new account");
            alert.showAndWait();
            return null;
        }
        else if(!DataSource.getInstance().accountLookup(loginInput.getText()).getPassword().equals(passwordInput.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Wrong password.");
            alert.setHeaderText("Wrong password.");
            alert.setContentText("Please, try again.");
            alert.showAndWait();
            return null;
        }
        else{
            String login = loginInput.getText();
            return DataSource.getInstance().accountLookup(login);
        }

    }

    @FXML
    public void createAccountDialog(){
        Dialog<ButtonType> dialog = new Dialog();
        dialog.setTitle("Create new account.");
        dialog.setHeaderText("Please enter your new login and password");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("newAccountUI.fxml"));
        try {
            dialog.getDialogPane().setContent(loader.load());
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            NewAccountUI controller = loader.getController();
            if(!controller.createAccount())
                createAccountDialog();
        }
    }
}

