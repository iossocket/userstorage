package com.iossocket;

import org.jboss.logging.Logger;
import org.keycloak.Config;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.UserStorageProviderFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class PropertyFileUserStorageProviderFactory implements
        UserStorageProviderFactory<PropertyFileUserStorageProvider> {

    public static final String PROVIDER_NAME = "readonly-property-file";
    private static final Logger logger = Logger.getLogger(PropertyFileUserStorageProviderFactory.class);
    protected Properties properties = new Properties();
    private ComponentModel componentModel;

    public void init(Config.Scope config) {
        InputStream is = getClass().getClassLoader().getResourceAsStream("/users.properties");

        if (is == null) {
            logger.warn("Could not find users.properties in classpath");
        } else {
            try {
                properties.load(is);
            } catch (IOException ex) {
                logger.error("Failed to load users.properties file", ex);
            }
        }
    }

    public PropertyFileUserStorageProvider create(KeycloakSession keycloakSession, ComponentModel componentModel) {
        this.componentModel = componentModel;
        return new PropertyFileUserStorageProvider(keycloakSession, properties, componentModel);
    }

    public String getId() {
        return PROVIDER_NAME;
    }

    public void close() {
    }

    public void postInit(KeycloakSessionFactory factory) {
    }

    public UserStorageProvider create(KeycloakSession session) {
        return new PropertyFileUserStorageProvider(session, properties, componentModel);
    }

    public List<ProviderConfigProperty> getConfigProperties() {
        return Collections.emptyList();
    }

    public String getHelpText() {
        return null;
    }
}
