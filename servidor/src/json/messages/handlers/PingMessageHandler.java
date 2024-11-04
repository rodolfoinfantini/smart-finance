package json.messages.handlers;

import json.SimpleJson;
import json.messages.PingMessage;
import network.Client;

public class PingMessageHandler extends MessageHandler<PingMessage> {

    public PingMessageHandler(final Client client) {
        super(client);
    }

    @Override
    public String getEventName() {
        return "ping";
    }

    @Override
    public void handle(final String message) throws Exception {
        final var ping = SimpleJson.parse(message, PingMessage.class);
        ping.setPing("pong");
        sendMessage(ping);
    }

}
