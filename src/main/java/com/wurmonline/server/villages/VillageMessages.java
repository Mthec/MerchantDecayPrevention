//
// Decompiled by Procyon v0.5.36
//

package com.wurmonline.server.villages;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@SuppressWarnings({"SpellCheckingInspection", "FinalStaticMethod", "FinalPrivateMethod"})
public final class VillageMessages {
    private static final Logger logger;
    private static final Map<Integer, VillageMessages> villagesMessages;
    private static final String LOAD_ALL_MSGS = "SELECT * FROM VILLAGEMESSAGES";
    private static final String DELETE_VILLAGE_MSGS = "DELETE FROM VILLAGEMESSAGES WHERE VILLAGEID=?";
    private static final String CREATE_MSG = "INSERT INTO VILLAGEMESSAGES (VILLAGEID,FROMID,TOID,MESSAGE,POSTED,PENCOLOR,EVERYONE) VALUES (?,?,?,?,?,?,?);";
    private static final String DELETE_MSG = "DELETE FROM VILLAGEMESSAGES WHERE VILLAGEID=? AND TOID=? AND POSTED=?";
    private static final String DELETE_PLAYER_MSGS = "DELETE FROM VILLAGEMESSAGES WHERE VILLAGEID=? AND TOID=?";
    private final Map<Long, Map<Long, VillageMessage>> villageMsgs;

    public VillageMessages() {
        this.villageMsgs = new ConcurrentHashMap<>();
    }

    @SuppressWarnings("UnusedReturnValue")
    public VillageMessage put(final long toId, final VillageMessage value) {
        Map<Long, VillageMessage> msgs = this.villageMsgs.get(toId);
        if (msgs == null) {
            msgs = new ConcurrentHashMap<>();
            this.villageMsgs.put(toId, msgs);
        }
        return msgs.put(value.getPostedTime(), value);
    }

    public Map<Long, VillageMessage> get(final long toId) {
        final Map<Long, VillageMessage> msgs = this.villageMsgs.get(toId);
        if (msgs == null) {
            return new ConcurrentHashMap<>();
        }
        return msgs;
    }

    public void remove(final long playerId, final long posted) {
        final Map<Long, VillageMessage> msgs = this.villageMsgs.get(playerId);
        if (msgs != null) {
            msgs.remove(posted);
        }
    }

    public void remove(final long playerId) {
        this.villageMsgs.remove(playerId);
    }

    public static void loadVillageMessages() {

    }

    public static void add(final VillageMessage villageMsg) {
        VillageMessages villageMsgs = VillageMessages.villagesMessages.get(villageMsg.getVillageId());
        if (villageMsgs == null) {
            villageMsgs = new VillageMessages();
            VillageMessages.villagesMessages.put(villageMsg.getVillageId(), villageMsgs);
        }
        villageMsgs.put(villageMsg.getToId(), villageMsg);
    }

    public static VillageMessage[] getVillageMessages(final int villageId, final long toId) {
        final VillageMessages villageMsgs = VillageMessages.villagesMessages.get(villageId);
        if (villageMsgs == null) {
            return new VillageMessage[0];
        }
        return villageMsgs.get(toId).values().toArray(new VillageMessage[villageMsgs.size()]);
    }

    private int size() {
        return 0;
    }

    public static final VillageMessage create(final int villageId, final long fromId, final long toId, final String message, final int penColour, final boolean everyone) {
        final long posted = System.currentTimeMillis();
        dbCreate(villageId, fromId, toId, message, posted, penColour, everyone);
        final VillageMessage villageMsg = new VillageMessage(villageId, fromId, toId, message, penColour, posted, everyone);
        add(villageMsg);
        return villageMsg;
    }

    public static final void delete(final int villageId) {
        dbDelete(villageId);
        VillageMessages.villagesMessages.remove(villageId);
    }

    public static final void delete(final int villageId, final long toId) {
        dbDelete(villageId, toId);
        final VillageMessages villageMsgs = VillageMessages.villagesMessages.get(villageId);
        if (villageMsgs != null) {
            villageMsgs.remove(toId);
        }
    }

    public static final void delete(final int villageId, final long toId, final long posted) {
        dbDelete(villageId, toId, posted);
        final VillageMessages villageMsgs = VillageMessages.villagesMessages.get(villageId);
        if (villageMsgs != null) {
            villageMsgs.remove(toId, posted);
        }
    }

    private static final void dbCreate(final int villageId, final long fromId, final long toId, final String message, final long posted, final int penColour, final boolean everyone) {

    }

    private static final void dbDelete(final int villageId) {

    }

    private static final void dbDelete(final int villageId, final long toId) {

    }

    private static final void dbDelete(final int villageId, final long toId, final long posted) {

    }

    static {
        logger = Logger.getLogger(VillageMessages.class.getName());
        villagesMessages = new ConcurrentHashMap<>();
    }
}
