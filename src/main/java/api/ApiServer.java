package api;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.net.InetSocketAddress;

public class ApiServer {
    @Autowired
    @Qualifier("tcpSocketAddress") // Autowired 어노테이션을 사용하여 ApiServerConfig의 InetSocketAddress객체를 자동으로 할당한다.
    private InetSocketAddress address;

    @Autowired
    @Qualifier("workerThreadCount") // Autowired 어노테이션을 사용하여 ApiServerConfig의 workerThreadCount필드를 자동으로 할당시킨다.
    private int workerThreadCount;

    @Autowired
    @Qualifier("bossThreadCount") // Autowired 어노테이션을 사용하여 ApiServerConfig의 bossThreadCount필드를 자동으로 할당시킨다.
    private int bossThreadCount;

    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(bossThreadCount);
        EventLoopGroup workerGroup = new NioEventLoopGroup(workerThreadCount);
        ChannelFuture channelFuture = null;

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ApiServerInitializer(null));
            // 채널 파이프라인에 설정 클래스를 지정한다. Initializer의 인자는 SSL 컨텍스트이며 지금은 HTTP만 다루기 때문에 null로 설정한다.

            Channel ch = b.bind(8080).sync().channel();

            channelFuture = ch.closeFuture();
            channelFuture.sync();   // 채널 닫힘 이벤트가 발생할 때까지 대기한다.
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}