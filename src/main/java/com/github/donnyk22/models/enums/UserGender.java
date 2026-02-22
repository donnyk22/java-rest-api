package com.github.donnyk22.models.enums;

public enum UserGender {
    M("Male"), F("Female");

    private String gender;

    UserGender(String gender) {
        this.gender = gender;
    }

    public String getVal() {
        return this.gender;
    }
}
