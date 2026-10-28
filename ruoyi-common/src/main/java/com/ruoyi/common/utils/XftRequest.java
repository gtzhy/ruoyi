package com.ruoyi.common.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson2.JSONObject;
import com.cmb.xft.open.api.BaseReqInf;
import com.cmb.xft.open.api.HttpResponseData;
import com.cmb.xft.open.api.Sm4Util;
import com.cmb.xft.open.api.XftOpenApiReqClient;
import com.cmb.xft.open.api.XftVerifyTokenClient;
import com.cmb.xft.open.login.token.LoginInfo;
import com.google.gson.Gson;

@Component
public class XftRequest {

    /* 薪福通租户号 */
    @Value("${xft.config.companyId}")
    private String companyId;

    /* 开放平台 appId*/
    @Value("${xft.config.appId}")
    private String appId;

    /* 开放平台 authoritySecret */
    @Value("${xft.config.authoritySecret}")
    private String authoritySecret;

    public String getCompanyId() {
        return companyId;
    }

    /**
     * 获取登录信息
     * @param data 跳转时url携带的data
     * @param sign 跳转时url携带的sign
     * @return 登录信息
     * @throws Exception
     */
    public LoginInfo getLoginInfo(String data, String sign, String accessTokenUrl, String loginInfoUrl) throws Exception {
        /* 公共信息构建 */
        BaseReqInf baseReqInf = new BaseReqInf(companyId, appId, authoritySecret);
        /* 3.进行验证，可以获得登录token信息LoginTokenInfo */
        XftVerifyTokenClient xftVerifyTokenClient = new XftVerifyTokenClient(baseReqInf, accessTokenUrl, loginInfoUrl);
        LoginInfo loginInfoByToken = xftVerifyTokenClient.getLoginInfo(data, sign);
        return loginInfoByToken;
    }

    public String postResponse(String url, JSONObject requestObject) throws Exception {
        /* 加密和解密密文的key */
        String key = authoritySecret.substring(0, 32);
        /* 2.公共信息构建 */
        BaseReqInf baseReqInf = new BaseReqInf(companyId, appId, authoritySecret);
        baseReqInf.setUsrNbr("A0001");
        baseReqInf.setUsrUid("AUTO0001");
        /* 3. 请求报文、queryParam构建：字符串形式(推荐使用方式)*/
        String requestBody =  requestObject.toString();
        String secretMsg = Sm4Util.encryptEcb(key, requestBody);
        Map<String, String> newRequestBodyMap = new HashMap<>();
        // secretMsg这个名称固定
        newRequestBodyMap.put("secretMsg", secretMsg);
        Gson gson = new Gson();
        requestBody = gson.toJson(newRequestBodyMap);
        /* url里面除了公共参数（CSCAPPUID，CSCPRJCOD，CSCUSRUID，CSCREQTIM，CSCUSRNBR）的其他参数 */
        String queryString = "{}";
        Gson gs = new Gson();
        Map<String, Object> queryParam = gs.fromJson(queryString,Map.class);
        /* 4.调用接口 */
        HttpResponseData postReqResult = XftOpenApiReqClient.doCommonPostReq(baseReqInf, url, queryParam,requestBody);
        String responseBody = postReqResult.getBody();
        responseBody = Sm4Util.decryptEcb(key, responseBody);
        return responseBody;
    }

    public String getResponse(String url) throws Exception {
        /* 加密和解密密文的key */
        String key = authoritySecret.substring(0, 32);
        /* 2.公共信息构建 */
        BaseReqInf baseReqInf = new BaseReqInf(companyId, appId, authoritySecret);
        baseReqInf.setUsrNbr("A0001");
        baseReqInf.setUsrUid("AUTO0001");
        /* 3. queryParam构建*/
        /* url里面除了公共参数（CSCAPPUID，CSCPRJCOD，CSCUSRUID，CSCREQTIM，CSCUSRNBR）的其他参数 */
        String queryString = "{}";
        Gson gs = new Gson();
        Map<String, Object> queryParam = gs.fromJson(queryString, new com.google.gson.reflect.TypeToken<Map<String, Object>>(){}.getType());
        /* 4.调用接口 */
        HttpResponseData result = XftOpenApiReqClient.doCommonGetReq(baseReqInf, url, queryParam);
        String responseBody = result.getBody();
        try {
            responseBody = Sm4Util.decryptEcb(key, responseBody);
            System.out.println(responseBody);
        } catch (Exception e) {
            System.out.println("解密失败：" + responseBody);
        }
        return responseBody;
    }
}

