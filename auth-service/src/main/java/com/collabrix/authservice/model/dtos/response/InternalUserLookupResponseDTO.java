package com.collabrix.authservice.model.dtos.response;

import com.collabrix.authservice.model.entities.UserCredential;
import lombok.Builder;

import java.util.UUID;

@Builder
public record InternalUserLookupResponseDTO(
        UUID authId,
        String username
) {
    public static InternalUserLookupResponseDTO fromEntity(UserCredential userCredential) {
        return InternalUserLookupResponseDTO.builder()
                .authId(userCredential.getId())
                .username(userCredential.getUsername())
                .build();
    }
}
