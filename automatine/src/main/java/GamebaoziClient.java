public class GamebaoziClient extends FintStateMachine {

    private ClientConnection _conn;

    public GamebaoziClient(ClientConnection conn) {
        _conn = conn;
        nullState = new EmptyState();
        ChangeTo(new GamebaoziConnectedState());
    }
}
