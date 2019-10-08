package ru.inkrot.kit.laba4;

import java.util.HashSet;
import java.util.Set;

public class FioObject {

    private static int MAX_NUMBER_OF_OBJECTS = 10;
    private static Set<FioObject> allObjects = new HashSet<>();

    private FioType type;

    // 0 < id < MAX_NUMBER_OF_OBJECTS
    private int id;

    // 1 - 5
    private int speed;

    public FioObject(FioType type) {
        this.type = type;
    }


}

