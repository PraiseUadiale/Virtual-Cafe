package Helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Class is used to listen to any messages from the server and prints out the messages to the console.
 * This class is used by the CustomerHandler class
 */

public class ServerListener implements Runnable {


    private final Socket socket;
    private final BufferedReader reader;

    public ServerListener(Socket socket) throws IOException {
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            while (socket.isConnected()) {
                String line = reader.readLine();
                System.out.println(line);
            }
        } catch (IOException e) {
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}

