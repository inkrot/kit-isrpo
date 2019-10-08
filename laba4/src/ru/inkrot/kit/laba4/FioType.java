package ru.inkrot.kit.laba4;

enum FioType {

    PICTURE("Картинка"),
    TEXT("Надпись");

    String type;

    FioType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static FioType fromString(String type) {
        for (FioType t : FioType.values()) {
            if (t.getType().equalsIgnoreCase(type)) {
                return t;
            }
        }
        throw new IllegalArgumentException("No constant with type " + type + " found");
    }
}
