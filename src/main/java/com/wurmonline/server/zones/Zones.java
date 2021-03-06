package com.wurmonline.server.zones;

import com.wurmonline.math.TilePos;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.villages.Village;
import com.wurmonline.server.villages.Villages;
import org.mockito.stubbing.Answer;

import javax.annotation.Nullable;
import java.util.*;
import java.util.logging.Logger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Zones {
    private static final Logger logger = Logger.getLogger(Zones.class.getName());

    public static int numberOfZones;
    public static int worldTileSizeX = 1024;
    public static int worldTileSizeY = 1024;
    public static float worldMeterSizeX = (float)((worldTileSizeX - 1) * 4);
    public static float worldMeterSizeY = (float)((worldTileSizeY - 1) * 4);
    private static Zone[][] zones;
    public static final Map<XY, VolaTile> tiles = new HashMap<>();

    private static class XY {
        private final int x;
        private final int y;
        private final Zone zone;

        private XY(int x, int y, Zone zone) {
            this.x = x;
            this.y = y;
            this.zone = zone;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, zone);
        }

        public boolean equals(Object other) {
            if (other instanceof XY) {
                XY otherXY = (XY)other;
                return otherXY.x == x && otherXY.y == y && otherXY.zone == zone;
            }

            return false;
        }
    }

    public static void resetStatic() {
        zones = new Zone[worldTileSizeX >> 6][worldTileSizeY >> 6];
        tiles.clear();
    }

    public static int safeTileX(int i) {
        return 1;
    }

    public static int safeTileY(int i) {
        return 1;
    }

    public static final float calculatePosZ(float posX, float posY, VolaTile tile, boolean isOnSurface, boolean floating, float currentPosZ, @Nullable Creature creature, long bridgeId) {
        return 1;
    }

    private static VolaTile getOrPut(int x, int y, Zone zone) {
        XY xy = new XY(x, y, zone);
        VolaTile maybeTile = tiles.get(xy);
        if (maybeTile != null)
            return maybeTile;

        VolaTile tile = mock(VolaTile.class);
        when(tile.getTileX()).thenReturn(x);
        when(tile.getTileY()).thenReturn(y);
        boolean isOnSurface = zone.isOnSurface();
        when(tile.isOnSurface()).thenReturn(isOnSurface);
        when(tile.getCreatures()).thenReturn(new Creature[0]);
        when(tile.getStructure()).thenReturn(null);
        when(tile.getVillage()).thenAnswer(i -> {
            for (Village village : Villages.getVillages()) {
                if (village.covers(tile.getTileX(), tile.getTileY())) {
                    return village;
                }
            }

            return null;
        });
        tiles.put(xy, tile);
        return tile;
    }

    private static Zone createMockZone() {
        Zone zone = mock(Zone.class);
        when(zone.getTileOrNull(anyInt(), anyInt())).thenAnswer((Answer<VolaTile>)i -> tiles.get(new XY(i.getArgument(0), i.getArgument(1), zone)));
        when(zone.getOrCreateTile(any(TilePos.class))).thenAnswer((Answer<VolaTile>)i -> {
            TilePos tilePos = i.getArgument(0);
            return getOrPut(tilePos.x, tilePos.y, zone);
        });
        when(zone.getOrCreateTile(anyInt(), anyInt())).thenAnswer((Answer<VolaTile>)i -> getOrPut(i.getArgument(0), i.getArgument(1), zone));
        return zone;
    }

    public static Zone getZone(int tilex, int tiley, boolean surfaced) throws NoSuchZoneException {
        Zone newZone;
        try {
            newZone = zones[tilex >> 6][tiley >> 6];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new NoSuchZoneException("No such zone at " + tilex + " - " + tiley);
        }

        if (newZone == null) {
            newZone = createMockZone();
            zones[tilex >> 6][tiley >> 6] = newZone;
        }

        return newZone;
    }

    public static VolaTile getOrCreateTile(int tileX, int tileY, boolean surface) {
        try {
            return getZone(tileX, tileY, surface).getOrCreateTile(tileX, tileY);
        } catch (NoSuchZoneException e) {
            return null;
        }
    }

    public static VirtualZone createZone(Creature watcher, int startX, int startY, int centerX, int centerY, int size, boolean surface) {
        return new VirtualZone(watcher, startX, startY, centerX, centerY, size, surface);
    }

    public static Zone[] getZonesCoveredBy(VirtualZone zone) {
        final Set<Zone> zoneList = new HashSet<>();
        for (int x = zone.getStartX() >> 6; x <= zone.getEndX() >> 6; ++x) {
            for (int y = zone.getStartY() >> 6; y <= zone.getEndY() >> 6; ++y) {
                try {
                    final Zone zone2 = getZone(x << 6, y << 6, zone.isOnSurface());
                    zoneList.add(zone2);
                }
                catch (NoSuchZoneException ignored) {}
            }
        }
        final Zone[] toReturn = new Zone[zoneList.size()];
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

    public static Zone[] getZonesCoveredBy(int startX, int startY, int endX, int endY, boolean surfaced) {
        final Set<Zone> zoneList = new HashSet<>();
        for (int x = startX >> 6; x <= endX >> 6; ++x) {
            for (int y = startY >> 6; y <= endY >> 6; ++y) {
                try {
                    final Zone zone2 = getZone(x << 6, y << 6, surfaced);
                    zoneList.add(zone2);
                }
                catch (NoSuchZoneException ignored) {}
            }
        }
        final Zone[] toReturn = new Zone[zoneList.size()];
        return zoneList.toArray(toReturn);
    }

    public static void removeZone(int var1) {

    }

    public static void setKingdom(int var1, int var2, byte var3) {

    }

    public static boolean isGoodTileForSpawn(int var1, int var2, boolean var3) {
        return true;
    }

    public static void setKingdom(final int tilex, final int tiley, final int sizeX, final int sizeY, final byte kingdom) {

    }

    public static void addWarDomains() {

    }

    public static boolean isGoodTileForSpawn(int i1, int i2, boolean b1, boolean b2) {
        return true;
    }
}
