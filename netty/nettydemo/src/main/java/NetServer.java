import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentHashMap;


public class NetServer {
    private static final Logger logger = LoggerFactory.getLogger(NetServer.class);
    protected int _port = 0;
    protected String _commandCodecMode = "message";
    protected String _serverMode = "server";
    private String _name = "NetServer";
    ServerBootstrap bootstrap;
    EventLoopGroup bossGroup;
    EventLoopGroup workGroup;
    protected ConcurrentHashMap<Channel, ClientConnectionListener> _clientConnectionListeners = new ConcurrentHashMap();
    public int connectionIdleTimeInSeconds = 0;
    private NetServer instance;
    protected ClientConnectionListenerFactory _clientSessionListenerFactory = null;
    protected CommandSet _commandSet = new CommandSet();
    public String getName() {
        return this._name;
    }

    public void init(String name, int port, String cmdCodecMode, String serverMode) {
        this._name = name;
        this._port = port;
        this.instance = this;
        this._commandCodecMode = cmdCodecMode;
        this._serverMode = serverMode;
    }

    public void start() throws Exception {
        try {
            this.bossGroup = new NioEventLoopGroup(0, new NameThreadFactory("server-boss"));
            this.workGroup = new NioEventLoopGroup(0, new NameThreadFactory("server-work"));
            this.bootstrap = ((ServerBootstrap)(new ServerBootstrap()).group(this.bossGroup, this.workGroup).channel(NioServerSocketChannel.class)).childHandler(new ChannelInitializer<io.netty.channel.socket.SocketChannel>() {
                protected void initChannel(io.netty.channel.socket.SocketChannel channel) throws Exception {
                    ChannelPipeline pipeline = channel.pipeline();
                    pipeline.addLast("logging", new LoggingHandler(LogLevel.DEBUG));
                    if (NetServer.this.connectionIdleTimeInSeconds > 0) {
                        pipeline.addLast("idleStateHandler", new IdleStateHandler(NetServer.this.connectionIdleTimeInSeconds, 0, 0));
                    }

                    pipeline.addLast(new ChannelHandler[]{new ProtocolCodecFilter(NetServer.this.instance.getName(), NetServer.this._commandSet, NetServer.this._commandCodecMode, NetServer.this._serverMode)});
                    pipeline.addLast(new ChannelHandler[]{new NettyServerHandler(NetServer.this.instance)});
                }
            });
            this.bindConnectionOptions(this.bootstrap);
            ChannelFuture future = this.bootstrap.bind(new InetSocketAddress(this._port));
            future.addListener((channelFuture) -> {
                if (channelFuture.isSuccess()) {
                    logger.info("NetServer has stared on port:" + this._port);
                } else {
                    logger.info("NetServer bind port has error:" + this._port);
                }

            });
            future.sync();
        } catch (Exception var2) {
            logger.error("server:【{}】 has error {} on port :{}", new Object[]{this.getName(), var2.getMessage(), this._port});
            throw var2;
        }
    }
    private void bindConnectionOptions(ServerBootstrap bootstrap) {
        ((ServerBootstrap)((ServerBootstrap)((ServerBootstrap)((ServerBootstrap)((ServerBootstrap)((ServerBootstrap)bootstrap.option(ChannelOption.SO_BACKLOG, 1024)).option(EpollChannelOption.SO_REUSEADDR, true)).option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)).option(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator())).option(ChannelOption.WRITE_BUFFER_WATER_MARK, WriteBufferWaterMark.DEFAULT)).option(ChannelOption.SO_RCVBUF, 1024)).childOption(ChannelOption.TCP_NODELAY, true).childOption(ChannelOption.SO_LINGER, 0).childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
    }
    public ClientConnectionListener getClientSession(ChannelHandlerContext ctx) {
        return (ClientConnectionListener)this._clientConnectionListeners.get(ctx.channel());
    }
    public void addClientSession(ChannelHandlerContext ctx) {
        ClientConnectionListener cl = this._clientSessionListenerFactory.createListener(new NettyClientConnection(ctx));
        this._clientConnectionListeners.put(ctx.channel(), cl);
    }
    public void removeClientSession(ChannelHandlerContext ctx) {
        try {
            ((ClientConnectionListener)this._clientConnectionListeners.remove(ctx.channel())).onDisconnected(true);
        } catch (Exception var3) {
            ;
        }

    }
    public void sessionIdle(ChannelHandlerContext ssn, int idleCount) {
        try {
            ((ClientConnectionListener)this._clientConnectionListeners.get(ssn.channel())).onIdle(idleCount);
        } catch (Exception var4) {
            ;
        }

    }
}
