package com.baeldung.springai.memory.chatmemory;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/conversation")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;

    /**
     * Main chat endpoint - continues existing conversation or creates new one
     *
     * POST /api/conversation/chat
     * Body: {
     *   "prompt": "Your message here",
     *   "conversationId": "optional-conversation-id"
     * }
     */
    @PostMapping("/chat")
    public ResponseEntity<ConversationResponse> chat(@RequestBody @Valid ConversationRequest request) {
        ConversationResponse response = conversationService.chat(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Get full history of a specific conversation
     *
     * GET /api/conversation/history/{conversationId}
     */
    @GetMapping("/history/{conversationId}")
    public ResponseEntity<List<ChatMemoryEntity>> getConversationHistory(
            @PathVariable String conversationId) {
        List<ChatMemoryEntity> history = conversationService.getConversationHistory(conversationId);
        return ResponseEntity.ok(history);
    }

    /**
     * Get all conversations for the current user
     *
     * GET /api/conversation/all
     */
    @GetMapping("/all")
    public ResponseEntity<List<ChatMemoryEntity>> getAllConversations() {
        List<ChatMemoryEntity> conversations = conversationService.getAllUserConversations();
        return ResponseEntity.ok(conversations);
    }
}