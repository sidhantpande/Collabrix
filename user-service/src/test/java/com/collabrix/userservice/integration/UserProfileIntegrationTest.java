package com.collabrix.userservice.integration;

import com.collabrix.userservice.model.dto.request.UpdateUserProfileDTO;
import com.collabrix.userservice.model.entities.UserProfile;
import com.collabrix.userservice.repository.UserProfileRepository;
import com.collabrix.userservice.services.StorageService;
import com.collabrix.userservice.services.UserProfileService;
import com.collabrix.userservice.testsupport.AbstractPostgresUserServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.UUID;

import static com.collabrix.userservice.testsupport.UserServiceTestFixtures.avatarFile;
import static com.collabrix.userservice.testsupport.UserServiceTestFixtures.updateUserProfileDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = {
        "spring.main.allow-bean-definition-overriding=true",
        "spring.cache.type=none",
        "minio.endpoint=http://localhost:9000",
        "minio.public-url=http://localhost:9000",
        "minio.root-user=minio",
        "minio.root-password=minio123",
        "minio.bucket=avatars",
        "security.jwt.secret-key=c3VwZXItc2VjdXJlLXRlc3Qta2V5LWZvci11c2VyLXNlcnZpY2UtMTIzNDU2Nzg5MA==",
        "internal.secret=test-internal-secret"
})
class UserProfileIntegrationTest extends AbstractPostgresUserServiceTest {

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @MockBean
    private StorageService storageService;

    @TestConfiguration
    static class CacheTestConfiguration {

        @Bean
        @Primary
        CacheManager cacheManager() {
            return new NoOpCacheManager();
        }
    }

    @BeforeEach
    void cleanUp() {
        userProfileRepository.deleteAll();
    }

    @Test
    void findOrCreateProfile_profileMissing_persistsFallbackProfile() {
        UUID authId = UUID.randomUUID();

        UserProfile profile = userProfileService.findOrCreateProfile(authId, "john");

        assertThat(profile.getId()).isNotNull();
        assertThat(profile.getDisplayName()).isEqualTo("john");
        assertThat(userProfileRepository.findByAuthId(authId)).isPresent();
    }

    @Test
    void updateProfile_existingProfile_updatesPersistedState() {
        UUID authId = UUID.randomUUID();
        userProfileService.findOrCreateProfile(authId, "john");
        UpdateUserProfileDTO dto = updateUserProfileDTO("johnny", "Updated bio", "99999");

        UserProfile updated = userProfileService.updateProfile(authId, dto);

        assertThat(updated.getDisplayName()).isEqualTo("johnny");
        assertThat(userProfileRepository.findByAuthId(authId)).get()
                .extracting(UserProfile::getBio)
                .isEqualTo("Updated bio");
    }

    @Test
    void updateAvatar_existingProfile_persistsNewAvatarUrl() {
        UUID authId = UUID.randomUUID();
        userProfileService.findOrCreateProfile(authId, "john");
        when(storageService.uploadAvatar(any())).thenReturn("http://cdn.sidhantpande.in/avatar-new.png");

        UserProfile updated = userProfileService.updateAvatar(authId, avatarFile());

        assertThat(updated.getAvatarUrl()).isEqualTo("http://cdn.sidhantpande.in/avatar-new.png");
        assertThat(userProfileRepository.findByAuthId(authId)).get()
                .extracting(UserProfile::getAvatarUrl)
                .isEqualTo("http://cdn.sidhantpande.in/avatar-new.png");
    }
}
