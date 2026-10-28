package com.ruoyi.xft.service;

import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.xft.entity.BillInfo;
import com.ruoyi.xft.entity.WorkFile;
import com.ruoyi.xft.mapper.TaskMapper;
import com.ruoyi.xft.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class TaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private XftService xftService;

    @DataSource(DataSourceType.MASTER)
    public LocalDateTime getLastTaskTime(String taskName) {
        return taskMapper.selectTime(taskName);
    }

    @DataSource(DataSourceType.MASTER)
    @Transactional(rollbackFor = Exception.class)  // 添加事务注解
    public void payQuery(String taskName, String startDate, String endDate) {
        List<BillInfo> billList = xftService.getPayPageQuery(taskName, startDate, endDate);
        if (billList != null && !billList.isEmpty()) {
            taskMapper.insertBillList(billList);
        }
        LocalDateTime endOfDay = LocalDateTime.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        taskMapper.updateTime(taskName, endOfDay);
    }

    public void searchDeatil() {
        List<BillInfo> billList =  taskMapper.getUnSync();
        for (int i = 0; i < billList.size(); i++) {
            BillInfo billInfo = billList.get(i);
            List<WorkFile> list = xftService.getWorkFile(billInfo.getBillId());
            if (!list.isEmpty()) {
                taskMapper.insertBillDetail(list);
            }
            taskMapper.updateInfo(billInfo);
        }
    }

    @DataSource(DataSourceType.SLAVE)
    public List<Map<String, String>> getBirthList(String today) {
        return userMapper.getBirthList(today);
    }

}
