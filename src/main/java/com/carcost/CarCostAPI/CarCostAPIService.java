package com.carcost.CarCostAPI;

import com.carcost.CarCostAPI.chatgpt.ChatGPTConnection;
import jakarta.transaction.Transactional;
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

    private final CarCostAPIRepositoryThreaded carCostAPIRepositoryThreaded;

    private final ChatGPTConnection chatGPTConnection;

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


    @Async
    public CompletableFuture<CarData> insertNewCarListing(CarData carData) {
        CompletableFuture<CarData> result = carCostAPIRepositoryThreaded.threadedInsertIntoDatabase(carData);
        return result;
    }

    @Async
    @Transactional
    public CompletableFuture<CarData> updateCarListing(int id, String price, String miles, String sellerName, String street, String city, String state, String zip) {
        CompletableFuture<CarData> result = carCostAPIRepositoryThreaded.threadedUpdateInDatabase(
                id, price, miles, sellerName, street, city, state, zip
        );
        return result;
    }

    @Async
    public CompletableFuture<CarData> deleteCarListing(int id){
        CompletableFuture<CarData> result = carCostAPIRepositoryThreaded.threadedDeleteFromDatabase(id);
        return result;
    }
}
