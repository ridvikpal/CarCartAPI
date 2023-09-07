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

    public APIDataReturn returnAllModels(String _make) {
        String chatGptMakeInfo = ChatGPTConnection.getMakeInfo(_make);
        List<CarData> matchingMakeEntries = carCostRepository.findAllByMakeContaining(_make);
        return new APIDataReturn(chatGptMakeInfo, matchingMakeEntries);
    }

    public APIDataReturn returnModelListings(String _make, String _model) {
        String chatGptModelInfo = ChatGPTConnection.getModelInfo(_make, _model);
        List<CarData> matchingModelEntries = carCostRepository.findAllByMakeContainingAndModelContaining(_make, _model);
        return new APIDataReturn(chatGptModelInfo, matchingModelEntries);
    }

    public List<String> returnCarRecommendations(String _type, String _make) {
        return List.of(ChatGPTConnection.getCarRecommendation(_type, _make));
    }
}
