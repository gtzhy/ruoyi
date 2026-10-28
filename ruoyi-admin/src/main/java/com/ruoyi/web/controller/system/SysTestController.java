package com.ruoyi.web.controller.system;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.restful.JsonRestful;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class SysTestController {

    @GetMapping("/user")
    public String user(HttpServletRequest request, HttpServletResponse response) {
        return "usertest";
    }

}
