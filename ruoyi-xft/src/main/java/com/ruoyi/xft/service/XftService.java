package com.ruoyi.xft.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.common.utils.XftRequest;
import com.ruoyi.xft.config.XftConfig;
import com.ruoyi.xft.entity.BillInfo;
import com.ruoyi.xft.entity.User;
import com.ruoyi.xft.entity.WorkFile;
import com.ruoyi.xft.mapper.LeaveMapper;
import com.ruoyi.xft.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class XftService {

    @Autowired
    private XftConfig xftConfig;

    @Autowired
    private XftRequest xftRequest;

    @Autowired
    private LeaveMapper leaveMapper;

    @Autowired
    private UserMapper userMapper;

    private static final Logger logger = LoggerFactory.getLogger(XftService.class);

    /**
     * 根据用户名获取用户项目
     * @param userName
     * @return
     */
    @DataSource(value = DataSourceType.SLAVE2)
    public List<String> selectUserProject(String userName) {
        return userMapper.selectUserProject(userName);
    }

    /**
     * 获取员工信息
     * @param userName 员工姓名
     * @return 【信息-员工号】
     */
    public JSONArray getApplyEmpNumbers(String userName) {
        // 构建 queryFilterList
        JSONObject root = getJsonObject(userName);

        String responseBody = "";
        try {
            responseBody = xftRequest.postResponse(xftConfig.getStaffInfo(), root);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error("具体错误描述: ", e);
        }

        // 将字符串转换为JSONObject
        JSONObject result = JSONObject.parseObject(responseBody);
        JSONObject body = result.getJSONObject("body");
        if (body == null) {
            return null;
        }
        JSONArray records  = body.getJSONArray("records");
        JSONArray projectArray = new JSONArray();
        // 确保records数组不为空并且至少有一个元素
        if (records != null && !records.isEmpty()) {
            for (int i = 0; i < records.size(); i++) {
                JSONObject project = new JSONObject();
                // 获取第一个记录
                JSONObject record = records.getJSONObject(i);
                JSONObject staffBasicInfo = record.getJSONObject("staffBasicInfo");
                String stfName = staffBasicInfo.getString("stfName");
                String stfNumber = staffBasicInfo.getString("stfNumber");
                String householdAddressProvince = staffBasicInfo.getString("householdAddressProvince");
                String householdAddressCity = staffBasicInfo.getString("householdAddressCity");
                String householdAddressDistrict = staffBasicInfo.getString("householdAddressDistrict");
                String householdAddressDetail = staffBasicInfo.getString("householdAddressDetail");
                String presentAddressProvince = staffBasicInfo.getString("presentAddressProvince");
                String presentAddressCity = staffBasicInfo.getString("presentAddressCity");
                String presentAddressDistrict = staffBasicInfo.getString("presentAddressDistrict");
                String presentAddressDetail = staffBasicInfo.getString("presentAddressDetail");

                JSONObject staffHrmInfo = record.getJSONObject("staffHrmInfo");
                String entryDate = staffHrmInfo.getString("entryDate");
                String actualPositiveDate = staffHrmInfo.getString("actualPositiveDate");

                project.put("stfName", stfName);
                project.put("stfNumber", stfNumber);
                project.put("householdAddressProvince", householdAddressProvince);
                project.put("householdAddressCity", householdAddressCity);
                project.put("householdAddressDistrict", householdAddressDistrict);
                project.put("householdAddressDetail", householdAddressDetail);
                project.put("presentAddressProvince", presentAddressProvince);
                project.put("presentAddressCity", presentAddressCity);
                project.put("presentAddressDistrict", presentAddressDistrict);
                project.put("presentAddressDetail", presentAddressDetail);
                project.put("entryDate", entryDate);
                project.put("actualPositiveDate", actualPositiveDate);

                projectArray.add(project);
            }
        }
        return projectArray;
    }

    /**
     * 获取员工差旅报销单数据
     * @param applyEmpNumbers 申请人花名册编号
     * @return
     */
    @DataSource(value = DataSourceType.SLAVE) // 指定使用从库
    public JSONObject queryBillList(String applyEmpNumbers) {
        // 获取当前年份
        int currentYear = LocalDate.now().getYear();
        // 当前年份的开始时间：1月1日 00:00:00
        LocalDateTime beginTime = LocalDateTime.of(currentYear, 1, 1, 0, 0, 0);
        // 当前年份的结束时间：12月31日 23:59:59
        LocalDateTime endTime = LocalDateTime.of(currentYear, 12, 31, 23, 59, 59);
        // 格式化输出（可选）
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String billSubmitBeginTime = beginTime.format(formatter);
        String billSubmitEndTime = endTime.format(formatter);

        String[] billStatusList	= new String[]{"1", "2"};
        String[] eventNames	= new String[]{"【亿山能源】员工差旅报销", "【亿山数字】员工差旅报销", "[能源]员工差旅报销", "[数字]员工差旅报销"};

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("applyEmpNumbers", new String[]{applyEmpNumbers});
        jsonObject.put("billStatusList", billStatusList);
        jsonObject.put("billSubmitBeginTime", billSubmitBeginTime);
        jsonObject.put("billSubmitEndTime", billSubmitEndTime);
        jsonObject.put("eventNames", eventNames);
        jsonObject.put("start", "0");
        jsonObject.put("limit", "120");

        String responseBody = "";
        try {
            responseBody = xftRequest.postResponse(xftConfig.getBillList(), jsonObject);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error("具体错误描述: ", e);
        }

        // 将字符串转换为JSONObject
        // 查询单据具体信息，差旅类型与是否已支付
        JSONObject result = JSONObject.parseObject(responseBody);
        JSONObject body = result.getJSONObject("body");

        if (body == null) {
            return null;
        }

        JSONArray lists  = body.getJSONArray("list");
        JSONObject resultObject = new JSONObject();
        JSONArray baseInfoArray = new JSONArray();
        List<String> billList = new ArrayList<>();

        // 确保records数组不为空并且至少有一个元素
        if (lists != null && !lists.isEmpty()) {
            for (int i = 0; i < lists.size(); i++) {
                JSONObject item = new JSONObject();
                JSONObject list = lists.getJSONObject(i);
                // 获取字段
                String billId = list.getString("billId");  // 单据编号
                String eventName = list.getString("eventName");  // 单据名称
                String billSubmitTime = list.getString("billSubmitTime");  // 单据提交日期
                String billAmount = list.getString("billAmount");  // 单据金额

                String billFinishTime = getBillFinishTime(list);  // 审批完结时间
                String billStatus = getBillStatus(list);  // 单据状态
                billList.add(billId);

                item.put("billId", billId);
                item.put("eventName", eventName);
                item.put("billSubmitTime", billSubmitTime);
                item.put("billFinishTime", billFinishTime);

                item.put("billStatus", billStatus);
                item.put("billAmount", billAmount);

                baseInfoArray.add(item);
            }
        }

        JSONObject payInfoArray = getPayInfo(billList);
        JSONObject detailInfoArray = getDetailInfoArray(billList);

        resultObject.put("baseInfo", baseInfoArray);
        resultObject.put("payInfo", payInfoArray);
        resultObject.put("detailInfo", detailInfoArray);

        return resultObject;
    }

    public List<BillInfo> getPayPageQuery(String taskName, String startDate, String endDate) {
        List<BillInfo> allRecords = new ArrayList<>();
        int start = 0;
        int limit = 20;
        long total = 0;
        // 定义与字符串匹配的格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        do {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("start", String.valueOf(start));
            jsonObject.put("limit", String.valueOf(limit));
            String[] payDetailStatusList = new String[]{"PAY_SUCCESS"};
            jsonObject.put("payDetailStatusList", payDetailStatusList);
            jsonObject.put("businessScenario", taskName);
            jsonObject.put("payFinishTimeStart", startDate);
            jsonObject.put("payFinishTimeEnd", endDate);

            String responseBody = "";
            try {
                responseBody = xftRequest.postResponse(xftConfig.getPayPageQuery(), jsonObject);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                logger.error("请求账单列表失败，start={}, limit={}", start, limit, e);
                allRecords = null;
                break;
            }

            logger.info("请求返回内容，responseBody={}", responseBody);

            // 解析响应
            JSONObject respJson = JSONObject.parseObject(responseBody);
            total = respJson.getLong("total");
            JSONArray list = respJson.getJSONArray("list");

            if (list != null && !list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    JSONObject object = list.getJSONObject(i);
                    String company = object.getString("payCompanyName");
                    if (!"福建亿山能源管理有限公司".equals(company) && !"福建亿山数字能源技术有限公司".equals(company)) {
                        continue;
                    }
                    BillInfo info = new BillInfo();
                    info.setBillId(object.getString("billId"));
                    info.setAmount(object.getBigDecimal("amount"));
                    String payFinishTime = object.getString("payFinishTime");
                    info.setPayFinishTime(LocalDateTime.parse(payFinishTime, formatter));
                    info.setPayCompanyName(company);
                    info.setBillType(taskName);
                    info.setDetail(false);
                    allRecords.add(info);
                }
            }
            // 更新分页偏移
            start += limit;
            // 防止无限循环：如果返回数据少于 limit，说明已到最后一页
            if (list == null || list.size() < limit) {
                break;
            }
        } while (start < total);

        return allRecords;
    }

    public List<WorkFile> getWorkFile(String billId) {
        String responseBody = "";
        List<WorkFile> workFileList = new ArrayList<>();
        try {
            responseBody = xftRequest.getResponse(xftConfig.getBillsDetail(billId));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error("具体错误描述: ", e);
            return workFileList;
        }

        JSONObject result = JSONObject.parseObject(responseBody);
        JSONObject billInfo = result.getJSONObject("billInfo");
        if (billInfo == null) {  // 没有单据实例DTO
            return workFileList;
        }

        String workPerson = billInfo.getString("aplNam");  // 员工姓名
        String workFileId = billInfo.getString("billId");  // 单据编号
        String workFileType = billInfo.getString("eventNam");  // 单据名称
        String workGroup = billInfo.getString("billDepNam");  // 单据所属部门名称
        String billProjectId = billInfo.getString("billProjectId");
        if (billProjectId == null) { // 没有关联项目
            return workFileList;
        }
        billProjectId = billProjectId.split("\\+")[0];  // 单据项目ID
        String billProject = billInfo.getString("billProject");  // 单据项目
        String workFileAmountDesc = billInfo.getString("billRemark");  // 单据备注事由

        JSONArray expList = result.getJSONArray("expList");
        if (expList != null) {  // 费用对象列表不为空
            for (int i = 0; i < expList.size(); i++) {
                JSONObject exp = expList.getJSONObject(i);
                String expNam = exp.getString("expNam");  // 费用名称
                Double reimAmt = exp.getDouble("reimAmt");  // 费用金额
                Double taxFreeAmount = exp.getDouble("taxFreeAmount");  // 不含税金额

                JSONObject expenseSpread = exp.getJSONObject("expenseSpread");
                // 没有分摊信息
                if (expenseSpread == null) {
                    WorkFile workFile = new WorkFile(workFileType, workFileId, workPerson, workGroup);
                    workFile.setWorkFileAmountType(expNam);
                    workFile.setWorkFileProject(billProjectId);
                    workFile.setWorkFilePjName(billProject);
                    workFile.setWorkDesc(exp.getString("remark"));

                    workFile.setWorkFileSingleAmount(taxFreeAmount);
                    workFile.setWorkFileTotalAmount(reimAmt);
                    workFileList.add(workFile);
                } else {
                    JSONArray expenseSpreadItemList =  expenseSpread.getJSONArray("expenseSpreadItemList");
                    if (expenseSpreadItemList == null || expenseSpreadItemList.isEmpty()) {  // 没有分摊明细
                        WorkFile workFile = new WorkFile(workFileType, workFileId, workPerson, workGroup);
                        workFile.setWorkFileAmountType(expNam);
                        workFile.setWorkFileSingleAmount(taxFreeAmount);  // 分摊不含税金额
                        workFile.setWorkFileTotalAmount(reimAmt);  // 分摊含税金额

                        workFile.setWorkFileProject(billProjectId);
                        workFile.setWorkFilePjName(billProject);
                        workFile.setWorkDesc(exp.getString("remark"));
                        workFile.setWorkFileAmountDesc(workFileAmountDesc);
                        workFileList.add(workFile);
                    } else {
                        String expenseName = expenseSpread.getString("expenseName");  // 费用类型
                        Double totalAmount = expenseSpread.getDouble("totalAmount");
                        for (int j = 0; j < expenseSpreadItemList.size(); j++) {
                            JSONObject expenseSpreadItem = expenseSpreadItemList.getJSONObject(j);
                            String projectId = expenseSpreadItem.getString("projectId");  // 单据项目ID
                            if (projectId == null) {
                                projectId = billProjectId;
                            }
                            projectId = projectId.split("\\+")[0];

                            String project = expenseSpreadItem.getString("projectName");  // 单据项目
                            Double proportion = expenseSpreadItem.getDouble("proportion");  // 分摊比例
                            Double spreadAmount = expenseSpreadItem.getDouble("spreadAmount");  // 分摊含税金额

                            WorkFile workFile = new WorkFile(workFileType, workFileId, workPerson, workGroup);
                            workFile.setWorkFileAmountType(expenseName);
                            workFile.setWorkFileSingleAmount(Math.round(taxFreeAmount * proportion) / 100d);  // 分摊不含税金额
                            workFile.setWorkFileTotalAmount(spreadAmount);  // 分摊含税金额

                            workFile.setWorkFileProject(projectId);
                            workFile.setWorkFilePjName(project);
                            workFile.setWorkDesc(exp.getString("remark"));
                            workFile.setWorkFileAmountDesc(workFileAmountDesc);
                            workFileList.add(workFile);
                        }
                    }
                }
            }
        } else {
            // 没有费用对象列表，属异常情况，特殊处理
            WorkFile workFile = new WorkFile(workFileType, workFileId, workPerson, workGroup);
            workFile.setWorkFilePjName("");
            workFile.setWorkFileProject("");
            workFile.setWorkFileAmountType("无");
            workFile.setWorkFileSingleAmount(0);
            workFile.setWorkFileTotalAmount(0);
            workFile.setWorkFileAmountDesc("");
            workFile.setWorkDesc("");
            workFileList.add(workFile);
        }

        return workFileList;
    }

    @DataSource(DataSourceType.SLAVE2)
    public User getUser(String username) {
        User user = null;
        if (username != null && !"".equals(username)) {
            user = userMapper.selectByUsername(username);
        }
        return user;
    }

    private JSONObject getJsonObject(String userName) {
        JSONObject filter = new JSONObject();
        filter.put("fieldKey", "stfName");
        filter.put("fieldQueryMethod", "FUZZY");
        filter.put("fieldValue", userName);

        JSONArray queryFilterList = new JSONArray();
        queryFilterList.add(filter);

        // 构建 queryResultType
        JSONObject queryResultType = new JSONObject();
        queryResultType.put("queryType", "GROUP");

        // queryClassKeyList 是空数组
        JSONArray queryClassKeyList = new JSONArray();
        queryClassKeyList.add("S01BASIC");
        queryClassKeyList.add("S02ONJOB");
        queryClassKeyList.add("S03PERSN");
        queryResultType.put("queryClassKeyList", queryClassKeyList);

        // 构建根对象
        JSONObject root = new JSONObject();
        root.put("queryFilterList", queryFilterList);
        root.put("queryResultType", queryResultType);
        root.put("currentPage", "1");
        root.put("pageSize", "50");
        return root;
    }

    /**
     * 获取单据支付详情
     * @param billIds
     * @return
     */
    private JSONObject getPayInfo(List<String> billIds) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("opauid", xftRequest.getCompanyId());
        jsonObject.put("billIdList", billIds);

        String responseBody = "";
        try {
            responseBody = xftRequest.postResponse(xftConfig.getPayInfoDeatil(), jsonObject);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error("具体错误描述: ", e);
        }

        JSONObject result = JSONObject.parseObject(responseBody);
        JSONArray billDetailArr = result.getJSONArray("data");

        JSONObject detailInfo = new JSONObject();
        for (int i = 0; i < billDetailArr.size(); i++) {
            JSONObject object = billDetailArr.getJSONObject(i);
            String billId = object.getString("billId");
            String payFinishTime = object.getString("payFinishTime");
            String payDetailStatus = object.getString("payDetailStatus");

            detailInfo.put(billId, payFinishTime + "," + payDetailStatus);
        }

        return detailInfo;
    }

    /**
     * 获取报销单中的差旅类型
     * @param billIds
     * @return
     */
    private JSONObject getDetailInfoArray(List<String> billIds) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("billIds", billIds);
        jsonObject.put("limit", billIds.size());
        jsonObject.put("start", 0);

        JSONObject payInfo = new JSONObject();

        String responseBody = "";
        try {
            responseBody = xftRequest.postResponse(xftConfig.getQueryDetails(), jsonObject);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error("具体错误描述: ", e);
        }

        // 将字符串转换为JSONObject
        // 查询单据具体信息，差旅类型与是否已支付
        JSONObject result = JSONObject.parseObject(responseBody);
        JSONArray billExtDetailInfoDtos = result.getJSONObject("data").getJSONArray("billExtDetailInfoDtos");

        for (int i = 0; i < billExtDetailInfoDtos.size(); i++) {
            JSONObject object = billExtDetailInfoDtos.getJSONObject(i);
            JSONObject billInfo = object.getJSONObject("billInfo");
            String billId = billInfo.getString("billId");
            JSONArray customFieldValues = billInfo.getJSONArray("customFieldValues");
            for (int j = 0; j < customFieldValues.size(); j++) {
                JSONObject customObject = customFieldValues.getJSONObject(j);
                String customFieldName = customObject.getString("customFieldName");
                if ("差旅类型".equals(customFieldName)) {
                    JSONArray customFieldValueObject = customObject.getJSONArray("customFieldValue");
                    String customFieldValue = customFieldValueObject.getJSONObject(0).getString("name");
                    payInfo.put(billId, customFieldValue);
                    break;
                }
            }
        }

        return payInfo;
    }

    /**
     * 获取单据完结时间
     * @param list 单据基础信息
     * @return 完结时间为空时返回未知
     */
    private String getBillFinishTime(JSONObject list) {
        String billFinishTime = list.getString("billFinishTime");
        if (billFinishTime == null) {
            return "未知";
        } else {
            return billFinishTime;
        }
    }

    /**
     * 获取单据状态 1 审批中 2 审批通过
     * @param list 单据基础信息
     * @return 审批中/审批通过
     */
    private String getBillStatus(JSONObject list) {
        String billStatus = list.getString("billStatus");
        if ("2".equals(billStatus)) {
            return "审批通过";
        } else {
            return "审批中";
        }
    }

}
