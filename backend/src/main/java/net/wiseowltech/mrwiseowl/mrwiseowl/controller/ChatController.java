package net.wiseowltech.mrwiseowl.mrwiseowl.controller;

import net.wiseowltech.mrwiseowl.mrwiseowl.service.AiChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final AiChatService aiChatService;

    @PostMapping
    public ResponseEntity<Map<String, String>> chat(@RequestBody Map<String, String> request) {
        String message = request.get("message");
        String context = request.get("context");
        
        if (message == null || message.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("response", "Please provide a message"));
        }
        
        String response = aiChatService.chat(message, context);
        return ResponseEntity.ok(Map.of("response", response));
    }
}
