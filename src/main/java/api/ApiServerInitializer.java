package api;

import api.core.ApiRequestParser;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.ssl.SslContext;

public class ApiServerInitializer extends ChannelInitializer<SocketChannel> {
    private final SslContext sslCtx;    // 채널 보안을 위해 SSL 컨텍스트 객체를 지정한다. 아직은 null이다.
    public ApiServerInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();  //  채널로 수신된 HTTP 데이터를 처리하기 위한 채널 파이프라인 객체를 설정한다.
        if (sslCtx != null) {
            p.addLast(sslCtx.newHandler(ch.alloc()));
        }

        p.addLast(new HttpRequestDecoder());    //  디코더 등록
        p.addLast(new HttpObjectAggregator(65536)); //  메세지 파편화를 처리하는 디코더(데이터가 나뉘어서 수신되었을때 하나로 합쳐준다.)
        p.addLast(new HttpRequestEncoder());   //   인코더 등록
        p.addLast(new HttpContentCompressor()); //  HTTP 본문 데이터를 gzip 압축 알고리즘으로 압축 및 압축 해제를 수행
        p.addLast(new ApiRequestParser()); //   클라이언트로 수신된 HTTP 데이터에서 비즈니스 로직 클래스로 분기한다.(컨트롤러)
    }
}