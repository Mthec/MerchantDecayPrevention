//
// Decompiled by Procyon v0.5.36
//

package com.wurmonline.server;

import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.players.Ban;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.webinterface.WebCommand;
import com.wurmonline.server.webinterface.WebInterface;
import com.wurmonline.shared.exceptions.WurmServerException;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public final class LoginServerWebConnection {
    static class PlayingTime {
        public final int months;
        public final int days;
        public final String detail;

        PlayingTime(int months, int days, String detail) {

            this.months = months;
            this.days = days;
            this.detail = detail;
        }
    }

    private WebInterface wurm;
    private static Logger logger;
    private int serverId;
    private static final char EXCLAMATION_MARK = '!';
    private static final String FAILED_TO_CREATE_TRINKET = ", failed to create trinket! ";
    private static final String YOU_RECEIVED = "You received ";
    private static final String AN_ERROR_OCCURRED_WHEN_CONTACTING_THE_LOGIN_SERVER = "An error occurred when contacting the login server. Please try later.";
    private static final String FAILED_TO_CONTACT_THE_LOGIN_SERVER = "Failed to contact the login server ";
    private static final String FAILED_TO_CONTACT_THE_LOGIN_SERVER_PLEASE_TRY_LATER = "Failed to contact the login server. Please try later.";
    private static final String FAILED_TO_CONTACT_THE_BANK_PLEASE_TRY_LATER = "Failed to contact the bank. Please try later.";
    private static final String GAME_SERVER_IS_CURRENTLY_UNAVAILABLE = "The game server is currently unavailable.";
    private static final char COLON_CHAR = ':';
    private String intraServerPassword;
    static final int[] failedIntZero;
    public final Map<String, Long> charged = new HashMap<>();
    public final Map<Creature, PlayingTime> playingTime = new HashMap<>();

    public LoginServerWebConnection() {
        this.wurm = null;
        this.serverId = Servers.loginServer.id;
        this.intraServerPassword = Servers.localServer.INTRASERVERPASSWORD;
    }

    public LoginServerWebConnection(final int aServerId) {
        this.wurm = null;
        this.serverId = Servers.loginServer.id;
        this.intraServerPassword = Servers.localServer.INTRASERVERPASSWORD;
        this.serverId = aServerId;
    }

    private void connect() throws MalformedURLException, RemoteException, NotBoundException {
        //
    }

    public int getServerId() {
        return this.serverId;
    }

    public byte[] createAndReturnPlayer(final String playerName, final String hashedIngamePassword, final String challengePhrase, final String challengeAnswer, final String emailAddress, final byte kingdom, final byte power, final long appearance, final byte gender, final boolean titleKeeper, final boolean addPremium, final boolean passwordIsHashed) throws Exception {
        throw new UnsupportedOperationException("Not implemented");
    }

    public long chargeMoney(final String playerName, final long moneyToCharge) {
        charged.merge(playerName, moneyToCharge, Long::sum);
        return moneyToCharge;
    }

    public boolean addPlayingTime(final Creature player, final String name, final int months, final int days, final String detail) {
        playingTime.put(player, new PlayingTime(months, days, detail));
        return true;
    }

    public boolean addMoney(final Creature player, final String name, final long money, final String detail) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public long getMoney(final Creature player) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public boolean addMoney(final long wurmid, final String name, final long money, final String detail) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void testAdding(final String playerName) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void setWeather(final float windRotation, final float windpower, final float windDir) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public Map<String, Byte> getReferrers(final Creature player, final long wurmid) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void addReferrer(final Player player, final String receiver) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void acceptReferrer(final Creature player, final String referrerName, final boolean money) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public String getReimburseInfo(final Player player) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public long[] getCurrentServer(final String name, final long wurmid) throws Exception {
        throw new UnsupportedOperationException("Not implemented");
    }

    public Map<Long, byte[]> getPlayerStates(final long[] wurmids) throws WurmServerException {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void manageFeature(final int aServerId, final int featureId, final boolean aOverridden, final boolean aEnabled, final boolean global) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void startShutdown(final String instigator, final int seconds, final String reason) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public String withDraw(final Player player, final String name, final String _email, final int _months, final int _silvers, final boolean titlebok, final boolean mbok, final int _daysLeft) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public boolean transferPlayer(final Player player, final String playerName, final int posx, final int posy, final boolean surfaced, final byte[] data) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public boolean changePassword(final long wurmId, final String newPassword) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public int[] getPremTimeSilvers(final long wurmId) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public boolean setCurrentServer(final String name, final int currentServer) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public String renamePlayer(final String oldName, final String newName, final String newPass, final int power) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public String changePassword(final String changerName, final String name, final String newPass, final int power) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public String ascend(final int newDeityId, final String deityName, final long wurmid, final byte existingDeity, final byte gender, final byte newPower, final float initialBStr, final float initialBSta, final float initialBCon, final float initialML, final float initialMS, final float initialSS, final float initialSD) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public String changeEmail(final String changerName, final String name, final String newEmail, final String password, final int power, final String pwQuestion, final String pwAnswer) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public String addReimb(final String changerName, final String name, final int numMonths, final int _silver, final int _daysLeft, final boolean setbok) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public String sendMail(final byte[] maildata, final byte[] items, final long sender, final long wurmid, final int targetServer) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public String ban(final String name, final String reason, final int days) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public String addBannedIp(final String ip, final String reason, final int days) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public Ban[] getPlayersBanned() throws Exception {
        throw new UnsupportedOperationException("Not implemented");
    }

    public Ban[] getIpsBanned() throws Exception {
        throw new UnsupportedOperationException("Not implemented");
    }

    public String pardonban(final String name) throws RemoteException {
        throw new UnsupportedOperationException("Not implemented");
    }

    public String removeBannedIp(final String ip) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public Map<String, String> doesPlayerExist(final String playerName) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public String sendVehicle(final byte[] passengerdata, final byte[] itemdata, final long pilot, final long vehicleId, final int targetServer, final int tilex, final int tiley, final int layer, final float rotation) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void sendWebCommand(final short type, final WebCommand command) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void setKingdomInfo(final byte kingdomId, final byte templateKingdom, final String _name, final String _password, final String _chatName, final String _suffix, final String mottoOne, final String mottoTwo, final boolean acceptsPortals) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public boolean kingdomExists(final int thisServerId, final byte kingdomId, final boolean exists) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void requestDemigod(final byte existingDeity, final String deityName) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public boolean requestDeityMove(final int deityNum, final int desiredHex, final String guide) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public boolean awardPlayer(final long wurmid, final String name, final int days, final int months) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public boolean isFeatureEnabled(final int featureId) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public boolean setPlayerFlag(final long wurmid, final int flag, final boolean set) {
        throw new UnsupportedOperationException("Not implemented");
    }

    static {
        failedIntZero = new int[] { -1, -1 };
    }
}
