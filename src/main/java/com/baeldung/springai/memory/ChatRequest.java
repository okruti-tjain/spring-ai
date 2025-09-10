package com.baeldung.springai.memory;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@AllArgsConstructor
public class ChatRequest {

    @NotNull
    private String prompt;
    private String response;
    private List<Float> embedding;
    private String conversationId;
}
