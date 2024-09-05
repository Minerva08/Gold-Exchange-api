package com.gold.api.gold_api.grpc.config;

import com.gold.api.gold_api.grpc.AuthServiceCaller;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class GrpcClientConfig {

    @Bean
    public ManagedChannel managedChannel() {
        String host = "127.0.0.1";
        int port = 50052;


        return ManagedChannelBuilder.forAddress(host, port)
            .usePlaintext()  // Consider using .useTransportSecurity() in production
            .build();
    }

    @Bean
    public AuthServiceCaller helloServiceCaller(ManagedChannel managedChannel) {
        log.info("Creating HelloServiceCaller bean");
        return new AuthServiceCaller(managedChannel);
    }
}
