import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandEncoder extends MessageToByteEncoder<NetCommand> {
    private static final Logger logger = LoggerFactory.getLogger(CommandEncoder.class);
    private CommandSet _commandSet;
    private String serverName;
    private String serverMode;

    public CommandEncoder(String serverName, CommandSet cmdSet, String serverMode) {
        this._commandSet = cmdSet;
        this.serverMode = serverMode;
        this.serverName = serverName;
    }

    protected void encode(ChannelHandlerContext channelHandlerContext, NetCommand cmd, ByteBuf out) throws Exception {
        byte[] bytes = cmd.toBytes();
        int cmdcode = cmd.getCode();
        if (cmdcode == -1) {
            logger.error("[" + this.serverName + "] : CommandEncoder.encode msg has not addCommand to CommandSet:" + cmd.getClass().toString());
            channelHandlerContext.close();
        } else {
            int length = bytes.length;
            ByteBuf buf = Unpooled.buffer(2 + length);
            buf.writeInt(2 + length);
            buf.writeShort(cmdcode);
            buf.writeBytes(bytes);
            out.writeBytes(buf);
        }
    }
}