package main.json.messages.handlers;

import main.json.SimpleJson;
import main.network.Client;

public abstract class MessageHandler<T> {
    private final Client client;

    public MessageHandler(final Client client) {
        this.client = client;
    }

    public abstract String getEventName();

    public abstract Class<T> getEventClass();

    public abstract void handle(final T message);

    public void handle(final String message) throws Exception {
        if (message == null) {
            handle((T) null);
        } else {
            handle(SimpleJson.parse(message, getEventClass()));
        }
    }

    protected void sendMessage(final Object message) {
        try {
            final var messageToSend = getEventName() + " " + SimpleJson.toJson(message);
            this.client.sendMessage(messageToSend);
        } catch (final Exception e) {
            System.err.println("Failed to send event: " + e.getMessage());
        }
    }
}
