package com.wurmonline.server.economy;

public class LocalSupplyDemand {
    public final long id;
    public int purchased = 0;
    public int sold = 0;


    LocalSupplyDemand(long traderId) {
        id = traderId;
    }

    public void addItemPurchased(int templateId, float f) {
        ++purchased;
    }

    public void addItemSold(int templateId, float f) {
        ++sold;
    }
}
