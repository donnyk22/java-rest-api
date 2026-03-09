package com.github.donnyk22.models.enums;

public enum Method {
    POST("create"), GET("get"), PUT("change"), PATCH("change"), DELETE("delete");

    private String method;

    Method(String method) {
        this.method = method;
    }

    public String getAction() {
        return this.method;
    }
}
