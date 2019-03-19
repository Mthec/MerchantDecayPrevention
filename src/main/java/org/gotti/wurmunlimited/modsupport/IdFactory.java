package org.gotti.wurmunlimited.modsupport;

import org.gotti.wurmunlimited.modsupport.IdType;

public class IdFactory {

    private static int counter = 100000;

    public static int getIdFor(String identifier, IdType starter) {
        return counter++;
    }
}
