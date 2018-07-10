import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetClientHandler extends SimpleChannelInboundHandler<NetCommand> {
    private static final Logger logger = LoggerFactory.getLogger(NetClientHandler.class);
    private NetClient client;

    public NetClientHandler(NetClient client) {
        this.client = client;
    }

    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("[" + this.client.getName() + "] : channelActive: session open for " + ctx.channel().remoteAddress());
        ctx.fireChannelActive();
    }

    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("[" + this.client.getName() + "] : channelInactive: session closed from " + ctx.channel().remoteAddress());
        ctx.fireChannelInactive();
        this.client.getListener().OnDisconnected();
    }

    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, NetCommand cmd) throws Exception {
        try {
            this.client.getListener().onCommand(cmd);
        } catch (Exception var4) {
            logger.debug("[" + this.client.getName() + "] : channelRead0: Exception in client messageReceived: " + var4);
        }

    }
}
