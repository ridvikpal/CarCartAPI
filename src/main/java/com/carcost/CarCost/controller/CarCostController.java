package com.carcost.CarCost.controller;

import com.carcost.CarCost.model.CarCostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/search")
public class CarCostController {
    private final CarCostService carCostService;

    @Autowired
    public CarCostController(CarCostService carCostService) {
        this.carCostService = carCostService;
    }

    @GetMapping
    public List<String> getAllModels(@RequestParam String make){
        return carCostService.returnAllModels(make);
    }

}
