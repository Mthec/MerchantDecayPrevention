package com.wurmonline.server.spells;

import com.wurmonline.server.WurmId;

@SuppressWarnings("FieldCanBeLocal")
public class SpellEffect {
    public final long id;
    public float power;
    @SuppressWarnings("SpellCheckingInspection")
    public int timeleft;
    public final long owner;
    public final byte type;
    private final boolean isPlayer;
    private final boolean isItem;
    private final byte effectType;
    private final byte influence;
    private boolean persistent;

    public SpellEffect(long aOwner, byte aType, float aPower, int aTimeLeft) {
        this(aOwner, aType, aPower, aTimeLeft, (byte)9, (byte)0, true);
    }

    public SpellEffect(long aOwner, byte aType, float aPower, int aTimeLeft, byte effType, byte influenceType, boolean persist) {
        this.power = 0.0F;
        this.timeleft = 0;
        this.persistent = true;
        this.owner = aOwner;
        this.type = aType;
        this.power = aPower;
        this.timeleft = aTimeLeft;
        this.effectType = effType;
        this.influence = influenceType;
        this.persistent = persist;
        this.id = WurmId.getNextSpellId();
        if (WurmId.getType(aOwner) == 0) {
            this.isPlayer = true;
            this.isItem = false;
        } else if (WurmId.getType(aOwner) != 2 && WurmId.getType(aOwner) != 19 && WurmId.getType(aOwner) != 20) {
            this.isPlayer = false;
            this.isItem = false;
        } else {
            this.isPlayer = false;
            this.isItem = true;
        }
    }
}
