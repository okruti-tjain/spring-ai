package com.baeldung.springai.memory.chatmemory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConversationResponse {

    private String conversationId;
    private String prompt;
    private String response;
    private boolean isNewConversation;
}