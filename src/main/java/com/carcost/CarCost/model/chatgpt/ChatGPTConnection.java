package com.carcost.CarCost.model.chatgpt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Component
public class ChatGPTConnection {
    private static WebClient webClient;

    private static String openAiApiKey;

    private static final String openAiUrl = "https://api.openai.com/v1/chat/completions";

    @Autowired
    public ChatGPTConnection(WebClient.Builder webClient, @Value("${openai.api.key}") String openAiApiKey) {
        this.webClient = WebClient.builder().baseUrl(openAiUrl).build();
        this.openAiApiKey = openAiApiKey;
    }

    private static String getChatGPTResponse(String _model, String _request, double _temperature){
        ChatGPTRequest.Message _message = new ChatGPTRequest.Message(
                "user",
                _request
        );
        ArrayList<ChatGPTRequest.Message> _messages = new ArrayList<>();
        _messages.add(_message);

        ChatGPTRequest makeInfoRequest = new ChatGPTRequest(_model, _messages, _temperature);

        ChatGPTResponse makeInfoObject = webClient.post()
                .header("Authorization", "Bearer " + openAiApiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(makeInfoRequest), ChatGPTRequest.class)
                .retrieve().bodyToMono(ChatGPTResponse.class).block();

        return makeInfoObject.getChoices().get(0).getMessage().getContent();
    }

    public static String getMakeInfo(String _make){
        return getChatGPTResponse(
                "gpt-3.5-turbo",
                "Tell me about the cars offered by " + _make,
                0.7
        );
    }

    public static String getModelInfo(String _make, String _makeModel, Integer _year){
        if (_make == null || _make.trim().isEmpty() || _makeModel == null || _makeModel.trim().isEmpty()){
            return "";
        }

        if (_year == null || _year < 1950 && _year > 2021){
            return getChatGPTResponse(
                    "gpt-3.5-turbo",
                    "Give me information about the following car: " + _make + " " +  _makeModel,
                    0.7
            );
        }

        return getChatGPTResponse(
                "gpt-3.5-turbo",
                "Give me information about the following car: " + _make + " " +  _makeModel + " " + _year,
                0.7
        );
    }
}
