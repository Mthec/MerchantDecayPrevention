package com.wurmonline.server.kingdom;

import com.wurmonline.server.players.Player;
import mod.wurmunlimited.WurmObjectsFactory;
import org.mockito.stubbing.Answer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.*;

public class King {
    public static final Set<King> kings = new HashSet<>();
    public static final Map<Byte, Map<Integer, Player>> appointments = new HashMap<>();
    public final long wurmId;
    public final byte kingdomId;

    static {
        appointments.put((byte)0, new HashMap<>());
        appointments.put((byte)1, new HashMap<>());
        appointments.put((byte)2, new HashMap<>());
        appointments.put((byte)3, new HashMap<>());
    }

    public King(long wurmId, byte kingdomId) {
        this.wurmId = wurmId;
        this.kingdomId = kingdomId;
    }

    @SuppressWarnings("UnusedReturnValue")
    public static King createKing(byte kingdomId, String name, long wurmId, byte gender) {
        King king = new King(wurmId, kingdomId);
        kings.add(king);
        return king;
    }

    public static Appointments getCurrentAppointments(byte kingdomId) {
        Appointments appointmentsObj = mock(Appointments.class);
        doAnswer((Answer<Void>)i -> {
            Player player = (Player)WurmObjectsFactory.getCurrent().getCreature((long)i.getArgument(1));
            appointments.get(player.getKingdomId()).put(i.getArgument(0), player);
            return null;
        }).when(appointmentsObj).setOfficial(anyInt(), anyLong());
        return appointmentsObj;
    }

    public static boolean isKing(long wurmId, byte kingdom) {
        return kings.stream().anyMatch(k -> k.wurmId == wurmId);
    }

    public static boolean isOfficial(int officeId, long wurmId, byte kingdomId) {
        Player player = appointments.get(kingdomId).get(officeId);
        return player != null && player.getWurmId() == wurmId;
    }

    public static King getKing(byte kingdom) {
        return kings.stream().filter(k -> k.kingdomId == kingdom).findAny().orElse(null);
    }

    public long getChallengeAcceptedDate() {
        return 0;
    }

    public static void purgeKing(byte kingdom) {
        kings.removeIf(k -> k.kingdomId == kingdom);
    }

    public static void setToNoKingdom(byte kingdom) {
        kings.removeIf(k -> k.kingdomId == kingdom);
    }
}
