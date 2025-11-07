package models;

import java.sql.Timestamp;

public class Transaction {
    public int id;
    public String username;
    public String stockName;
    public String type; // BUY/SELL/DEPOSIT/WITHDRAW
    public int quantity;
    public float price;
    public float amount;
    public Timestamp timestamp;
}
