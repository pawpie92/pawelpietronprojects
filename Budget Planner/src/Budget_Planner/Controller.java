package Budget_Planner;

import Budget_Planner.Model.DataSource;
import Budget_Planner.Model.Transaction;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Optional;

public class Controller {

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private TableView<Transaction> tableView;

    @FXML
    private DatePicker startDate;

    @FXML
    private DatePicker endDate;

    @FXML
    private Label depositsSum;

    @FXML
    private Label paymentSum;

    @FXML
    private Label balanceSum;

    @FXML
    private RadioMenuItem depositsOnly;

    @FXML
    private RadioMenuItem paymentsOnly;

    private String currentUserID;

    private boolean loggedIn = false;

    public String getCurrentUserID() { return currentUserID; }

    public void setCurrentUserID(String currentUserID) { this.currentUserID = currentUserID; }


    @FXML
    public void initialize(){
        while(!loggedIn){
            Dialog<ButtonType> dialog = new Dialog();
            dialog.setTitle("Welcome to Budget Planner");
            dialog.setHeaderText("Please enter your credentials");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("LoginUI.fxml"));
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
            if (result.isPresent() && result.get() == ButtonType.CANCEL) {
                Platform.exit();
                break;
            }
            if (result.isPresent() && result.get() == ButtonType.OK){
                LoginUI controller = loader.getController();
                if(controller.loginToBudget() != null){
                    loggedIn = true;
                    currentUserID = controller.loginToBudget().getUserID();
                }

                }
        }

    }

    @FXML
    public void aboutDialogHandle(){
        Alert about = new Alert(Alert.AlertType.INFORMATION);
        about.setTitle("About...");
        about.setHeaderText("Application instructions: ");
        about.setContentText("\n" +
                "Budget Planner for people who wants organise their budget.\n" +
                "\n\n" +
                "By Pawel Pietron." +
                "\n\n" +
                "If you want to delete record from table, simply select the record and press delete button." +
                "\n\n" +
                "To Create new transaction in table please go to new -> Deposit/Payment." +
                "\n\n" +
                "If you wish to filter view to only see deposits or payments please go to Edit -> View Preferences to choose preference.");
        about.showAndWait();
    }

    @FXML
    public void handleExit(){
        Platform.exit();
    }

    @FXML
    public void listTransactions() {
        Task<ObservableList<Transaction>> task;
        if(depositsOnly.isSelected())
            task = new GetDepositsOnly();
        else if(paymentsOnly.isSelected())
            task = new GetPaymentsOnly();
        else
            task  = new GetAllTransactionTask();
        tableView.itemsProperty().bind(task.valueProperty());
        double deposits = DataSource.getInstance().depositSumQuery(startDate.getValue(), endDate.getValue(), currentUserID);
        double payments = DataSource.getInstance().paymentSumQuery(startDate.getValue(), endDate.getValue(), currentUserID);
        double balances = deposits - payments;
        DecimalFormat df = new DecimalFormat("#####.##");
        depositsSum.setText(df.format(deposits) + "$");
        paymentSum.setText(df.format(payments) + "$");
        balanceSum.setText(df.format(balances) + "$");
        if(balances < 0.0)
            balanceSum.setTextFill(Color.RED);
        else if(balances > 0.0)
            balanceSum.setTextFill(Color.GREEN);
        new Thread(task).start();
    }

    @FXML
    public void handleKeyPressed(KeyEvent keyEvent){
        Transaction transaction = tableView.getSelectionModel().getSelectedItem();
        if(transaction != null){
            if(keyEvent.getCode().equals(KeyCode.DELETE))
                deleteTransaction(transaction);
        }
    }


    @FXML
    public void deleteTransaction(Transaction transaction){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Transaction");
        alert.setHeaderText("Delete transaction: " + transaction.getTitle());
        alert.setContentText("Are you sure you want to delete this transaction?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && (result.get() == ButtonType.OK))
            DataSource.getInstance().deleteTransaction(transaction);
        listTransactions();
    }

    @FXML
    public void showNewPaymentDialog() {
        Dialog<ButtonType> dialog = new Dialog();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Add New Payment");
        dialog.setHeaderText("Use this field to add new payment");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("newPaymentDialog.fxml"));
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
        if (result.isPresent() && result.get() == ButtonType.OK) {
            NewPaymentDialogController controller = loader.getController();
            controller.addPaymentRecord(currentUserID);
            listTransactions();
        }
    }

    @FXML
    public void showNewDepositDialog() {
        Dialog<ButtonType> dialog = new Dialog();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Add New Transaction");
        dialog.setHeaderText("Use this field to add new deposit");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("newDepositDialog.fxml"));
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
        if (result.isPresent() && result.get() == ButtonType.OK) {
            NewDepositDialogController controller = loader.getController();
            controller.addDepositRecord(currentUserID);
            listTransactions();
        }
    }

    class GetAllTransactionTask extends Task {
        @Override
        public ObservableList<Transaction> call() {
            return FXCollections.observableArrayList(DataSource.getInstance().queryTransactions(startDate.getValue(), endDate.getValue(), currentUserID));
        }
    }

    class GetDepositsOnly extends Task{
        @Override
        public ObservableList<Transaction> call() {
            return FXCollections.observableArrayList(DataSource.getInstance().queryDeposits(startDate.getValue(), endDate.getValue(), currentUserID));

        }
    }

    class GetPaymentsOnly extends Task{
        @Override
        public ObservableList<Transaction> call() {
            return FXCollections.observableArrayList(DataSource.getInstance().queryPayments(startDate.getValue(), endDate.getValue(), currentUserID));

        }
    }
}
