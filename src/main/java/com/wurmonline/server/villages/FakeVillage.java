package com.wurmonline.server.villages;

import com.wurmonline.server.NoSuchPlayerException;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.creatures.NoSuchCreatureException;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

@Deprecated
public class FakeVillage extends Village {
    private static long nextVillageId = 100;

    private FakeVillage(int aStartX, int aEndX, int aStartY, int aEndY, String aName, Creature aFounder, long aDeedId, boolean aSurfaced, boolean aDemocracy, String aMotto, boolean aPermanent, byte aSpawnKingdom, int initialPerimeter) throws NoSuchCreatureException, NoSuchPlayerException, IOException {
        super(aStartX, aEndX, aStartY, aEndY, aName, aFounder, aDeedId, aSurfaced, aDemocracy, aMotto, aPermanent, aSpawnKingdom, initialPerimeter);
    }

    private static long getNextVillageId() {
        return nextVillageId++;
    }

    public static Village createNewVillage() {
        try {
            Objenesis ob = new ObjenesisStd();
            Village newVillage = ob.newInstance(FakeVillage.class);
            Field roles = Village.class.getDeclaredField("roles");
            roles.setAccessible(true);
            Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(roles, roles.getModifiers() & ~Modifier.FINAL);
            roles.set(newVillage, new HashMap<>());

            newVillage.deedid = getNextVillageId();
            newVillage.name = "Village" + newVillage.deedid;
            newVillage.plan = new FakeGuardPlan((int)newVillage.deedid);
            return newVillage;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    int create() throws IOException {
        return 0;
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
