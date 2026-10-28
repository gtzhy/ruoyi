package com.ruoyi.xft.mapper;

import com.ruoyi.xft.dto.WorkFileDTO;
import com.ruoyi.xft.entity.WorkFile;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface SelectDoneMapper {
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

    @Select("SELECT a.jst_3230_f5391 AS filetime, a.jst_3230_f5392 AS fileorder, a.jst_3230_f5393 AS fileperson, a.jst_3230_f5394 AS filegroup, w.WORKSUBMITTIME AS subtime, w.WORKDONEWITHDATE AS donetime, w.WORKTABLE_ID AS formid, w.WORKRECORD_ID AS recordid FROM jst_3230 a JOIN jsf_work w ON w.WORKRECORD_ID = a.jst_3230_id AND w.WF_CUREMPLOYEE_ID = a.jst_3230_owner AND w.WORKSTATUS = 100 AND WORKPROCESS_ID=524 ORDER BY donetime")
    List<Map<String, Object>> selectWR();
}
