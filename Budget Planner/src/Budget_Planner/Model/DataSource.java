package Budget_Planner.Model;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DataSource implements DataConstants{

    private Connection con;

    private PreparedStatement initTable;
    private PreparedStatement insertTransaction;
    private PreparedStatement showTransactionPeriod;
    private PreparedStatement depositSummary;
    private PreparedStatement paymentSummary;
    private PreparedStatement yearSummary;
    private PreparedStatement deleteTransaction;
    private PreparedStatement showDeposits;
    private PreparedStatement showPayments;


    private static DataSource instance = new DataSource();
    private DataSource(){

    }

    public static DataSource getInstance(){
        return instance;
    }


    public boolean open(){
        try{

            con = DriverManager.getConnection(CONNECTION_STRING);
            initTable = con.prepareStatement(CREATE_TRANSACTION_TABLE);
            initTable.executeUpdate();
            insertTransaction = con.prepareStatement(INSERT_TRANSACTION);
            showTransactionPeriod = con.prepareStatement(SHOW_TRANSACTIONS);
            depositSummary = con.prepareStatement(DEPOSIT_SUMMARY);
            paymentSummary = con.prepareStatement(PAYMENT_SUMMARY);
            yearSummary = con.prepareStatement(YEAR_SUMMARY);
            deleteTransaction = con.prepareStatement(DELETE_TRANSACTION);
            showDeposits = con.prepareStatement(SHOW_DEPOSITS);
            showPayments = con.prepareStatement(SHOW_PAYMENTS);
            return true;

        } catch (SQLException e){
            System.err.println("Something went wrong during opening: " + e.getMessage());
            return false;
        }
    }

    public void close(){
        try{
            if(initTable != null)
                initTable.close();
            if(insertTransaction != null)
                insertTransaction.close();
            if(showTransactionPeriod != null)
                showTransactionPeriod.close();
            if(yearSummary != null)
                yearSummary.close();
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
            if(con != null)
                con.close();
        }
        catch (SQLException e){
            System.err.println("Something went wrong when closing statement:" + e.getMessage());
        }
    }

    public void addTransaction(String title, double amount, LocalDate date, String type) throws SQLException{

        insertTransaction.setInt(1,insertTransaction.getGeneratedKeys().getInt(1));
        insertTransaction.setString(2, title);
        insertTransaction.setDouble(3, amount);
        insertTransaction.setDate(4, Date.valueOf(date));
        insertTransaction.setString(5, type);
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
        }
    }


    public List<Transaction> queryTransactions(LocalDate start, LocalDate end){


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
            ResultSet results = showTransactionPeriod.executeQuery();
            while(results.next()){
                Transaction transaction = new Transaction();
                transaction.setTitle(results.getString(COLUMN_TITLE));
                transaction.setAmount(results.getDouble(COLUMN_AMOUNT));
                transaction.setDate(results.getDate(COLUMN_DATE));
                transaction.setType(results.getString(COLUMN_TYPE));
                transactions.add(transaction);
            }
            return transactions;
        }
        catch (SQLException e){
            System.err.println("Something went wrong: " + e.getMessage());
            return null;
        }
    }

    public List<Transaction> queryDeposits(LocalDate start, LocalDate end){


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
            ResultSet results = showDeposits.executeQuery();
            while(results.next()){
                Transaction transaction = new Transaction();
                transaction.setTitle(results.getString(COLUMN_TITLE));
                transaction.setAmount(results.getDouble(COLUMN_AMOUNT));
                transaction.setDate(results.getDate(COLUMN_DATE));
                transaction.setType(results.getString(COLUMN_TYPE));
                transactions.add(transaction);
            }
            return transactions;
        }
        catch (SQLException e){
            System.err.println("Something went wrong: " + e.getMessage());
            return null;
        }
    }
    public List<Transaction> queryPayments(LocalDate start, LocalDate end){


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
            ResultSet results = showPayments.executeQuery();
            while(results.next()){
                Transaction transaction = new Transaction();
                transaction.setTitle(results.getString(COLUMN_TITLE));
                transaction.setAmount(results.getDouble(COLUMN_AMOUNT));
                transaction.setDate(results.getDate(COLUMN_DATE));
                transaction.setType(results.getString(COLUMN_TYPE));
                transactions.add(transaction);
            }
            return transactions;
        }
        catch (SQLException e){
            System.err.println("Something went wrong: " + e.getMessage());
            return null;
        }
    }


    public double depositSumQuery(LocalDate start, LocalDate end){
        double depositsum = 0.0;

        if(start == null || end == null)
        {
            start = LocalDate.of(0,1,1);
            end = LocalDate.now();
        }

        try{
            depositSummary.setDate(1,Date.valueOf(start));
            depositSummary.setDate(2,Date.valueOf(end));
            ResultSet result = depositSummary.executeQuery();
            if(result.next())
                depositsum = result.getDouble(1);
        }
        catch (SQLException e){
            System.err.println("Couldn't return sum for deposits: " + e.getMessage());
        }
        finally {
            return depositsum;
        }
    }

    public double paymentSumQuery(LocalDate start, LocalDate end){
        double paymentsum = 0.0;

        if(start == null || end == null)
        {
            start = LocalDate.of(0,1,1);
            end = LocalDate.now();
        }

        try{
            paymentSummary.setDate(1,Date.valueOf(start));
            paymentSummary.setDate(2,Date.valueOf(end));
            ResultSet result = paymentSummary.executeQuery();
            if(result.next())
                paymentsum = result.getDouble(1);
        }
        catch (SQLException e){
            System.err.println("Couldn't return sum for deposits: " + e.getMessage());
        }
        finally {
            return paymentsum;
        }
    }

}
