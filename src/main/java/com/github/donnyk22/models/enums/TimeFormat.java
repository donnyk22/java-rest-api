package com.github.donnyk22.models.enums;

public enum TimeFormat {
    DD_MM_YYYY_HH_MM_SS("dd-MM-yyyy HH:mm:ss"),
    DD_MM_YYYY_HH_MM("dd-MM-yyyy HH:mm"),
    DD_MM_YYYY("dd-MM-yyyy");

    private String format;

    TimeFormat(String format) {
        this.format = format;
    }

    public String getVal() {
        return this.format;
    }
}
