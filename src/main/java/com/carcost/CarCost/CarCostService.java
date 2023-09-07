package com.carcost.CarCost;

import com.carcost.CarCost.chatgpt.ChatGPTConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarCostService {

    private final CarCostRepository carCostRepository;

    @Autowired
    public CarCostService(CarCostRepository carCostRepository) {
        this.carCostRepository = carCostRepository;
    }

    public APIDataReturn returnMakeListings(String _make, boolean _low_price) {
        String chatGptMakeInfo = ChatGPTConnection.getMakeInfo(_make);
        List<CarData> matchingMakeEntries;
        if (_low_price){
            matchingMakeEntries = carCostRepository.findAllByMakeContainingOrderByPriceAsc(_make);
        }else{
            matchingMakeEntries = carCostRepository.findAllByMakeContaining(_make);
        }
        return new APIDataReturn(chatGptMakeInfo, matchingMakeEntries);
    }

    public APIDataReturn returnModelListings(String _make, String _model, boolean _low_price) {
        String chatGptModelInfo = ChatGPTConnection.getModelInfo(_make, _model);
        List<CarData> matchingModelEntries;
        if (_low_price){
            matchingModelEntries =
                    carCostRepository.findAllByMakeContainingAndModelContainingOrderByPriceAsc(_make, _model);
        }else{
            matchingModelEntries = carCostRepository.findAllByMakeContainingAndModelContaining(_make, _model);
        }
        return new APIDataReturn(chatGptModelInfo, matchingModelEntries);
    }

    public APIDataReturn returnCarRecommendations(String _type, String _make) {
        String chatGptCarRecommendation = ChatGPTConnection.getCarRecommendation(_type, _make);
        List<CarData> matchingTypeEntries;
        if (_make == null || _make.trim().isEmpty()){
            matchingTypeEntries = carCostRepository.findAllByBody_typeContaining(_type);
        }else{
            matchingTypeEntries = carCostRepository.findAllByBody_typeContainingAndMakeContaining(_type, _make);
        }
        return new APIDataReturn(chatGptCarRecommendation, matchingTypeEntries);
    }
}
