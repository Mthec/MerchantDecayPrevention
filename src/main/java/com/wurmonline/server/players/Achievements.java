package com.wurmonline.server.players;

import com.wurmonline.server.WurmId;

import java.util.HashMap;
import java.util.Map;

public class Achievements {
    public static Map<Long, Map<Integer, Integer>> triggeredAchievements = new HashMap<>();

    public static void triggerAchievement(long creature, int achievementId) {
        if (WurmId.getType(creature) == 0)
            triggerAchievement(creature, achievementId, 1);
    }

    public static void triggerAchievement(long creature, int achievementId, int counterModifier) {
        triggeredAchievements.putIfAbsent(creature, new HashMap<>());
        triggeredAchievements.get(creature).merge(achievementId, counterModifier, Integer::sum);
    }

    public static boolean hasAchievement(long wurmId, int achievementId) {
        return false;
    }
}
