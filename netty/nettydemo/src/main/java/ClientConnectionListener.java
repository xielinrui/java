

public interface ClientConnectionListener {
    void onCommand(NetCommand var1);
    void onUnknowCommand(UnknownCommand var1);
    void onDisconnected(boolean var1);

    void onIdle(int var1);
}
