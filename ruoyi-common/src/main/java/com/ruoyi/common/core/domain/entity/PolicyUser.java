package com.ruoyi.common.core.domain.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

public class PolicyUser implements Serializable  {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 序号 */
    @Getter
    @Setter
    private Long id;

    /** 用户名 */
    @Getter
    @Setter
    private String username;

    /** 身份证 */
    @Getter
    @Setter
    private String userId;

    /** 部门 */
    @Getter
    @Setter
    private String dept;

    /** 险种 */
    @Getter
    @Setter
    private String type;

    /** 用户账号 */
    @Getter
    @Setter
    private String account;

    /** 增员日期 */
    @Getter
    @Setter
    private Date addDate;

    /** 减员日期 */
    @Getter
    @Setter
    private Date cutDate;
}
