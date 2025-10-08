package com.baeldung.springai.memory.chatmemory;

import com.baeldung.springai.memory.springsecurity.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final ChatMemoryRepository chatMemoryRepository;
    private final ChatModel chatModel;
    private final JwtUtils jwtUtils;

    @Transactional
    public ConversationResponse chat(ConversationRequest request) {
        // Get current user ID from JWT
        Long userId = jwtUtils.getCurrentUserId();
        if (userId == null) {
            throw new RuntimeException("User not authenticated");
        }

        // Determine conversation ID
        String conversationId = request.getConversationId();
        boolean isNewConversation = false;

        if (conversationId == null || conversationId.isEmpty()) {
            // Create new conversation
            conversationId = UUID.randomUUID().toString();
            isNewConversation = true;
        } else {
            // Validate that conversation exists and belongs to user
            if (!chatMemoryRepository.existsByUserIdAndConversationId(userId, conversationId)) {
                throw new RuntimeException("Conversation not found or access denied");
            }
        }

        // Load conversation history
        List<Message> conversationHistory = loadConversationHistory(userId, conversationId);

        // Build chat client with history
        ChatClient chatClient = ChatClient.builder(chatModel).build();

        // Create messages list including history
        List<Message> messages = new ArrayList<>(conversationHistory);
        messages.add(new UserMessage(request.getPrompt()));

        // Call AI
        String response = chatClient.prompt()
                .messages(messages)
                .call()
                .content();

        // Save to database
        ChatMemoryEntity memoryEntity = new ChatMemoryEntity(
                userId,
                conversationId,
                request.getPrompt(),
                response
        );
        chatMemoryRepository.save(memoryEntity);

        return new ConversationResponse(
                conversationId,
                request.getPrompt(),
                response,
                isNewConversation
        );
    }

    @Transactional(readOnly = true)
    public List<ChatMemoryEntity> getConversationHistory(String conversationId) {
        Long userId = jwtUtils.getCurrentUserId();
        if (userId == null) {
            throw new RuntimeException("User not authenticated");
        }

        return chatMemoryRepository.findByUserIdAndConversationIdOrderByCreatedAtAsc(userId, conversationId);
    }

    @Transactional(readOnly = true)
    public List<ChatMemoryEntity> getAllUserConversations() {
        Long userId = jwtUtils.getCurrentUserId();
        if (userId == null) {
            throw new RuntimeException("User not authenticated");
        }

        return chatMemoryRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    private List<Message> loadConversationHistory(Long userId, String conversationId) {
        List<ChatMemoryEntity> history = chatMemoryRepository
                .findByUserIdAndConversationIdOrderByCreatedAtAsc(userId, conversationId);

        List<Message> messages = new ArrayList<>();
        for (ChatMemoryEntity entity : history) {
            messages.add(new UserMessage(entity.getPrompt()));
            messages.add(new AssistantMessage(entity.getResponse()));
        }

        return messages;
    }
}