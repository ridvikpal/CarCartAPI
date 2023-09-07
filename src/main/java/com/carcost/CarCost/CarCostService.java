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


    public List<String> returnAllModels(String _make) {
        return List.of(ChatGPTConnection.getMakeInfo(_make));
    }

    public List<String> returnModelListings(String _make, String _model) {
        return List.of(ChatGPTConnection.getModelInfo(_make, _model));
    }

    public List<String> returnCarRecommendations(String _type, String _make) {
        return List.of(ChatGPTConnection.getCarRecommendation(_type, _make));
    }

    public List<CarData> returnMakeListings(String _make) {
        return carCostRepository.findAllByMake(_make);
    }

//    public List<String> returnDatabaseEntries(){
//        carCostRepository.
//    }
}
