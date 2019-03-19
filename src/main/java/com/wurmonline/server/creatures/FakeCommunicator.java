package com.wurmonline.server.creatures;

import com.wurmonline.server.items.Item;
import com.wurmonline.server.players.Player;

import java.util.ArrayList;
import java.util.List;

public class FakeCommunicator extends Communicator {

    private Creature me;
    public static final String empty = "EMPTY";
    private List<String> messages = new ArrayList<>(5);
    public String lastBmlContent = empty;
    private List<String> bml = new ArrayList<>(5);
    public boolean tradeWindowClosed = false;
    public Boolean tradeAgreed = null;
    public Item sentToInventory;
    public int sentToInventoryPrice;
    public boolean sentStartTrading = false;

    public FakeCommunicator(Creature creature) {
        me = creature;
        if (me instanceof Player)
            player = (Player)me;
    }

    public String[] getMessages() {
        return messages.toArray(new String[0]);
    }

    public String getLastMessage() {
        if (messages.isEmpty())
            return empty;
        else
            return messages.get(messages.size() - 1);
    }

    @Override
    public void sendNormalServerMessage(String message) {
        System.out.println(message);
        messages.add(message);
    }

    @Override
    public void sendNormalServerMessage(String message, byte messageType) {
        sendNormalServerMessage(message);
    }

    @Override
    public void sendSafeServerMessage(String message) {
        System.out.println(message);
        messages.add(message);
    }

    @Override
    public void sendSafeServerMessage(String message, byte messageType) {
        sendSafeServerMessage(message);
    }

    @Override
    public void sendAlertServerMessage(String message) {
        System.out.println(message);
        messages.add(message);
    }

    @Override
    public void sendAlertServerMessage(String message, byte messageType) {
        sendAlertServerMessage(message);
    }

    public String[] getBml() {
        return bml.toArray(new String[0]);
    }

    @Override
    public void sendBml(int width, int height, boolean resizeable, boolean closeable, String content, int r, int g, int b, String title) {
        lastBmlContent = content;
        bml.add(content);
    }

    @Override
    public void sendCloseTradeWindow() {
        if (!(me instanceof Player))
            me.endTrade();
        tradeWindowClosed = true;
        System.out.println("Trade window closed.");
    }

    @Override
    public void sendTradeAgree(Creature creature, boolean satisfied) {
        if (creature == me)
            tradeAgreed = satisfied;
    }

    @Override
    public void sendTradeChanged(int id) {
        if (me instanceof Player)
            //super.sendTradeChanged(id);
            return;
        else
            me.getTradeHandler().tradeChanged();
    }

    @Override
    public void sendAddToInventory(Item item, long inventoryWindow, long rootid, int price) {
        sentToInventory = item;
        sentToInventoryPrice = price;
    }

    @Override
    public void sendUpdateInventoryItem(Item item) {

    }

    @Override
    public void sendRemoveFromInventory(Item item) {
    }

    @Override
    public void sendRemoveFromInventory(Item item, long inventoryWindow) {
    }

    @Override
    public void sendStartTrading(Creature opponent) {
        sentStartTrading = true;
    }

    @Override
    public void sendIsEmpty(long window, long wurmId) {}

    public void clear() {
        lastBmlContent = empty;
        messages.clear();
        bml.clear();
        tradeWindowClosed = false;
        tradeAgreed = null;
        sentToInventory = null;
        sentToInventoryPrice = 0;
        sentStartTrading = false;
    }
}