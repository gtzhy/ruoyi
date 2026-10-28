package com.ruoyi.xft.task.cron;

import com.ruoyi.xft.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component("billSyncTask")
public class BillSyncTask {

    private static final Logger log = LoggerFactory.getLogger(BillSyncTask.class);

    @Autowired
    private TaskService taskService;

    public void billTask() {
        log.info("============ 对私报销同步任务执行开始 ============");
        billQueryTask("individual");
        log.info("============ 对私报销同步任务执行结束 ============");
        log.info("============ 对公报销同步任务执行开始 ============");
        billQueryTask("corporate");
        log.info("============ 对公报销同步任务执行结束 ============");

        taskService.searchDeatil();
    }

    /**
     * 已支付报销单数据同步
     */
    private void billQueryTask(String taskName) {
        // 获取上次查询时间
    	LocalDateTime startOfDay = taskService.getLastTaskTime(taskName);
    	// 定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 格式化为字符串
        String startDate = startOfDay.format(formatter);

        // 将日期设置为当天的 00:00:00
        LocalDateTime endOfDay = LocalDateTime.now();
        String endDate = endOfDay.format(formatter);
        taskService.payQuery(taskName, startDate, endDate);
    }

}
