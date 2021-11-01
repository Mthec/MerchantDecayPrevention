package com.wurmonline.server.deities;

import static org.mockito.Mockito.mock;

public class Deities {
    public static Deity[] getDeities() {
        return new Deity[0];
    }

    public static Deity getDeity(int number) {
        return mock(Deity.class);
    }
}
