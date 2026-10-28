package com.ruoyi.xft.dto;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson2.JSONObject;

import lombok.Getter;
import lombok.Setter;

public class WorkFileDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -4614108190146615618L;

    @Getter
    @Setter
    // 筛选项
    private JSONObject checked;

    @Getter
    @Setter
    // 表单名称
    private String workFileType;

    @Getter
    @Setter
    // 流程编号
    private String workFileId;

    @Getter
    @Setter
    // 办结日期
    private Date workFileDoneStartTime;

    @Getter
    @Setter
    // 办结日期
    private Date workFileDoneEndTime;

    @Getter
    @Setter
    // 报销人
    private String workPerson;

    @Getter
    @Setter
    // 部门
    private String workGroup;

    @Getter
    @Setter
    // 收款单位
    private String payee;

    @Getter
    @Setter
    // 项目名称
    private String workFilePjName;

    @Getter
    @Setter
    // 项目编号
    private String workFileProject;

    @Getter
    @Setter
    // 费用类型
    private String workFileAmountType;

    @Getter
    @Setter
    // 费用金额
    private Double workFileTotalAmount;

    @Getter
    @Setter
    // 费用说明
    private String workFileAmountDesc;

    @Getter
    @Setter
    // 备注
    private String workDesc;
}
