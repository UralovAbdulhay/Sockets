import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server implements TCPConnectionListener {
    public static void main(String[] args) {
        new Server();
    }

    private final ArrayList<TCPConnection> connections = new ArrayList<>();

    private Server() {
        System.out.println("Server running...");
        try (ServerSocket serverSocket = new ServerSocket(8086)) {

            while (true) {
                try {
                    new TCPConnection(this, serverSocket.accept());

                } catch (IOException e) {
                    System.out.println("TCPConnection exception: " + e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
connections.add(tcpConnection);
        sendMessageToAll("Client connected: " + tcpConnection);
    }

    @Override
    public synchronized void onReceiveString(TCPConnection tcpConnection, String value) {
        sendMessageToAll(value);
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        connections.remove(tcpConnection);
        sendMessageToAll("Client disconnected: " + tcpConnection);
    }

    @Override
    public synchronized void onException(TCPConnection tcpConnection, Exception e) {
        System.out.println("TCPConnection exception: " + e);
    }

    private void sendMessageToAll(String value) {
        System.out.println(value);
        connections.forEach(e->e.sendString(value));
    }
}
