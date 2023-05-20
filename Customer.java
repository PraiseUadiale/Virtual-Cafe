import Helpers.ServerListener;
import Helpers.VirtualCafeUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Class is used to represent a customer in the Virtual Cafe
 */
public class Customer implements AutoCloseable {

    // Writer used to send messages to the server
    private final PrintWriter writer;
   private static final Socket socket;

    static {
        try {
            socket = new Socket(VirtualCafeUtils.HOST, VirtualCafeUtils.PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public Customer() throws Exception {
        writer = new PrintWriter(socket.getOutputStream(), true);

    }

    /**
     * Method is used to send the customer name to the server at the start of the connection
    * */
    public void sendCustomerName(String customerName) {
        writer.println(customerName);
    }

    /**
     * Method is used to send the exit command to the server to close the connection
     * */
    public void exit() {
        writer.println("EXIT");
    }

    /**
     * Method is used to send the order command to the server to order a tea or coffee
     * */
    public void order(String order) {
        writer.println("ORDER " + order);
    }

    /**
     * Method is used to send the order status command to the server to check the status of the order
     * */
    public void checkOrderStatus() {
        writer.println("ORDER STATUS");
    }

    /**
     * Method represents an invalid command sent to the server
     * */
    public void invalidCommand() {
        writer.println("INVALID COMMAND");

    }

    /**
     * Method is used to close the connection to the server
     * */
    @Override
    public void close() throws Exception {
        writer.close();
    }


    public static void main(String[] args) throws IOException {

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {

            System.out.println("Exiting...");

        }));


        //Starts the server listener to listen for messages from the server
        ServerListener serverListener = new ServerListener(socket);
        new Thread(serverListener).start();

        try (Customer customer = new Customer()) {
            Scanner in = new Scanner(System.in);
            String customerName = in.nextLine();
            customer.sendCustomerName(customerName);

            while (socket.isConnected()) {
                String line = in.nextLine();
                if (line.toLowerCase().startsWith("order status")) {
                    customer.checkOrderStatus();
                } else if (line.toLowerCase().startsWith("order")) {
                    customer.order(line);
                } else if (line.toLowerCase().startsWith("exit")) {
                    customer.exit();
                    break;
                } else {
                    customer.invalidCommand();
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}