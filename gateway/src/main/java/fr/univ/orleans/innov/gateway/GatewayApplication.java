package fr.univ.orleans.innov.gateway;

import org.bouncycastle.jcajce.provider.asymmetric.rsa.RSAUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route( r-> r.path("/api/service/**")
                        .filters(f->f.rewritePath("/api/service/(?<remains>.*)","/${remains}")
                                .preserveHostHeader()
                        )
                        .uri("lb://service-message")
                        .id("service-message")
                )
                .route(r -> r.path("/api/auth/**")
                        .filters(f -> f.rewritePath("/api/auth/(?<remains>.*)", "/${remains}")                                )
                        .uri("lb://service-auth")
                        .id("service-auth"))
                .build();
    }

}
