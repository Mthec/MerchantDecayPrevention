package com.wurmonline.server.skills;

import java.io.IOException;

public class DbSkill extends Skill {
    DbSkill(int aNumber, double startValue, Skills aParent) {
        super(aNumber, startValue, aParent);
    }

    DbSkill(long _id, int _number, double _knowledge, double _minimum, long _lastUsed) {
        super(_id, _number, _knowledge, _minimum, _lastUsed);
    }

    DbSkill(long _id, Skills _parent, int _number, double _knowledge, double _minimum, long _lastUsed) {
        super(_id, _parent, _number, _knowledge, _minimum, _lastUsed);
    }

    DbSkill(long aId, Skills aParent) throws IOException {
        super(aId, aParent);
    }

    @Override
    void save() throws IOException {

    }

    @Override
    void load() throws IOException {

    }

    @Override
    void saveValue(boolean b) throws IOException {

    }

    @Override
    public void setJoat(boolean b) throws IOException {

    }

    @Override
    public void setNumber(int i) throws IOException {

    }

    static byte[][] loadSkillChances() throws Exception {
        return null;
    }

    static void saveSkillChances(byte[][] chances) {

    }
}
