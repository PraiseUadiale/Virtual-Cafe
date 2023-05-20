package Helpers;

public class VirtualCafeUtils {

    public static final int PORT = 1234;
    public static final String HOST = "localhost";

    public static final long TIME_TO_MAKE_A_CUP_OF_TEA = 30000;
    public static final long TIME_TO_MAKE_A_CUP_OF_COFFEE = 45000;

    //Get total number of tea and coffee orders given an order
    public static int getTotalNumberOfDrinks(Order order) {
        return order.getNumberOfTeas() + order.getNumberOfCoffees();
    }

    public static Order inferOrderFromInput(String line) {

        line = line.trim();

        int numberOfTeas = 0;
        int numberOfCoffees = 0;
        String[] substrings = line.split(" ");
        try {
            for (int i = 1; i < substrings.length; i++) {

                if (substrings[i].equals("tea") || substrings[i].equals("teas")) {
                    numberOfTeas += Integer.parseInt(substrings[i - 1]);
                } else if (substrings[i].equals("coffee") || substrings[i].equals("coffees")) {
                    numberOfCoffees += Integer.parseInt(substrings[i - 1]);
                }
            }
        } catch (Exception ex) {

            return new Order(0, 0);
        }

        return new Order(numberOfTeas, numberOfCoffees);
    }
}
