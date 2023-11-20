import models.User;
import services.ConnectionService;
import services.Teclado;

import java.util.ArrayList;

public class Server {
    public static String DEFAULT_PORT = "12345";

    public static void main(String[] args) {
        if (args.length > 1) {
            System.err.println("Expected use: java Server [PORT]\n");
            return;
        }

        String port = DEFAULT_PORT;
        if (args.length == 1) port = args[0];

        ArrayList<User> users = new ArrayList<>();

        ConnectionService connectionService;
        try {
            connectionService = new ConnectionService(port, users);
            connectionService.start();
        } catch (Exception e) {
            System.err.println("Port in use or is invalid.\n");
            return;
        }

        while (true) {
            System.out.println("Server started at http://localhost:" + port + ",");
            System.out.println("to stop type \"exit\"\n");
            System.out.print("> ");

            String command = null;
            try {
                command = Teclado.getUmString();
            } catch (Exception e) {
            }

            if (command.equalsIgnoreCase("EXIT")) {
                synchronized (users) {
                    String shutdownMessage = "Server has been shutdown";

                    for (User user : users) {
                        try {
                            user.receive(shutdownMessage);
                            user.goodbye();
                        } catch (Exception e) {
                        }
                    }
                }

                System.out.println("Server has been shutdown.\n");
                System.exit(0);
            } else System.err.println("Invalid command.\n");
        }
    }
}
