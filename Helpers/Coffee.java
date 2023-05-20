package Helpers;

public class Coffee extends Drink {

    public Coffee(int quantity, String name) {
        super(quantity, name);
    }

    public Coffee(int quantity) {
        super(quantity);
    }

    @Override
    public String toString() {
        if (getQuantity() == 1) {
            return getQuantity() + "coffee";
        } else if (getQuantity() == 0) {
            return "";
        } else {
            return getQuantity() + "coffees";
        }


    }
}
