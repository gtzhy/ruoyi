package com.ruoyi.xft.mapper;

import com.ruoyi.xft.entity.BillInfo;
import com.ruoyi.xft.entity.ProjectDetail;
import com.ruoyi.xft.entity.WorkFile;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TaskMapper {

    LocalDateTime selectTime(@Param("type")String type);

    void insertBillList(@Param("billList")List<BillInfo> billList);

    void updateTime(@Param("taskName")String taskName, @Param("endOfDay")LocalDateTime endOfDay);

    List<BillInfo> getUnSync();

    void insertBillDetail(@Param("workFileList")List<WorkFile> workFileList);

    void updateInfo(@Param("billInfo")BillInfo billInfo);

    List<ProjectDetail> sumProjectPay(@Param("projectList") List<String> projectList, @Param("workFileDoneStartTime") Date workFileDoneStartTime, @Param("workFileDoneEndTime") Date workFileDoneEndTime);

    List<WorkFile> selectEpenses(@Param("workFileProject") String workFileProject, @Param("workFileDoneStartTime") Date workFileDoneStartTime, @Param("workFileDoneEndTime") Date workFileDoneEndTime);

}
