package com.ruoyi.xft.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

@Configuration
public class XftConfig {

    /* 批量查询单据详情 */
    @Getter
    @Setter
    private String queryDetails = "https://api.cmbchina.com/itrip/xft-api/v1/bills/queryDetails";

    /* 查询支付明细 */
    @Getter
    @Setter
    private String payInfoDeatil = "https://api.cmbchina.com/itrip/xft-api/v1/payInfo/detailList";

    /* 分页查询支付明细 */
    @Getter
    @Setter
    private String payPageQuery = "https://api.cmbchina.com/itrip/xft-api/v1/pay-detail/page-query";

    /* 批量查询单据列表 */
    @Getter
    @Setter
    private String billList = "https://api.cmbchina.com/itrip/xft-api/v1/bills/bill-list";

    /* 查询用户信息 */
    @Getter
    @Setter
    private String staffInfo = "https://api.cmbchina.com/hrm/hrm2/xft-employeeprofile/employee/external/api/query/staffInfo";

    public String getBillsDetail(String billId) {
        return "https://api.cmbchina.com/itrip/xft-api/v1/bills/detail/" + billId;
    }

}
