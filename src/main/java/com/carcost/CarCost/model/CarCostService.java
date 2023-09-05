package com.carcost.CarCost.model;

import com.carcost.CarCost.model.chatgpt.ChatGPTConnection;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarCostService {

    public List<String> returnAllModels(String _make) {
        return List.of(ChatGPTConnection.getMakeInfo(_make));
    }

    public List<String> returnModelListings(String _make, String _model, Integer _year) {
        return List.of(ChatGPTConnection.getModelInfo(_make, _model, _year));
    }

    public List<String> returnCarRecommendations(String _type, String _make) {
        return List.of(ChatGPTConnection.getCarRecommendation(_type, _make));
    }
}
