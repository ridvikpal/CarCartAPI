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
    public List<String> getAllModels(@RequestParam String make){
        return carCostService.returnAllModels(make);
    }

    @GetMapping(path = "/search_model")
    public List<String> getModelListings(@RequestParam String make, @RequestParam String model){
        return carCostService.returnModelListings(make, model);
    }

    @GetMapping(path = "/recommendation")
    public List<String> getRecommendation(@RequestParam String type, @RequestParam(required = false) String make){
        return carCostService.returnCarRecommendations(type, make);
    }

    @GetMapping(path = "/database")
    public List<CarData> getMakeListings(@RequestParam String make){
        return carCostService.returnMakeListings(make);
    }
}
