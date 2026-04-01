package com.github.donnyk22.models.enums;

public enum Action {
    POST("create"), GET("get"), PUT("change"), PATCH("change"), DELETE("delete");

    private String value;

    Action(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
