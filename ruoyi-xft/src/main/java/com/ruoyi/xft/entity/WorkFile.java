package com.ruoyi.xft.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

public class WorkFile implements Serializable {

    private static final long serialVersionUID = 7402135903453483540L;

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
    // 发起日期
    private Date workFileStartTime;

    @Getter
    @Setter
    // 办结日期
    private Date workFileDoneTime;

    @Getter
    @Setter
    // 报销人
    private String workPerson;

    @Getter
    @Setter
    // 收款单位
    private String payee;

    @Getter
    @Setter
    // 部门
    private String workGroup;

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
    // 费用子金额
    private double workFileSingleAmount;

    @Getter
    @Setter
    // 费用金额
    private double workFileTotalAmount;

    @Getter
    @Setter
    // 费用说明
    private String workFileAmountDesc;

    @Getter
    @Setter
    // 备注
    private String workDesc;

    @Getter
    @Setter
    private String formId;

    @Getter
    @Setter
    private String recordId;

    public WorkFile(String workFileType, String workFileId, String workFilePerson, String workGroup) {
        this.workFileType = workFileType;
        this.workFileId = workFileId;
        this.workPerson = workFilePerson;
        this.workGroup = workGroup;
    }
}
