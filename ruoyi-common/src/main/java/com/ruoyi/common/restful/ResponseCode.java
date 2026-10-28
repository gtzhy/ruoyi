package com.ruoyi.common.restful;

import lombok.Getter;
import lombok.Setter;

public enum ResponseCode {
    SUCCESS(200, "操作成功"),
    SYSTEM_ERROR(300, "系统错误"),
    ACCOUNT_UNKNOWN(400, "账户不存在"),
    PARAM_ERROR(500, "参数错误"),
    PARAM_REPEAT(600, "参数已存在"),
    NOT_TIME(700, "不在活动时间"),
    OTHER(-100, "其他错误");

    @Getter
    @Setter
    private int code;

    @Getter
    @Setter
    private String message;

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}

