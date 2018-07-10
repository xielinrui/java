import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import java.net.InetSocketAddress;

public class NettyClientConnection implements ClientConnection {
    protected Channel _ssn;

    public NettyClientConnection(ChannelHandlerContext ssn) {
        this._ssn = ssn.channel();
    }

    public Channel getIoSession() {
        return this._ssn;
    }

    public void sendCommand(NetCommand cmd) {
        this._ssn.writeAndFlush(cmd);
    }

    public void close(boolean immediately) {
        if (!immediately) {
            this._ssn.flush();
        }

        this._ssn.close();
    }

    public boolean isConnected() {
        return this._ssn.isActive();
    }

    public String getClientIP() {
        String ip = null;

        try {
            ip = ((InetSocketAddress)this._ssn.remoteAddress()).getAddress().getHostAddress();
        } catch (Exception var3) {
            ;
        }

        return ip;
    }
}
