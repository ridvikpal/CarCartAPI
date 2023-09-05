package com.carcost.CarCost.model.chatgpt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ChatGPTConnection {
    private final WebClient webClient;

    @Value("${openai.api.key}")
    private static String openAiApiKey;

    @Value("${openai.api.url}")
    private static String openAiUrl;

    @Autowired
    public ChatGPTConnection(WebClient.Builder webClient) {
        this.webClient = WebClient.builder().baseUrl(openAiUrl).build();
    }

    public String getMakeInfo(){
        ChatGPTRequest makeInfoRequest = new ChatGPTRequest(
                "gpt-3.5-turbo",
                "Tell me about the cars Ferrari makes",
                0.7
        );

        ChatGPTResponse makeInfoObject = this.webClient.post()
                .uri("/v1/chat/completions")
                .header("Authorization", "Bearer " + openAiApiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(makeInfoRequest), ChatGPTRequest.class)
                .retrieve().bodyToMono(ChatGPTResponse.class).block();

        return "";
    }
}
