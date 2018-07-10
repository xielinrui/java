import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import java.net.InetSocketAddress;

public class NetClient {
    private int _port;
    private String _host;
    private Bootstrap _connector;
    protected ClientListener _clientListener;
    public Channel _session;
    EventLoopGroup group;
    public int connectTimeoutInMillis = 5000;
    protected String _commandCodecMode = "message";
    protected String _serverMode = "server";
    private NetClient instance;
    private String _name = "NetClient";
    protected CommandSet _commandSet = new CommandSet();

    public String getName() {
        return this._name;
    }

    public NetClient(String host, int port, String commandCodecMode) {
        this.init(this._name, host, port, commandCodecMode, this._serverMode);
    }

    public NetClient(String name, String host, int port, String commandCodecMode, String serverMode) {
        this.init(name, host, port, commandCodecMode, serverMode);
    }

    public void init(String name, String host, int port, String commandCodecMode, String serverMode) {
        this._name = name;
        this.instance = this;
        this.group = new NioEventLoopGroup(0, new NameThreadFactory("client-work"));
        this._host = host;
        this._port = port;
        this._serverMode = serverMode;
        this._commandCodecMode = commandCodecMode;
        this._connector = new Bootstrap();
        ((Bootstrap)((Bootstrap)((Bootstrap)((Bootstrap)((Bootstrap)this._connector.group(this.group)).channel(NioSocketChannel.class)).option(ChannelOption.TCP_NODELAY, true)).option(ChannelOption.SO_LINGER, 0)).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, this.connectTimeoutInMillis)).handler(new ChannelInitializer<SocketChannel>() {
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline();
                pipeline.addLast("logging", new LoggingHandler(LogLevel.DEBUG));
                pipeline.addLast("codec", new ProtocolCodecFilter(NetClient.this.instance._name, NetClient.this._commandSet, NetClient.this._commandCodecMode, NetClient.this._serverMode));
                socketChannel.pipeline().addLast(new ChannelHandler[]{new NetClientHandler(NetClient.this.instance)});
            }
        });
    }

    public boolean connect() {
        if (this.isConnected()) {
            return true;
        } else {
            try {
                ChannelFuture cf = this._connector.connect(new InetSocketAddress(this._host, this._port)).sync();
                this._session = cf.channel();
            } catch (Exception var2) {
                var2.printStackTrace();
            }

            boolean isOK = this.isConnected();
            return isOK;
        }
    }

    public void close() {
        if (this._session != null) {
            try {
                this._session.close();
                this._session = null;
                System.out.println("NetClient has closed!");
            } catch (Exception var2) {
                var2.printStackTrace();
            }
        }

    }

    public void destroy() {
        if (this.isConnected()) {
            this.close();
        }

        this.group.shutdownGracefully();
        this._connector = null;
    }

    public boolean isConnected() {
        return this._session != null && this._session.isOpen();
    }

    public boolean sendCommand(NetCommand cmd) {
        if (this.isConnected()) {
            this._session.writeAndFlush(cmd);
            return true;
        } else {
            return false;
        }
    }

    public String getHost() {
        return this._host;
    }

    public int getPort() {
        return this._port;
    }

    public void setListener(ClientListener cl) {
        this._clientListener = cl;
    }

    public ClientListener getListener() {
        return this._clientListener;
    }

    public CommandSet getCommandSet() {
        return this._commandSet;
    }
}
