package com.wurmonline.server.skills;

import java.io.IOException;
import java.sql.*;

public class DbSkill extends Skill {
    DbSkill(int aNumber, double startValue, Skills aParent) {
        super(aNumber, startValue, aParent);
    }

    DbSkill(long _id, int _number, double _knowledge, double _minimum, long _lastused) {
        super(_id, _number, _knowledge, _minimum, _lastused);
    }

    DbSkill(long _id, Skills _parent, int _number, double _knowledge, double _minimum, long _lastused) {
        super(_id, _parent, _number, _knowledge, _minimum, _lastused);
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
        Connection dbcon = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        byte[][] toReturn = (byte[][])null;

        try {
            dbcon = DriverManager.getConnection("jdbc:sqlite:wurmtemplates.db");
            ps = dbcon.prepareStatement("select * from SKILLCHANCES");

            byte sk;
            byte diff;
            byte chance;
            for(rs = ps.executeQuery(); rs.next(); toReturn[sk][diff] = chance) {
                if (toReturn == null) {
                    toReturn = new byte[101][101];
                }

                sk = rs.getByte("SKILL");
                diff = rs.getByte("DIFFICULTY");
                chance = rs.getByte("CHANCE");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
            if (dbcon != null)
                dbcon.close();
        }

        return toReturn;
    }
}
