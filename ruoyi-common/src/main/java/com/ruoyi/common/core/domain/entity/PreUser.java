package com.ruoyi.common.core.domain.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

public class PreUser implements Serializable {

    @Serial
    private static final long serialVersionUID = -3905419053390090703L;

    /** 用户ID */
    @Getter
    @Setter
    private Long id;

    /** 用户名 */
    @Getter
    @Setter
    private String username;

    /** 用户账号 */
    @Getter
    @Setter
    private String account;

    /** 部门名称 */
    @Getter
    @Setter
    private String dept;

    /** 入职日期 */
    @Getter
    @Setter
    private Date startDate;

    /** 身份证 */
    @Getter
    @Setter
    private String userId;

    /** 手机号码 */
    @Getter
    @Setter
    private String phone;

    /** 薪福通是否创建（0否 1是） */
    @Getter
    @Setter
    private String xft;

    /** oa是否创建（0否 1是） */
    @Getter
    @Setter
    private String oa;

    /** 薪福通是否同步（0否 1是） */
    @Getter
    @Setter
    private String sync;
}
