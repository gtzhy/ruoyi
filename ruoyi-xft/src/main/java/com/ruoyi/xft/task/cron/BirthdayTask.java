package com.ruoyi.xft.task.cron;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ruoyi.xft.service.TaskService;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Component("birthdayTask")
public class BirthdayTask {

    private static final Logger log = LoggerFactory.getLogger(BirthdayTask.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private TaskService taskService;

    /**
     * 生日提醒任务
     */
    public void birthTask() {
        // =========== 通过OA查询今日生日人员名单 ======================
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("MMdd"));
        List<Map<String, String>> birthList = taskService.getBirthList(today);
        log.info("正在为 {} 人发送生日祝福", birthList.size());
        // =========== 发放生日祝福 ======================
        try {
            String accessToken = getAccessToken();
            for (Map<String, String> employee : birthList) {
                String empName = employee.get("empname");
                String userAccounts = employee.get("useraccounts");
                log.info("正在为 {} 发送生日祝福", empName);
                sendBirthdayCard(accessToken, userAccounts, empName);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        String accessToken = null;
//        try {
//           o accessToken = getAccessTken();
//            sendBirthdayCard(accessToken, "maxiaochi", "马骁驰");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

    private String getAccessToken() throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=" + "ww8f10a05da5dd44a6" + "&corpsecret=" + "FXzCut7ZuBKVb68czNtKrFDmN9C9dPhLrG10QicTMcw";
            HttpGet httpGet = new HttpGet(url);
            var response = httpClient.execute(httpGet);
            String jsonStr = new String(response.getEntity().getContent().readAllBytes());
            JsonNode node = objectMapper.readTree(jsonStr);
            if (node.get("errcode").asInt() == 0) {
                return node.get("access_token").asText();
            } else {
                throw new RuntimeException("获取 access_token 失败: " + node.get("errmsg").asText());
            }
        }
    }

    private void sendBirthdayCard(String accessToken, String userId, String name) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String url = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + accessToken;

            ObjectNode message = objectMapper.createObjectNode();
            message.put("touser", userId);
            message.put("msgtype", "template_card");
            message.put("agentid", "1000053");

            ObjectNode templateCard = objectMapper.createObjectNode();
            templateCard.put("card_type", "text_notice");

            ObjectNode cardAction = objectMapper.createObjectNode();
            cardAction.put("type", 1);  // 1表示跳转URL
            cardAction.put("url", "http://xft.e-shan.cn/qywx/birth.html?name=" + name);
            templateCard.set("card_action", cardAction);

            ObjectNode mainTitle = objectMapper.createObjectNode();
            mainTitle.put("title", "🎂 生日快乐！");
            mainTitle.put("desc", "致 " + name + " 的生日祝福");
            templateCard.set("main_title", mainTitle);

            ObjectNode quoteArea = objectMapper.createObjectNode();
            quoteArea.put("title", "公司祝福");
            quoteArea.put("quote_text", "在这特别的日子里，愿你平安喜乐，笑口常开；工作顺利，生活美满！");
            templateCard.set("quote_area", quoteArea);

            ArrayNode horizontalContentList = templateCard.putArray("horizontal_content_list");
            ObjectNode item1 = horizontalContentList.addObject();
            item1.put("keyname", "祝福");
            item1.put("value", "生日快乐");
            ObjectNode item2 = horizontalContentList.addObject();
            item2.put("keyname", "日期");
            item2.put("value", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            templateCard.set("horizontal_content_list", horizontalContentList);

            ArrayNode jumpList = templateCard.putArray("jump_list");
            ObjectNode jumpItem = jumpList.addObject();
            jumpItem.put("title", "点击查看公司祝福");
            jumpItem.put("url", "http://xft.e-shan.cn/qywx/test.html");
            templateCard.set("jump_list", jumpList);

            message.set("template_card", templateCard);
            message.put("enable_id_trans", 0);
            message.put("enable_duplicate_check", 0);

            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(message.toString(), ContentType.APPLICATION_JSON));
            var response = httpClient.execute(httpPost);
            String result = new String(response.getEntity().getContent().readAllBytes());
            System.out.println("发送结果: " + result);
        }
    }
}
