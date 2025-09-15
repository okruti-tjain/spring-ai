//package com.baeldung.springai.memory;
//
//import lombok.Getter;
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
//import org.springframework.ai.chat.memory.ChatMemory;
//import org.springframework.ai.chat.model.ChatModel;
//import org.springframework.ai.document.Document;
//import org.springframework.ai.embedding.EmbeddingModel;
//import org.springframework.ai.vectorstore.VectorStore;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.annotation.SessionScope;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//
//@Component
//@SessionScope
//public class ChatService {
//
//    private final ChatClient chatClient;
//    @Getter
//    private final String conversationId;
//    private final EmbeddingModel embeddingModel;
//    private final VectorStore vectorStore;
//
//    public ChatService(ChatModel chatModel, ChatMemory chatMemory, EmbeddingModel embeddingModel, VectorStore vectorStore) {
//        this.chatClient = ChatClient.builder(chatModel)
//                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
//                .build();
//        this.conversationId = UUID.randomUUID().toString();
//        this.embeddingModel = embeddingModel;
//        this.vectorStore = vectorStore;
//    }
//
//    public ChatRequest chat(String prompt) {
//        System.out.println("Received prompt: " + prompt);
//
//        String response = chatClient.prompt()
//                .user(userMessage -> userMessage.text(prompt))
//                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
//                .call()
//                .content();
//        System.out.println("AI Response: " + response);
//
//        // embedding.(vectors)
//        //not as much needed because vectorStore automatically generates embeddings via the configured embedding model when we add documents."
//        float[] embeddingArray = embeddingModel.embed(prompt);
//        List<Float> embedding = new ArrayList<>();
//        for (float f : embeddingArray) {
//            embedding.add(f);
//        }
//        Document doc = new Document(
//                prompt,
//                Map.of(
//                        "response", response,
//                        "conversationId", conversationId)
//        );
//        vectorStore.add(List.of(doc));
//        return new ChatRequest(prompt, response, embedding, conversationId);
//    }
//
//    public List<Document> semanticSearch(String query) {
//        System.out.println("Performing semantic search for: " + query);
//
//        List<Document> results = vectorStore.similaritySearch(query);
//
//        results.forEach(doc -> {
//            System.out.println("Matched Prompt: " + doc.getText());
//            System.out.println("Response: " + doc.getMetadata().get("response"));
//        });
//
//        return results;
//    }
//
//    public ChatRequest redisChat(String prompt) {
//        System.out.println("Received prompt: " + prompt);
//
//        // Step 1: Semantic search first
//        List<Document> results = vectorStore.similaritySearch(prompt);
//
//        if (!results.isEmpty()) {
//            Document topDoc = results.get(0); // top match
//
//            Object rawDistance = topDoc.getMetadata().get("distance");
//            double distance = rawDistance == null ? Double.MAX_VALUE : ((Number) rawDistance).doubleValue();
//
//            System.out.println("Closest match distance: " + distance);
//
//            // Step 2: Check if it's similar enough
//            if (distance < 0.05) { // threshold value
//                String cachedResponse = (String) topDoc.getMetadata().get("response");
//                System.out.println("Found cached response: " + cachedResponse);
//
//                return new ChatRequest(prompt, cachedResponse, null, conversationId);
//            }
//        }
//
//        // Step 3: Fallback → Call AI model
//        String response = chatClient.prompt()
//                .user(userMessage -> userMessage.text(prompt))
//                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
//                .call()
//                .content();
//        System.out.println("AI Response: " + response);
//
//        // Step 4: Save to vector store for future use
//        Document doc = new Document(
//                prompt,
//                Map.of(
//                        "response", response,
//                        "conversationId", conversationId
//                )
//        );
//        vectorStore.add(List.of(doc));
//
//        return new ChatRequest(prompt, response, null, conversationId);
//    }
//
//
//}

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
package com.baeldung.springai.memory;

import com.baeldung.springai.memory.iplinfo.IplTools;
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
    private static final double DEFAULT_SIMILARITY_THRESHOLD = 0.05;

    public ChatService(ChatModel chatModel, ChatMemory chatMemory, EmbeddingModel embeddingModel, VectorStore vectorStore,
                       WeatherTool WeatherTool, IplTools iplTools, EmailTool emailTool) {
        this.chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultTools(WeatherTool, iplTools, emailTool)
                .build();
        this.conversationId = UUID.randomUUID().toString();
        this.embeddingModel = embeddingModel;
        this.vectorStore = vectorStore;
    }


    //      Handles a chat request: always calls AI and stores result.
    public ChatRequest chat(String prompt) {
        System.out.println("Received prompt: " + prompt);

        String response = callAI(prompt);
        saveToVectorStore(prompt, response);

        return new ChatRequest(prompt, response, embedPrompt(prompt), conversationId);
    }

    //      Handles a chat request with Redis caching:
//      returns cached response if a close match exists; otherwise calls AI.
    public ChatRequest redisChat(String prompt) {
        System.out.println("Received prompt: " + prompt);

        // Step 1: Try to fetch cached response
        String cachedResponse = getCachedResponse(prompt, DEFAULT_SIMILARITY_THRESHOLD);
        if (cachedResponse != null) {
            return new ChatRequest(prompt, cachedResponse, null, conversationId);
        }

        // Step 2: Fallback to AI call
        String response = callAI(prompt);
        saveToVectorStore(prompt, response);

        return new ChatRequest(prompt, response, null, conversationId);
    }

    //      Performs semantic search and prints matched prompts and responses.
    public List<Document> semanticSearch(String query) {
        System.out.println("Performing semantic search for: " + query);
        List<Document> results = vectorStore.similaritySearch(query);

        results.forEach(doc -> {
            Object rawDistance = doc.getMetadata().get("distance");
            double distance = rawDistance == null ? Double.MAX_VALUE : ((Number) rawDistance).doubleValue();
            System.out.println("Matched Prompt: " + doc.getText());
            System.out.println("Response: " + doc.getMetadata().get("response"));
            System.out.println("distance:" + distance);
        });

        return results;
    }

    //      Reusable method to generate embeddings for a prompt.
    private List<Float> embedPrompt(String prompt) {
        System.out.println("EmbeddingModel: " + embeddingModel.getClass().getName());
        float[] embeddingArray = embeddingModel.embed(prompt);
        List<Float> embedding = new ArrayList<>(embeddingArray.length);
        for (float f : embeddingArray) {
            embedding.add(f);
        }
        return embedding;
    }

    //      Calls AI client and returns response.
    private String callAI(String prompt) {
        String response = chatClient.prompt()
                .user(userMessage -> userMessage.text(prompt))
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
        System.out.println("AI Response: " + response);
        return response;
    }

    //      Saves a prompt and response to the vector store.
    private void saveToVectorStore(String prompt, String response) {
        Document doc = new Document(prompt, Map.of("response", response, "conversationId", conversationId));
        vectorStore.add(List.of(doc));
    }

    //      Performs a semantic search and returns the cached response if similarity is above threshold.
    private String getCachedResponse(String prompt, double threshold) {
        List<Document> results = vectorStore.similaritySearch(prompt);

        if (!results.isEmpty()) {
            Document topDoc = results.get(0);
            Object rawDistance = topDoc.getMetadata().get("distance");
            double distance = rawDistance == null ? Double.MAX_VALUE : ((Number) rawDistance).doubleValue();
            System.out.println("Closest match distance: " + distance);

            if (distance <= threshold) {
                String cachedResponse = (String) topDoc.getMetadata().get("response");
                System.out.println("Found cached response: " + cachedResponse);
                return cachedResponse;
            }
        }
        return null;
    }
}
