package com.carcost.CarCostAPI;

import com.carcost.CarCostAPI.chatgpt.ChatGPTConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarCostAPIService {

    private final CarCostAPIRepository carCostAPIRepository;
    private final ChatGPTConnection chatGPTConnection;

    @Autowired
    public CarCostAPIService(CarCostAPIRepository carCostAPIRepository, ChatGPTConnection chatGPTConnection) {
        this.carCostAPIRepository = carCostAPIRepository;
        this.chatGPTConnection = chatGPTConnection;
    }

    public APIDataReturn returnMakeListings(String _make, boolean _low_price) {
        String chatGptMakeInfo = chatGPTConnection.getMakeInfo(_make);
        List<CarData> matchingMakeEntries;
        if (_low_price){
            matchingMakeEntries = carCostAPIRepository.findAllByMakeContainingOrderByPriceAsc(_make);
        }else{
            matchingMakeEntries = carCostAPIRepository.findAllByMakeContaining(_make);
        }
        return new APIDataReturn(chatGptMakeInfo, matchingMakeEntries);
    }

    public APIDataReturn returnModelListings(String _make, String _model, boolean _low_price) {
        String chatGptModelInfo = chatGPTConnection.getModelInfo(_make, _model);
        List<CarData> matchingModelEntries;
        if (_low_price){
            matchingModelEntries =
                    carCostAPIRepository.findAllByMakeContainingAndModelContainingOrderByPriceAsc(_make, _model);
        }else{
            matchingModelEntries = carCostAPIRepository.findAllByMakeContainingAndModelContaining(_make, _model);
        }
        return new APIDataReturn(chatGptModelInfo, matchingModelEntries);
    }

    public APIDataReturn returnCarRecommendations(String _type, String _make) {
        String chatGptCarRecommendation = chatGPTConnection.getCarRecommendation(_type, _make);
        List<CarData> matchingTypeEntries;
        if (_make == null || _make.trim().isEmpty()){
            matchingTypeEntries = carCostAPIRepository.findAllByBodyTypeContaining(_type);
        }else{
            matchingTypeEntries = carCostAPIRepository.findAllByBodyTypeContainingAndMakeContaining(_type, _make);
        }
        return new APIDataReturn(chatGptCarRecommendation, matchingTypeEntries);
    }
}
