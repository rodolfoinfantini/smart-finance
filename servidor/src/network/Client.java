package network;

import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class Client {
    private boolean closed = false;

    private final Socket socket;
    private final BufferedReader input;
    private final OutputStreamWriter output;

    private String nextMessage = null;

    private final Semaphore mutex = new Semaphore(1, true);

    public Client(final Socket socket, final BufferedReader input, final OutputStreamWriter output) throws Exception {
        if (socket == null) throw new Exception("Socket cannot be null");
        if (input == null) throw new Exception("Input cannot be null");
        if (output == null) throw new Exception("Output cannot be null");

        this.socket = socket;
        this.input = input;
        this.output = output;
    }

    public void sendMessage(final String message) throws Exception {
        if (message == null) throw new Exception("Message cannot be null");

        try {
            output.write(message);
            output.write('\n');
            output.flush();
        } catch (final Exception e) {
            throw new Exception("Failed to send message: " + e.getMessage());
        }
    }

    public String spy() throws Exception {
        try {
            mutex.acquireUninterruptibly();
            if (nextMessage == null) nextMessage = input.readLine();
            mutex.release();
            return nextMessage;
        } catch (final Exception e) {
            throw new Exception("Failed to spy: " + e.getMessage());
        }
    }

    public String readMessage() throws Exception {
        try {
            if (nextMessage == null) nextMessage = input.readLine();
            final var result = nextMessage;
            nextMessage = null;
            return result;
        } catch (final Exception e) {
            throw new Exception("Failed to read message: " + e.getMessage());
        }
    }

    public void close() throws Exception {
        closed = true;
        try {
            input.close();
            output.close();
            socket.close();
        } catch (final Exception e) {
            throw new Exception("Failed to close socket: " + e.getMessage());
        }
    }

    public boolean isClosed() {
        return closed;
    }
}
