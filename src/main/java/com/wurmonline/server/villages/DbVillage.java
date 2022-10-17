package com.wurmonline.server.villages;

import com.wurmonline.server.NoSuchPlayerException;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.creatures.NoSuchCreatureException;

import java.io.IOException;

public class DbVillage extends Village {
    DbVillage(int aStartX, int aEndX, int aStartY, int aEndY, String aName, Creature aFounder, long aDeedId, boolean aSurfaced, boolean aDemocracy, String aMotto, boolean aPermanent, byte aSpawnKingdom, int initialPerimeter) throws NoSuchCreatureException, NoSuchPlayerException, IOException {
        super(aStartX, aEndX, aStartY, aEndY, aName, aFounder, aDeedId, aSurfaced, aDemocracy, aMotto, aPermanent, aSpawnKingdom, initialPerimeter);
    }

    DbVillage(int aId, int aStartX, int aEndX, int aStartY, int aEndY, String aName, String aFounderName, String aMayor, long aDeedId, boolean aSurfaced, boolean aDemocracy, String aDevise, long _creationDate, boolean aHomestead, long aTokenid, long aDisbandTime, long aDisbId, long aLast, byte aKingdom, long aUpkeep, byte aSettings, boolean aAcceptsHomes, boolean aAcceptsMerchants, int aMaxCitizens, boolean aPermanent, byte aSpawnkingdom, int perimetert, boolean allowsAggro, String _consumerKeyToUse, String _consumerSecretToUse, String _applicationToken, String _applicationSecret, boolean _twitChat, boolean _twitEnabled, float _faithWar, float _faithHeal, float _faithCreate, byte _spawnSituation, int _allianceNumber, short _hotaWins, long lastChangeName, String _motd) {
        super(aId, aStartX, aEndX, aStartY, aEndY, aName, aFounderName, aMayor, aDeedId, aSurfaced, aDemocracy, aDevise, _creationDate, aHomestead, aTokenid, aDisbandTime, aDisbId, aLast, aKingdom, aUpkeep, aSettings, aAcceptsHomes, aAcceptsMerchants, aMaxCitizens, aPermanent, aSpawnkingdom, perimetert, allowsAggro, _consumerKeyToUse, _consumerSecretToUse, _applicationToken, _applicationSecret, _twitChat, _twitEnabled, _faithWar, _faithHeal, _faithCreate, _spawnSituation, _allianceNumber, _hotaWins, lastChangeName, _motd);
    }

    @Override
    int create() throws IOException {
        return Integer.parseInt(name.replace("Village", ""));
    }

    @Override
    void delete() throws IOException {

    }

    @Override
    void save() {

    }

    @Override
    void loadCitizens() {

    }

    @Override
    void loadVillageMapAnnotations() {

    }

    @Override
    void loadVillageRecruitees() {

    }

    @Override
    void deleteVillageMapAnnotations() {

    }

    @Override
    public void setMayor(String s) throws IOException {

    }

    @Override
    public void setDisbandTime(long l) throws IOException {

    }

    @Override
    public void setLogin() {

    }

    @Override
    public void setDisbander(long l) throws IOException {

    }

    @Override
    public void setName(String s) throws IOException {

    }

    @Override
    void setStartX(int i) throws IOException {

    }

    @Override
    void setEndX(int i) throws IOException {

    }

    @Override
    void setStartY(int i) throws IOException {

    }

    @Override
    void setEndY(int i) throws IOException {

    }

    @Override
    public void setDemocracy(boolean b) throws IOException {

    }

    @Override
    void setDeedId(long l) throws IOException {

    }

    @Override
    public void setTokenId(long l) throws IOException {
        tokenId = l;
    }

    @Override
    void loadRoles() {

    }

    @Override
    void loadGuards() {

    }

    @Override
    void loadReputations() {

    }

    @Override
    public void setMotto(String s) throws IOException {

    }

    @Override
    void setUpkeep(long l) throws IOException {

    }

    @Override
    public void setUnlimitedCitizens(boolean b) throws IOException {

    }

    @Override
    public void setMotd(String s) throws IOException {

    }

    @Override
    public void saveSettings() throws IOException {

    }

    @Override
    void loadHistory() {

    }

    @Override
    public void addHistory(String s, String s1) {

    }

    @Override
    void saveRecruitee(VillageRecruitee villageRecruitee) {

    }

    @Override
    void setMaxcitizens(int i) {

    }

    @Override
    public void setAcceptsMerchants(boolean b) throws IOException {

    }

    @Override
    public void setAllowsAggroCreatures(boolean b) throws IOException {

    }

    @Override
    public void setPerimeter(int i) throws IOException {

    }

    @Override
    public void setKingdom(byte b) throws IOException {

    }

    @Override
    public void setKingdom(byte b, boolean b1) throws IOException {

    }

    @Override
    public void setTwitCredentials(String s, String s1, String s2, String s3, boolean b, boolean b1) {

    }

    @Override
    public void setFaithCreate(float v) {

    }

    @Override
    public void setFaithWar(float v) {

    }

    @Override
    public void setFaithHeal(float v) {

    }

    @Override
    public void setSpawnSituation(byte b) {

    }

    @Override
    public void setAllianceNumber(int i) {
        allianceNumber = i;
    }

    @Override
    public void setHotaWins(short i) {

    }

    @Override
    public void setLastChangedName(long l) {

    }

    @Override
    public void setVillageRep(int i) {

    }

    @Override
    void deleteRecruitee(VillageRecruitee villageRecruitee) {

    }
}
