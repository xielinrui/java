import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NameThreadFactory implements ThreadFactory {
    private int _prio;
    private String _name;
    private AtomicInteger _threadNumber = new AtomicInteger(1);
    private ThreadGroup _group;

    public String get_name() {
        return this._name;
    }

    public NameThreadFactory(String name) {
        this._prio = 5;
        this._name = name;
        this._group = new ThreadGroup(this._name);
    }

    public NameThreadFactory(String name, int _prio) {
        this._name = name;
        this._group = new ThreadGroup(this._name);
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(this._group, r);
        t.setName(this._name + "-#-" + this._threadNumber.getAndIncrement());
        t.setPriority(this._prio);
        return t;
    }

    public ThreadGroup getGroup() {
        return this._group;
    }
}