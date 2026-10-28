package com.ruoyi.xft.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class User implements Serializable {

    private static final long serialVersionUID = 8441497849875791522L;

    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private int gender;

    @Getter
    @Setter
    private boolean isEnable;
}
