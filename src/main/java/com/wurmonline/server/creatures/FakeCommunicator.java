package com.wurmonline.server.creatures;

import com.wurmonline.communication.SocketConnection;
import com.wurmonline.server.bodys.Wound;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.sounds.Sound;
import org.gotti.wurmunlimited.modloader.ReflectionUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FakeCommunicator extends Communicator {
    private final Creature me;
    public static final String empty = "EMPTY";
    private final List<String> messages = new ArrayList<>();
    public String lastBmlContent = empty;
    private final List<String> bml = new ArrayList<>();
    public boolean tradeWindowClosed = false;
    public Boolean tradeAgreed = null;
    public Item sentToInventory;
    public int sentToInventoryPrice;
    public boolean sentStartTrading = false;
    public CustomizeFace sendCustomizeFace = null;
    public final List<Long> openedInventoryWindows = new ArrayList<>();
    public final List<Long> closedInventoryWindows = new ArrayList<>();
    public int stamina = 65535;
    public int damage = 0;
    public String actionMe = "";
    public String actionOther = "";

    public static class CustomizeFace {
        public final long face;
        public final long itemId;

        CustomizeFace(long face, long itemId) {
            this.face = face;
            this.itemId = itemId;
        }
    }

    public FakeCommunicator(Creature creature) {
        me = creature;
        if (me instanceof Player)
            player = (Player)me;

        try {
            ReflectionUtil.setPrivateField(this, Communicator.class.getDeclaredField("connection"), new SocketConnection("", -1, 10));
        } catch (IllegalAccessException | NoSuchFieldException | IOException e) {
            throw new RuntimeException(e);
        }
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

    public void clearBml() {
        bml.clear();
        lastBmlContent = empty;
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
        if (!(me instanceof Player))
            me.getTradeHandler().tradeChanged();
    }

    @Override
    public void sendAddToInventory(Item item, long inventoryWindow, long rootId, int price) {
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

    @Override
    public void sendPlonk(short plonkId) {}

    @Override
    public void sendAddStatusEffect(SpellEffectsEnum effect, int duration) {}

    @Override
    public void sendAddSpellEffect(SpellEffectsEnum effect, int duration, float power) {}

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

    @Override
    public void sendCustomizeFace(long face, long itemId) {
        sendCustomizeFace = new CustomizeFace(face, itemId);
    }

    @Override
    protected void sendCombatStatus(final float distanceToTarget, final float footing, final byte stance) {

    }

    @Override
    protected void sendTarget(final long id) {

    }

    @Override
    protected void sendStatus(final String status) {

    }

    @Override
    public void sendMusic(final Sound sound) {

    }

    @Override
    public void sendRemoveAlly(final String name) {

    }

    @Override
    public void sendOpenInventoryWindow(final long inventoryWindow, final String title) {
        openedInventoryWindows.add(inventoryWindow);
    }

    @Override
    public void sendClearMapAnnotationsOfType(final byte type) {

    }

    @Override
    protected void sendStamina(int stamina, int damage) {
        this.stamina = stamina;
        this.damage = damage;
    }

    @Override
    public void sendAddWound(final Wound wound, final Item bodyPart) {}

    @Override
    public void setGroundOffset(final int offset, final boolean immediately) {}

    @Override
    public void sendSpecialMove(final short move, final String moveName) {}

    @Override
    public void sendCombatOptions(final byte[] options, final short tenthsOfSeconds) {}

    @Override
    public void sendCombatNormalMessage(final String message) {
        this.sendCombatNormalMessage(message, (byte)0);
    }

    @Override
    public void sendCombatNormalMessage(final String message, final byte messageType) {
        this.sendCombatServerMessage(message, (byte)(-1), (byte)(-1), (byte)(-1), messageType);
    }

    @Override
    public void sendCombatAlertMessage(final String message) {
        this.sendCombatServerMessage(message, (byte)(-1), (byte)(-106), (byte)10, (byte)0);
    }

    @Override
    public void sendCombatSafeMessage(final String message) {
        this.sendCombatServerMessage(message, (byte)102, (byte)(-72), (byte)120, (byte)0);
    }

    @Override
    public void sendCombatServerMessage(final String message, final byte r, final byte g, final byte b) {
        this.sendCombatServerMessage(message, r, g, b, (byte)0);
    }

    @Override
    public void sendCombatServerMessage(final String message, final byte r, final byte g, final byte b, final byte messageType) {
        System.out.println(message);
    }

    @Override
    public void sendRemoveWound(final Wound wound) {}

    @Override
    public void sendRemoveSpellEffect(final long id, final SpellEffectsEnum spellEffect) {}

    @Override
    public void sendRemoveSpellEffect(final SpellEffectsEnum effect) {}

    @Override
    protected void sendSpeedModifier(final float speedModifier) {}

    @Override
    public void sendDead() {}

    public void sendUpdateWound(final Wound wound, final Item bodyPart) {}

    @Override
    public boolean sendCloseInventoryWindow(long windowId) {
        closedInventoryWindows.add(windowId);
        return true;
    }

    @Override
    public void sendActionControl(final long creatureId, final String actionString, final boolean start, final int timeLeft) {
        if (creatureId == me.getWurmId()) {
            actionMe = actionString;
        } else {
            actionOther = actionString;
        }
    }
}