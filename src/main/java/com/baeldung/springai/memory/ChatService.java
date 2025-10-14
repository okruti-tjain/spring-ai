package com.baeldung.springai.memory;

import com.baeldung.springai.memory.iplinfo.IplTools;
import com.baeldung.springai.memory.springsecurity.JwtUtils;
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

    @Getter
    private final String conversationId;
    private final EmbeddingModel embeddingModel;
    private final VectorStore vectorStore;
    private static final double DEFAULT_SIMILARITY_THRESHOLD = 0.05;
    private final ChatModel chatModel;
    private final ChatMemory chatMemory;
    private final WeatherTool weatherTool;
    private final IplTools iplTools;
    private final EmailTool emailTool;
    private final JwtUtils jwtUtils;

public ChatService(ChatModel chatModel,
                   ChatMemory chatMemory,
                   EmbeddingModel embeddingModel,
                   VectorStore vectorStore,
                   WeatherTool weatherTool,
                   IplTools iplTools,
                   EmailTool emailTool,
                   JwtUtils jwtUtils) {
    this.chatModel = chatModel;
    this.chatMemory = chatMemory;
    this.embeddingModel = embeddingModel;
    this.vectorStore = vectorStore;
    this.weatherTool = weatherTool;
    this.iplTools = iplTools;
    this.emailTool = emailTool;
    this.jwtUtils = jwtUtils;
    this.conversationId = UUID.randomUUID().toString();
}

    private ChatClient buildChatClient() {
        ChatClient.Builder builder = ChatClient.builder(chatModel)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
//                .defaultTools(weatherTool, emailTool)
                ;


        if (jwtUtils.isAuthenticated()) {
            System.out.println("Authenticated user: " + jwtUtils.getCurrentUsername());
            builder.defaultTools(iplTools);
        }
        else {
            System.out.println("UnAuthorised");
        }
        return builder.build();
    }

    public ChatRequest chat(String prompt) {
        System.out.println("Received prompt: " + prompt);

        String response = callAI(prompt);
        saveToVectorStore(prompt, response);

        return new ChatRequest(prompt, response, null, conversationId);
    }


    public ChatRequest redisChat(String prompt) {
        System.out.println("Received prompt: " + prompt);

        String cachedResponse = getCachedResponse(prompt, DEFAULT_SIMILARITY_THRESHOLD);
        if (cachedResponse != null) {
            return new ChatRequest(prompt, cachedResponse, null, conversationId);
        }

        String response = callAI(prompt);
        saveToVectorStore(prompt, response);

        return new ChatRequest(prompt, response, null, conversationId);
    }

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


    private List<Float> embedPrompt(String prompt) {
//        System.out.println("EmbeddingModel: " + embeddingModel.getClass().getName());
        float[] embeddingArray = embeddingModel.embed(prompt);
        List<Float> embedding = new ArrayList<>(embeddingArray.length);
        for (float f : embeddingArray) {
            embedding.add(f);
        }
        return embedding;
    }


    private String callAI(String prompt) {
        ChatClient client = buildChatClient();
        String response = client.prompt()
                .user(userMessage -> userMessage.text(prompt))
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
        System.out.println("AI Response: " + response);
        return response;
    }


    private void saveToVectorStore(String prompt, String response) {
        Document doc = new Document(prompt, Map.of("response", response, "conversationId", conversationId));
        vectorStore.add(List.of(doc));
    }

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
