package com.wurmonline.server.bodys;

public class DbWound extends Wound {
    public DbWound(final byte aType, final byte aLocation, final float aSeverity, final long aOwner, final float aPoisonSeverity, final float aInfectionSeverity, final boolean pvp, final boolean spell) {
        super(aType, aLocation, aSeverity, aOwner, aPoisonSeverity, aInfectionSeverity, false, pvp, spell);
    }

    public DbWound(final long aId, final byte aType, final byte aLocation, final float aSeverity, final long aOwner, final float aPoisonSeverity, final float aInfectionSeverity, final long aLastPolled, final boolean aBandaged, final byte aHealEff) {
        super(aId, aType, aLocation, aSeverity, aOwner, aPoisonSeverity, aInfectionSeverity, aLastPolled, aBandaged, aHealEff);
    }

    @Override
    void create() {}

    @Override
    void setSeverity(float p0) {
        severity = p0;
    }

    @Override
    public void setPoisonSeverity(float p0) {
        poisonSeverity = p0;
    }

    @Override
    public void setInfectionSeverity(float p0) {
        infectionSeverity = p0;
    }

    @Override
    public void setBandaged(boolean p0) {
        isBandaged = p0;
    }

    @Override
    void setLastPolled(long p0) {
        lastPolled = p0;
    }

    @Override
    public void setHealeff(byte p0) {
        healEff = p0;
    }

    @Override
    void delete() {}
}
