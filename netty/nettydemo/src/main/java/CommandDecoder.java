import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandDecoder extends ByteToMessageDecoder {
    private static final Logger logger = LoggerFactory.getLogger(CommandDecoder.class);
    private CommandSet _commandSet;
    private String serverMode;
    private String serverName;

    public CommandDecoder(String serverName, CommandSet cmdSet, String serverMode) {
        this._commandSet = cmdSet;
        this.serverMode = serverMode;
        this.serverName = serverName;
    }

    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        NetCommand cmd = null;

        try {
            cmd = this.readOneCommand(byteBuf, channelHandlerContext);
        } catch (Exception var6) {
            logger.error("[" + this.serverName + "] : " + var6.toString());
        }

        if (cmd != null) {
            list.add(cmd);
        }
    }

    private NetCommand readOneCommand(ByteBuf cmdDataBuf, ChannelHandlerContext context) throws Exception {
        if (!this.isCommandDataReady(cmdDataBuf, context)) {
            return null;
        } else {
            int cmdLen = cmdDataBuf.readInt();
            int cmdCode = cmdDataBuf.readUnsignedShort();
            int bodyLen = cmdLen - 2;
            ByteBuf buf = Unpooled.buffer(bodyLen);
            cmdDataBuf.readBytes(buf);
            NetCommand cmd;
//            UnknownCommand unknownCommand;
            if ("server".equals(this.serverMode)) {
                if (!this._commandSet.isExitMessage(cmdCode)) {
                    System.out.println("[" + this.serverName + "] : =====command read error, invalid command code: 0x" + Integer.toHexString(cmdCode));
//                    unknownCommand = new UnknownCommand();
//                    return unknownCommand;
                }

                cmd = new NetCommand(cmdCode);

                try {
                    cmd.setBody(this._commandSet.parseMessage(cmdCode, buf.array()));
                } catch (Exception var10) {
                    System.out.println("[" + this.serverName + "] : cmdcode:0x" + Integer.toHexString(cmdCode) + " paresform error: " + var10.toString());
//                    unknownCommand = new UnknownCommand();
//                    return unknownCommand;
                }
            } else {
                cmd = this._commandSet.newNetCommandClass(cmdCode);
                if (cmd == null) {
                    System.out.println("[" + this.serverName + "] : =====command read error, invalid command code: 0x" + Integer.toHexString(cmdCode));
//                    unknownCommand = new UnknownCommand();
//                    return unknownCommand;
                }

                try {
                    cmd.parseFrom(buf.array());
                } catch (Exception var9) {
                    System.out.println("[" + this.serverName + "] : cmdcode:0x" + Integer.toHexString(cmdCode) + " paresform error: " + var9.toString());
//                    unknownCommand = new UnknownCommand();
//                    return unknownCommand;
                }
            }

            return cmd;
        }
    }

    protected boolean isCommandDataReady(ByteBuf cmdDataBuf, ChannelHandlerContext channelHandlerContext) {
        cmdDataBuf.markReaderIndex();

        int cmdLen;
        try {
            cmdLen = cmdDataBuf.readInt();
        } catch (Exception var5) {
            cmdDataBuf.resetReaderIndex();
            return false;
        }

        if (cmdLen >= 2 && cmdLen <= 65536) {
            boolean isReady = cmdLen > 0 && cmdDataBuf.readableBytes() >= cmdLen;
            cmdDataBuf.resetReaderIndex();
            return isReady;
        } else {
            channelHandlerContext.close();
            System.out.println("[" + this.serverName + "] : MessageDecoder.isCommandDataReady command exceeds limit:" + cmdLen + ", close:");
            return false;
        }
    }
}
