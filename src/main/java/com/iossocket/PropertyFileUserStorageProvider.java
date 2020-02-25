package com.iossocket;

import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputUpdater;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.models.*;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.storage.ReadOnlyException;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.adapter.AbstractUserAdapter;
import org.keycloak.storage.user.UserLookupProvider;

import java.util.*;

public class PropertyFileUserStorageProvider implements
        UserStorageProvider,
        UserLookupProvider,
        CredentialInputValidator,
        CredentialInputUpdater {

    protected KeycloakSession session;
    protected Properties properties;
    protected ComponentModel model;
    protected Map<String, UserModel> loadedUsers = new HashMap();

    public PropertyFileUserStorageProvider(KeycloakSession session, Properties properties, ComponentModel model) {
        this.session = session;
        this.properties = properties;
        this.model = model;
    }

    public void close() { }

    public UserModel getUserById(String id, RealmModel realmModel) {
        StorageId storageId = new StorageId(id);
        String username = storageId.getExternalId();
        return getUserByUsername(username, realmModel);
    }

    public UserModel getUserByUsername(String username, RealmModel realmModel) {
        UserModel adapter = loadedUsers.get(username);
        if (adapter == null) {
            String password = properties.getProperty(username);
            if (password != null) {
                adapter = createAdapter(realmModel, username);
                loadedUsers.put(username, adapter);
            }
        }
        return adapter;
    }

    protected UserModel createAdapter(RealmModel realm, final String username) {
        return new AbstractUserAdapter(session, realm, model) {
            public String getUsername() {
                return username;
            }
        };
    }

    public UserModel getUserByEmail(String s, RealmModel realmModel) {
        return null;
    }

    public boolean supportsCredentialType(String credentialType) {
        return credentialType.equals(PasswordCredentialModel.TYPE);
    }

    public boolean isConfiguredFor(RealmModel realmModel, UserModel userModel, String credentialType) {
        String password = properties.getProperty(userModel.getUsername());
        return credentialType.equals(PasswordCredentialModel.TYPE) && password != null;
    }

    public boolean isValid(RealmModel realmModel, UserModel userModel, CredentialInput credentialInput) {
        if (!supportsCredentialType(credentialInput.getType())) return false;
        String password = properties.getProperty(userModel.getUsername());
        if (password == null) return false;
        return password.equals(credentialInput.getChallengeResponse());
    }

    public boolean updateCredential(RealmModel realmModel, UserModel userModel, CredentialInput credentialInput) {
        if (credentialInput.getType().equals(PasswordCredentialModel.TYPE))
            throw new ReadOnlyException("user is read only for this update");
        return false;
    }

    public void disableCredentialType(RealmModel realmModel, UserModel userModel, String s) {

    }

    public Set<String> getDisableableCredentialTypes(RealmModel realmModel, UserModel userModel) {
        return Collections.EMPTY_SET;
    }
}
