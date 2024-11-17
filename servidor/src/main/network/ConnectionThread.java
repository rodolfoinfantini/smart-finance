package main.network;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ConnectionThread extends Thread {
    private final ServerSocket serverSocket;
    private final ArrayList<Client> clients;

    public ConnectionThread(final String port, final ArrayList<Client> clients) throws Exception {
        if (port == null)
            throw new Exception("Port cannot be null");
        if (clients == null)
            throw new Exception("Clients cannot be null");

        try {
            this.serverSocket = new ServerSocket(Integer.parseInt(port));
        } catch (final Exception e) {
            throw new Exception("Failed to create server socket: " + e.getMessage());
        }

        this.clients = clients;
    }

    @Override
    public void run() {
        for (;;) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
            } catch (final Exception e) {
                System.out.println("Failed to accept connection: " + e.getMessage());
                continue;
            }

            try {
                final var manager = new ConnectionManager(socket, this.clients);
                manager.start();
            } catch (final Exception e) {
                System.out.println("Failed to create connection manager: " + e.getMessage());
            }
        }
    }
}
