import java.util.Stack;

public class FintStateMachine {
    protected EmptyState nullState = new EmptyState();
    private Stack<State> states = new Stack();

    public State getCurrentState() {
        return (State)(this.states.size() == 0 ? this.nullState : (State)this.states.peek());
    }

    public FintStateMachine() {
    }

    public void ChangeTo(State newSate) {
        this.getCurrentState().exit(this);
        if (this.states.size() > 0) {
            this.states.pop();
        }

        this.states.push(newSate);
        System.out.println("状态----------"+newSate.getClass());
        this.getCurrentState().enter(this);
    }
}