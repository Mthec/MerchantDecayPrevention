package com.wurmonline.server.villages;

@Deprecated
public class FakeGuardPlan extends GuardPlan {
    FakeGuardPlan(int aVillageId) {
        super(aVillageId);
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
