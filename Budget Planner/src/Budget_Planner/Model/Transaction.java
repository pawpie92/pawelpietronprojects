package Budget_Planner.Model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Date;

public class Transaction {

    private int id;
    private SimpleDoubleProperty amount;
    private SimpleStringProperty title;
    private SimpleStringProperty type;
    private Date date;

    public Transaction() {
        this.amount = new SimpleDoubleProperty();
        this.title = new SimpleStringProperty();
        this.type = new SimpleStringProperty();
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public double getAmount() { return amount.get(); }

    public void setAmount(double amount) { this.amount.set(amount); }

    public String getTitle() { return title.get(); }

    public void setTitle(String title) { this.title.set(title); }

    public Date getDate() { return date; }

    public void setDate(Date date) { this.date = date; }

    public String getType() { return type.get(); }

    public void setType(String type) { this.type.set(type); }
}
