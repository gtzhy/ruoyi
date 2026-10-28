package com.ruoyi.xft.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.restful.JsonRestful;
import com.ruoyi.common.restful.ResponseCode;
import com.ruoyi.xft.dto.WorkFileDTO;
import com.ruoyi.xft.entity.ProjectDetail;
import com.ruoyi.xft.entity.User;
import com.ruoyi.xft.entity.WorkFile;
import com.ruoyi.xft.service.SelectService;
import com.ruoyi.xft.service.XftService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 薪福通 操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/openApi/")
public class XftController {

    @Autowired
    private XftService xftService;

    @Autowired
    private SelectService selectService;

    /**
     * 获取项目详细信息列表
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @return 返回包含项目详细信息的JSON响应
     */
    @GetMapping("/projectDetailList")
    public JsonRestful projectDetailList(HttpServletRequest request, HttpServletResponse response) {
        String userName = request.getParameter("userName");
        String workFileDoneStartTimeStr = request.getParameter("workFileDoneStartTime");
        String workFileDoneEndTimeStr = request.getParameter("workFileDoneEndTime");

        Date workFileDoneStartTime = null;
        Date workFileDoneEndTime = null;

        if (workFileDoneStartTimeStr != null && !workFileDoneStartTimeStr.isEmpty()) {
            long timestamp = Long.parseLong(workFileDoneStartTimeStr);
            workFileDoneStartTime = new Date(timestamp);
        }

        if (workFileDoneEndTimeStr != null && !workFileDoneEndTimeStr.isEmpty()) {
            long timestamp = Long.parseLong(workFileDoneEndTimeStr);
            workFileDoneEndTime = new Date(timestamp);
        }

        List<String> projectList = xftService.selectUserProject(userName);
        JSONObject jsonObject = getProjectDetailList(projectList, workFileDoneStartTime, workFileDoneEndTime);
        return JsonRestful.success(jsonObject);
    }

    /**
     * 获取项目支出详细信息
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @return 返回包含项目支出详细信息的JSON响应
     */
    @GetMapping("/projectExpenses")
    public JsonRestful projectExpenses(HttpServletRequest request, HttpServletResponse response) {
        String workFileProject = request.getParameter("workFileProject");
        // 从请求中获取时间戳字符串
        String workFileDoneStartTimeStr = request.getParameter("workFileDoneStartTime");
        String workFileDoneEndTimeStr = request.getParameter("workFileDoneEndTime");
        Date workFileDoneStartTime = null;
        Date workFileDoneEndTime = null;

        if (workFileDoneStartTimeStr != null && !workFileDoneStartTimeStr.isEmpty()) {
            long timestamp = Long.parseLong(workFileDoneStartTimeStr);
            workFileDoneStartTime = new Date(timestamp);
        }

        if (workFileDoneEndTimeStr != null && !workFileDoneEndTimeStr.isEmpty()) {
            long timestamp = Long.parseLong(workFileDoneEndTimeStr);
            workFileDoneEndTime = new Date(timestamp);
        }

        JSONObject jsonObject = selectService.getProjectExpenses(workFileProject, workFileDoneStartTime, workFileDoneEndTime);
        List<WorkFile> xftWorkFileList = selectService.getXftProjectExpenses(workFileProject, workFileDoneStartTime, workFileDoneEndTime);
        jsonObject.put("xft", xftWorkFileList);
        return JsonRestful.success(jsonObject);
    }

    /**
     * 查询用户名称
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/user")
    public JsonRestful user(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("userName");
        JSONObject jsonObject = new JSONObject();
        JSONArray nameArr = xftService.getApplyEmpNumbers(username);
        jsonObject.put("result", nameArr);
        return JsonRestful.success(jsonObject);
    }

    /**
     * 查询用户差旅单信息
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/user/travel/bill")
    public JsonRestful userTravelBill(HttpServletRequest request, HttpServletResponse response) {
        String userName = request.getParameter("userName");
        JSONObject jsonObject = xftService.queryBillList(userName);
        return JsonRestful.success(jsonObject);
    }

    /**
     * 项目到款通知查询
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @return 返回包含到款通知数据的JSON响应
     */
    @GetMapping("/projectIncome")
    public JsonRestful projectIncome(HttpServletRequest request, HttpServletResponse response) {
        String workFileProject = request.getParameter("workFileProject");
        Date workFileDoneStartTime = null;
        Date workFileDoneEndTime = null;

        List<WorkFile> list = selectService.selectDKTZ(workFileProject, workFileDoneStartTime, workFileDoneEndTime);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", list);
        return JsonRestful.success(jsonObject);
    }

    /**
     项目开票申请单查询
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @return 返回包含开票申请和作废数据的JSON响应
     */
    @GetMapping("/projectInvoice")
    public JsonRestful projectInvoice(HttpServletRequest request, HttpServletResponse response) {
        String workFileProject = request.getParameter("workFileProject");
        Date workFileDoneStartTime = null;
        Date workFileDoneEndTime = null;

        JSONObject jsonObject = selectService.getIssueAnInvocieInfo(workFileProject, workFileDoneStartTime, workFileDoneEndTime);
        return JsonRestful.success(jsonObject);
    }

    private JSONObject getProjectDetailList(List<String> projectList, Date workFileDoneStartTime, Date workFileDoneEndTime) {
        if (projectList.isEmpty()) {
            // 没有权限限制
            System.out.println("projectList size === 0");
            projectList = null;
        }

        List<ProjectDetail> list = selectService.getProjectList(projectList);
        Map<String, ProjectDetail> projectMap = new HashMap<>();

        for (ProjectDetail projectDetail: list) {
            String project = projectDetail.getWorkFileProject();
            projectMap.put(project, projectDetail);
        }

        JSONObject jiusiObject = selectService.getProjectJiusiDetail(projectList, projectMap, workFileDoneStartTime, workFileDoneEndTime);
        JSONObject xftObject = selectService.getProjectXftDetail(projectList, projectMap, workFileDoneStartTime, workFileDoneEndTime);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("jiusi", jiusiObject);
        jsonObject.put("xft", xftObject);
        return jsonObject;
    }

    /**
     * 查询用户名称
     * @param request   HTTP请求对象
     * @param response  HTTP响应对象
     * @return 返回包含用户名称的JSON响应
     */
    @GetMapping("/getUser")
    public JsonRestful getUser(HttpServletRequest request, HttpServletResponse response) {
        String useraccounts = request.getParameter("username");
        JSONObject jsonObject = new JSONObject();
        User user = xftService.getUser(useraccounts);
        if (user == null) {  // 不可访问所有人
            String name = selectService.selectUser(useraccounts);
            jsonObject.put("code", 0);
            jsonObject.put("result", name);
        } else {
            jsonObject.put("code", 1);
        }
        return JsonRestful.success(jsonObject);
    }

    /**
     * 办结报销单查询
     * @param workfile 工作文件DTO对象
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @return 返回包含办结报销单数据的JSON响应
     */
    @PostMapping("/doneList")
    public JsonRestful doneList(@RequestBody WorkFileDTO workfile, HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject;
        try {
            jsonObject = selectService.getVoucherList(1, workfile);
            return JsonRestful.success(jsonObject);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.getCause().printStackTrace();
            return JsonRestful.error(ResponseCode.OTHER);
        }
    }

    /**
     * 办结报销单查询(boss模板)
     * @param workfile 工作文件DTO对象
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @return 返回包含办结报销单数据的JSON响应
     */
    @PostMapping("/bossList")
    public JsonRestful bossList(@RequestBody WorkFileDTO workfile, HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject;
        try {
            jsonObject = selectService.getVoucherList(workfile);
            return JsonRestful.success(jsonObject);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return JsonRestful.error(ResponseCode.OTHER);
        }
    }
}
