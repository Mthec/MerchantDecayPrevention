package com.wurmonline.server;

import com.wurmonline.server.players.Player;

public class ServerPackageFactory {

    public static void addPlayer(Player player) {
        Players.getInstanceForUnitTestingWithoutDatabase().addPlayer(player);
    }
}
