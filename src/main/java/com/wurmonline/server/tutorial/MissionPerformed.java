package com.wurmonline.server.tutorial;

import com.wurmonline.shared.constants.CounterTypes;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@SuppressWarnings({"FieldCanBeLocal", "SpellCheckingInspection", "FieldMayBeFinal", "PointlessBitwiseExpression"})
public final class MissionPerformed implements CounterTypes {
    private static Logger logger;
    private static final Map<Long, MissionPerformer> missionsPerformers = new HashMap<>();
    private static final String LOADALLMISSIONSPERFORMER = "SELECT * FROM MISSIONSPERFORMED";
    private static final String ADDMISSIONSPERFORMED = "INSERT INTO MISSIONSPERFORMED (PERFORMER,MISSION,STATE,STARTTIME) VALUES(?,?,?,?)";
    private static final String DELETEALLMISSIONSPERFORMER = "DELETE FROM MISSIONSPERFORMED WHERE PERFORMER=?";
    private static final String UPDATESTATE = "UPDATE MISSIONSPERFORMED SET STATE=? WHERE MISSION=? AND PERFORMER=?";
    private static final String SETINACTIVATED = "UPDATE MISSIONSPERFORMED SET INACTIVE=? WHERE MISSION=? AND PERFORMER=?";
    private static final String RESTARTMISSION = "UPDATE MISSIONSPERFORMED SET STARTTIME=?,FINISHEDDATE=? WHERE MISSION=? AND PERFORMER=?";
    private static final String UPDATEFINISHEDDATE = "UPDATE MISSIONSPERFORMED SET FINISHEDDATE=?, ENDTIME=? WHERE MISSION=? AND PERFORMER=?";
    public static final float FINISHED = 100.0f;
    public static final float NOTSTARTED = 0.0f;
    public static final float STARTED = 1.0f;
    public static final float FAILED = -1.0f;
    public static final float SOME_COMPLETED = 33.0f;
    private final int mission;
    private float state;
    private long startTime;
    private long endTime;
    private String endDate;
    private boolean inactive;
    private final long wurmid;
    private final MissionPerformer performer;
    private static long tempMissionPerformedCounter;

    public MissionPerformed(final int missionId, final MissionPerformer perf) {
        this.state = 0.0f;
        this.startTime = 0L;
        this.endTime = 0L;
        this.endDate = "";
        this.inactive = false;
        this.mission = missionId;
        this.wurmid = generateWurmId(this.mission);
        this.performer = perf;
    }

    private static long generateWurmId(final int mission) {
        ++MissionPerformed.tempMissionPerformedCounter;
        return BigInteger.valueOf(MissionPerformed.tempMissionPerformedCounter).shiftLeft(24).longValue() + ((long)mission << 8) + 22L;
    }

    public static int decodeMissionId(final long wurmId) {
        return (int)(wurmId >> 8 & -1L);
    }

    public long getWurmId() {
        return this.wurmid;
    }

    public int getMissionId() {
        return this.mission;
    }

    public Mission getMission() {
        return Missions.getMissionWithId(this.mission);
    }

    public float getState() {
        return this.state;
    }

    public boolean isInactivated() {
        return this.inactive;
    }

    public boolean isCompleted() {
        return this.state == 100.0f;
    }

    public boolean isFailed() {
        return this.state == -1.0f;
    }

    public boolean isStarted() {
        return this.state >= 1.0f;
    }

    public long getStartTimeMillis() {
        return this.startTime;
    }

    protected String getStartDate() {
        return DateFormat.getDateInstance(1).format(new Timestamp(this.startTime));
    }

    protected String getLastTimeToFinish(final int maxSecondsToFinish) {
        return DateFormat.getDateInstance(1).format(new Timestamp(this.startTime + maxSecondsToFinish * 1000L));
    }

    protected long getFinishTimeAsLong(final int maxSecondsToFinish) {
        return this.startTime + maxSecondsToFinish * 1000L;
    }

    protected long getStartTime() {
        return this.startTime;
    }

    String getEndDate() {
        return this.endDate;
    }

    long getEndTime() {
        return this.endTime;
    }

    public static MissionPerformer getMissionPerformer(final long id) {
        return MissionPerformed.missionsPerformers.get(id);
    }

    public static MissionPerformer[] getAllPerformers() {
        return MissionPerformed.missionsPerformers.values().toArray(new MissionPerformer[0]);
    }

    public void setInactive(final boolean inactivate) {
        if (this.inactive != inactivate) {
            this.inactive = inactivate;
        }
    }

    public static void deleteMissionPerformer(final long id) {
        MissionPerformed.missionsPerformers.remove(id);

    }

    public boolean setState(final float newState, final long aPerformer) {
        if (this.state != newState) {
            this.state = newState;
            if (this.state > 100.0f) {
                this.state = 100.0f;
            }
            if (this.state < -1.0f) {
                this.state = -1.0f;
            }
        }
        return this.state >= 100.0f || this.state <= -1.0f;
    }

    public static MissionPerformer startNewMission(final int mission, final long performerId, final float state) {
        MissionPerformer mp = MissionPerformed.missionsPerformers.get(performerId);
        if (mp == null) {
            mp = new MissionPerformer(performerId);
            MissionPerformed.missionsPerformers.put(performerId, mp);
        }
        final MissionPerformed mpf = new MissionPerformed(mission, mp);
        mpf.state = state;
        mpf.startTime = System.currentTimeMillis();
        mp.addMissionPerformed(mpf);

        return mp;
    }
}
