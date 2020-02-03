package com.wurmonline.server.zones;

import com.wurmonline.math.TilePos;
import com.wurmonline.server.Constants;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.villages.Village;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.annotation.Nullable;

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class Zones {
    private static final Logger logger = Logger.getLogger(Zones.class.getName());
    public static Item marketStall = null;
    public static Creature creature = null;

    public static int numberOfZones;
    public static int worldTileSizeX = 1024;
    public static int worldTileSizeY = 1024;
    public static float worldMeterSizeX = (float)((worldTileSizeX - 1) * 4);
    public static float worldMeterSizeY = (float)((worldTileSizeY - 1) * 4);
    private static Zone[][] zones;
    public static Map<VolaTile, Village> villages = new HashMap<>();

    public static void resetStatic() {
        marketStall = null;
        creature = null;
        zones = new Zone[worldTileSizeX >> 6][worldTileSizeY >> 6];
        villages = new HashMap<>();
    }

    public static int safeTileX(int i) {
        return 1;
    }

    public static int safeTileY(int i) {
        return 1;
    }

    public static final float calculatePosZ(float posx, float posy, VolaTile tile, boolean isOnSurface, boolean floating, float currentPosZ, @Nullable Creature creature, long bridgeId) {
        return 1;
    }

    public static Zone getZone(int tilex, int tiley, boolean surfaced) throws NoSuchZoneException {
        Zone newZone;
        try {
            newZone = zones[tilex >> 6][tiley >> 6];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new NoSuchZoneException("No such zone at " + tilex + " - " + tiley);
        }

        if (newZone == null) {
            newZone = mock(Zone.class);
            zones[tilex >> 6][tiley >> 6] = newZone;

            VolaTile volaTile = mock(VolaTile.class);
            when(newZone.getOrCreateTile(anyInt(), anyInt())).thenReturn(volaTile);
            when(volaTile.getVillage()).thenAnswer((Answer<Village>)invocation -> villages.get(volaTile));
            when(volaTile.getStructure()).thenReturn(null);
            if (marketStall == null)
                when(volaTile.getItems()).thenReturn(new Item[0]);
            else
                when(volaTile.getItems()).thenReturn(new Item[] {marketStall});
            if (creature == null)
                when(volaTile.getCreatures()).thenReturn(new Creature[0]);
            else
                when(volaTile.getCreatures()).thenReturn(new Creature[] {creature});

            doAnswer((Answer<Void>)i -> {
                villages.put(volaTile, i.getArgument(0));
                return null;
            }).when(newZone).addVillage(any());
        }

//        ;new Zone(tilex, tilex, tiley, tiley, surfaced) {
//            @Override
//            void save() throws IOException {
//
//            }
//
//            @Override
//            void load() throws IOException {
//
//            }
//
//            @Override
//            void loadFences() throws IOException {
//
//            }
//        };
//        try {
//            Field wurmid = Zone.class.getDeclaredMethod("getOrCreateTile", TilePos.class);
//            wurmid.setAccessible(true);
//            Field modifiers = Field.class.getDeclaredField("modifiers");
//            modifiers.setAccessible(true);
//            modifiers.setInt(wurmid, wurmid.getModifiers() & ~Modifier.FINAL);
//            wurmid.set(newShop, wurmId);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }

        return newZone;
    }

    public static VolaTile getOrCreateTile(int tileX, int tileY, boolean surface) throws NoSuchZoneException {
        return getZone(tileX, tileY, surface).getOrCreateTile(tileX, tileY);
    }

    public static VirtualZone createZone(Creature watcher, int startX, int startY, int centerX, int centerY, int size, boolean surface) {
        return new VirtualZone(watcher, startX, startY, centerX, centerY, size, surface);
    }

    public static Zone[] getZonesCoveredBy(VirtualZone zone) {
        Set<Zone> zoneList = new HashSet<>();

        try {
            Zone zone2 = getZone(zone.getStartX(), zone.getStartY(), zone.isOnSurface());

            zoneList.add(zone2);
        } catch (NoSuchZoneException var5) {
        }

        Zone[] toReturn = new Zone[zoneList.size()];
        return zoneList.toArray(toReturn);
    }

    public static float calculateHeight(float x, float y, boolean surface) {
        return 0f;
    }

    public static VolaTile getTileOrNull(int tileX, int tileY, boolean surface) {
        try {
            return getZone(tileX, tileY, surface).getOrCreateTile(tileX, tileY);
        } catch (NoSuchZoneException e) {
            return null;
        }
    }

    public static VolaTile getTileOrNull(TilePos pos, boolean var1) {
        try {
            int tileX = pos.x;
            int tileY = pos.y;
            return getZone(tileX, tileY, true).getOrCreateTile(tileX, tileY);
        } catch (NoSuchZoneException e) {
            return null;
        }
    }

    public static Zone[] getZonesCoveredBy(int startx, int starty, int var3, int var4, boolean var5) {
        Set<Zone> zoneList = new HashSet<>();

        try {
            Zone zone2 = getZone(startx, starty, true);

            zoneList.add(zone2);
        } catch (NoSuchZoneException e) {
        }

        Zone[] toReturn = new Zone[zoneList.size()];
        return zoneList.toArray(toReturn);
    }

    public static void removeZone(int var1) {

    }

    public static void setKingdom(int var1, int var2, byte var3) {

    }

    public static boolean isGoodTileForSpawn(int var1, int var2, boolean var3) {
        return true;
    }
}
