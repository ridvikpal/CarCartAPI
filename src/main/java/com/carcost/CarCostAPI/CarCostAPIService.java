package com.carcost.CarCostAPI;

import com.carcost.CarCostAPI.chatgpt.ChatGPTConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class CarCostAPIService {

    private final CarCostAPIRepository carCostAPIRepository;
    private final ChatGPTConnection chatGPTConnection;

    // create a new logger
    Logger logger = LoggerFactory.getLogger(CarCostAPIService.class);

    @Autowired
    public CarCostAPIService(CarCostAPIRepository carCostAPIRepository, ChatGPTConnection chatGPTConnection) {
        this.carCostAPIRepository = carCostAPIRepository;
        this.chatGPTConnection = chatGPTConnection;
    }

    @Async
    public CompletableFuture<APIDataReturn> returnMakeListings(String _make, boolean _low_price) {
        logger.info("Starting thread for returning make listings on {}", Thread.currentThread().getName());
        long start = System.currentTimeMillis();
        String chatGptMakeInfo = chatGPTConnection.getMakeInfo(_make);
        List<CarData> matchingMakeEntries;
        if (_low_price){
            matchingMakeEntries = carCostAPIRepository.findAllByMakeContainingOrderByPriceAsc(_make);
        }else{
            matchingMakeEntries = carCostAPIRepository.findAllByMakeContaining(_make);
        }
        long end = System.currentTimeMillis();
        logger.info("Ending thread for returning make listings on {}", Thread.currentThread().getName());
        logger.info("Completed returning make listings in {}", (end - start));
        return CompletableFuture.completedFuture(new APIDataReturn(chatGptMakeInfo, matchingMakeEntries));
    }

    @Async
    public CompletableFuture<APIDataReturn> returnModelListings(String _make, String _model, boolean _low_price) {
        logger.info("Starting thread for returning model listings on {}", Thread.currentThread().getName());
        long start = System.currentTimeMillis();
        String chatGptModelInfo = chatGPTConnection.getModelInfo(_make, _model);
        List<CarData> matchingModelEntries;
        if (_low_price){
            matchingModelEntries =
                    carCostAPIRepository.findAllByMakeContainingAndModelContainingOrderByPriceAsc(_make, _model);
        }else{
            matchingModelEntries = carCostAPIRepository.findAllByMakeContainingAndModelContaining(_make, _model);
        }
        long end = System.currentTimeMillis();
        logger.info("Ending thread for returning model listings on {}", Thread.currentThread().getName());
        logger.info("Completed returning model listings in {}", (end - start));
        return CompletableFuture.completedFuture(new APIDataReturn(chatGptModelInfo, matchingModelEntries));
    }

    @Async
    public CompletableFuture<APIDataReturn> returnCarRecommendations(String _type, String _make) {
        logger.info("Starting thread for returning type recommendations on {}", Thread.currentThread().getName());
        long start = System.currentTimeMillis();
        String chatGptCarRecommendation = chatGPTConnection.getCarRecommendation(_type, _make);
        List<CarData> matchingTypeEntries;
        if (_make == null || _make.trim().isEmpty()){
            matchingTypeEntries = carCostAPIRepository.findAllByBodyTypeContaining(_type);
        }else{
            matchingTypeEntries = carCostAPIRepository.findAllByBodyTypeContainingAndMakeContaining(_type, _make);
        }
        long end = System.currentTimeMillis();
        logger.info("Ending thread for returning type recommendations listings on {}", Thread.currentThread().getName());
        logger.info("Completed returning type recommendations in {}", (end - start));
        return CompletableFuture.completedFuture(new APIDataReturn(chatGptCarRecommendation, matchingTypeEntries));
    }
}
