package com.wurmonline.server.creatures;

import mod.wurmunlimited.WurmObjectsFactory;

import java.util.Collection;

public class Creatures {

    private static Creatures instance;

    public static Creatures getInstance() {
        if (instance == null)
            instance = new Creatures();

        return instance;
    }

    public boolean addCreature(Creature creature, boolean offline, boolean sendToWorld) {

        WurmObjectsFactory.getCurrent().addCreature(creature);
        return true;
    }

    public Creature getCreature(long wurmId) throws NoSuchCreatureException {
        Creature creature = WurmObjectsFactory.getCurrent().getCreature(wurmId);
        if (creature == null)
            throw new NoSuchCreatureException("");
        return creature;
    }

    public Creature getCreatureOrNull(long wurmId) {
        try {
            return WurmObjectsFactory.getCurrent().getCreature(wurmId);
        } catch (NoSuchCreatureException e) {
            return null;
        }
    }

    public void sendToWorld(Creature creature) {

    }

    public Brand getBrand(long creatureId) {
        return null;
    }

    public void permanentlyDelete(Creature creature) {
        WurmObjectsFactory.getCurrent().removeCreature(creature);
    }

    public Collection<Creature> getAllCreatures() {
        return WurmObjectsFactory.getCurrent().getAllCreatures();
    }
}
