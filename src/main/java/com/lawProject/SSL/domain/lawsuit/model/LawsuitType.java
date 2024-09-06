package com.lawProject.SSL.domain.lawsuit.model;

public enum LawsuitType {
    CIVIL("민사"),
    CRIMINAL("형사");

    private final String type;

    LawsuitType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return type;
    }
}
