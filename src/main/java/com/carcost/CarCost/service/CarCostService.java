package com.carcost.CarCost.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarCostService {

    public List<String> returnAllModels(String make) {
        return List.of("Testing 1", "Testing 2");
    }
}
