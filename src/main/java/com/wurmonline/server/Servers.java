package com.wurmonline.server;

import com.wurmonline.server.economy.MonetaryConstants;
import com.wurmonline.server.webinterface.WebCommand;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Servers {
    @SuppressWarnings("WeakerAccess")
    public static ServerEntry localServer;
    public static ServerEntry loginServer;
    private static boolean isTestServer = false;
    private static boolean isPVPServer = false;
    private static boolean isEpicServer = false;
    private static boolean isHomeServer = true;
    private static boolean isChaosServer = false;
    public static List<WebCommand> sentCommands = new ArrayList<>();

    static {
        localServer = mock(ServerEntry.class);
        when(localServer.isFreeDeeds()).thenReturn(false);
        when(localServer.getTraderMaxIrons()).thenReturn(MonetaryConstants.COIN_GOLD);
        when(localServer.isChaosServer()).thenAnswer((Answer<Boolean>)i -> isThisAChaosServer());
        when(localServer.isChallengeOrEpicServer()).thenAnswer((Answer<Boolean>)i -> isThisAEpicServer());
        loginServer = localServer;
        setHostileServer(false);
    }

    public static boolean isThisATestServer() {
        return isTestServer;
    }
    public static boolean isThisAHomeServer() {
        return isHomeServer;
    }
    public static boolean isThisAPvpServer() {
        return isPVPServer;
    }
    public static boolean isThisAEpicServer() {
        return isEpicServer;
    }
    public static boolean isThisLoginServer() { return localServer.id == loginServer.id; }
    public static boolean isThisAChaosServer() { return isChaosServer; }

    public static int getLocalServerId() {
        return 1;
    }

    public static void setTestServer(boolean value) {
        isTestServer = value;
    }

    public static void setHostileServer(boolean value) {
        isPVPServer = value;
        isEpicServer = value;
        isHomeServer = !value;

        localServer.PVPSERVER = isPVPServer;
        localServer.EPIC = isEpicServer;
        localServer.HOMESERVER = isHomeServer;
    }

    public static void sendWebCommandToAllServers(short type, WebCommand command, boolean epicOnly) {
        sentCommands.add(command);
    }

    public static ServerEntry getClosestSpawnServer(byte b) {
        return localServer;
    }

    public static String getLocalServerName() {
        return localServer.name;
    }

    public static ServerEntry getServerWithId(int serverId) {
        if (serverId == loginServer.id) {
            return loginServer;
        } else if (serverId == localServer.id) {
            return localServer;
        } else {
            throw new RuntimeException("No server found with id " + serverId + ".");
        }
    }
}
