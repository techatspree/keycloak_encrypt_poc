package de.spree.keycloak.data_encrypt;

import org.junit.jupiter.api.Test;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.testframework.annotations.InjectRealm;
import org.keycloak.testframework.annotations.KeycloakIntegrationTest;
import org.keycloak.testframework.injection.LifeCycle;
import org.keycloak.testframework.realm.ManagedRealm;
import org.keycloak.testframework.realm.UserConfigBuilder;
import org.keycloak.testframework.server.KeycloakServerConfig;
import org.keycloak.testframework.server.KeycloakServerConfigBuilder;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@KeycloakIntegrationTest(config = UserWithEncryptionTest.ServerConfig.class)
public class UserWithEncryptionTest {

    @InjectRealm(lifecycle = LifeCycle.METHOD)
    ManagedRealm realm;

    private void createUser(String username, String password, String firstName, String lastName, String email) {
        UserRepresentation user = UserConfigBuilder.create()
                .username(username)
                .password(password)
                .name(firstName, lastName)
                .email(email)
                .enabled(true)
                .build();
        realm.admin().users().create(user);
    }

    @Test
    public void searchUserNameExactSuccessfully() {
        createUser("User", "password", "firstName", "lastName", "user@example.com");
        assertThat(realm.admin().users().search("User", true), hasSize(1));
    }

    @Test
    public void searchEmailExactSuccessfully() {
        createUser("User", "password", "firstName", "lastName", "user@example.com");
        assertThat(realm.admin().users().searchByEmail("user@example.com", true), hasSize(1));
    }

    @Test
    public void searchBySearchAttributeExactSuccessfully() {
        createUser("User", "password", "firstName", "lastName", "user@example.com");
        assertThat(realm.admin().users().searchByAttributes("User", true), hasSize(1));
        assertThat(realm.admin().users().searchByAttributes("firstname", true), hasSize(1));
        assertThat(realm.admin().users().searchByAttributes("lastname", true), hasSize(1));
        assertThat(realm.admin().users().searchByAttributes("user@example.com", true), hasSize(1));
    }

    @Test
    public void searchNotExactQueriesNoWildcardsSuccessfully() {
        createUser("User", "password", "firstName", "lastName", "user@example.com");
        assertThat(realm.admin().users().searchByAttributes("User", false), hasSize(1));
        assertThat(realm.admin().users().searchByAttributes("firstname", false), hasSize(1));
        assertThat(realm.admin().users().searchByAttributes("lastname", false), hasSize(1));
        assertThat(realm.admin().users().searchByAttributes("user@example.com", false), hasSize(1));
    }

    @Test
    public void searchUsernameOrEmailNoWildcardsWillNotWork() {
        createUser("User", "password", "firstName", "lastName", "user@example.com");

        assertThat(realm.admin().users().search("User", null, null), hasSize(0));
        assertThat(realm.admin().users().search("user@example.com", null, null), hasSize(0));
    }

    @Test
    public void searchUserWithWildcardsWillNotWork() {
        createUser("User", "password", "firstName", "lastName", "user@example.com");

        assertThat(realm.admin().users().search("Use%", null, null), hasSize(0));
        assertThat(realm.admin().users().search("Use_", null, null), hasSize(0));
        assertThat(realm.admin().users().search("Us_r", null, null), hasSize(0));
        assertThat(realm.admin().users().search("Use", null, null), hasSize(0));
        assertThat(realm.admin().users().search("Use*", null, null), hasSize(0));
        assertThat(realm.admin().users().search("Us*e", null, null), hasSize(0));
    }

    @Test
    public void createUserWithLastNameAbove256CharactersShouldFail() {
        String lastNameExceeds256Chars = "lastNameTooLonglastNameTooLonglastNameTooLonglastNameTooLonglastNameTooLonglastNameTooLonglastNameTooLonglastNameTooLonglastNameTooLonglastNameTooLonglastNameTooLonglastNameTooLonglastNameTooLonglastNameTooLonglastNameTooLonglastNameTooLonglastNameTooLonglastNameTooLonglastNameTooLonglastNameTooLonglastNameTooLong";
        createUser("User", "password", "firstName",
                lastNameExceeds256Chars,
                "user@example.com");
        // User creation failed, no User can be found
        assertThat(realm.admin().users().search("User", true), hasSize(0));
    }


    public static class ServerConfig implements KeycloakServerConfig {

        @Override
        public KeycloakServerConfigBuilder configure(KeycloakServerConfigBuilder config) {
            config.log().categoryLevel("de.spree.keycloak.data_encrypt", "DEBUG");
            return config.dependency("de.spree.keycloak", "keycloak_encrypt_poc_attribute_converter");
        }
    }
}
