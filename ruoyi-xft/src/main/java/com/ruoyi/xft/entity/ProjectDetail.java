package com.ruoyi.xft.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class ProjectDetail implements Serializable {

    private static final long serialVersionUID = -7688100814659154892L;

    @Getter
    @Setter
    // 项目编号
    private String workFileProject;

    @Getter
    @Setter
    // 项目名称
    private String workFilePjName;

    @Getter
    @Setter
    // 项目金额（合同金额）
    private Double workFileProjectAmout;

    @Getter
    @Setter
    // 费用金额
    private double workFileAmount = 0;

    @Getter
    @Setter
    // 到款金额
    private double workFileIncome = 0;

    @Getter
    @Setter
    // 开票金额
    private double workFileKp = 0;

}
