package cn.com.msca.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class WebClientConfig implements WebFluxConfigurer {

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // 限制只对特定路径开放
                .allowedOrigins("*")// 明确允许的源
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的 HTTP 方法
                .allowedHeaders("Content-Type", "Authorization", "Accept") // 允许的请求头
                .exposedHeaders("X-Custom-Header", "Location") // 暴露给客户端的响应头
                .allowCredentials(true) // 是否允许发送 Cookie
                .maxAge(3600); // 预检请求（preflight）的有效期，单位：秒
    }
    @Bean
    public RouterFunction<ServerResponse> staticResourceRouter() {
        // 匹配所有路径，交给 static/ 下资源处理
        return RouterFunctions.resources("/**", new ClassPathResource("static/"));
    }

}