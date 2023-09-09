package com.carcost.CarCostAPI;

import com.carcost.CarCostAPI.chatgpt.ChatGPTConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class CarCostAPIService {

//    private final CarCostAPIRepository carCostAPIRepositoryImpl;

    private final CarCostAPIRepositoryThreaded carCostAPIRepositoryThreaded;

    private final ChatGPTConnection chatGPTConnection;

    // create a new logger
    Logger logger = LoggerFactory.getLogger(CarCostAPIService.class);

    @Autowired
    public CarCostAPIService(CarCostAPIRepositoryThreaded carCostAPIRepositoryThreaded, ChatGPTConnection chatGPTConnection) {
        this.carCostAPIRepositoryThreaded = carCostAPIRepositoryThreaded;
        this.chatGPTConnection = chatGPTConnection;
    }
    
    @Async
    public CompletableFuture<APIDataReturn> returnMakeListings(String _make, boolean _low_price) throws ExecutionException, InterruptedException {
        CompletableFuture<String> chatGptMakeInfo = chatGPTConnection.getMakeInfo(_make);
        CompletableFuture<List<CarData>> matchingMakeEntries;
        if (_low_price){
            matchingMakeEntries = carCostAPIRepositoryThreaded.threadedFindAllByMakeContainingOrderByPriceAsc(_make);
        }else{
            matchingMakeEntries = carCostAPIRepositoryThreaded.threadedFindAllByMakeContaining(_make);
        }
        return CompletableFuture.completedFuture(new APIDataReturn(chatGptMakeInfo.get(), matchingMakeEntries.get()));
    }

    @Async
    public CompletableFuture<APIDataReturn> returnModelListings(String _make, String _model, boolean _low_price) throws ExecutionException, InterruptedException {
        CompletableFuture<String> chatGptModelInfo = chatGPTConnection.getModelInfo(_make, _model);
        CompletableFuture<List<CarData>> matchingModelEntries;
        if (_low_price){
            matchingModelEntries =
                    carCostAPIRepositoryThreaded.threadedFindAllByMakeContainingAndModelContainingOrderByPriceAsc(_make, _model);
        }else{
            matchingModelEntries = carCostAPIRepositoryThreaded.threadedFindAllByMakeContainingAndModelContaining(_make, _model);
        }
        return CompletableFuture.completedFuture(new APIDataReturn(chatGptModelInfo.get(), matchingModelEntries.get()));
    }

    @Async
    public CompletableFuture<APIDataReturn> returnCarRecommendations(String _type, String _make) throws ExecutionException, InterruptedException {
        CompletableFuture<String> chatGptCarRecommendation = chatGPTConnection.getCarRecommendation(_type, _make);
        CompletableFuture<List<CarData>> matchingTypeEntries;
        if (_make == null || _make.trim().isEmpty()){
            matchingTypeEntries = carCostAPIRepositoryThreaded.threadedFindAllByBodyTypeContaining(_type);
        }else{
            matchingTypeEntries = carCostAPIRepositoryThreaded.threadedFindAllByBodyTypeContainingAndMakeContaining(_type, _make);
        }
        return CompletableFuture.completedFuture(new APIDataReturn(chatGptCarRecommendation.get(), matchingTypeEntries.get()));
    }
}
