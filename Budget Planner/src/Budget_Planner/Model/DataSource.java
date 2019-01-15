package Budget_Planner.Model;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DataSource implements DataConstants{

    private Connection con;

    private PreparedStatement initTransactionTable;
    private PreparedStatement initAccountTable;
    private PreparedStatement insertTransaction;
    private PreparedStatement showTransactionPeriod;
    private PreparedStatement depositSummary;
    private PreparedStatement paymentSummary;
    private PreparedStatement deleteTransaction;
    private PreparedStatement showDeposits;
    private PreparedStatement showPayments;
    private PreparedStatement lookUpAccount;
    private PreparedStatement createAccount;
    private PreparedStatement queryTittlesDeposits;
    private PreparedStatement queryTittlesPayments;


    private static DataSource instance = new DataSource();
    private DataSource(){

    }

    public static DataSource getInstance(){
        return instance;
    }


    public boolean open(){
        try{

            con = DriverManager.getConnection(CONNECTION_STRING);
            initTransactionTable = con.prepareStatement(CREATE_TRANSACTION_TABLE);
            initTransactionTable.executeUpdate();
            initAccountTable = con.prepareStatement(CREATE_ACCOUNT_TABLE);
            initAccountTable.executeUpdate();
            lookUpAccount = con.prepareStatement(ACCOUNT_LOOKUP);
            createAccount = con.prepareStatement(INSERT_ACCOUNT);
            insertTransaction = con.prepareStatement(INSERT_TRANSACTION);
            showTransactionPeriod = con.prepareStatement(SHOW_TRANSACTIONS);
            depositSummary = con.prepareStatement(DEPOSIT_SUMMARY);
            paymentSummary = con.prepareStatement(PAYMENT_SUMMARY);
            deleteTransaction = con.prepareStatement(DELETE_TRANSACTION);
            showDeposits = con.prepareStatement(SHOW_DEPOSITS);
            showPayments = con.prepareStatement(SHOW_PAYMENTS);
            queryTittlesDeposits = con.prepareStatement(SHOW_TITLES_DEPOSITS);
            queryTittlesPayments = con.prepareStatement(SHOW_TITLES_PAYMENTS);

            return true;

        } catch (SQLException e){
            System.err.println("Something went wrong during opening: " + e.getMessage());
            return false;
        }
    }

    public void close(){
        try{
            if(initTransactionTable != null)
                initTransactionTable.close();
            if(initAccountTable != null)
                initAccountTable.close();
            if(lookUpAccount != null)
                lookUpAccount.close();
            if(createAccount != null)
                createAccount.close();
            if(insertTransaction != null)
                insertTransaction.close();
            if(showTransactionPeriod != null)
                showTransactionPeriod.close();
            if(depositSummary != null)
                depositSummary.close();
            if(paymentSummary != null)
                paymentSummary.close();
            if(deleteTransaction != null)
                deleteTransaction.close();
            if(showDeposits != null)
                showDeposits.close();
            if(showPayments != null)
                showPayments.close();
            if(queryTittlesDeposits != null)
                queryTittlesDeposits.close();
            if(queryTittlesPayments != null)
                queryTittlesPayments.close();
            if(con != null)
                con.close();
        }
        catch (SQLException e){
            System.err.println("Something went wrong when closing statement:" + e.getMessage());
        }
    }

    public void createAccount(String login, String password) throws SQLException {
            createAccount.setString(1,login);
            createAccount.setString(2,password);
            int affectedRows = createAccount.executeUpdate();

            if( affectedRows != 1){
                throw new SQLException("Transaction was not entered into database");
            }
    }

    public List<String> querryTittlesDeposits(){
        List<String> titles = new ArrayList<String>();
        try{

            ResultSet results = queryTittlesDeposits.executeQuery();
            while(results.next())
            {
                String title = results.getString(1);
                System.out.println(title);
                titles.add(title);
            }
            return titles;
        }
        catch (SQLException e){
            System.err.println("Error in listing titles from DB: " + e.getMessage());
            return null;
        }
    }


    public List<String> querryTittlesPayments(){
        List<String> titles = new ArrayList<String>();
        try{

            ResultSet results = queryTittlesPayments.executeQuery();
            while(results.next())
            {

                String title = results.getString(1);
                System.out.println(title);
                titles.add(title);
            }
            return titles;
        }
        catch (SQLException e){
            System.err.println("Error in listing titles from DB: " + e.getMessage());
            return null;
        }
    }

    public Account accountLookup(String login){
        try
        {
            Account account = new Account();
            lookUpAccount.setString(1,login);
            ResultSet result = lookUpAccount.executeQuery();
            if(result.next())
            {
                account.setUserID(result.getString(COLUMN_ACCOUNT_LOGIN));
                account.setPassword(result.getString(COLUMN_ACCOUNT_PASSWORD));
                return account;
            }
            return null;
        }
        catch(SQLException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Exception");
            alert.setHeaderText("Exception - Account");
            alert.setContentText("Cannot lookup this account" + e.getMessage());


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
            return null;
        }


    }

    public void addTransaction(String title, double amount, LocalDate date, String type, String account) throws SQLException{

        insertTransaction.setInt(1,insertTransaction.getGeneratedKeys().getInt(1));
        insertTransaction.setString(2, title);
        insertTransaction.setDouble(3, amount);
        if(date == null)
            insertTransaction.setDate(4,Date.valueOf(LocalDate.now()));
        insertTransaction.setDate(4, Date.valueOf(date));
        insertTransaction.setString(5, type);
        insertTransaction.setString(6, account);
        int affectedRows = insertTransaction.executeUpdate();

        if( affectedRows != 1){
            throw new SQLException("Transaction was not entered into database");
        }

    }

    public void deleteTransaction(Transaction transaction){
        try{

            deleteTransaction.setInt(1,transaction.getId());
            deleteTransaction.executeUpdate();

        }catch(SQLException e){
            System.err.println("Cannot delete this record:" + e.getMessage());
            System.err.println("Something went wrong: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Exception");
            alert.setHeaderText("Exception - Transaction");
            alert.setContentText("Cannot delete this record:" + e.getMessage());


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


            alert.getDialogPane().setExpandableContent(expContent);

            alert.showAndWait();
        }
    }

    public void addAccount(String login, String password){

    }

    public List<Transaction> queryTransactions(LocalDate start, LocalDate end, String account){


        List<Transaction> transactions = new ArrayList<Transaction>();
        //If time is undefined period is set to the beginning of year 0
        if(start == null || end == null)
        {
            start = LocalDate.of(0,1,1);
            end = LocalDate.now();
        }

        try
        {
            showTransactionPeriod.setDate(1, Date.valueOf(start));
            showTransactionPeriod.setDate(2, Date.valueOf(end));
            showTransactionPeriod.setString(3, account);

            ResultSet results = showTransactionPeriod.executeQuery();
            while(results.next()){
                Transaction transaction = new Transaction();
                transaction.setId(results.getInt(COLUMN_TRANSACTIONS_ID));
                transaction.setTitle(results.getString(COLUMN_TRANSACTIONS_TITLE));
                transaction.setAmount(results.getDouble(COLUMN_TRANSACTIONS_AMOUNT));
                transaction.setDate(results.getDate(COLUMN_TRANSACTIONS_DATE));
                transaction.setType(results.getString(COLUMN_TRANSACTIONS_TYPE));
                transaction.setAccount(results.getString(COLUMN_TRANSACTIONS_ACCOUNT_ID));
                transactions.add(transaction);
            }
            return transactions;
        }
        catch (SQLException e){
            System.err.println("Something went wrong: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Exception");
            alert.setHeaderText("Exception - Transaction");
            alert.setContentText("Something went wrong in  queryTransaction: " + e.getMessage());


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


            alert.getDialogPane().setExpandableContent(expContent);

            alert.showAndWait();
            return null;
        }
    }

    public List<Transaction> queryDeposits(LocalDate start, LocalDate end, String account){


        List<Transaction> transactions = new ArrayList<Transaction>();
        //If time is undefined period is set to the beginning of year 0
        if(start == null || end == null)
        {
            start = LocalDate.of(0,1,1);
            end = LocalDate.now();
        }

        try
        {
            showDeposits.setDate(1, Date.valueOf(start));
            showDeposits.setDate(2, Date.valueOf(end));
            showTransactionPeriod.setString(3, account);

            ResultSet results = showDeposits.executeQuery();
            while(results.next()){
                Transaction transaction = new Transaction();
                transaction.setTitle(results.getString(COLUMN_TRANSACTIONS_TITLE));
                transaction.setAmount(results.getDouble(COLUMN_TRANSACTIONS_AMOUNT));
                transaction.setDate(results.getDate(COLUMN_TRANSACTIONS_DATE));
                transaction.setType(results.getString(COLUMN_TRANSACTIONS_TYPE));
                transaction.setAccount(results.getString(COLUMN_TRANSACTIONS_ACCOUNT_ID));
                transactions.add(transaction);
            }
            return transactions;
        }
        catch (SQLException e){
            System.err.println("Something went wrong: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Exception");
            alert.setHeaderText("Exception - Deposit");
            alert.setContentText("Something went wrong in  queryDeposits: " + e.getMessage());


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


            alert.getDialogPane().setExpandableContent(expContent);

            alert.showAndWait();
            return null;
        }
    }
    public List<Transaction> queryPayments(LocalDate start, LocalDate end, String account){


        List<Transaction> transactions = new ArrayList<Transaction>();
        //If time is undefined period is set to the beginning of year 0
        if(start == null || end == null)
        {
            start = LocalDate.of(0,1,1);
            end = LocalDate.now();
        }

        try
        {
            showPayments.setDate(1, Date.valueOf(start));
            showPayments.setDate(2, Date.valueOf(end));
            showTransactionPeriod.setString(3, account);

            ResultSet results = showPayments.executeQuery();
            while(results.next()){
                Transaction transaction = new Transaction();
                transaction.setTitle(results.getString(COLUMN_TRANSACTIONS_TITLE));
                transaction.setAmount(results.getDouble(COLUMN_TRANSACTIONS_AMOUNT));
                transaction.setDate(results.getDate(COLUMN_TRANSACTIONS_DATE));
                transaction.setType(results.getString(COLUMN_TRANSACTIONS_TYPE));
                transaction.setAccount(results.getString(COLUMN_TRANSACTIONS_ACCOUNT_ID));
                transactions.add(transaction);
            }
            return transactions;
        }
        catch (SQLException e){
            System.err.println("Something went wrong in  queryPayments: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Exception");
            alert.setHeaderText("Exception - Payment");
            alert.setContentText("Something went wrong in  queryPayments: " + e.getMessage());


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


            alert.getDialogPane().setExpandableContent(expContent);

            alert.showAndWait();
            return null;
        }
    }


    public double depositSumQuery(LocalDate start, LocalDate end, String account){
        double depositsum = 0.0;

        if(start == null || end == null)
        {
            start = LocalDate.of(0,1,1);
            end = LocalDate.now();
        }

        try{
            depositSummary.setDate(1,Date.valueOf(start));
            depositSummary.setDate(2,Date.valueOf(end));
            depositSummary.setString(3,account);

            ResultSet result = depositSummary.executeQuery();
            if(result.next())
                depositsum = result.getDouble(1);
        }
        catch (SQLException e){
            System.err.println("Couldn't return sum for deposits: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Exception");
            alert.setHeaderText("Exception  - Deposit");
            alert.setContentText("Couldn't return sum for deposits: " + e.getMessage());


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


            alert.getDialogPane().setExpandableContent(expContent);

            alert.showAndWait();
        }
        finally {
            return depositsum;
        }
    }

    public double paymentSumQuery(LocalDate start, LocalDate end, String account){
        double paymentsum = 0.0;

        if(start == null || end == null)
        {
            start = LocalDate.of(0,1,1);
            end = LocalDate.now();
        }

        try{
            paymentSummary.setDate(1,Date.valueOf(start));
            paymentSummary.setDate(2,Date.valueOf(end));
            paymentSummary.setString(3,account);
            ResultSet result = paymentSummary.executeQuery();
            if(result.next())
                paymentsum = result.getDouble(1);
        }
        catch (SQLException e){
            System.err.println("Couldn't return sum for payments: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Exception");
            alert.setHeaderText("Exception - Payment");
            alert.setContentText("Couldn't return sum for payments: " + e.getMessage());


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


            alert.getDialogPane().setExpandableContent(expContent);

            alert.showAndWait();
        }
        finally {
            return paymentsum;
        }
    }

}
