package com.ruoyi.xft.mapper;

import com.ruoyi.xft.entity.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {

    User selectByUsername(String username);

    List<String> selectUserProject(String username);

    List <Map<String, String>> getBirthList(String today);
}
