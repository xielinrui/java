public abstract class State {
    public State() {
    }

    public abstract void enter(FintStateMachine var1);

    public abstract void exit(FintStateMachine var1);

    public abstract void pause(FintStateMachine var1);

    public abstract void resume(FintStateMachine var1);
}
