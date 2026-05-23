package com.collabrix.taskservice.exception;

import com.collabrix.taskservice.exception.enums.ErrorType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponseDTO {

    private ErrorType errorType;
    private LocalDateTime timestamp;

    public static ErrorResponseDTO of(ErrorType errorType) {
        return ErrorResponseDTO.builder()
                .errorType(errorType)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
