package com.iossocket;

import org.jboss.logging.Logger;
import org.keycloak.Config;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.authentication.ConfigurableAuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.Collections;
import java.util.List;

public class SmsOtpAuthenticatorFactory implements AuthenticatorFactory, ConfigurableAuthenticatorFactory {

    private static final Logger logger = Logger.getLogger(SmsOtpAuthenticatorFactory.class);
    private static final String ID = "sms-otp-auth";

    public String getDisplayType() {
        return "SMS OTP Authentication";
    }

    public String getReferenceCategory() {
        return ID;
    }

    public boolean isConfigurable() {
        return true;
    }

    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return new AuthenticationExecutionModel.Requirement[] {
                AuthenticationExecutionModel.Requirement.REQUIRED
        };
    }

    public boolean isUserSetupAllowed() {
        return true;
    }

    public String getHelpText() {
        return "Validates SMS OTP";
    }

    public String getId() {
        return ID;
    }

    public List<ProviderConfigProperty> getConfigProperties() {
        return Collections.emptyList();
    }

    public Authenticator create(KeycloakSession session) {
        logger.info("SmsOtpAuthenticatorFactory create");
        return new SmsOtpAuthenticator(session);
    }

    public void init(Config.Scope config) {
        logger.info("SmsOtpAuthenticatorFactory init");
    }

    public void postInit(KeycloakSessionFactory factory) {
        logger.info("SmsOtpAuthenticatorFactory post init");
    }

    public void close() { }
}
