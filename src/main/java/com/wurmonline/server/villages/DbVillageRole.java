package com.wurmonline.server.villages;

import java.io.IOException;

public class DbVillageRole extends VillageRole {
    private static int ids = 1;

    public DbVillageRole(int aVillageId, String aName, boolean aTerraform, boolean aCutTrees, boolean aMine, boolean aFarm, boolean aBuild, boolean aHire, boolean aMayInvite, boolean aMayDestroy, boolean aMayManageRoles, boolean aMayExpand, boolean aMayLockFences, boolean aMayPassFences, boolean aIsDiplomat, boolean aMayAttackCitizens, boolean aMayAttackNonCitizens, boolean aMayFish, boolean aMayCutOldTrees, byte aStatus, int appliedToVillage, boolean aMayPushPullTurn, boolean aMayUpdateMap, boolean aMayLead, boolean aMayPickup, boolean aMayTame, boolean aMayLoad, boolean aMayButcher, boolean aMayAttachLock, boolean aMayPickLocks, long appliedToPlayer, int aSettings, int aMoreSettings, int aExtraSettings) throws IOException {
        super(aVillageId, aName, aTerraform, aCutTrees, aMine, aFarm, aBuild, aHire, aMayInvite, aMayDestroy, aMayManageRoles, aMayExpand, aMayLockFences, aMayPassFences, aIsDiplomat, aMayAttackCitizens, aMayAttackNonCitizens, aMayFish, aMayCutOldTrees, aStatus, appliedToVillage, aMayPushPullTurn, aMayUpdateMap, aMayLead, aMayPickup, aMayTame, aMayLoad, aMayButcher, aMayAttachLock, aMayPickLocks, appliedToPlayer, aSettings, aMoreSettings, aExtraSettings);
        this.id = ++ids;
    }

    DbVillageRole(int aId, int aVillageId, String aRoleName, boolean aMayTerraform, boolean aMayCuttrees, boolean aMayMine, boolean aMayFarm, boolean aMayBuild, boolean aMayHire, boolean aMayInvite, boolean aMayDestroy, boolean aMayManageRoles, boolean aMayExpand, boolean aMayPassAllFences, boolean aMayLockFences, boolean aMayAttackCitizens, boolean aMayAttackNonCitizens, boolean aMayFish, boolean aMayCutOldTrees, boolean aMayPushPullTurn, boolean aDiplomat, byte aStatus, int aVillageAppliedTo, boolean aMayUpdateMap, boolean aMayLead, boolean aMayPickup, boolean aMayTame, boolean aMayLoad, boolean aMayButcher, boolean aMayAttachLock, boolean aMayPickLocks, long aPlayerAppliedTo, int aSettings, int aMoreSettings, int aExtraSettings) {
        super(aId, aVillageId, aRoleName, aMayTerraform, aMayCuttrees, aMayMine, aMayFarm, aMayBuild, aMayHire, aMayInvite, aMayDestroy, aMayManageRoles, aMayExpand, aMayPassAllFences, aMayLockFences, aMayAttackCitizens, aMayAttackNonCitizens, aMayFish, aMayCutOldTrees, aMayPushPullTurn, aDiplomat, aStatus, aVillageAppliedTo, aMayUpdateMap, aMayLead, aMayPickup, aMayTame, aMayLoad, aMayButcher, aMayAttachLock, aMayPickLocks, aPlayerAppliedTo, aSettings, aMoreSettings, aExtraSettings);
        this.id = ++ids;
    }

    @Override
    void create() throws IOException {

    }

    @Override
    public void setName(String s) throws IOException {
        name = s;
    }

    @Override
    public void setMayHire(boolean b) throws IOException {
        mayHire = b;
    }

    @Override
    public void setMayBuild(boolean b) throws IOException {
        mayBuild = b;
    }

    @Override
    public void setMayCuttrees(boolean b) throws IOException {
        mayCuttrees = b;
    }

    @Override
    public void setMayMine(boolean b) throws IOException {
        mayMine = b;
    }

    @Override
    public void setMayFarm(boolean b) throws IOException {
        mayFarm = b;
    }

    @Override
    public void setMayManageRoles(boolean b) throws IOException {
        mayManageRoles = b;
    }

    @Override
    public void setMayDestroy(boolean b) throws IOException {
        mayDestroy = b;
    }

    @Override
    public void setMayTerraform(boolean b) throws IOException {
        mayTerraform = b;
    }

    @Override
    public void setMayExpand(boolean b) throws IOException {
        mayExpand = b;
    }

    @Override
    public void setMayInvite(boolean b) throws IOException {
        mayInvite = b;
    }

    @Override
    public void setMayPassAllFences(boolean b) throws IOException {
        mayPassAllFences = b;
    }

    @Override
    public void setMayLockFences(boolean b) throws IOException {
        mayLockFences = b;
    }

    @Override
    public void setMayAttackCitizens(boolean b) throws IOException {
        mayAttackCitizens = b;
    }

    @Override
    public void setMayAttackNonCitizens(boolean b) throws IOException {
        mayAttackNonCitizens = b;
    }

    @Override
    public void setDiplomat(boolean b) throws IOException {
        diplomat = b;
    }

    @Override
    public void setVillageAppliedTo(int i) throws IOException {
        villageAppliedTo = i;
    }

    @Override
    public void setMayFish(boolean b) throws IOException {
        mayFish = b;
    }

    @Override
    public void setMayPushPullTurn(boolean b) throws IOException {
        mayPushPullTurn = b;
    }

    @Override
    public void setMayLead(boolean b) throws IOException {
        mayLead = b;
    }

    @Override
    public void setMayPickup(boolean b) throws IOException {
        mayPickup = b;
    }

    @Override
    public void setMayTame(boolean b) throws IOException {
        mayTame = b;
    }

    @Override
    public void setMayLoad(boolean b) throws IOException {
        mayLoad = b;
    }

    @Override
    public void setMayButcher(boolean b) throws IOException {
        mayButcher = b;
    }

    @Override
    public void setMayAttachLock(boolean b) throws IOException {
        mayAttachLock = b;
    }

    @Override
    public void setMayPickLocks(boolean b) throws IOException {
        mayPickLocks = b;
    }

    @Override
    public void setMayUpdateMap(boolean b) throws IOException {
        mayUpdateMap = b;
    }

    @Override
    public void setCutOld(boolean b) throws IOException {
        mayCutOldTrees = b;
    }

    @Override
    public void delete() throws IOException {
        // Nothing
    }

    @Override
    public void save() throws IOException {
        if (status == 1) {
            mayDestroy = false;
        }
    }

    public int compareTo(DbVillageRole otherDbVillageRole) {
        return this.getName().compareTo(otherDbVillageRole.getName());
    }
}
