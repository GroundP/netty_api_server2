package api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.net.InetSocketAddress;

/**
 * 어노테이션 기반 설정 클래스, 주요 설정은 api-server.properties 파일을 참조
 */
@Configuration
@ImportResource("classpath:spring/hsqlApplicationContext.xml")
@ComponentScan("api, api.core, api.service")    // 스프링이 컴포넌트를 검색할 위치를 지정
@PropertySource("classpath:api-server.properties")  // api 서버 설정 파일 위치 지정
public class ApiServerConfig {
    @Value("${boss.thread.count}")
    private int bossThreadCount;

    @Value("${worker.thread.count}")
    private int workerThreadCount;

    @Value("${tcp.port}")
    private int tcpPort;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    // Api-server.properties에서 읽어들인 boss.thread.count값을 Bean 어노테이션을 사용하여 다른 클래스에서 참조할 수 있도록 설정한다.
    @Bean(name = "bossThreadCount")
    public int getBossThreadCount() {
        return bossThreadCount;
    }

    // Api-server.properties에서 읽어들인 woker.thread.count값을 Bean 어노테이션을 사용하여 다른 클래스에서 참조할 수 있도록 설정한다.
    @Bean(name = "workerThreadCount")
    public int getWorkerThreadCount() {
        return workerThreadCount;
    }

    public int getTcpPort() {
        return tcpPort;
    }

    @Bean(name = "tcpSocketAddress")
    public InetSocketAddress makeTcpPort() {
        return new InetSocketAddress(tcpPort);
    }
}