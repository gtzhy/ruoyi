package com.ruoyi.xft.mapper;

import com.ruoyi.xft.entity.Project;
import com.ruoyi.xft.entity.ProjectDetail;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ProjectMapper {

    List<Project> getProject();

    @MapKey("id")
    Map<Integer, Project> getProjectMap();

    String selectUser(@Param("useraccounts") String useraccounts);

    List<ProjectDetail> selectProject(@Param("projectList") List<String> projectList);

    List<ProjectDetail> selectAll(@Param("projectList") List<String> projectList, @Param("workFileDoneStartTime") Date workFileDoneStartTime, @Param("workFileDoneEndTime") Date workFileDoneEndTime);

    List<ProjectDetail> selectDKTZ(@Param("projectList") List<String> projectList, @Param("workFileDoneStartTime") Date workFileDoneStartTime, @Param("workFileDoneEndTime") Date workFileDoneEndTime);

    List<ProjectDetail> selectKPSQ(@Param("projectList") List<String> projectList, @Param("workFileDoneStartTime") Date workFileDoneStartTime, @Param("workFileDoneEndTime") Date workFileDoneEndTime);

    List<ProjectDetail> selectFPZF(@Param("projectList") List<String> projectList, @Param("workFileDoneStartTime") Date workFileDoneStartTime, @Param("workFileDoneEndTime") Date workFileDoneEndTime);

}
