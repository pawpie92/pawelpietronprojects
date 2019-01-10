package Budget_Planner.Model;

public interface DataConstants {

    String DB_NAME = "transactions.db";
    String PATH = System.getProperty("user.dir");
    String CONNECTION_STRING = "jdbc:sqlite:" + PATH + "\\" + DB_NAME;

    String TABLE_TRANSACTIONS = "transactions";

    String COLUMN_ID = "id";
    String COLUMN_TITLE = "title";
    String COLUMN_AMOUNT = "amount";
    String COLUMN_DATE = "date";
    String COLUMN_TYPE = "type";

    String DEPOSIT = "Deposit";
    String PAYMENT = "Payment";

    String CREATE_TRANSACTION_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_TRANSACTIONS + " ( "+ COLUMN_ID + ", " + COLUMN_TITLE + " TEXT , " +
            COLUMN_AMOUNT + " DOUBLE, " + COLUMN_DATE + " DATE, " + COLUMN_TYPE + " TEXT)";

    String INSERT_TRANSACTION = "INSERT INTO " + TABLE_TRANSACTIONS + "( "+ COLUMN_ID + ", " + COLUMN_TITLE + ", " + COLUMN_AMOUNT + ", " + COLUMN_DATE + ", " + COLUMN_TYPE + ") VALUES(?,?,?,?,?)";

    String SHOW_TRANSACTIONS = "SELECT * FROM " + TABLE_TRANSACTIONS + " WHERE " + COLUMN_DATE + " BETWEEN ? AND ? " +
            " ORDER BY " + COLUMN_DATE + " DESC";
    String SHOW_DEPOSITS = "SELECT * FROM " + TABLE_TRANSACTIONS + " WHERE "+ COLUMN_TYPE + " = '" + DEPOSIT + "' AND " + COLUMN_DATE + " BETWEEN ? AND ? " +
            " ORDER BY " + COLUMN_DATE + " DESC";
    String SHOW_PAYMENTS = "SELECT * FROM " + TABLE_TRANSACTIONS + " WHERE "+ COLUMN_TYPE + " = '" + PAYMENT + "' AND " + COLUMN_DATE + " BETWEEN ? AND ? " +
            " ORDER BY " + COLUMN_DATE + " DESC";
    String YEAR_SUMMARY ="SELECT " + COLUMN_AMOUNT + " FROM " + TABLE_TRANSACTIONS + " WHERE strftime('%Y'," + COLUMN_DATE + ") = ?" +
            "GROUP BY strftime('%Y'," + COLUMN_DATE + ")";

    String DEPOSIT_SUMMARY = "SELECT SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_TRANSACTIONS + " WHERE " + COLUMN_TYPE + " LIKE '%" + DEPOSIT + "%' AND " + COLUMN_DATE + " BETWEEN ? AND ? ";
    String PAYMENT_SUMMARY = "SELECT SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_TRANSACTIONS + " WHERE " + COLUMN_TYPE + " LIKE '%" + PAYMENT + "%' AND " + COLUMN_DATE + " BETWEEN ? AND ? ";

    String DELETE_TRANSACTION = "DELETE FROM " + TABLE_TRANSACTIONS + " WHERE " + COLUMN_ID + " = ? ";

}