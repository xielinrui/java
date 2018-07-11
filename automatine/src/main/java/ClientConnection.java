public interface ClientConnection {
    void sendCommand(NetCommand var1);

    void close(boolean var1);

    String getClientIP();

    boolean isConnected();
}