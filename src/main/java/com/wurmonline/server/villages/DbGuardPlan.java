package com.wurmonline.server.villages;

public class DbGuardPlan extends GuardPlan {
    DbGuardPlan(int aVillageId) {
        super(aVillageId);
    }

    DbGuardPlan(int aType, int aVillageId) {
        super(aType, aVillageId);
    }

    @Override
    void create() {

    }

    @Override
    void load() {

    }

    @Override
    public void updateGuardPlan(int type, long upkeep, int guardCount) {
        this.type = type;
        this.moneyLeft = upkeep;
        this.hiredGuardNumber = guardCount;
    }

    @Override
    void delete() {

    }

    @Override
    void addReturnedGuard(long l) {

    }

    @Override
    void removeReturnedGuard(long l) {

    }

    @Override
    void saveDrainMod() {

    }

    @Override
    void deleteReturnedGuards() {

    }

    @Override
    public void addPayment(String s, long l, long l1) {

    }

    @Override
    void drainGuardPlan(long l) {

    }
}

