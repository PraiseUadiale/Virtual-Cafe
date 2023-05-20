package Helpers;

public abstract class Drink {
    private int quantity;
    private String customerName;

    public Drink(int quantity) {
        this.quantity = quantity;

    }

    public Drink(int quantity, String customerName) {
        this.quantity = quantity;
        this.customerName = customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
