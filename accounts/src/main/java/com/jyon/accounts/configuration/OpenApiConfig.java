package com.jyon.accounts.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI accountsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Accounts Microservice REST API Documentation")
                        .description("DigiBank Accounts microservice REST API documentation")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Jyoti Narayan Mishra")
                                .email("jyotinarayanmishra05@gmail.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}
