package Helpers;

public class Tea extends Drink {

    public Tea(int quantity, String name) {
        super(quantity, name);
    }

    public Tea(int quantity) {
        super(quantity);
    }

    @Override
    public String toString() {
        if (getQuantity() == 1) {
            return getQuantity() + "tea";
        } else if (getQuantity() == 0) {
            return "";
        } else {
            return getQuantity() + "teas";
        }


    }
}
