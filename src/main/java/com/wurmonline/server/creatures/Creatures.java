package com.wurmonline.server.creatures;

import com.wurmonline.server.behaviours.Vehicles;
import com.wurmonline.server.villages.Village;
import mod.wurmunlimited.WurmObjectsFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Creatures {

    private static Creatures instance;
    private final Map<Long, Brand> brandedCreatures = new HashMap<>();

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

    public void permanentlyDelete(Creature creature) {
        Village village = creature.getCitizenVillage();
        if (village != null) {
            village.removeCitizen(creature);
        }

        WurmObjectsFactory.getCurrent().removeCreature(creature);
    }

    public Collection<Creature> getAllCreatures() {
        return WurmObjectsFactory.getCurrent().getAllCreatures();
    }

    public int getNumberOfCreatures() {
        return WurmObjectsFactory.getCurrent().getAllCreatures().size();
    }

    public void setCreatureDead(Creature dead) {
        final long deadId = dead.getWurmId();
        for (final Creature creature : WurmObjectsFactory.getCurrent().getAllCreatures()) {
            if (creature.opponent == dead) {
                creature.setOpponent(null);
            }
            if (creature.target == deadId) {
                creature.setTarget(-10L, true);
            }
            creature.removeTarget(deadId);
        }
        Vehicles.removeDragger(dead);
    }

    public Brand getBrand(long creatureId) {
        return brandedCreatures.get(creatureId);
    }

    public final void addBrand(Brand brand) {
        this.brandedCreatures.put(brand.getCreatureId(), brand);
    }

    public final void setBrand(long creatureId, long brandId) {
        if (brandId <= 0L) {
            this.brandedCreatures.remove(creatureId);
        } else {
            Brand brand = this.brandedCreatures.get(creatureId);
            if (brand == null) {
                brand = new Brand(creatureId, System.currentTimeMillis(), brandId, false);
            } else {
                brand.setBrandId(brandId);
            }

            this.brandedCreatures.put(creatureId, brand);
        }

    }

    public void removeBrandingFor(int villageId) {
        for (final Brand b : brandedCreatures.values()) {
            if (b.getBrandId() == villageId) {
                b.deleteBrand();
            }
        }
    }
}
