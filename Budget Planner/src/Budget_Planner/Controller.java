package Budget_Planner;

import Budget_Planner.Model.DataConstants;
import Budget_Planner.Model.DataSource;
import Budget_Planner.Model.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Observable;
import java.util.Optional;
import java.util.function.Predicate;

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

    @FXML
    private RadioMenuItem showAll;




    private FilteredList<Transaction> transactionList;

    private Predicate<Transaction> wantAllTransactions;
    private Predicate<Transaction> onlyDeposits;
    private Predicate<Transaction> onlyPayments;


    //TODO: handling multiple gui elements: CheckBoxMenu, Labels for summaries


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
        double deposits = DataSource.getInstance().depositSumQuery(startDate.getValue(), endDate.getValue());
        double payments = DataSource.getInstance().paymentSumQuery(startDate.getValue(), endDate.getValue());
        double balances = deposits - payments;
        depositsSum.setText(Double.toString(deposits) + "$");
        paymentSum.setText(Double.toString(payments) + "$");
        balanceSum.setText(Double.toString(balances) + "$");
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
            controller.addPaymentRecord();
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
            controller.addDepositRecord();
            listTransactions();
        }
    }

    class GetAllTransactionTask extends Task {
        @Override
        public ObservableList<Transaction> call() {
            return FXCollections.observableArrayList(DataSource.getInstance().queryTransactions(startDate.getValue(), endDate.getValue()));
        }
    }

    class GetDepositsOnly extends Task{
        @Override
        public ObservableList<Transaction> call() {
            return FXCollections.observableArrayList(DataSource.getInstance().queryDeposits(startDate.getValue(), endDate.getValue()));

        }
    }

    class GetPaymentsOnly extends Task{
        @Override
        public ObservableList<Transaction> call() {
            return FXCollections.observableArrayList(DataSource.getInstance().queryPayments(startDate.getValue(), endDate.getValue()));

        }
    }
}
