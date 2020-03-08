package com.iossocket;

import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

import javax.ws.rs.core.MultivaluedMap;

public class SmsOtpAuthenticator implements Authenticator {

    private static final Logger logger = Logger.getLogger(SmsOtpAuthenticator.class);
    private KeycloakSession session;

    public SmsOtpAuthenticator(KeycloakSession session) {
        logger.info("SmsOtpAuthenticator constructor");
        this.session = session;
    }

    public void authenticate(AuthenticationFlowContext context) {
        logger.info("SmsOtpAuthenticator authenticate");
        MultivaluedMap<String, String> params = context.getHttpRequest().getDecodedFormParameters();
        String otpId = params.getFirst("otpId");
        String otpValue = params.getFirst("otpValue");
        String username = params.getFirst("username");
        if (otpId == null || otpValue == null || username == null) {
            logger.error("invalid params");
            context.failure(AuthenticationFlowError.INTERNAL_ERROR);
            return;
        }
        // some mock validation, to validate the username is bind to the otpId and otpValue
        if (!otpId.equals("123") || !otpValue.equals("1111")) {
            context.failure(AuthenticationFlowError.INVALID_CREDENTIALS);
            return;
        }

        UserModel userModel = session.users().getUserByUsername(username, context.getRealm());
        if (userModel == null) {
            context.failure(AuthenticationFlowError.INVALID_USER);
            return;
        }
        context.setUser(userModel);
        context.success();
    }

    public boolean requiresUser() {
        return false;
    }

    public boolean configuredFor(KeycloakSession keycloakSession, RealmModel realmModel, UserModel userModel) {
        return true;
    }

    public void action(AuthenticationFlowContext authenticationFlowContext) { }

    public void setRequiredActions(KeycloakSession keycloakSession, RealmModel realmModel, UserModel userModel) { }

    public void close() { }
}
