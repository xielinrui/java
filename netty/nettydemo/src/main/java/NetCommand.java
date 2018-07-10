

import com.google.protobuf.MessageLite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetCommand {
    private static final Logger logger = LoggerFactory.getLogger(NetCommand.class);
    private int _cmdCode;
    private MessageLite body;

    public int getCode() {
        return this._cmdCode;
    }

    public NetCommand(int cmdCode) {
        this._cmdCode = cmdCode;
    }

    public void parseFrom(byte[] data) throws Exception {
    }

    public byte[] toBytes() {
        try {
//            logger.info("accept:=========命令为：{}",this._cmdCode);
            return this.body.toByteArray();
        } catch (Exception var2) {
            logger.error("error:============0x:" + Integer.toHexString(this._cmdCode));
            var2.printStackTrace();
            return null;
        }
    }

    public void setBody(MessageLite body) {
        this.body = body;
    }

    public MessageLite getBody() {
        return this.body;
    }
}