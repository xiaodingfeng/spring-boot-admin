package com.ciicgat.circlefk.enums;

public enum UserStatusEnum {
    ACTIVE(1, "正常"),
    FORBIDDEN(2, "禁用");

    private final Integer typeValue;
    private final String typeName;

    UserStatusEnum(Integer typeValue, String typeName) {
        this.typeValue = typeValue;
        this.typeName = typeName;
    }

    public Integer getTypeValue() {
        return typeValue;
    }
}
