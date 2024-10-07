package utb.fai;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TelnetClient {

    private String serverIp;
    private int port;
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    public TelnetClient(String serverIp, int port) {
        this.serverIp = serverIp;
        this.port = port;
    }

    public void run() {
        try {
            socket = new Socket(serverIp, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            ExecutorService executor = Executors.newFixedThreadPool(2);

            // Thread for receiving data from the server
            executor.submit(this::receiveFromServer);

            // Thread for sending data to the server
            executor.submit(this::sendToServer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveFromServer() {
        try {
            String response;
            while (socket != null && !socket.isClosed() && (response = reader.readLine()) != null) {
                System.out.println(response);
            }
            System.out.println("Connection closed by the server.");
        } catch (IOException e) {
            if (socket.isClosed()) {
                System.out.println("Stream closed due to socket closure.");
            } else {
                e.printStackTrace();
            }
        }
    }

    private void sendToServer() {
        try (BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {
            String input;
            while ((input = userInput.readLine()) != null) {
                if (input.equalsIgnoreCase("/QUIT")) {
                    closeConnection();  // Ensure connection is properly closed.
                    break;
                }
                writer.write(input);
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        try {
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            System.out.println("Connection closed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
