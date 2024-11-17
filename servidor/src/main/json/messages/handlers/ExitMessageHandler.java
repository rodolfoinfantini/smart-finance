package main.json.messages.handlers;

import java.util.ArrayList;

import main.network.Client;

public class ExitMessageHandler extends MessageHandler<Object> {
    private final ArrayList<Client> clients;
    private final Client client;

    public ExitMessageHandler(Client client, ArrayList<Client> clients) {
        super(client);
        this.clients = clients;
        this.client = client;
    }

    @Override
    public String getEventName() {
        return "exit";
    }

    @Override
    public Class<Object> getEventClass() {
        return Object.class;
    }

    @Override
    public void handle(final Object message) {
        synchronized (this.clients) {
            this.clients.remove(this.client);
        }
        try {
            this.client.close();
        } catch (final Exception e) {
            System.out.println("Failed to close client: " + e.getMessage());
        }
    }

}
