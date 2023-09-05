package com.carcost.CarCost.model.chatgpt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class ChatGPTConnection {
    private final WebClient webClient;

    @Value("${openai.api.key}")
    private static String apiKey;

    @Autowired
    public ChatGPTConnection(WebClient.Builder webClient) {
        this.webClient = WebClient.builder().baseUrl().build();
    }

    public Mono<String> getMakeInfo(){
        return this.webClient.get().uri("/{name}/details").retrieve().bodyToMono(String.class);
    }
}
