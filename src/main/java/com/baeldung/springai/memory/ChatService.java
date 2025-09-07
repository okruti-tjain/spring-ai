package com.baeldung.springai.memory;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.List;
import java.util.UUID;

@Component
@SessionScope
public class ChatService {

    private final ChatClient chatClient;
    private final String conversationId;
    private final VectorStore vectorStore;

    public ChatService(ChatModel chatModel, ChatMemory chatMemory,VectorStore vectorStore) {
        this.chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
        this.vectorStore=vectorStore;
        this.conversationId = UUID.randomUUID().toString();
    }

    public String getConversationId() {
        return conversationId;
    }

    public String chat(String prompt) {

        // Step 1: Look for semantically similar prompt in Redis
        List<Document> similarDocs=vectorStore.similaritySearch(
          SearchRequest.builder()
        .query(prompt)
        .topK(1)
        .similarityThreshold(0.90f)
        .build()
        );
        if (!similarDocs.isEmpty()){
            return (String) similarDocs.get(0).getMetadata().get("response");
        }
        // Step 2: If not found, call AI API
        String response=chatClient.prompt()
                .user(userMessage->userMessage.text(prompt))
                .advisors(a->a.param(ChatMemory.CONVERSATION_ID,conversationId))
                .call()
                .content();
        // Step 3: Store prompt as the embedded content and response in metadata
        Document doc=new Document(prompt);
        doc.getMetadata().put("response",response);
        vectorStore.add(List.of(doc));
        return response;
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
