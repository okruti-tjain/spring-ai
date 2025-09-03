package Spring.AI.Model.sample.services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

//@Service
//@SessionScope
//public class ChatService {
//
//    private final ChatClient chatClient;
//
//    @Autowired
//    ChatMemory chatMemory;
//
//    public ChatService(ChatModel chatModel) {
//        this.chatClient = ChatClient.builder(chatModel)
//                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
//                .build();
//    }
//
//    public String chat(String prompt) {
//        return chatClient.prompt()
//                .user(userMessage -> userMessage.text(prompt))
//                .stream().content().blockFirst();
//    }
//}

@Service
@SessionScope
public class ChatService {

    private final ChatClient chatClient;

    @Autowired
    public ChatService(ChatModel chatModel) {
        this.chatClient = ChatClient.builder(chatModel).build();
    }

    public String chat(String prompt) {
        return chatClient.prompt()
                .user(userMessage -> userMessage.text(prompt))
                .call()
                .content();
    }
}
