package main.json.messages;

public class EventMessage {
    private String event;
    private String data;

    public EventMessage(final String event, final String data) {
        this.event = event;
        this.data = data;
    }

    public EventMessage() {}

    public String getEvent() {
        return this.event;
    }

    public String getData() {
        return this.data;
    }

    public String toString() {
        return "EventMessage(event=" + this.getEvent() + ", data=" + this.getData() + ")";
    }
}
