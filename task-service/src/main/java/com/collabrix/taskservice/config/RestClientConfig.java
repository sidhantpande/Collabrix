package com.collabrix.taskservice.config;

import com.collabrix.taskservice.observability.CorrelationIdClientHttpRequestInterceptor;
import com.collabrix.taskservice.resilience.InternalCallPolicy;
import com.collabrix.taskservice.resilience.InternalHttpClientSupport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class RestClientConfig {

    @Bean
    public ClientHttpRequestFactory internalClientHttpRequestFactory(
            @Value("${internal.http.connect-timeout:500ms}") Duration connectTimeout,
            @Value("${internal.http.read-timeout:2s}") Duration readTimeout
    ) {
        return InternalHttpClientSupport.requestFactory(connectTimeout, readTimeout);
    }

    @Bean("workspaceRestClient")
    public RestClient workspaceRestClient(
            @Value("${workspace.service.url}") String baseUrl,
            ClientHttpRequestFactory internalClientHttpRequestFactory,
            CorrelationIdClientHttpRequestInterceptor correlationIdInterceptor
    ) {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(internalClientHttpRequestFactory)
                .requestInterceptor(correlationIdInterceptor)
                .build();
    }

    @Bean("userRestClient")
    public RestClient userRestClient(
            @Value("${user.service.url}") String baseUrl,
            ClientHttpRequestFactory internalClientHttpRequestFactory,
            CorrelationIdClientHttpRequestInterceptor correlationIdInterceptor
    ) {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(internalClientHttpRequestFactory)
                .requestInterceptor(correlationIdInterceptor)
                .build();
    }

    @Bean("workspaceMembershipPolicy")
    public InternalCallPolicy workspaceMembershipPolicy() {
        return InternalCallPolicy.critical("task-workspace-membership");
    }

    @Bean("userProfileLookupPolicy")
    public InternalCallPolicy userProfileLookupPolicy() {
        return InternalCallPolicy.retryOnly("task-user-profile-lookup");
    }
}
