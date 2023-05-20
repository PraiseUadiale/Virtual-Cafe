package Helpers;


import java.util.*;
import java.util.concurrent.*;

/**
 * Café class is used to handle everything Order related
 */
public class Cafe {


    private final static Queue<Order> waitingArea = new ConcurrentLinkedQueue<>();

    private static final BlockingQueue<Tea> teaBrewingArea = new ArrayBlockingQueue<>(2);
    private final static BlockingQueue<Coffee> coffeeBrewingArea = new ArrayBlockingQueue<>(2);


    private List<CustomerHandler> allCustomers;
    private static final Map<String, List<Drink>> trayArea = new ConcurrentHashMap<>();

    public Cafe(List<CustomerHandler> allCustomers) {
        this.allCustomers = allCustomers;
        Thread coffeeBrewer1 = new Thread(new CoffeeBrewingArea());
        coffeeBrewer1.setName("CoffeeBrewer 1");
        coffeeBrewer1.start();


        Thread coffeeBrewer2 = new Thread(new CoffeeBrewingArea());
        coffeeBrewer2.setName("CoffeeBrewer 2");
        coffeeBrewer2.start();

        Thread teaBrewer1 = new Thread(new TeaBrewingArea());
        teaBrewer1.setName("TeaBrewer 1");
        teaBrewer1.start();

        Thread teaBrewer2 = new Thread(new TeaBrewingArea());
        teaBrewer2.setName("TeaBrewer 2");
        teaBrewer2.start();

        Thread thread = new Thread(this::transferFromWaitingToBrewingArea);
        thread.setName("OrderToBrewingArea");
        thread.start();

        Thread thread1 = new Thread(runnable);
        thread1.start();


    }

    public void addCustomerHandler(CustomerHandler customerHandler) {
        allCustomers.add(customerHandler);
    }

    public void removeCustomerHandler(CustomerHandler customerHandler) {
        allCustomers.remove(customerHandler);
    }

    private Runnable runnable = () -> {
        try {
            while (true) {
                //Monitor if the size of any  the data structures change size

                Thread.sleep(5000);
                System.out.println("Total number of clients in the cafe: " + allCustomers.size());
                waitingArea();
                teaArea();
                coffeeArea();
                trayArea();

                System.out.println();

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    };

    private void teaArea() {

        int tea = 0;
        for (Drink order : teaBrewingArea) {
            tea += order.getQuantity();
        }
        System.out.println(tea + " tea(s) in tea brewing area");

    }

    private void coffeeArea() {

        int coffee = 0;
        for (Drink order : coffeeBrewingArea) {
            coffee += order.getQuantity();
        }
        System.out.println(coffee + " coffee(s) in coffee brewing area");

    }

    private static void waitingArea() {

        //Calculate the number of coffee and tea in the waiting area

        int coffee = 0;
        int tea = 0;
        for (Order order : waitingArea) {
            coffee += order.getNumberOfCoffees();
            tea += order.getNumberOfTeas();
        }
        System.out.println(coffee + " coffee(s) and " + tea + " tea(s) in waiting area");

    }


    private static void trayArea() {
        int coffeeCount = 0;
        int teaCount = 0;
        for (String key : trayArea.keySet()) {
            for (Drink drink : trayArea.get(key)) {
                if (drink instanceof Coffee) {
                    coffeeCount++;
                } else if (drink instanceof Tea) {
                    teaCount++;
                }
            }
        }
        System.out.println(coffeeCount + " coffee(s) and " + teaCount + " tea(s) in tray area");

    }

    /***
     * Method is used to add an order to the waiting area
     * @param order Order to be added
     */
    public void order(Order order) {
        addOrderToWaitingArea(order);
    }


    /***
     * Method is used to remove an order from the waiting area and add it to the brewing area
     */

    private void transferFromWaitingToBrewingArea() {
        while (true) {
            //Wait for 2 seconds before checking the waiting area
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Order order = waitingArea.poll();

            if (order == null) {
                continue;
            }

            Runnable teaRunnable = () -> {
                if (order.getNumberOfTeas() > 0) {

                    int numberOfTeas = order.getNumberOfTeas();
                    while (numberOfTeas > 0) {
                        try {
                            teaBrewingArea.put(new Tea(1, order.getCustomerName()));
                            numberOfTeas--;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }
            };
            Thread thread = new Thread(teaRunnable);


            Runnable coffeeRunnable = () -> {
                if (order.getNumberOfCoffees() > 0) {
                    int numberOfCoffees = order.getNumberOfCoffees();
                    while (numberOfCoffees > 0) {
                        try {
                            coffeeBrewingArea.put(new Coffee(1, order.getCustomerName()));
                            numberOfCoffees--;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };


            Thread thread1 = new Thread(coffeeRunnable);

            thread.start();
            thread1.start();

            try {
                thread1.join();
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


        }
    }


    /***
     * Method is used to check if the order is ready
     * @param customerName, name of the customer
     * @param order, order of the customer
     *
     * @return true if the order is ready, false otherwise
     */
    public boolean isOrderInTray(String customerName, Order order) {

        if (customerName == null || order == null) {
            return false;
        } else {
            if (trayArea.containsKey(customerName) && trayArea.get(customerName).size() > 0) {
                return trayArea.get(customerName).size() == VirtualCafeUtils.getTotalNumberOfDrinks(order);
            }
        }
        return false;
    }


    /***
     * Method is used to remove an order from all the areas
     * @param customerName, name of the customer
     */
    public void removeOrderFromAllAreas(String customerName) {


        if (trayArea.containsKey(customerName)) {
            trayArea.remove(customerName);
        }


        waitingArea.removeIf(order -> order.getCustomerName().equals(customerName));

        teaBrewingArea.removeIf(tea -> tea.getCustomerName().equals(customerName));
        coffeeBrewingArea.removeIf(coffee -> coffee.getCustomerName().equals(customerName));


    }


    /***
     * Class is used to handle the brewing of tea
     */
    private static class TeaBrewingArea implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    Thread.sleep(VirtualCafeUtils.TIME_TO_MAKE_A_CUP_OF_TEA);
                    Tea teaOrder = teaBrewingArea.take();
                    addToTrayArea(teaOrder);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /***
     * Class is used to handle the brewing of coffee
     */
    private static class CoffeeBrewingArea implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    Thread.sleep(VirtualCafeUtils.TIME_TO_MAKE_A_CUP_OF_COFFEE);
                    Coffee coffeeOrder = coffeeBrewingArea.take();
                    addToTrayArea(coffeeOrder);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


    /***
     * Method is used to add an order to the tray area
     * @param drink Drink to be added
     */
    private synchronized static void addToTrayArea(Drink drink) {
        if (trayArea.containsKey(drink.getCustomerName())) {
            trayArea.get(drink.getCustomerName()).add(drink);
        } else {
            List<Drink> value = new CopyOnWriteArrayList<>();
            value.add(drink);
            trayArea.put(drink.getCustomerName(), value);
        }
    }


    private void addOrderToWaitingArea(Order order) {
        waitingArea.add(order);
        System.out.println(order.getCustomerName() + " order has been added to waiting area");
    }

    /***
     * Method is used to get the number of orders in the waiting area
     * @param customerName name of the customer
     * @return String representation of the number of orders in the datastructures
     */
    public String checkOrderStatus(String customerName) {

        StringBuffer orderStatus = new StringBuffer();

        for (Order order : waitingArea) {
            if (order.getCustomerName().equals(customerName)) {
                orderStatus.append(" - ").append(order).append("Your order is in the waiting area\n");
            }
        }

        for (Tea tea : teaBrewingArea) {
            if (tea.getCustomerName().equals(customerName)) {
                orderStatus.append(" - ").append(tea.getQuantity()).append(" tea(s) is currently being prepared in the tea brewing area\n");
            }
        }

        for (Coffee order : coffeeBrewingArea) {
            if (order.getCustomerName().equals(customerName)) {
                orderStatus.append(" - ").append(order.getQuantity()).append(" coffee(s) is currently being prepared in the coffee brewing area\n");
                break;
            }
        }


        if (trayArea.containsKey(customerName)) {
            trayArea.get(customerName).forEach(drink -> orderStatus.append(" - ").append(drink).append(" is in the tray area temporarily\n"));
        }

        return orderStatus.toString();

    }
}
