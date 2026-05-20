package com.github.donnyk22.models.enums;

public enum UserGender {
    M("Male"), F("Female");

    private String gender;

    UserGender(String gender) {
        this.gender = gender;
    }

    public static String getVal(Character gender) {
        if (gender == 'M') {
            return UserGender.M.getVal();
        } else if (gender == 'F') {
            return UserGender.F.getVal();
        } else {
            return "";
        }
    }

    public String getVal() {
        return this.gender;
    }
}
