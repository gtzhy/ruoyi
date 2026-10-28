package com.ruoyi.xft.mapper;

import com.ruoyi.xft.dto.WorkFileDTO;
import com.ruoyi.xft.entity.WorkFile;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SelectProcessMapper {
    String selectUser(@Param("useraccounts") String useraccounts);

    List<WorkFile> selectYBFK(WorkFileDTO workfileDto);

    List<WorkFile> selectBXLB(WorkFileDTO workfileDto);

    List<WorkFile> selectBXWL(WorkFileDTO workfileDto);

    List<WorkFile> selectFYBX(WorkFileDTO workfileDto);

    List<WorkFile> selectPXBX(WorkFileDTO workfileDto);

    List<WorkFile> selectCLBX(WorkFileDTO workfileDto);

    List<WorkFile> selectXMCL(WorkFileDTO workfileDto);

    List<WorkFile> selectYWZD(WorkFileDTO workfileDto);

    List<WorkFile> selectGCFK(WorkFileDTO workfileDto);

    List<WorkFile> selectCBX(WorkFileDTO workfileDto);

    List<WorkFile> selectCBXG(WorkFileDTO workfileDto);
}
