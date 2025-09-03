package Spring.AI.Model.sample.controller;

import Spring.AI.Model.sample.services.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.constraints.NotNull;

@Controller
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/chat")
    public ResponseEntity<String> chat(@RequestBody @NotNull String prompt) {
        String response = chatService.chat(prompt);
        return ResponseEntity.ok(response);
    }
}