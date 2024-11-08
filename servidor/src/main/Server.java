package main;

import main.input.Keyboard;
import main.network.Client;
import main.network.ConnectionThread;

import java.util.ArrayList;

public class Server {
    public static final String DEFAULT_PORT = "3001";

    public static void main(String[] args) {
        if (args.length > 1) {
            System.out.println("Usage: java main.Server [port]");
            System.exit(1);
            return;
        }

        final var port = args.length == 1 ? args[0] : DEFAULT_PORT;

        final var clients = new ArrayList<Client>();
        try {
            final var connectionThread = new ConnectionThread(port, clients);
            connectionThread.start();
        } catch (final Exception e) {
            System.out.println("Failed to start server: " + e.getMessage());
            System.exit(1);
            return;
        }

        for (; ; ) {
            System.out.println("main.Server is running on port " + port);
            System.out.println("Type 'exit' to stop the server\n");
            System.out.print("> ");

            final var command = Keyboard.readLine();
            if (command == null) {
                System.out.println("Failed to read command");
                continue;
            }

            if (command.equalsIgnoreCase("exit")) {
                synchronized (clients) {
                    for (final var client : clients) {
                        try {
                            client.sendMessage("Server is shutting down");
                            client.close();
                        } catch (final Exception e) {
                            System.out.println("Failed to send message to client: " + e.getMessage());
                        }
                    }
                }
                System.exit(0);
                return;
            } else {
                System.out.println("Unknown command: " + command);
            }
        }
    }
}
