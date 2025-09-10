package com.baeldung.springai.memory;

import lombok.Getter;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@SessionScope
public class ChatService {

    private final ChatClient chatClient;
    @Getter
    private final String conversationId;
    private final EmbeddingModel embeddingModel;
    private final VectorStore vectorStore;

    public ChatService(ChatModel chatModel, ChatMemory chatMemory, EmbeddingModel embeddingModel, VectorStore vectorStore) {
        this.chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
        this.conversationId = UUID.randomUUID().toString();
        this.embeddingModel = embeddingModel;
        this.vectorStore = vectorStore;
    }

    public ChatRequest chat(String prompt) {
        System.out.println("Received prompt: " + prompt);

        String response = chatClient.prompt()
                .user(userMessage -> userMessage.text(prompt))
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
        System.out.println("AI Response: " + response);

        // embedding.(vectors)
        //not as much needed because vectorStore automatically generates embeddings via the configured embedding model when we add documents."
        float[] embeddingArray = embeddingModel.embed(prompt);
        List<Float> embedding = new ArrayList<>();
        for (float f : embeddingArray) {
            embedding.add(f);
        }
        Document doc = new Document(
                prompt,
                Map.of(
                        "response", response,
                        "conversationId", conversationId)
        );
        vectorStore.add(List.of(doc));
        return new ChatRequest(prompt, response, embedding, conversationId);
    }

    public List<Document> semanticSearch(String query) {
        System.out.println("Performing semantic search for: " + query);

        List<Document> results = vectorStore.similaritySearch(query);

        results.forEach(doc -> {
            System.out.println("Matched Prompt: " + doc.getText());
            System.out.println("Response: " + doc.getMetadata().get("response"));
        });

        return results;
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
