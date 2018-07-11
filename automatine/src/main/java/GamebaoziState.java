public abstract class GamebaoziState extends State {

    protected GamebaoziClient client;
    public abstract void onCommand(NetCommand cmd);

    @Override
    public void enter(FintStateMachine stateMachine) {

        client = (GamebaoziClient) stateMachine;
    }

    @Override
    public void exit(FintStateMachine stateMachine) {
        client = null;
    }

    @Override
    public void pause(FintStateMachine stateMachine) {

    }

    @Override
    public void resume(FintStateMachine stateMachine) {

    }
}
