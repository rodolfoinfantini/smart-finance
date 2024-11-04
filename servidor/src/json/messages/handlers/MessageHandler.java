package json.messages.handlers;

import json.SimpleJson;
import network.Client;

public abstract class MessageHandler<T> {
    private final Client client;

    public MessageHandler(final Client client) {
        this.client = client;
    }

    public abstract String getEventName();

    public abstract void handle(final String message) throws Exception;

    protected void sendMessage(final T message) {
        try {
            final var messageToSend = getEventName() + " " + SimpleJson.toJson(message);
            this.client.sendMessage(messageToSend);
        } catch (final Exception e) {
            System.err.println("Failed to send event: " + e.getMessage());
        }
    }
}
