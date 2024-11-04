package com.modsen.software.rides.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Rides service Api",
                description = "Rides service", version = "1.0.0",
                contact = @Contact(
                        name = "Aleksej",
                        email = "example@mail.com"
                )
        )
)
public class OpenApiConfig {

}
