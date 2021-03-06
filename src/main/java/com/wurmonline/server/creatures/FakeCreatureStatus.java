package com.wurmonline.server.creatures;

import com.wurmonline.server.bodys.BodyFactory;
import com.wurmonline.server.kingdom.Kingdom;

import java.io.IOException;

public class FakeCreatureStatus extends CreatureStatus {
    public static String unset = "UNSET";
    public String savedName = unset;

    public FakeCreatureStatus(Creature creature) {
        setPosition(new CreaturePos(creature.getWurmId(), 512, 512, 1, 1, 1, 1, -10, false));
        this.template = creature.getTemplate();
        statusHolder = creature;
        try {
            body = BodyFactory.getBody(creature, this.template.getBodyType(), this.template.getCentimetersHigh(), this.template.getCentimetersLong(), this.template.getCentimetersWide());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        stamina = 65535;
        kingdom = Kingdom.KINGDOM_FREEDOM;
    }

    @Override
    public void setVehicle(long l, byte b) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    void setLoyalty(float v) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void load() throws Exception {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void savePosition(long l, boolean b, int i, boolean b1) throws IOException {

    }

    @Override
    public boolean save() throws IOException {
        return false;
    }

    @Override
    public void setKingdom(byte b) throws IOException {
        kingdom = b;
    }

    @Override
    public void setDead(boolean b) throws IOException {
        dead = b;
    }

    @Override
    void updateAge(int i) throws IOException {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    void setDominator(long l) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void setReborn(boolean b) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void setLastPolledLoyalty() {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    void setOffline(boolean b) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    boolean setStayOnline(boolean b) {
        return false;
    }

    @Override
    void setDetectionSecs() {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    void setType(byte b) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    void updateFat() throws IOException {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    void setInheritance(long l, long l1, long l2) throws IOException {

    }

    @Override
    public void setInventoryId(long l) throws IOException {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    void saveCreatureName(String s) throws IOException {
        savedName = s;
    }

    @Override
    void setLastGroomed(long l) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    void setDisease(byte b) {
        disease = b;
    }
}
