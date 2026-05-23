package com.collabrix.authservice.integration;

import com.collabrix.authservice.exceptions.GenericAplicationException;
import com.collabrix.authservice.events.AuthEventPublisher;
import com.collabrix.authservice.model.dtos.request.LoginUserDTO;
import com.collabrix.authservice.model.dtos.request.RegisterUserCredentialsDTO;
import com.collabrix.authservice.model.entities.UserCredential;
import com.collabrix.authservice.model.enums.ErrorTP;
import com.collabrix.authservice.model.enums.Role;
import com.collabrix.authservice.repository.UserCredentialRepository;
import com.collabrix.authservice.services.AuthenticationService;
import com.collabrix.authservice.testsupport.AbstractPostgresIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static com.collabrix.authservice.testsupport.AuthTestFixtures.loginRequest;
import static com.collabrix.authservice.testsupport.AuthTestFixtures.registerRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class AuthServiceIntegrationTest extends AbstractPostgresIntegrationTest {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserCredentialRepository userCredentialRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private AuthEventPublisher authEventPublisher;

    @BeforeEach
    void cleanUp() {
        userCredentialRepository.deleteAll();
    }

    @Test
    void contextShouldStartWithRealInfrastructureBeans() {
        // Then
        assertThat(authenticationService).isNotNull();
        assertThat(userCredentialRepository).isNotNull();
    }

    @Test
    void shouldSaveAndLoadUserFromRealDatabase() {
        // Given
        RegisterUserCredentialsDTO request = registerRequest("john", "john@sidhantpande.in", "Password123!");

        // When
        UserCredential registeredUser = authenticationService.register(request);
        UserCredential reloadedUser = userCredentialRepository.findById(registeredUser.getId()).orElseThrow();

        // Then
        assertThat(reloadedUser.getUsername()).isEqualTo("john");
        assertThat(reloadedUser.getEmail()).isEqualTo("john@sidhantpande.in");
        assertThat(reloadedUser.getPasswordHash()).isNotEqualTo("Password123!");
    }

    @Test
    void shouldRegisterUserThroughServiceLayerUsingRealDatabase() {
        // Given
        RegisterUserCredentialsDTO request = registerRequest("john", "john@sidhantpande.in", "Password123!");

        // When
        UserCredential registeredUser = authenticationService.register(request);

        // Then
        assertThat(registeredUser.getId()).isNotNull();
        assertThat(registeredUser.getUsername()).isEqualTo("john");
        assertThat(registeredUser.getEmail()).isEqualTo("john@sidhantpande.in");
        assertThat(registeredUser.getPasswordHash()).isNotEqualTo("Password123!");
        assertThat(userCredentialRepository.findByUsername("john")).isPresent();
    }

    @Test
    void shouldLoginUserThroughServiceLayerUsingRealDatabase() {
        // Given
        persistAuthenticatableUser("john", "john@sidhantpande.in", "Password123!");
        LoginUserDTO loginRequest = loginRequest("john@sidhantpande.in", "Password123!");

        // When
        UserCredential authenticatedUser = authenticationService.login(loginRequest);

        // Then
        assertThat(authenticatedUser.getUsername()).isEqualTo("john");
        assertThat(authenticatedUser.getEmail()).isEqualTo("john@sidhantpande.in");
    }

    @Test
    void shouldRejectDuplicateUsernameThroughServiceLayer() {
        // Given
        authenticationService.register(registerRequest("john", "john@sidhantpande.in", "Password123!"));

        // When / Then
        assertThatThrownBy(() -> authenticationService.register(registerRequest("john", "other@sidhantpande.in", "Password123!")))
                .isInstanceOf(GenericAplicationException.class)
                .hasMessage(ErrorTP.USERNAME_ALREADY_EXIST.name());
    }

    @Test
    void shouldRejectDuplicateEmailThroughServiceLayer() {
        // Given
        authenticationService.register(registerRequest("john", "john@sidhantpande.in", "Password123!"));

        // When / Then
        assertThatThrownBy(() -> authenticationService.register(registerRequest("other", "john@sidhantpande.in", "Password123!")))
                .isInstanceOf(GenericAplicationException.class)
                .hasMessage(ErrorTP.EMAIL_ALREADY_EXIST.name());
    }

    private UserCredential persistAuthenticatableUser(String username, String email, String rawPassword) {
        UserCredential user = UserCredential.builder()
                .username(username)
                .email(email)
                .passwordHash(passwordEncoder.encode(rawPassword))
                .role(Role.ROLE_USER)
                .enabled(true)
                .accountNonLocked(true)
                .failedLoginAttempts(0)
                .build();
        return userCredentialRepository.save(user);
    }
}
