package com.polarbookshop.orderservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;

@ConfigurationProperties("polar")
public record ClientProperties(
        URI catalogServiceUri
) {
}
