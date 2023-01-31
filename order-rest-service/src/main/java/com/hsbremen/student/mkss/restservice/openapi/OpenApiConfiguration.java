package com.hsbremen.student.mkss.restservice.openapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Order Rest Service API",
                description = "" +
                        "This is the documentation for the Rest API doc. " +
                        "Exercise Lab 08 MKSS ",
                contact = @Contact(name = "Lab Group 1", url = "https://github.com/s4profi/L07-mkss-rest-rabbitmq", email = "tha001@stud.hs-bremen.de"),
                version = "1.0.0"),
        servers = @Server(url = "http://localhost:8080")

)
public class OpenApiConfiguration {
}
