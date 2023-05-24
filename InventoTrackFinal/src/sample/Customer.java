package sample;

import java.sql.Date;

public class Customer {
    private int customerID;
    private String customerName;
    private String city;
    private int productID;
    private int quantity;
    private Date soldOn;
    private double amount;

    public Customer(int customerID, String customerName, String city, int productID, int quantity, Date soldOn,double amount) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.city = city;
        this.productID = productID;
        this.quantity = quantity;
        this.soldOn = soldOn;
        this.amount = amount;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getSoldOn() {
        return soldOn;
    }

    public void setSoldOn(Date soldOn) {
        this.soldOn = soldOn;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
