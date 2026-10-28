package com.ruoyi.xft.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ruoyi.xft.entity.WorkFile;
import org.apache.ibatis.annotations.Param;


public interface SelectMapper {

    List<WorkFile> selectBXLB(@Param("workFileProject") String workFileProject, @Param("workFileDoneStartTime") Date workFileDoneStartTime, @Param("workFileDoneEndTime") Date workFileDoneEndTime);

    List<WorkFile> selectPXBX(@Param("workFileProject") String workFileProject, @Param("workFileDoneStartTime") Date workFileDoneStartTime, @Param("workFileDoneEndTime") Date workFileDoneEndTime);

    List<WorkFile> selectYWZD(@Param("workFileProject") String workFileProject, @Param("workFileDoneStartTime") Date workFileDoneStartTime, @Param("workFileDoneEndTime") Date workFileDoneEndTime);

    List<WorkFile> selectXMCL(@Param("workFileProject") String workFileProject, @Param("workFileDoneStartTime") Date workFileDoneStartTime, @Param("workFileDoneEndTime") Date workFileDoneEndTime);

    List<WorkFile> selectCLBX(@Param("workFileProject") String workFileProject, @Param("workFileDoneStartTime") Date workFileDoneStartTime, @Param("workFileDoneEndTime") Date workFileDoneEndTime);

    List<WorkFile> selectYBFK(@Param("workFileProject") String workFileProject, @Param("workFileDoneStartTime") Date workFileDoneStartTime, @Param("workFileDoneEndTime") Date workFileDoneEndTime);

    List<WorkFile> selectCBX(@Param("workFileProject") String workFileProject, @Param("workFileDoneStartTime") Date workFileDoneStartTime, @Param("workFileDoneEndTime") Date workFileDoneEndTime);

//	List<WorkFile> selectYKCZ(@Param("workFileProject") String workFileProject, @Param("workFileDoneStartTime") Date workFileDoneStartTime, @Param("workFileDoneEndTime") Date workFileDoneEndTime);

    List<WorkFile> selectFYBX(@Param("workFileProject") String workFileProject, @Param("workFileDoneStartTime") Date workFileDoneStartTime, @Param("workFileDoneEndTime") Date workFileDoneEndTime);

    List<WorkFile> selectGCFK(@Param("workFileProject") String workFileProject, @Param("workFileDoneStartTime") Date workFileDoneStartTime, @Param("workFileDoneEndTime") Date workFileDoneEndTime);

    List<Map<String, Object>> selectZYGZ(@Param("workFileProject") String workFileProject, @Param("workFileDoneStartTime") Date workFileDoneStartTime, @Param("workFileDoneEndTime") Date workFileDoneEndTime);

    List<WorkFile> selectGCJK(@Param("workFileProject") String workFileProject, @Param("workFileDoneStartTime") Date workFileDoneStartTime, @Param("workFileDoneEndTime") Date workFileDoneEndTime);

    List<WorkFile> selectKPSQ(@Param("workFileProject") String workFileProject, @Param("workFileDoneStartTime") Date workFileDoneStartTime, @Param("workFileDoneEndTime") Date workFileDoneEndTime);

    List<WorkFile> selectFPZF(@Param("workFileProject") String workFileProject, @Param("workFileDoneStartTime") Date workFileDoneStartTime, @Param("workFileDoneEndTime") Date workFileDoneEndTime);

    List<WorkFile> selectDKTZ(@Param("workFileProject") String workFileProject, @Param("workFileDoneStartTime") Date workFileDoneStartTime, @Param("workFileDoneEndTime") Date workFileDoneEndTime);

    Double expensesInTransit(@Param("workFileProject") String workFileProject, @Param("workFileDoneStartTime") Date workFileDoneStartTime, @Param("workFileDoneEndTime") Date workFileDoneEndTime);
}
