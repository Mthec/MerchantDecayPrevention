package com.wurmonline.server.items;

import com.wurmonline.server.FailedException;
import com.wurmonline.server.Items;
import com.wurmonline.server.WurmId;
import com.wurmonline.server.bodys.Body;
import com.wurmonline.server.epic.HexMap;
import mod.wurmunlimited.WurmObjectsFactory;

import javax.annotation.Nullable;
import java.io.IOException;

public class ItemFactory {
    public static int[] metalLumpList = new int[]{46, 221, 223, 205, 47, 220, 49, 44, 45, 48, 837, 698, 694, 1411};

    public static Item createItem(int templateId, float qualityLevel, String creator) throws NoSuchTemplateException {
        return createItem(templateId, qualityLevel, (byte)0, (byte)0, -10, creator);
    }

    public static Item createItem(int templateId, float qualityLevel, byte material, byte rarity, String creator) throws NoSuchTemplateException {
        return createItem(templateId, qualityLevel, material, rarity, -10, creator);
    }

    public static Item createItem(int templateId, float qualityLevel, float posX, float posY, float rot, boolean onSurface, byte rarity, long bridgeId, @Nullable String creator) throws NoSuchTemplateException {
        return createItem(templateId, qualityLevel, posX, posY, rot, onSurface, (byte)0, rarity, bridgeId, creator);
    }

    public static Item createItem(int templateId, float qualityLevel, float posX, float posY, float rot, boolean onSurface, byte material, byte aRarity, long bridgeId, @Nullable String creator) throws NoSuchTemplateException {
        return createItem(templateId, qualityLevel, posX, posY, rot, onSurface, material, aRarity, bridgeId, creator, (byte)0);
    }

    public static Item createItem(int templateId, float ql, float posX, float posY, float rot, boolean onSurface, byte material, byte aRarity, long bridgeId, @Nullable String creator, byte initialAuxData) {
        Item item = WurmObjectsFactory.getCurrent().createNewItem(templateId);
        item.setQualityLevel(ql);
        item.setMaterial(material);
        item.setRarity(aRarity);
        return item;
    }

    public static Item createItem(int templateId, float ql, byte material, byte rarity, long bridgeId, String creator) throws NoSuchTemplateException {
        return createItem(templateId, ql, 1.0f, 1.0f, 0.0f, true, material, rarity, bridgeId, creator);
    }

    public static Item createInventory(long ownerId, short s, float f) {
        return WurmObjectsFactory.getCurrent().createNewItem(0);
    }

    public static Item createBodyPart(Body body, short place, int templateId, String name, float qualityLevel) throws FailedException, NoSuchTemplateException {
        ItemTemplate template = ItemTemplateFactory.getInstance().getTemplate(templateId);
        Item toReturn = null;

        try {
            long wurmId = com.wurmonline.server.WurmId.getNextBodyPartId(body.getOwnerId(), (byte)place, WurmId.getType(body.getOwnerId()) == 0);
            if (template.isRecycled) {
                toReturn = Itempool.getRecycledItem(templateId, qualityLevel);
                if (toReturn != null) {
                    toReturn.clear(-10L, "", 0.0F, 0.0F, 0.0F, 0.0F, "", name, qualityLevel, template.getMaterial(), (byte)0, -10L);
                    toReturn.setPlace(place);
                }
            }

            if (toReturn == null) {
                toReturn = new TempItem(wurmId, place, name, template, qualityLevel, "");
            }

            return toReturn;
        } catch (IOException var9) {
            throw new FailedException(var9);
        }
    }

    public static void decay(long itemId, DbStrings dbStrings) {
        WurmObjectsFactory.getCurrent().removeItem(itemId);
        Items.removeItem(itemId);
    }

    public static String generateName(ItemTemplate template, byte material) {
        String name = template.sizeString + template.getName();
        if (template.getTemplateId() == 683) {
            name = HexMap.generateFirstName() + " " + HexMap.generateSecondName();
        }

        if (template.unique) {
            name = template.getName();
        }

        return name;
    }

    public static void clearData(long wurmId) {

    }

    public static boolean isMetalLump(int itemTemplateId) {
        for (int lumpId : metalLumpList) {
            if (lumpId == itemTemplateId) {
                return true;
            }
        }

        return false;
    }
}
