package com.baeldung.springai.memory;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@SessionScope
public class ChatService {

    private final ChatClient chatClient;
    private final String conversationId;
    private final EmbeddingModel embeddingModel;

    public ChatService(ChatModel chatModel, ChatMemory chatMemory, EmbeddingModel embeddingModel) {
        this.chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
        this.conversationId = UUID.randomUUID().toString();
        this.embeddingModel = embeddingModel;
    }

    public String getConversationId() {
        return conversationId;
    }

    public ChatRequest chat(String prompt) {
        System.out.println("Received prompt: " + prompt);

        String response = chatClient.prompt()
                .user(userMessage -> userMessage.text(prompt))
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();

        System.out.println("AI Response: " + response);
        // 2. Generate embedding vector for the prompt
        float[] embeddingArray = embeddingModel.embed(prompt);
        List<Float> embedding = new ArrayList<>();
        for (float f : embeddingArray) {
            embedding.add(f);
        }

        // 3. Wrap everything in ChatResponse DTO
        return new ChatRequest(prompt, response, embedding);
    }

}

//exact match-not sementic cache.
//package com.baeldung.springai.memory;
//
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
//import org.springframework.ai.chat.memory.ChatMemory;
//import org.springframework.ai.chat.model.ChatModel;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.annotation.SessionScope;
//import redis.clients.jedis.JedisPooled;
//
//import java.util.UUID;
//
//@Component
//@SessionScope
//public class ChatService {
//
//    private final ChatClient chatClient;
//    private final String conversationId;
//    private final JedisPooled jedis;
//
//    public ChatService(ChatModel chatModel, ChatMemory chatMemory, JedisPooled jedis) {
//        this.chatClient = ChatClient.builder(chatModel)
//                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
//                .build();
//        this.jedis = jedis;
//        this.conversationId = UUID.randomUUID().toString();
//    }
//
//    public String getConversationId() {
//        return conversationId;
//    }
//
//    public String chat(String prompt) {
//        // Step 1: Check cache (exact match)
//        String cachedResponse = jedis.get(prompt);
//        if (cachedResponse != null) {
//            System.out.println(" Cache hit for prompt: " + prompt);
//            return cachedResponse + " [from cache]";
//        }
//
//        // Step 2: If not found, call AI API
//        System.out.println("Calling AI API for prompt: " + prompt);
//        String response = chatClient.prompt()
//                .user(userMessage -> userMessage.text(prompt))
//                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
//                .call()
//                .content();
//
//        // Step 3: Store response in Redis
//        jedis.set(prompt, response);
//
//        return response;
//    }
//
//}
//
