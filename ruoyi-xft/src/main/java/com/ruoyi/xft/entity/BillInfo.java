package com.ruoyi.xft.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class BillInfo implements Serializable  {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    // 项目id
    private int id;

    @Getter
    @Setter
    // 项目名称
    private String billId;

    @Getter
    @Setter
    // 项目编号
    private BigDecimal amount;

    @Getter
    @Setter
    // 项目类型
    private LocalDateTime payFinishTime;

    @Getter
    @Setter
    // 项目类型
    private String payCompanyName;

    @Getter
    @Setter
    // 项目类型
    private String billType;

    @Getter
    @Setter
    // 项目类型
    private boolean isDetail;
}
