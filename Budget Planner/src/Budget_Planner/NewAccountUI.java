package Budget_Planner;

import Budget_Planner.Model.DataSource;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;


public class NewAccountUI {

    @FXML
    private TextField newLoginInput;

    @FXML
    private PasswordField newPasswordInput;

    @FXML
    private PasswordField passwordInputConfirm;


    public boolean createAccount(){

            if(newLoginInput.getText().isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Please enter login.");
                alert.setHeaderText("Please enter login.");
                alert.showAndWait();
                return false;
            }
            if(newPasswordInput.getText().isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Please enter password.");
                alert.setHeaderText("Please enter password.");
                alert.showAndWait();
                return false;
            }
            if(newPasswordInput.getText().isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Please confirm password.");
                alert.setHeaderText("Please confirm password.");
                alert.showAndWait();
                return false;
            }
            if(!newPasswordInput.getText().equals(passwordInputConfirm.getText()))
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Passwords does not match.");
                alert.setHeaderText("Passwords does not match.Please try again.");
                alert.showAndWait();
                return false;
            }
            try{
                if((DataSource.getInstance().accountLookup(newLoginInput.getText()) != null))
                    if (DataSource.getInstance().accountLookup(newLoginInput.getText()).getUserID().equals(newLoginInput.getText())){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Login already exists.");
                    alert.setHeaderText("UserID already exists please try another one");
                    alert.showAndWait();
                    return false;
                }
                DataSource.getInstance().createAccount(newLoginInput.getText().trim(), newPasswordInput.getText().trim());
                }
            catch(SQLException e){

                System.err.println("Record was not added: " + e.getMessage());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Exception");
                alert.setHeaderText("Exception - New Account");
                alert.setContentText("Cannot create this account" + e.getMessage());


                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                String exceptionText = sw.toString();

                Label label = new Label("The exception stacktrace was:");

                TextArea textArea = new TextArea(exceptionText);
                textArea.setEditable(false);
                textArea.setWrapText(true);

                textArea.setMaxWidth(Double.MAX_VALUE);
                textArea.setMaxHeight(Double.MAX_VALUE);
                GridPane.setVgrow(textArea, Priority.ALWAYS);
                GridPane.setHgrow(textArea, Priority.ALWAYS);

                GridPane expContent = new GridPane();
                expContent.setMaxWidth(Double.MAX_VALUE);
                expContent.add(label, 0, 0);
                expContent.add(textArea, 0, 1);
                return false;
            }
            return true;
        }
}

