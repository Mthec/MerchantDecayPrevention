package com.wurmonline.server.skills;

import com.wurmonline.server.Servers;
import com.wurmonline.server.WurmId;
import com.wurmonline.server.players.PlayerInfo;
import com.wurmonline.server.players.PlayerInfoFactory;

import java.util.TreeMap;

public class DbSkills extends Skills {
    DbSkills(long aId) {
        this.id = aId;
        if (aId != -10L && WurmId.getType(aId) == 0) {
            PlayerInfo p = PlayerInfoFactory.getPlayerInfoWithWurmId(aId);
            if (p != null) {
                if (!p.isPaying()) {
                    this.paying = false;
                }

                if (!p.hasSkillGain) {
                    this.hasSkillGain = false;
                }

                if (Servers.localServer.isChallengeOrEpicServer() && p.realdeath == 0) {
                    this.priest = p.isPriest;
                }
            }
        }

    }

    DbSkills(String aTemplateName) {
        this.templateName = aTemplateName;
    }

    @Override
    public void load() throws Exception {
        this.skills = new TreeMap<>();
    }

    @Override
    public void delete() throws Exception {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
