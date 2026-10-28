package com.ruoyi.xft.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface LeaveMapper {

    Map<String, LocalDateTime> selectUser(@Param("user") String user);

    List<Map<String, String>> selectJoinTime(@Param("workPerson") String workPerson);

    Integer selectLeaveTime(@Param("startOfYear") LocalDate startOfYear,  @Param("workPerson") String workPerson);

    List<Map<String, Object>> selectNoneMessage();

    Map<String, Object> queryForMap(@Param("empId")String empId);
}
