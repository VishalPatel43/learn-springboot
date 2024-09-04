package com.springboot.coding.prod_ready_features.config;

import com.springboot.coding.prod_ready_features.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
public class RestClientConfig {

    @Value("${employeeService.base.url}")
    private String BASE_URL;

    // What if we have multiple REST Clients in our application or
    // multiple microservices and communicate with each other therefore we should use @Qualifier annotation
    // for each REST Client bean to avoid ambiguity.
    @Bean
//    @Qualifier("getEmployeeServiceRestClient") // unique Identifier
    @Qualifier("employeeRestClient")
    public RestClient getEmployeeServiceRestClient() {
        // Here we handle the 5xx Server Error coz server is common for all the clients
        return RestClient
                .builder()
                .baseUrl(BASE_URL)
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .defaultStatusHandler(HttpStatusCode::is5xxServerError, (req, res) -> {
                    throw new RuntimeException("Server Error Occur");
                })
                .build();
    }
}
