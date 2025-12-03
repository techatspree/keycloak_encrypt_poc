package de.spree.keycloak.data_encrypt;

import org.keycloak.testframework.server.KeycloakServerConfig;
import org.keycloak.testframework.server.KeycloakServerConfigBuilder;

public class KeycloakEncryptAttributeConverterServerConfig implements KeycloakServerConfig {

    @Override
    public KeycloakServerConfigBuilder configure(KeycloakServerConfigBuilder config) {
        config.log().categoryLevel("de.spree.keycloak.data_encrypt", "DEBUG");
        return config.dependency("de.spree.keycloak", "keycloak_encrypt_poc_attribute_converter");
    }
}