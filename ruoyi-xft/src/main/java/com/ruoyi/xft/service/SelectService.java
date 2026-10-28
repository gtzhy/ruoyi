package com.ruoyi.xft.service;

import java.lang.reflect.Method;
import java.util.*;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.xft.dto.WorkFileDTO;
import com.ruoyi.xft.entity.ProjectDetail;
import com.ruoyi.xft.entity.WorkFile;
import com.ruoyi.xft.mapper.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SelectService {

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private SelectMapper selectMapper;

    @Autowired
    private SelectDoneMapper selectDoneMapper;

    @Autowired
    private SelectProcessMapper selectProcessMapper;

    @Autowired
    private TaskMapper taskMapper;

    @DataSource(DataSourceType.SLAVE)
    public List<ProjectDetail> getProjectList(List<String> projectList) {
        return projectMapper.selectProject(projectList);
    }

    @DataSource(DataSourceType.SLAVE)
    public List<WorkFile> selectDKTZ(String workFileProject, Date workFileDoneStartTime, Date workFileDoneEndTime) {
        return selectMapper.selectDKTZ(workFileProject, workFileDoneStartTime, workFileDoneEndTime);
    }

    @DataSource(DataSourceType.SLAVE)
    public JSONObject getIssueAnInvocieInfo(String workFileProject, Date workFileDoneStartTime, Date workFileDoneEndTime) {
        List<WorkFile> kpList = selectMapper.selectKPSQ(workFileProject, workFileDoneStartTime, workFileDoneEndTime);
        List<WorkFile> fpzfList = selectMapper.selectFPZF(workFileProject, workFileDoneStartTime, workFileDoneEndTime);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("kp", kpList);
        jsonObject.put("fpzf", fpzfList);
        return jsonObject;
    }

    /**
     * 获取项目详情
     * @param workFileDoneStartTime 流程办理结束起始时间
     * @param workFileDoneEndTime  流程办理结束结束时间
     */
    @DataSource(DataSourceType.SLAVE)
    public JSONObject getProjectJiusiDetail(List<String> projectList, Map<String, ProjectDetail> projectMap, Date workFileDoneStartTime, Date workFileDoneEndTime) {
        // 项目支出
        List<ProjectDetail> mergedList = projectMapper.selectAll(projectList, workFileDoneStartTime, workFileDoneEndTime);
        // 项目到款
        List<ProjectDetail> DKTZList = projectMapper.selectDKTZ(projectList, workFileDoneStartTime, workFileDoneEndTime);
        // 开票申请
        List<ProjectDetail> KPSQList = projectMapper.selectKPSQ(projectList, workFileDoneStartTime, workFileDoneEndTime);
        // 开票作废申请
        List<ProjectDetail> FPZFList = projectMapper.selectFPZF(projectList, workFileDoneStartTime, workFileDoneEndTime);

        return addProjectList(projectMap, mergedList, DKTZList, KPSQList, FPZFList);
    }

    /**
     * 获取项目在新福通平台的详情
     *
     * @param workFileDoneStartTime 流程办理结束起始时间
     * @param workFileDoneEndTime  流程办理结束结束时间
     */
    @DataSource(DataSourceType.MASTER)
    public JSONObject getProjectXftDetail(List<String> projectList, Map<String, ProjectDetail> projectMap, Date workFileDoneStartTime, Date workFileDoneEndTime) {
        // 项目支出
        List<ProjectDetail> mergedList = taskMapper.sumProjectPay(projectList, workFileDoneStartTime, workFileDoneEndTime);
        // 项目到款
        List<ProjectDetail> DKTZList = new ArrayList<>();
        // 开票申请
        List<ProjectDetail> KPSQList = new ArrayList<>();
        // 开票作废申请
        List<ProjectDetail> FPZFList = new ArrayList<>();

        return addProjectList(projectMap, mergedList, DKTZList, KPSQList, FPZFList);
    }

    @DataSource(DataSourceType.SLAVE)
    public String selectUser(String useraccounts) {
        return projectMapper.selectUser(useraccounts);
    }

    @DataSource(DataSourceType.SLAVE)
    public JSONObject getProjectExpenses(String workFileProject, Date workFileDoneStartTime, Date workFileDoneEndTime) {
        JSONObject jsonObject = new JSONObject();
        // 工器具/劳保/物料
        List<WorkFile> BXLBList = selectMapper.selectBXLB(workFileProject, workFileDoneStartTime, workFileDoneEndTime);
        // 培训费用
        List<WorkFile> PXBXList = selectMapper.selectPXBX(workFileProject, workFileDoneStartTime, workFileDoneEndTime);
        // 业务招待费用
        List<WorkFile> YWZDList = selectMapper.selectYWZD(workFileProject, workFileDoneStartTime, workFileDoneEndTime);
        // 差旅费用
        List<WorkFile> XMCLList = selectMapper.selectXMCL(workFileProject, workFileDoneStartTime, workFileDoneEndTime);
        // 差旅费用
        List<WorkFile> CLBXList = selectMapper.selectCLBX(workFileProject, workFileDoneStartTime, workFileDoneEndTime);
        // 一般付款
        List<WorkFile> YBFKList = selectMapper.selectYBFK(workFileProject, workFileDoneStartTime, workFileDoneEndTime);
        // 车辆费用
        List<WorkFile> CBXList = selectMapper.selectCBX(workFileProject, workFileDoneStartTime, workFileDoneEndTime);
//		// 加油费 ZG_02油卡办理/充值审批单
//		List<WorkFile> YKCZList = selectMapper.selectYKCZ(workFileProject, workFileDoneStartTime, workFileDoneEndTime);
        // 费用报销
        List<WorkFile> FYBXList = selectMapper.selectFYBX(workFileProject, workFileDoneStartTime, workFileDoneEndTime);
        // 已支付工程款
        List<WorkFile> GCFKList = selectMapper.selectGCFK(workFileProject, workFileDoneStartTime, workFileDoneEndTime);
        // 项目组工资
        List<Map<String, Object>> ZYGZList = selectMapper.selectZYGZ(workFileProject, workFileDoneStartTime, workFileDoneEndTime);
        // 员工借款  CW_07员工借款单
        List<WorkFile> GCJKList = selectMapper.selectGCJK(workFileProject, workFileDoneStartTime, workFileDoneEndTime);

        jsonObject.put("BXLB", BXLBList);
        jsonObject.put("PXBX", PXBXList);
        jsonObject.put("YWZD", YWZDList);
        jsonObject.put("XMCL", XMCLList);
        jsonObject.put("CLBX", CLBXList);
        jsonObject.put("YBFK", YBFKList);
        jsonObject.put("CBX", CBXList);
//		jsonObject.put("YKCZ", YKCZList);
        jsonObject.put("FYBX", FYBXList);
        jsonObject.put("GCFK", GCFKList);
        jsonObject.put("ZYGZ", ZYGZList);
        jsonObject.put("GCJK", GCJKList);

        return jsonObject;
    }

    @DataSource(DataSourceType.MASTER)
    public List<WorkFile> getXftProjectExpenses(String workFileProject, Date workFileDoneStartTime, Date workFileDoneEndTime) {
        return taskMapper.selectEpenses(workFileProject, workFileDoneStartTime, workFileDoneEndTime);
    }

    /**
     * 报销单据查询
     * @param type 0 表示未完成 ； 其它为已完成
     * @param workfile 工作文件DTO对象，包含查询条件
     * @return 返回包含各种报销单据数据的JSONObject
     * @throws Exception 反射调用过程中可能抛出的异常
     */
    @SuppressWarnings("unchecked")
    @DataSource(DataSourceType.SLAVE)
    public JSONObject getVoucherList(int type, WorkFileDTO workfile) throws Exception {
        JSONObject jsonObject = new JSONObject();
        JSONObject checked = workfile.getChecked();

        Object mapper;
        Class<?> mapperClass;
        if (type == 0) {
            mapper = selectProcessMapper;
            mapperClass = selectProcessMapper.getClass();
        } else {
            mapper = selectDoneMapper;
            mapperClass = selectDoneMapper.getClass();
        }

        Map<String, String> checkedMap = new HashMap<String, String>();
        checkedMap.put("checked01", "YBFK");  // CW_03一般付款申请单
        checkedMap.put("checked02", "BXLB");  // CW_04工器具、劳保、物料费用报销单 (对私)
        checkedMap.put("checked03", "BXWL");  // CW_04工器具、劳保、物料费用付款申请 (对公)
        checkedMap.put("checked04", "FYBX");  // CW_05费用报销单
        checkedMap.put("checked05", "PXBX");  // CW_05证件/培训费用报销单
        checkedMap.put("checked06", "CLBX");  // CW_06差旅费用报销单（公司）
        checkedMap.put("checked07", "XMCL");  // CW_06员工差旅费报销单（项目）
        checkedMap.put("checked08", "YWZD");  // CW_08业务招待费用报销单
        checkedMap.put("checked09", "GCFK");  // CW_11工程款付款申请单
        checkedMap.put("checked10", "CBX");  // CW_12车辆费用报销单（对私）
        checkedMap.put("checked11", "CBXG");  // CW_12车辆费用付款申请单（对公）

        for (String key : checkedMap.keySet()) {
            if ((boolean) checked.get(key)) {
                String value = checkedMap.get(key);
                Method selectMethod = mapperClass.getMethod("select" + value, WorkFileDTO.class);
                List<WorkFile> list = (List<WorkFile>) selectMethod.invoke(mapper, workfile);
                jsonObject.put(value, list);
            }
        }

        return jsonObject;
    }

    /**
     * 报销单据查询
     * @param workfile 工作文件DTO对象，包含查询条件
     * @return 返回包含各种报销单据数据的JSONObject
     * @throws Exception 反射调用过程中可能抛出的异常
     */
    @DataSource(DataSourceType.SLAVE)
    public JSONObject getVoucherList(WorkFileDTO workfile) throws Exception {
        JSONObject jsonObject = new JSONObject();

        Map<String, String> checkedMap = new HashMap<String, String>();
        checkedMap.put("checked01", "YBFK");  // CW_03一般付款申请单
        checkedMap.put("checked03", "BXWL");  // CW_04工器具、劳保、物料费用付款申请 (对公)
        checkedMap.put("checked09", "GCFK");  // CW_11工程款付款申请单
        checkedMap.put("checked11", "CBXG");  // CW_12车辆费用付款申请单（对公）

        for (String key : checkedMap.keySet()) {
            String value = checkedMap.get(key);
            Method selectMethod = selectDoneMapper.getClass().getMethod("select" + value, WorkFileDTO.class);
            @SuppressWarnings("unchecked")
            List<WorkFile> list = (List<WorkFile>) selectMethod.invoke(selectDoneMapper, workfile);
            jsonObject.put(value, list);
        }

        return jsonObject;
    }

    /**
     * 生成项目字典，包含项目支出、项目到款、开票申请、开票作废申请
     * @param projectMap
     * @param mergedList
     * @param DKTZList
     * @param KPSQList
     * @param FPZFList
     * @return
     */
    private JSONObject addProjectList(Map<String, ProjectDetail> projectMap, List<ProjectDetail> mergedList, List<ProjectDetail> DKTZList, List<ProjectDetail> KPSQList, List<ProjectDetail> FPZFList) {
        JSONObject jsonObject = new JSONObject();
        for (ProjectDetail projectDetail: mergedList) {
            String project = projectDetail.getWorkFileProject();
            if (project == null || project.isEmpty()) {
                continue;
            }
            project = project.trim();
            ProjectDetail detail;
            if (jsonObject.get(project) == null) {
                detail = projectMap.get(project);
                if (detail == null) {
                    detail = new ProjectDetail();
                    detail.setWorkFileProject(project);
                    detail.setWorkFilePjName(projectDetail.getWorkFilePjName());
                }
                jsonObject.put(project, detail);
            } else {
                detail = (ProjectDetail) jsonObject.get(project);
            }
            double amount = detail.getWorkFileAmount();
            double addAmount = projectDetail.getWorkFileAmount();
            detail.setWorkFileAmount(amount + addAmount);
        }
        for (ProjectDetail projectDetail: DKTZList) {
            String project = projectDetail.getWorkFileProject();
            if (project == null || project.isEmpty()) {
                continue;
            }
            project = project.trim();
            ProjectDetail detail;
            if (jsonObject.get(project) == null) {
                detail = projectMap.get(project);
                if (detail == null) {
                    detail = new ProjectDetail();
                    detail.setWorkFileProject(project);
                    detail.setWorkFilePjName(projectDetail.getWorkFilePjName());
                }
                jsonObject.put(project, detail);
            } else {
                detail = (ProjectDetail) jsonObject.get(project);
            }
            double amount = detail.getWorkFileIncome();
            double addAmount = projectDetail.getWorkFileAmount();
            detail.setWorkFileIncome(amount + addAmount);
        }
        for (ProjectDetail projectDetail: KPSQList) {
            String project = projectDetail.getWorkFileProject();
            if (project == null || project.isEmpty()) {
                continue;
            }
            project = project.trim();
            ProjectDetail detail;
            if (jsonObject.get(project) == null) {
                detail = projectMap.get(project);
                if (detail == null) {
                    detail = new ProjectDetail();
                    detail.setWorkFileProject(project);
                    detail.setWorkFilePjName(projectDetail.getWorkFilePjName());
                }
                jsonObject.put(project, detail);
            } else {
                detail = (ProjectDetail) jsonObject.get(project);
            }
            double amount = detail.getWorkFileKp();
            double addAmount = projectDetail.getWorkFileAmount();
            detail.setWorkFileKp(amount + addAmount);
        }
        for (ProjectDetail projectDetail: FPZFList) {
            String project = projectDetail.getWorkFileProject();
            if (project == null || project.isEmpty()) {
                continue;
            }
            project = project.trim();
            ProjectDetail detail;
            if (jsonObject.get(project) == null) {
                detail = projectMap.get(project);
                if (detail == null) {
                    detail = new ProjectDetail();
                    detail.setWorkFileProject(project);
                    detail.setWorkFilePjName(projectDetail.getWorkFilePjName());
                }
                jsonObject.put(project, detail);
            } else {
                detail = (ProjectDetail) jsonObject.get(project);
            }
            double amount = detail.getWorkFileKp();
            double addAmount = projectDetail.getWorkFileAmount();
            detail.setWorkFileKp(amount - addAmount);
        }
        return jsonObject;
    }

}
