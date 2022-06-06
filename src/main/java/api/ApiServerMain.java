package api;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public class ApiServerMain {
    public void main(String[] args) {
        AbstractApplicationContext springContext = null;
        try {
            // 어노테이션 기반의 스프링 설정 사용(xml이 아닌 어노테이션 방식)
            springContext = new AnnotationConfigApplicationContext(ApiServerConfig.class);
            springContext.registerShutdownHook();

            ApiServer server = springContext.getBean(ApiServer.class);
            server.start();
        }
        finally {
            springContext.close();
        }
    }
}