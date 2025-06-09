package com.techlearning.Order.config;

import com.techlearning.Order.clients.ProductServiceClient;
import com.techlearning.Order.clients.UserServiceClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.Optional;

@Configuration
public class UserServiceClientConfig {

    @Bean
    public UserServiceClient userRestClientInterface(RestClient.Builder restClientBuilder){
        RestClient restClient = restClientBuilder
                .baseUrl("http://USER-SERVICE")
                .defaultStatusHandler(HttpStatusCode::is4xxClientError,
                        ((request, response) -> Optional.empty()))
                .build();
        RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        UserServiceClient serviceClient = factory.createClient(UserServiceClient.class);

        return serviceClient;
    }
}
