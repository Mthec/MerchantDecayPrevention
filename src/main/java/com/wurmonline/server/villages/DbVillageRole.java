package com.wurmonline.server.villages;

import java.io.IOException;

public class DbVillageRole extends VillageRole {
    private static int ids = 1;

    DbVillageRole(int aVillageid, String aName, boolean aTerraform, boolean aCutTrees, boolean aMine, boolean aFarm, boolean aBuild, boolean aHire, boolean aMayInvite, boolean aMayDestroy, boolean aMayManageRoles, boolean aMayExpand, boolean aMayLockFences, boolean aMayPassFences, boolean aIsDiplomat, boolean aMayAttackCitizens, boolean aMayAttackNonCitizens, boolean aMayFish, boolean aMayCutOldTrees, byte aStatus, int appliedToVillage, boolean aMayPushPullTurn, boolean aMayUpdateMap, boolean aMayLead, boolean aMayPickup, boolean aMayTame, boolean aMayLoad, boolean aMayButcher, boolean aMayAttachLock, boolean aMayPickLocks, long appliedToPlayer, int aSettings, int aMoreSettings, int aExtraSettings) throws IOException {
        super(aVillageid, aName, aTerraform, aCutTrees, aMine, aFarm, aBuild, aHire, aMayInvite, aMayDestroy, aMayManageRoles, aMayExpand, aMayLockFences, aMayPassFences, aIsDiplomat, aMayAttackCitizens, aMayAttackNonCitizens, aMayFish, aMayCutOldTrees, aStatus, appliedToVillage, aMayPushPullTurn, aMayUpdateMap, aMayLead, aMayPickup, aMayTame, aMayLoad, aMayButcher, aMayAttachLock, aMayPickLocks, appliedToPlayer, aSettings, aMoreSettings, aExtraSettings);
        this.id = ++ids;
    }

    DbVillageRole(int aId, int aVillageid, String aRoleName, boolean aMayTerraform, boolean aMayCuttrees, boolean aMayMine, boolean aMayFarm, boolean aMayBuild, boolean aMayHire, boolean aMayInvite, boolean aMayDestroy, boolean aMayManageRoles, boolean aMayExpand, boolean aMayPassAllFences, boolean aMayLockFences, boolean aMayAttackCitizens, boolean aMayAttackNonCitizens, boolean aMayFish, boolean aMayCutOldTrees, boolean aMayPushPullTurn, boolean aDiplomat, byte aStatus, int aVillageAppliedTo, boolean aMayUpdateMap, boolean aMayLead, boolean aMayPickup, boolean aMayTame, boolean aMayLoad, boolean aMayButcher, boolean aMayAttachLock, boolean aMayPickLocks, long aPlayerAppliedTo, int aSettings, int aMoreSettings, int aExtraSettings) {
        super(aId, aVillageid, aRoleName, aMayTerraform, aMayCuttrees, aMayMine, aMayFarm, aMayBuild, aMayHire, aMayInvite, aMayDestroy, aMayManageRoles, aMayExpand, aMayPassAllFences, aMayLockFences, aMayAttackCitizens, aMayAttackNonCitizens, aMayFish, aMayCutOldTrees, aMayPushPullTurn, aDiplomat, aStatus, aVillageAppliedTo, aMayUpdateMap, aMayLead, aMayPickup, aMayTame, aMayLoad, aMayButcher, aMayAttachLock, aMayPickLocks, aPlayerAppliedTo, aSettings, aMoreSettings, aExtraSettings);
        this.id = ++ids;
    }

    @Override
    void create() throws IOException {

    }

    @Override
    public void setName(String s) throws IOException {

    }

    @Override
    public void setMayHire(boolean b) throws IOException {

    }

    @Override
    public void setMayBuild(boolean b) throws IOException {

    }

    @Override
    public void setMayCuttrees(boolean b) throws IOException {

    }

    @Override
    public void setMayMine(boolean b) throws IOException {

    }

    @Override
    public void setMayFarm(boolean b) throws IOException {

    }

    @Override
    public void setMayManageRoles(boolean b) throws IOException {

    }

    @Override
    public void setMayDestroy(boolean b) throws IOException {

    }

    @Override
    public void setMayTerraform(boolean b) throws IOException {

    }

    @Override
    public void setMayExpand(boolean b) throws IOException {

    }

    @Override
    public void setMayInvite(boolean b) throws IOException {

    }

    @Override
    public void setMayPassAllFences(boolean b) throws IOException {

    }

    @Override
    public void setMayLockFences(boolean b) throws IOException {

    }

    @Override
    public void setMayAttackCitizens(boolean b) throws IOException {

    }

    @Override
    public void setMayAttackNonCitizens(boolean b) throws IOException {

    }

    @Override
    public void setDiplomat(boolean b) throws IOException {

    }

    @Override
    public void setVillageAppliedTo(int i) throws IOException {

    }

    @Override
    public void setMayFish(boolean b) throws IOException {

    }

    @Override
    public void setMayPushPullTurn(boolean b) throws IOException {

    }

    @Override
    public void setMayLead(boolean b) throws IOException {

    }

    @Override
    public void setMayPickup(boolean b) throws IOException {

    }

    @Override
    public void setMayTame(boolean b) throws IOException {

    }

    @Override
    public void setMayLoad(boolean b) throws IOException {

    }

    @Override
    public void setMayButcher(boolean b) throws IOException {

    }

    @Override
    public void setMayAttachLock(boolean b) throws IOException {

    }

    @Override
    public void setMayPickLocks(boolean b) throws IOException {

    }

    @Override
    public void setMayUpdateMap(boolean b) throws IOException {

    }

    @Override
    public void setCutOld(boolean b) throws IOException {

    }

    @Override
    public void delete() throws IOException {

    }

    @Override
    public void save() throws IOException {

    }

    public int compareTo(DbVillageRole otherDbVillageRole) {
        return this.getName().compareTo(otherDbVillageRole.getName());
    }
}
