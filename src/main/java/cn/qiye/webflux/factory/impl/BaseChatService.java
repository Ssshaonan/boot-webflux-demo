package cn.qiye.webflux.factory.impl;

import cn.qiye.webflux.common.JsonUtils;
import cn.qiye.webflux.enums.FlowChatSceneEnum;
import cn.qiye.webflux.factory.AbstractFlowChatTemplate;
import cn.qiye.webflux.factory.FLowChatRequest;
import cn.qiye.webflux.factory.FlowChatContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service("baseChatService")
public class BaseChatService extends AbstractFlowChatTemplate {


    @Override
    public FlowChatSceneEnum getFlowChatEnum() {
        return FlowChatSceneEnum.BASE_CHAT;
    }

    @Override
    public String onNext(String data, FlowChatContext context) {

        // 解析响应数据项
        JsonNode jsonNode = JsonUtils.toJsonNode(data);
        if (jsonNode == null || !jsonNode.isObject()) {
            return data;
        }
        ObjectNode objectNode = (ObjectNode) jsonNode;
        String answer = objectNode.get("answer").asText();
        // 拼接answer
        context.getAnswer().append(answer);

        return data;
    }

    @Override
    public void completed(FlowChatContext context) {
        // 更新对话
        updateChatMsg(context);
    }

    @Override
    public void error(FlowChatContext context) {
        // 更新对话
        updateChatMsg(context);
    }

    @Override
    protected FLowChatRequest buildRequest(FlowChatContext context) {

        // 请求大模型API地址
        String url = "https://...";

        // 构建请求参数
        Map<String, Object> reqMap = new HashMap<>();
        // ...

        return FLowChatRequest.builder().url(url).jsonBody(JsonUtils.toJson(reqMap)).build();
    }

    @Override
    protected void doPreRequest(FlowChatContext context) {

        String query = context.getRequestParam().getQuery();

        // 处理校验逻辑...

        // 保存query
        super.saveChatMsg(context, query);
    }
}
