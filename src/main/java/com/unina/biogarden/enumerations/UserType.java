package com.unina.biogarden.enumerations;

public enum UserType {
    OWNER("proprietario"),
    FARMER("coltivatore");

    private final String type;

    UserType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static UserType fromString(String type) {
        for (UserType userType : UserType.values()) {
            if (userType.getType().equalsIgnoreCase(type)) {
                return userType;
            }
        }
        throw new IllegalArgumentException("Unknown user type: " + type);
    }
}
