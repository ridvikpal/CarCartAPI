package com.carcost.CarCost;

import org.springframework.stereotype.Component;

import java.util.List;

/* THIS CLASS HOLDS THE DATA RETURNED BY THE API */
// Really it just holds both a recommendation by ChatGPT and then a list of database listings matching
// the user requests
@Component
public class APIDataReturn {
    private String ChatGPTInfo;

    private List<CarData> databaseListings;

    public String getChatGPTInfo() {
        return ChatGPTInfo;
    }

    public void setChatGPTInfo(String chatGPTInfo) {
        ChatGPTInfo = chatGPTInfo;
    }

    public List<CarData> getDatabaseListings() {
        return databaseListings;
    }

    public void setDatabaseListings(List<CarData> databaseListings) {
        this.databaseListings = databaseListings;
    }
}