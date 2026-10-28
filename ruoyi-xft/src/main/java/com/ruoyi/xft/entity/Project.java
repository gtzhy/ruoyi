package com.ruoyi.xft.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

public class Project implements Serializable {

    private static final long serialVersionUID = -3093639133410681801L;

    @Getter
    @Setter
    // 项目id
    private int id;

    @Getter
    @Setter
    // 项目名称
    private String name;

    @Getter
    @Setter
    // 项目编号
    private String idx;

    @Getter
    @Setter
    // 项目类型
    private String type;


}