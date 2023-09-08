package com.carcost.CarCostAPI.chatgpt;

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
            "Provide a short description of the car manufacturer " + _make + ". Describe the " +
                        "manufacturer based on reviews, reliability, maintenance, cost of operation, and user experience. " +
                        " Then describe up to 5 cars in list format provided by the car manufacturer " + _make +
                        " in the following format: \n\n " +
                        "Name: [Output] \n\n " +
                        "Engine: [Output] \n\n " +
                        "Type: [Output] \n\n " +
                        "Features: [Output] \n\n" +
                        "MSRP: [Output]",
        0.7
        );
    }

    public static String getModelInfo(String _make, String _makeModel){
        if (_make == null || _make.trim().isEmpty() || _makeModel == null || _makeModel.trim().isEmpty()){
            return "";
        }

        return getChatGPTResponse(
                "gpt-3.5-turbo",
                "Provide a short description about the following car, " + _make + " " +  _makeModel +
                    " with respect to reviews, reliability, maintenance, cost of operation, user experience, " +
                    "and common problems with used models of this car. Then provide some information about it's " +
                    "features. Finally, recommend a good used price and mileage for the " + _make + " " +
                    _makeModel + " to look for.",
                0.7
        );
    }

    public static String getCarRecommendation(String _type, String _make){
        if (_make == null || _make.trim().isEmpty()){
            return getChatGPTResponse(
                    "gpt-3.5-turbo",
                    "Describe me the features of cars of the type " + _type + ". Then, recommend me " +
                            "up to 5 good cars of the type " + _type + " based on based on Car and Driver Reviews. " +
                            "Make sure to include information about features of the cars, reliability, maintenance," +
                            "cost of operation, price, user experience and common problems associated with the cars.",
                    0.7
            );
        }
        return getChatGPTResponse(
                "gpt-3.5-turbo",
                "Describe me the features of cars of the type " + _type + ". Then, recommend me " +
                        "cars of the type " + _type + " from " + _make + " based on based on Car and Driver Reviews. " +
                        "Make sure to include information about features of the cars, reliability, maintenance," +
                        "cost of operation, price, user experience and common problems associated with the cars.",
                0.7
        );
    }
}
