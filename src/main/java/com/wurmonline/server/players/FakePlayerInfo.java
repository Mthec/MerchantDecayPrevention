package com.wurmonline.server.players;

import com.wurmonline.server.*;
import com.wurmonline.server.deities.Deity;
import com.wurmonline.server.intra.MoneyTransfer;
import com.wurmonline.server.skills.Skills;
import com.wurmonline.server.steam.SteamId;
import com.wurmonline.server.villages.Village;
import com.wurmonline.server.villages.Villages;

import javax.annotation.Nullable;
import java.io.IOException;

public class FakePlayerInfo extends PlayerInfo {
    public FakePlayerInfo(String name) {
        super(name);
    }

    @Override
    public void setPower(byte var1) throws IOException {
        power = var1;
    }

    @Override
    public void setPaymentExpire(long var1) throws IOException {
        setPaymentExpire(var1, true);
    }

    @Override
    public void setPaymentExpire(long paymentExpire, boolean silverReturn) throws IOException {
        final int numsPaying = PlayerInfoFactory.getNumberOfPayingPlayers();
        if (this.getPaymentExpire() <= 0L && paymentExpire > System.currentTimeMillis()) {
            if (this.awards == null) {
                this.awards = new Awards(this.wurmId, 0, 0, 0, 0, 0, System.currentTimeMillis(), 0, 0, true);
            }
            if (Servers.localServer.LOGINSERVER) {
                if (silverReturn) {
                    this.setMoney(this.money + 20000L);
                    new MoneyTransfer(this.name, this.wurmId, this.money, 20000L, this.name + "Premium", (byte)3, "", false);
                }
            }
        } else if (Features.Feature.RETURNER_PACK_REGISTRATION.isEnabled()) {
            if (System.currentTimeMillis() > this.getPaymentExpire() && !this.isFlagSet(47) && !this.isFlagSet(46) && Servers.localServer.id == this.currentServer && this.getPaymentExpire() > 0L && (playingTime / TimeConstants.DAY_MILLIS) > 14L) {
                this.setFlag(47, true);
            }
        }
        if (this.isFlagSet(8) && paymentExpire > this.paymentExpireDate) {
            this.setFlag(8, false);
        }
        this.paymentExpireDate = paymentExpire;
        this.setFlag(63, false);
        try {
            final Skills skills = Players.getInstance().getPlayer(this.wurmId).getSkills();
            if (skills != null && !Servers.isThisATestServer()) {
                skills.paying = true;
            }
        } catch (NoSuchPlayerException ignored) {}
    }

    @Override
    public void setBanned(boolean ban, String reason, long expiry) throws IOException {
        this.banned = ban;
        this.banexpiry = expiry;
        this.banreason = reason;
        if (this.banned) {
            final Village v = Villages.getVillageForCreature(this.wurmId);
            if (v != null && v.getMayor().wurmId == this.wurmId) {
                v.setDemocracy(true);
            }
            this.setRank(1000);
        }
    }

    @Override
    public void resetWarnings() throws IOException {
        warnings = 0;
        lastWarned = System.currentTimeMillis();
    }

    @Override
    void setReputation(int var1) {
        reputation = var1;
    }

    @Override
    public void setMuted(boolean var1, String var2, long var3) {
        muted = var1;
        mutereason = var2;
        muteexpiry = var3;

        if (muted) {
            setMuteTimes((short)(muteTimes + 1));
        }
    }

    @Override
    void setFatigueSecs(int var1, long var2) {}

    @Override
    void setCheated(String reason) {
        if (this.lastCheated == 0L || "CLASS_CHECK_DISCONNECT".equals(reason)) {
            this.lastCheated = System.currentTimeMillis();
        }
    }

    @Override
    public void updatePassword(String var1) throws IOException {
        password = var1;
    }

    @Override
    public void setRealDeath(byte var1) throws IOException {
        realdeath = var1;
    }

    @Override
    public void setFavor(float var1) throws IOException {
        favor = var1;
    }

    @Override
    public void setFaith(float var1) throws IOException {
        faith = var1;
    }

    @Override
    void setDeity(Deity var1) throws IOException {
        deity = var1;
    }

    @Override
    void setAlignment(float var1) throws IOException {
        alignment = var1;
    }

    @Override
    void setGod(Deity var1) throws IOException {
        god = var1;
    }

    @Override
    public void load() throws IOException {
        // Ignored.
    }

    @Override
    public void warn() throws IOException {
        lastWarned = System.currentTimeMillis();
        ++warnings;
    }

    @Override
    public void save() throws IOException {
        lastServer = Servers.localServer.id;
        currentServer = Servers.localServer.id;
    }

    @Override
    public void setLastTrigger(int var1) {
        lastTriggerEffect = var1;
    }

    @Override
    void setIpaddress(String var1) throws IOException {
        ipaddress = var1;
    }

    @Override
    void setSteamId(SteamId var1) throws IOException {
        steamId = var1;
    }

    @Override
    public void setRank(int var1) throws IOException {
        rank = var1;
    }

    @Override
    public void setReimbursed(boolean var1) throws IOException {
        reimbursed = var1;
    }

    @Override
    void setPlantedSign() throws IOException {
        plantedSign = System.currentTimeMillis();
    }

    @Override
    void setChangedDeity() throws IOException {
        lastChangedDeity = System.currentTimeMillis();
    }

    @Override
    public String getIpaddress() {
        return null;
    }

    @Override
    void setDead(boolean var1) {
        dead = var1;
    }

    @Override
    public void setSessionKey(String var1, long var2) throws IOException {
        sessionKey = var1;
        sessionExpiration = var2;
    }

    @Override
    void setName(String var1) throws IOException {
        name = var1;
    }

    @Override
    public void setVersion(long var1) throws IOException {
        version = var1;
    }

    @Override
    void saveFriend(long var1, long var3, byte var5, String var6) throws IOException {}

    @Override
    void updateFriend(long var1, long var3, byte var5, String var6) throws IOException {}

    @Override
    void deleteFriend(long var1, long var3) throws IOException {}

    @Override
    void saveEnemy(long var1, long var3) throws IOException {}

    @Override
    void deleteEnemy(long var1, long var3) throws IOException {}

    @Override
    void saveIgnored(long var1, long var3) throws IOException {}

    @Override
    void deleteIgnored(long var1, long var3) throws IOException {}

    @Override
    public void setNumFaith(byte var1, long var2) throws IOException {
        numFaith = var1;
    }

    @Override
    long getFlagLong() {
        return flags;
    }

    @Override
    long getFlag2Long() {
        return flags2;
    }

    @Override
    public void setMoney(long var1) throws IOException {
        money = var1;
    }

    @Override
    void setSex(byte var1) throws IOException {
        sex = var1;
    }

    @Override
    void setClimbing(boolean var1) throws IOException {
        climbing = var1;
    }

    @Override
    void setChangedKingdom(byte var1, boolean var2) throws IOException {
        if (changedKingdom != var1) {
            changedKingdom = var1;

            if (var2) {
                setChangedKingdom();
            }
        }
    }

    @Override
    public void setFace(long var1) throws IOException {
        face = var1;
    }

    @Override
    boolean addTitle(Titles.Title var1) {
        titles.add(var1);
        return true;
    }

    @Override
    boolean removeTitle(Titles.Title var1) {
        return titles.remove(var1);
    }

    @Override
    void setAlcohol(float var1) {
        alcohol = var1;
    }

    @Override
    void setPet(long var1) {
        pet = var1;
    }

    @Override
    public void setNicotineTime(long var1) {
        nicotineAddiction = var1;
    }

    @Override
    public boolean setAlcoholTime(long newAlcohol) {
        boolean titleAdded = false;
        final long newAlcoholLevel = Math.max(0L, Math.min(10000L, newAlcohol));
        if (newAlcoholLevel != this.alcoholAddiction) {
            this.alcoholAddiction = newAlcoholLevel;
            if (this.alcoholAddiction >= 10000L) {
                titleAdded = this.addTitle(Titles.Title.Alcoholic);
            }
        }
        return titleAdded;
    }

    @Override
    void setNicotine(float var1) {
        nicotine = var1;
    }

    @Override
    public void setMayMute(boolean var1) {
        mayMute = true;
    }

    @Override
    public void setEmailAddress(String var1) {
        emailAddress = var1;
    }

    @Override
    void setPriest(boolean var1) {
        if (isPriest != var1) {
            isPriest = var1;

            if (Servers.localServer.isChallengeOrEpicServer() && realdeath == 0) {
                try {
                    final Skills skills = Players.getInstance().getPlayer(this.wurmId).getSkills();
                    if (skills != null) {
                        skills.priest = this.isPriest;
                    }
                } catch (NoSuchPlayerException ignored) {}
            }
        }
    }

    @Override
    public void setOverRideShop(boolean var1) {
        overRideShop = var1;
    }

    @Override
    public void setReferedby(long var1) {
        referrer = var1;
    }

    @Override
    public void setBed(long var1) {
        bed = var1;
    }

    @Override
    void setLastChangedVillage(long var1) {
        lastChangedVillage = var1;
    }

    @Override
    void setSleep(int var1) {
        sleep = var1;
    }

    @Override
    void setTheftwarned(boolean var1) {
        isTheftWarned = var1;
    }

    @Override
    public void setHasNoReimbursementLeft(boolean var1) {
        noReimbursementLeft = var1;
    }

    @Override
    void setDeathProtected(boolean var1) {
        deathProtected = var1;
    }

    @Override
    public void setCurrentServer(int var1) {
        currentServer = var1;
    }

    @Override
    public void setDevTalk(boolean var1) {
        mayHearDevTalk = var1;
    }

    @Override
    public void transferDeity(@Nullable Deity d) throws IOException {
        if (this.deity != d && this.deity != null) {
            //noinspection ConstantConditions
            if (d.isHateGod() != this.deity.isHateGod()) {
                this.alignment *= -1.0f;
            }
            this.deity = d;
        }
    }

    @Override
    void saveSwitchFatigue() {
        if (fatigueSecsYesterday > 0 || fatigueSecsToday > 0) {
            fatigueSecsYesterday = fatigueSecsToday;
            fatigueSecsToday = 0;
        }
    }

    @Override
    void saveFightMode(byte var1) {
        fightmode = var1;
    }

    @Override
    void setNextAffinity(long var1) {
        nextAffinity = var1;
    }

    @Override
    public void saveAppointments() {}

    @Override
    void setTutorialLevel(int var1) {
        tutorialLevel = var1;
    }

    @Override
    void setAutofight(boolean var1) {
        autoFighting = var1;
    }

    @Override
    void setLastVehicle(long var1) {
        lastvehicle = var1;
    }

    @Override
    public void setIsPlayerAssistant(boolean var1) {
        playerAssistant = var1;
    }

    @Override
    public void setMayAppointPlayerAssistant(boolean var1) {
        mayAppointPlayerAssistant = var1;
    }

    @Override
    public boolean togglePlayerAssistantWindow(boolean var1) {
        if (seesPlayerAssistantWindow != var1) {
            seesPlayerAssistantWindow = var1;
        }

        return seesPlayerAssistantWindow;
    }

    @Override
    public void setLastTaggedTerr(byte newKingdom) {
        if (this.lastTaggedKindom != newKingdom) {
            this.lastTaggedKindom = newKingdom;
            this.lastMovedBetweenKingdom = System.currentTimeMillis();
        }
    }

    @Override
    public void setNewPriestType(byte var1, long var2) {
        if (priestType != var1) {
            priestType = var1;
            lastChangedPriestType = var2;
        }
    }

    @Override
    public void setChangedJoat() {
        lastChangedJoat = System.currentTimeMillis();
    }

    @Override
    public void setMovedInventory(boolean var1) {
        hasMovedInventory = var1;
    }

    @Override
    public void setFreeTransfer(boolean var1) {
        hasFreeTransfer = var1;
    }

    @Override
    public boolean setHasSkillGain(boolean var1) {
        if (hasSkillGain != var1) {
            hasSkillGain = var1;
        }

        return hasSkillGain;
    }

    @Override
    public void loadIgnored(long var1) {}

    @Override
    public void loadTitles(long var1) {}

    @Override
    public void loadFriends(long var1) {}

    @Override
    public void loadHistorySteamIds(long var1) {}

    @Override
    public void loadHistoryIPs(long var1) {}

    @Override
    public void loadHistoryEmails(long var1) {}

    @Override
    public boolean setChampionPoints(short var1) {
        var1 = (short)Math.max(0, var1);
        if (championPoints != var1 || (realdeath > 0 && var1 == 0)) {
            championPoints = var1;
        }

        return championPoints <= 0;
    }

    @Override
    public void setChangedKingdom() {
        lastChangedKindom = System.currentTimeMillis();
    }

    @Override
    public void setChampionTimeStamp() {
        championTimeStamp = System.currentTimeMillis();
    }

    @Override
    public void setChampChanneling(float var1) {
        champChanneling = var1;
    }

    @Override
    public void setMuteTimes(short var1) {
        muteTimes = var1;
    }

    @Override
    public void setVotedKing(boolean var1) {
        votedKing = var1;
    }

    @Override
    public void setEpicLocation(byte var1, int var2) {
        epicKingdom = var1;
        epicServerId = var2;
        lastUsedEpicPortal = System.currentTimeMillis();
    }

    @Override
    public void setChaosKingdom(byte var1) {
        chaosKingdom = var1;
    }

    @Override
    public void setHotaWins(short var1) {
        hotaWins = var1;
    }

    @Override
    public void setSpamMode(boolean var1) {
        spamMode = var1;
    }

    @Override
    public void setKarma(int var1) {
        karma = var1;
    }

    @Override
    public void setScenarioKarma(int var1) {
        scenarioKarma = var1;
    }

    @Override
    public void setBlood(byte var1) {
        blood = var1;
    }

    @Override
    public void setFlag(int number, boolean value) {
        if (number < 64) {
            this.flagBits.set(number, value);
            this.flags = this.getFlagLong();
        }
        else {
            this.flag2Bits.set(number - 64, value);
            this.flags2 = this.getFlag2Long();
        }
    }

    @Override
    public void setFlagBits(long bits) {
        for (int x = 0; x < 64; ++x) {
            if (x == 0) {
                flagBits.set(x, (bits & 0x1L) == 0x1L);
            } else
                flagBits.set(x, (bits >> x & 0x1L) == 0x1L);
        }
    }

    @Override
    public void setFlag2Bits(long bits) {
        for (int x = 0; x < 64; ++x) {
            if (x == 0) {
                flag2Bits.set(x, (bits & 0x1L) == 0x1L);
            } else
                flag2Bits.set(x, (bits >> x & 0x1L) == 0x1L);
        }
    }

    @Override
    public void forceFlagsUpdate() {}

    @Override
    public void setAbility(int number, boolean value) {
        abilityBits.set(number, value);
        long ret = 0L;
        for (int x = 0; x < 64; ++x) {
            if (abilityBits.get(x)) {
                ret += 1L << x;
            }
        }
        abilities = ret;
    }

    @Override
    public void setCurrentAbilityTitle(int var1) {
        abilityTitle = var1;
    }

    @Override
    public void setUndeadData() {}

    @Override
    public void setModelName(String var1) {
        modelName = var1;
    }

    @Override
    public void addMoneyEarnedBySellingEver(long var1) {
        moneyEarnedBySellingEver = var1;
    }

    @Override
    public void setPointsForChamp() {
        final WurmRecord record = PlayerInfoFactory.getChampionRecord(name);
        if (record == null) {
            PlayerInfoFactory.addChampRecord(new WurmRecord(championPoints, name, true));
        }
    }

    @Override
    public void switchChamp() {}

    @Override
    public void setPassRetrieval(String var1, String var2) throws IOException {
        pwQuestion = var1;
        pwAnswer = var2;
    }
}
