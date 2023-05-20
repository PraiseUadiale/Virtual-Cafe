package Helpers;

/**
 * Order class is used to store the number of teas and coffees in an order and the name of the customer
 */

public class Order {

    private final Tea numberOfTeas;
    private final Coffee numberOfCoffees;


    private String customerName;

    public Order(int numberOfTeas, int numberOfCoffees) {
        this.numberOfTeas = new Tea(numberOfTeas);
        this.numberOfCoffees = new Coffee(numberOfCoffees);
    }

    public int getNumberOfTeas() {
        return numberOfTeas.getQuantity();
    }

    public int getNumberOfCoffees() {
        return numberOfCoffees.getQuantity();
    }

    public void setNumberOfTeas(int numberOfTeas) {
        this.numberOfTeas.setQuantity(numberOfTeas);
    }

    public void setNumberOfCoffees(int numberOfCoffees) {
        this.numberOfCoffees.setQuantity(numberOfCoffees);
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
        this.numberOfCoffees.setCustomerName(customerName);
        this.numberOfTeas.setCustomerName(customerName);
    }


    public boolean equals(Object obj) {
        if (obj instanceof Order order) {
            return order.getNumberOfTeas() == this.getNumberOfTeas() && order.getNumberOfCoffees() == this.getNumberOfCoffees() && order.getCustomerName().equals(this.getCustomerName());
        }
        return false;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(numberOfTeas);
        sb.append(" and ");
        sb.append(numberOfCoffees);

        return sb.toString();
    }
}
