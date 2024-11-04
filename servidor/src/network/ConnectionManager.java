package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import json.messages.handlers.ExitMessageHandler;
import json.messages.handlers.MessageHandler;
import json.messages.handlers.PingMessageHandler;

public class ConnectionManager extends Thread {
    private final ArrayList<Client> clients;
    private final Socket socket;

    public ConnectionManager(final Socket socket, final ArrayList<Client> clients) throws Exception {
        if (socket == null) throw new Exception("Socket is null");
        if (clients == null) throw new Exception("Clients is null");

        this.socket = socket;
        this.clients = clients;
    }

    @Override
    public void run() {
        OutputStreamWriter output;
        try {
            output = new OutputStreamWriter(this.socket.getOutputStream());
        } catch (final Exception e) {
            try {
                this.socket.close();
            } catch (final IOException e2) {
                System.err.println("Failed to close socket: " + e2.getMessage());
            }
            throw new RuntimeException("Failed to run: " + e.getMessage());
        }
        BufferedReader input;
        try {
            input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        } catch (final Exception e) {
            try {
                this.socket.close();
            } catch (final IOException e2) {
                System.err.println("Failed to close socket: " + e2.getMessage());
            }
            throw new RuntimeException("Failed to run: " + e.getMessage());
        }

        try {
            final var client = new Client(this.socket, input, output);

            synchronized (this.clients) {
                this.clients.add(client);
            }

            final var handlers = List.of(
                    new ExitMessageHandler(client, this.clients),
                    new PingMessageHandler(client)
            );

            while (!client.isClosed()) {
                final var message = client.readMessage();
                if (message == null) continue;

                final var firstSpace = message.indexOf(' ');
                final var event = firstSpace == -1 ? message : message.substring(0, firstSpace);
                final var json = firstSpace == -1 ? null : message.substring(firstSpace + 1);

                for (final var handler : handlers) {
                    if (handler.getEventName().equals(event)) {
                        handler.handle(json);
                    }
                }
            }
        } catch (final Exception e) {
            System.err.println("Failed to run: " + e.getMessage());
        } finally {
            try {
                output.close();
                input.close();
            } catch (final IOException e) {
                System.err.println("Failed to close stream: " + e.getMessage());
            }
        }
    }
}
