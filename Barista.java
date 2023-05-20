import Helpers.Cafe;
import Helpers.CustomerHandler;
import Helpers.VirtualCafeUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class is used to Run the Virtual Cafe Server
 */

public class Barista {
    public static List<CustomerHandler> allCustomers = Collections.synchronizedList(new ArrayList<>());
    private static final Cafe cafe = new Cafe(allCustomers);



    public static void main(String[] args) throws IOException {
        runServer();
    }




    /**
     * RunServer method is used to run the server and listen for incoming connections
     * */

    public static void runServer() {


        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(VirtualCafeUtils.PORT);
            System.out.println("Waiting for incoming connections...");
            while (true) {
                Socket socket = serverSocket.accept();
                CustomerHandler customerThread = new CustomerHandler(socket, cafe);
                new Thread(customerThread).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}