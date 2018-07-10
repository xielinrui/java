import com.google.protobuf.MessageLite;
import java.io.IOException;
import java.util.HashMap;

public class CommandSet {
    private static HashMap<Integer, CommandSet.ParsingMessage> parseMap = new HashMap();
    private static HashMap<Class<?>, Integer> msg2ptoNum = new HashMap();
    private HashMap<Integer, Class> _classMap = null;
    private Class[] _classArray = new Class[0];

    public CommandSet() {
    }

    public void addMessageClasses(MessageClass[] classes) {
        for(int i = 0; i < classes.length; ++i) {
            MessageClass mclass = classes[i];
            this.addMessageClass(mclass.cmdCode, mclass.parse, mclass.cla);
        }

    }

    public void addMessageClass(int cmdCode, CommandSet.ParsingMessage parse, Class<?> cla) {
        if (parseMap.get(cmdCode) == null) {
            parseMap.put(cmdCode, parse);
            if (msg2ptoNum.get(cla) == null) {
                msg2ptoNum.put(cla, cmdCode);
            } else {
                System.out.print("command has been registered in msg2ptoNum, cmdCode:" + Integer.toHexString(cmdCode));
            }
        } else {
            System.out.print("command has been registered in parseMap, cmdCode:" + Integer.toHexString(cmdCode));
        }
    }

    public MessageLite parseMessage(int cmdcode, byte[] bytes) throws Exception {
        if (!this.isExitMessage(cmdcode)) {
            return null;
        } else {
            CommandSet.ParsingMessage parser = (CommandSet.ParsingMessage)parseMap.get(cmdcode);
            MessageLite msg = parser.process(bytes);
            return msg;
        }
    }

    public boolean isExitMessage(int cmdCode) {
        return parseMap.containsKey(cmdCode);
    }

    public int getCommandCode(MessageLite message) {
        Class cl = message.getClass();
        return msg2ptoNum.containsKey(cl) ? (Integer)msg2ptoNum.get(cl) : -1;
    }

    private void makeClassMap() {
        this._classMap = new HashMap();

        for(int i = 0; i < this._classArray.length; ++i) {
            try {
                NetCommand cmd = (NetCommand)this._classArray[i].newInstance();
                this._classMap.put(new Integer(cmd.getCode()), this._classArray[i]);
            } catch (Exception var3) {
                System.out.println("Unable to create instance for class: " + this._classArray[i].getCanonicalName());
            }
        }

    }

    public void addCommandClass(Class c) {
        if (this._classMap == null) {
            this.makeClassMap();
        }

        try {
            NetCommand cmd = (NetCommand)c.newInstance();
            if (this._classMap.get(cmd.getCode()) == null) {
                this._classMap.put(new Integer(cmd.getCode()), c);
            } else {
                System.out.println("=========== addNetCommandClass() error，Command code of " + c.getCanonicalName() + " already exists: 0x" + Integer.toHexString(cmd.getCode()));
            }
        } catch (Exception var3) {
            System.out.println("Unable to create instance for class: " + c.getCanonicalName());
        }

    }

    public void addCommandClasses(Class[] classes) {
        if (this._classMap == null) {
            this.makeClassMap();
        }

        for(int i = 0; i < classes.length; ++i) {
            this.addCommandClass(classes[i]);
        }

    }

    public NetCommand newNetCommandClass(int cmdCode) {
        if (this._classMap == null) {
            this.makeClassMap();
        }

        NetCommand cmd = null;

        try {
            cmd = (NetCommand)((Class)this._classMap.get(new Integer(cmdCode))).newInstance();
        } catch (Exception var4) {
            System.out.println("CommandSet.newCommandClass() failed for command code: 0x" + Integer.toHexString(cmdCode));
        }

        return cmd;
    }

    @FunctionalInterface
    public interface ParsingMessage {
        MessageLite process(byte[] var1) throws IOException;
    }
}
