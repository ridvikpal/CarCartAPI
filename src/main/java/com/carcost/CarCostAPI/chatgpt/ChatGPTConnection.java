package com.carcost.CarCostAPI.chatgpt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

@Component
public class ChatGPTConnection {
    private WebClient webClient;

    private String openAiApiKey;

    private final String openAiUrl = "https://api.openai.com/v1/chat/completions";

    Logger logger = LoggerFactory.getLogger(ChatGPTConnection.class);

    @Autowired
    public ChatGPTConnection(WebClient.Builder webClient, @Value("${openai.api.key}") String openAiApiKey) {
        this.webClient = WebClient.builder().baseUrl(openAiUrl).build();
        this.openAiApiKey = openAiApiKey;
    }

    private String getChatGPTResponse(String _model, String _request, double _temperature){
        Message _messsage = new Message();
        _messsage.setRole("user");
        _messsage.setContent(_request);

        ArrayList<Message> _messages = new ArrayList<>();
        _messages.add(_messsage);

        ChatGPTRequest makeInfoRequest = new ChatGPTRequest();
        makeInfoRequest.setModel(_model);
        makeInfoRequest.setMessages(_messages);
        makeInfoRequest.setTemperature(_temperature);

        ChatGPTResponse makeInfoObject = webClient.post()
                .header("Authorization", "Bearer " + openAiApiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(makeInfoRequest), ChatGPTRequest.class)
                .retrieve().bodyToMono(ChatGPTResponse.class).block();

        return makeInfoObject.getChoices().get(0).getMessage().getContent();
    }

    @Async
    public CompletableFuture<String> getMakeInfo(String _make){
        logger.info("Starting thread for chatgpt make info on {}", Thread.currentThread().getName());
        long start = System.currentTimeMillis();
        CompletableFuture<String> result = CompletableFuture.completedFuture(getChatGPTResponse(
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
        ));
        long end = System.currentTimeMillis();
        logger.info("Ending thread for returning chatgpt make info on {}", Thread.currentThread().getName());
        logger.info("Completed returning chatpgt make info in {} ms", (end - start));
        return result;
    }

    @Async
    public CompletableFuture<String> getModelInfo(String _make, String _makeModel){
        logger.info("Starting thread for chatgpt model info on {}", Thread.currentThread().getName());
        long start = System.currentTimeMillis();
        CompletableFuture<String> result;
        if (_make == null || _make.trim().isEmpty() || _makeModel == null || _makeModel.trim().isEmpty()){
            result = CompletableFuture.completedFuture("");
        }else{
            result = CompletableFuture.completedFuture(getChatGPTResponse(
                    "gpt-3.5-turbo",
                    "Provide a short description about the following car, " + _make + " " + _makeModel +
                            " with respect to reviews, reliability, maintenance, cost of operation, user experience, " +
                            "and common problems with used models of this car. Then provide some information about it's " +
                            "features. Finally, recommend a good used price and mileage for the " + _make + " " +
                            _makeModel + " to look for.",
                    0.7
            ));
        }
        long end = System.currentTimeMillis();
        logger.info("Ending thread for returning chatgpt model info on {}", Thread.currentThread().getName());
        logger.info("Completed returning chatpgt model info in {} ms", (end - start));
        return result;
    }

    @Async
    public CompletableFuture<String> getCarRecommendation(String _type, String _make){
        logger.info("Starting thread for chatgpt type recommendation info on {}", Thread.currentThread().getName());
        long start = System.currentTimeMillis();
        CompletableFuture<String> result;
        if (_make == null || _make.trim().isEmpty()){
            result = CompletableFuture.completedFuture(getChatGPTResponse(
                    "gpt-3.5-turbo",
                    "Describe me the features of cars of the type " + _type + ". Then, recommend me " +
                            "up to 5 good cars of the type " + _type + " based on based on Car and Driver Reviews. " +
                            "Make sure to include information about features of the cars, reliability, maintenance," +
                            "cost of operation, price, user experience and common problems associated with the cars.",
                    0.7
            ));
        }else{
            result = CompletableFuture.completedFuture(getChatGPTResponse(
                    "gpt-3.5-turbo",
                    "Describe me the features of cars of the type " + _type + ". Then, recommend me " +
                            "cars of the type " + _type + " from " + _make + " based on based on Car and Driver Reviews. " +
                            "Make sure to include information about features of the cars, reliability, maintenance," +
                            "cost of operation, price, user experience and common problems associated with the cars.",
                    0.7
            ));
        }
        long end = System.currentTimeMillis();
        logger.info("Ending thread for returning chatgpt type recommendation info on {}", Thread.currentThread().getName());
        logger.info("Completed returning chatpgt type recommendation info in {} ms", (end - start));
        return result;
    }
}
