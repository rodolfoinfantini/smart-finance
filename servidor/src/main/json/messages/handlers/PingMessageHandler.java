package main.json.messages.handlers;

import main.json.messages.PingMessage;
import main.network.Client;

public class PingMessageHandler extends MessageHandler<PingMessage> {

    public PingMessageHandler(final Client client) {
        super(client);
    }

    @Override
    public String getEventName() {
        return "ping";
    }

    @Override
    public Class<PingMessage> getEventClass() {
        return PingMessage.class;
    }

    @Override
    public void handle(final PingMessage message) {
        message.setPing("pong");
        sendMessage(message);
    }
}
