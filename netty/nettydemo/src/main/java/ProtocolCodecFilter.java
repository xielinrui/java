import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import java.util.List;

public class ProtocolCodecFilter extends ByteToMessageCodec {
    public static final int COMMAND_HEADER_BYTES = 2;
    public static final int MAX_COMMAND_DECODE_BYTES = 65536;
    MessageToByteEncoder encoder;
    ByteToMessageDecoder decoder;

    public ProtocolCodecFilter(String serverName, CommandSet commandSet, String commandCodecMode, String serverMode) {
        this.encoder = new CommandEncoder(serverName, commandSet, serverMode);
        this.decoder = new CommandDecoder(serverName, commandSet, serverMode);
    }

    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        ((CommandEncoder)this.encoder).encode(channelHandlerContext, (NetCommand)o, byteBuf);
    }

    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List list) throws Exception {
        ((CommandDecoder)this.decoder).decode(channelHandlerContext, byteBuf, list);
    }
}