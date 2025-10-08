package com.baeldung.springai.memory;

import org.springframework.ai.document.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/chat")
    public ResponseEntity<ChatRequest> chat(@RequestBody @Valid ChatRequest request) {
        ChatRequest response = chatService.chat(request.getPrompt());
        return ResponseEntity.ok(response);
    }

    @PostMapping("redis/chat")
    public ResponseEntity<ChatRequest> redisChat(@RequestBody @Valid ChatRequest request) {
        ChatRequest response = chatService.redisChat(request.getPrompt());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/search")
    public List<Document> semanticSearch(@RequestBody String prompt) {
        return chatService.semanticSearch(prompt);
    }
}
