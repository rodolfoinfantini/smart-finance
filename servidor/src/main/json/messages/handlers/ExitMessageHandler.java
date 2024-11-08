package json.messages.handlers;

import java.util.ArrayList;

import network.Client;

public class ExitMessageHandler extends MessageHandler<Void> {
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
    public void handle(final String message) throws Exception {
        System.out.println("Client disconnected");

        synchronized (this.clients) {
            this.clients.remove(this.client);
        }
        this.client.close();
    }

}
