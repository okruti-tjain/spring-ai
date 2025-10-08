package com.baeldung.springai.memory.chatmemory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConversationRequest {

    @NotNull(message = "Prompt cannot be empty")
    private String prompt;

    private String conversationId; // Optional - if null, creates new conversation
}