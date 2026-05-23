package com.collabrix.chatservice.websocket;

import com.collabrix.chatservice.model.dto.websocket.ChatSendMessageRequestDTO;
import com.collabrix.chatservice.security.AuthenticatedUser;
import com.collabrix.chatservice.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import java.security.Principal;

@Controller
@Validated
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final MessageService messageService;

    @MessageMapping("/chat.send")
    public void sendMessage(@Valid ChatSendMessageRequestDTO request, Principal principal) {
        ChatPrincipal chatPrincipal = ChatPrincipal.from(principal);
        messageService.sendMessage(
                AuthenticatedUser.of(chatPrincipal.getAuthId(), chatPrincipal.getUsername()),
                request
        );
    }
}
