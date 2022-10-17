package com.wurmonline.server.items;

import java.util.ArrayList;
import java.util.List;

public class WurmMail {

    public static List<WurmMail> allMail = new ArrayList<>();
    public long itemId;
    public long ownerId;

    public WurmMail(byte _type, long _itemId, long _sender, long _receiver, long _price, long _sent, long _expiration, int _sourceServer, boolean _rejected, boolean loading) {
        itemId = _itemId;
        ownerId = _receiver;
    }

    public static void resetStatic() {
        allMail = new ArrayList<>();
    }

    public static void addWurmMail(WurmMail mail) {
        allMail.add(mail);
    }

    public void createInDatabase() {

    }
}
