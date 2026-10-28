package com.ruoyi.common.restful;

public enum EventType {
    TRAVEL_REIM("差旅报销"),
    COMMON_REIM("普通报销"),
    EMP_LEND("员工借款"),
    REPAYMENT("还款"),
    SUPPLIER_LEND("供应商借款"),
    SUPPLIER_REIM("供应商报销"),
    SUPPLIER_SETTLE("供应商结算"),
    REVERSE("冲销"),
    REBATE("返点");

    private final String eventType;

    EventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventType() {
        return eventType;
    }
}