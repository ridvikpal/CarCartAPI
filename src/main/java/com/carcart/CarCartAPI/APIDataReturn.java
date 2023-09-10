package com.carcart.CarCartAPI;

import java.util.List;

/* THIS CLASS HOLDS THE DATA RETURNED BY THE API */
// Really it just holds both a recommendation by ChatGPT and then a list of database listings matching
// the user requests
public class APIDataReturn {
    private String chatGptInfo;

    private List<CarData> databaseListings;

    public APIDataReturn(String chatGptInfo, List<CarData> databaseListings) {
        this.chatGptInfo = chatGptInfo;
        this.databaseListings = databaseListings;
    }

    public String getChatGptInfo() {
        return chatGptInfo;
    }

    public void setChatGptInfo(String chatGptInfo) {
        this.chatGptInfo = chatGptInfo;
    }

    public List<CarData> getDatabaseListings() {
        return databaseListings;
    }

    public void setDatabaseListings(List<CarData> databaseListings) {
        this.databaseListings = databaseListings;
    }
}