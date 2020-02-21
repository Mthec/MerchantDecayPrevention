package com.wurmonline.server.spells;

import com.wurmonline.server.WurmId;
import com.wurmonline.server.items.RuneUtilities;

public class SpellEffect {
    public final long id;
    public float power;
    public int timeleft;
    public final long owner;
    public final byte type;
    private final boolean isplayer;
    private final boolean isItem;
    private final byte effectType;
    private final byte influence;
    private boolean persistant;

    public SpellEffect(long aOwner, byte aType, float aPower, int aTimeleft) {
        this(aOwner, aType, aPower, aTimeleft, (byte)9, (byte)0, true);
    }

    public SpellEffect(long aOwner, byte aType, float aPower, int aTimeleft, byte effType, byte influenceType, boolean persist) {
        this.power = 0.0F;
        this.timeleft = 0;
        this.persistant = true;
        this.owner = aOwner;
        this.type = aType;
        this.power = aPower;
        this.timeleft = aTimeleft;
        this.effectType = effType;
        this.influence = influenceType;
        this.persistant = persist;
        this.id = WurmId.getNextSpellId();
        if (WurmId.getType(aOwner) == 0) {
            this.isplayer = true;
            this.isItem = false;
        } else if (WurmId.getType(aOwner) != 2 && WurmId.getType(aOwner) != 19 && WurmId.getType(aOwner) != 20) {
            this.isplayer = false;
            this.isItem = false;
        } else {
            this.isplayer = false;
            this.isItem = true;
        }
    }
}
