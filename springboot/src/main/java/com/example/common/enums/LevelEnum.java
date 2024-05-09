package com.example.common.enums;

//  用户身份枚举实体类
public enum LevelEnum {
    // 普通户及贫困户为这两个对象的属性
    ORDINARY("普通户"),
    POOR("贫困户"),
    ;

    public String level;

    LevelEnum(String level) {
        this.level = level;
    }
}
