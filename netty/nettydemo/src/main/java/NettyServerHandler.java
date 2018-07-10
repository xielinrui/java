import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.ChannelInputShutdownEvent;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);
    private NetServer server;
    private int idleCount;

    public NettyServerHandler(NetServer server) {
        this.server = server;
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("[" + this.server.getName() + "] : exceptionCaught: ");
        cause.printStackTrace();
        ctx.fireExceptionCaught(cause);
    }

    public void channelActive(ChannelHandlerContext ctx) {
        logger.debug("[" + this.server.getName() + "] : channelActive: session open for " + ctx.channel().remoteAddress());
        this.server.addClientSession(ctx);
    }

    public void channelInactive(ChannelHandlerContext ctx) {
        logger.debug("[" + this.server.getName() + "] : channelInactive: session closed from " + ctx.channel().remoteAddress());
        this.server.removeClientSession(ctx);
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        this.idleCount = 0;
//        logger.debug("远程的地址为：{}，请求的端口号为:{}",ctx.channel().remoteAddress().toString(),ctx.channel().localAddress().toString());

        try {
            NetCommand cmd = (NetCommand)msg;
//            logger.debug("命令为{}",cmd.getCode());
            if (cmd instanceof UnknownCommand) {
//                logger.debug("进入到不知名的commandid中");
                this.server.getClientSession(ctx).onUnknowCommand((UnknownCommand)cmd);
            } else {
//                logger.debug("state: "+this.server.getClientSession(ctx));
                this.server.getClientSession(ctx).onCommand(cmd);
            }
        } catch (Exception var4) {
            logger.debug("[" + this.server.getName() + "] : onCommand: Exception in server messageReceived: " + var4.toString());
            var4.printStackTrace();
        }

    }

    public void handlerRemoved(ChannelHandlerContext ctx) {
        logger.debug("[" + this.server.getName() + "] : client shutdown: session closed from " + ctx.channel().remoteAddress());
        this.server.removeClientSession(ctx);
    }

    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (IdleStateEvent.class.isAssignableFrom(evt.getClass())) {
            IdleStateEvent event = (IdleStateEvent)evt;
            if (event.state() == IdleState.READER_IDLE) {
                ++this.idleCount;
                this.server.sessionIdle(ctx, this.idleCount);
            } else if (event.state() != IdleState.WRITER_IDLE && event.state() == IdleState.ALL_IDLE) {
                ;
            }
        } else if (evt instanceof ChannelInputShutdownEvent) {
            logger.debug("[" + this.server.getName() + "] : client shutdown: session closed from " + ctx.channel().remoteAddress());
            this.server.removeClientSession(ctx);
        }

    }
}
