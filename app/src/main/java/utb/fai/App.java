package utb.fai;

public class App {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java TelnetClient <IP Address> <Port>");
            return;
        }

        try {
            String serverIp = args[0];
            int port = Integer.parseInt(args[1]);

            TelnetClient telnetClient = new TelnetClient(serverIp, port);
            telnetClient.run(); // Start the Telnet Client

        } catch (NumberFormatException e) {
            System.out.println("Invalid port number. Please provide a valid integer.");
        }
    }
}
