package com.wurmonline.server.villages;

import com.wurmonline.server.creatures.Creature;

import java.io.IOException;

public class DbCitizen extends Citizen {
    DbCitizen(long aWurmId, String aName, VillageRole aRole, long aVotedate, long aVotedfor) {
        super(aWurmId, aName, aRole, aVotedate, aVotedfor);
    }

    @Override
    public void setRole(VillageRole villageRole) throws IOException {

    }

    @Override
    void setVoteDate(long l) throws IOException {

    }

    @Override
    void setVotedFor(long l) throws IOException {

    }

    @Override
    void create(Creature creature, int i) throws IOException {

    }
}
