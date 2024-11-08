package main.json.messages;

public class PingMessage {
    private String ping;

    public PingMessage() {}

    public String getPing() {
        return ping;
    }

    public void setPing(final String ping) {
        this.ping = ping;
    }

    public String toString() {
        return "PingMessage(ping=" + ping + ")";
    }
}
