package Helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


/**
 * Class is used to handle individual interactions with the Client on the server side
 * This class is used by the Barista class
 */
public class CustomerHandler implements Runnable {


    private final Socket socket;
    private final Cafe cafe;

    private final BufferedReader in;
    private final PrintWriter out;


    public CustomerHandler(Socket socket, Cafe cafe) throws IOException {
        this.socket = socket;
        this.cafe = cafe;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);


    }


    @Override
    public void run() {
        String customerName = null;
        try {

            out.println("Barista: Welcome to the Virtual Cafe! Please enter your first-name: ");
            customerName = in.readLine();
            cafe.addCustomerHandler(this);

            //Sends menu to the client


            while (socket.isConnected()) {
                out.println("Enter 'order <order>' to order i.e order 1 coffee and 2 teas");
                out.println("Enter 'order status' to check status or 'exit' to exit");

                String line = in.readLine();
                if (line.toLowerCase().startsWith("order status")) {
                    String orderStatus = cafe.checkOrderStatus(customerName);
                    if (orderStatus.isEmpty()) {
                        out.println("No order found for " + customerName);
                        continue;
                    }
                    out.println("Order Status for " + customerName + ":");
                    out.println(orderStatus);

                } else if (line.toLowerCase().startsWith("order")) {

                    Order newOrder = VirtualCafeUtils.inferOrderFromInput(line);

                    if (newOrder.getNumberOfCoffees() == 0 && newOrder.getNumberOfTeas() == 0) {
                        out.println("Barista: I'm sorry, I don't understand your request. Please try again.");
                        continue;
                    } else {
                        newOrder.setCustomerName(customerName);

                        cafe.order(newOrder);

                        String finalCustomerName = customerName;
                        Thread orderListener = new Thread(() -> {
                            try {

                                while (true) {
                                    if (cafe.isOrderInTray(finalCustomerName, newOrder)) {
                                        out.println("Order delivered to  " + finalCustomerName + " " + newOrder + " is ready.");
                                        cafe.removeOrderFromAllAreas(finalCustomerName);
                                        Thread.currentThread().interrupt();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                        orderListener.start();
                    }

                    out.println("Barista: order received for " + customerName + " (" + line.split("order")[1] + " )");


                } else if (line.toLowerCase().startsWith("exit")) {
                    out.println("Barista: Thank you for visiting the Virtual Cafe. Have a nice day!");
                    socket.close();
                } else {
                    out.println("Barista: I'm sorry, I don't understand your request. Please try again.");

                }


            }

        } catch (Exception ignored) {

        } finally {
            System.out.println("Customer " + customerName + " left the cafe.");
            cafe.removeOrderFromAllAreas(customerName);
            System.out.println("Any item " + customerName + " may own has been discarded");
            cafe.removeCustomerHandler(this);


            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }


    }


}
