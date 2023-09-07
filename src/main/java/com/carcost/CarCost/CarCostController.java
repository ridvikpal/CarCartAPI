package com.carcost.CarCost;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1")
public class CarCostController {
    private final CarCostService carCostService;

    @Autowired
    public CarCostController(CarCostService carCostService) {
        this.carCostService = carCostService;
    }

    @GetMapping(path = "/search_make")
    public APIDataReturn getAllModels(@RequestParam String make, @RequestParam(required = false) boolean low_price){
        return carCostService.returnAllModels(make, low_price);
    }

    @GetMapping(path = "/search_model")
    public APIDataReturn getModelListings(@RequestParam String make, @RequestParam String model){
        return carCostService.returnModelListings(make, model);
    }

    /* STILL IN DEVELOPMENT */
    @GetMapping(path = "/recommendation")
    public List<String> getRecommendation(@RequestParam String type, @RequestParam(required = false) String make){
        return carCostService.returnCarRecommendations(type, make);
    }
}
