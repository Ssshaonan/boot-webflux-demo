package cn.qiye.webflux.factory;

import cn.qiye.webflux.common.CommonError;
import cn.qiye.webflux.common.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/***
 * @ClassName AbstractFlowChatTemplate
 * @Description 流式问答抽象父类模板
 * @Author qiye
 * @Date 2024/11/29 16:40
 * @Version 1.0
 */
@Slf4j
@Component
public abstract class AbstractFlowChatTemplate implements IFlowChat, FlowChatCallBack {

    private WebClient webClient;

    @PostConstruct
    private void init() {
        webClient = WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json").build();
    }

    private final ConcurrentHashMap<Long, FlowChatSubscriber> subscriberMap = new ConcurrentHashMap<>();

    /**
     * 问答入口
     */
    @Override
    public Flux<String> request(FlowChatContext context) {

        // 请求大模型问答之前的逻辑处理
        doPreRequest(context);

        // 请求大模型、处理回调逻辑
        return Flux.create(emitter -> {
            Flux<String> response = this.doRequest(context, buildRequest(context));
            log.info("subscriberMap in AbstractChatService before put: {}", JsonUtils.toJson(subscriberMap));
            FlowChatSubscriber subscriber = new FlowChatSubscriber(emitter, this, context, subscriberMap);
            subscriberMap.put(context.getRequestParam().getSessionId(), subscriber);
            log.info("subscriberMap in AbstractChatService after put: " + JsonUtils.toJson(subscriberMap));
            response.subscribe(subscriber);
            emitter.onDispose(subscriber);
        });
    }

    @Override
    public void stop(Long sessionId) {
        FlowChatSubscriber subscriber = subscriberMap.get(sessionId);
        if (subscriber == null) {
            return;
        }
        subscriber.stop();
    }

    /**
     * 保存对话内容
     */
    protected Long saveChatMsg(FlowChatContext context, String query) {

        // 保存对话query

        return null;
    }

    /**
     * 更新对话内容
     */
    protected void updateChatMsg(FlowChatContext context) {

        // 更新answer

    }

    /**
     * 构建响应参数
     */
    protected Map<String, Object> buildAnswer(String answer) {
        Map<String, Object> res = new HashMap<>(4, 1);
        res.put("answer", answer);
        res.put("docs", Collections.emptyList());
        res.put("process_docs", Collections.emptyList());
        return res;
    }

    /**
     * 请求大模型
     */
    private Flux<String> doRequest(FlowChatContext context, FLowChatRequest request) {
        log.info("请求大模型开始，URL:{}, 参数:{}", request.getUrl(), request.getJsonBody());
        return webClient.post()
                .uri(request.getUrl())
                .accept(MediaType.TEXT_EVENT_STREAM)
                .bodyValue(request.getJsonBody())
                .retrieve()
                .bodyToFlux(String.class)
                .onErrorResume(WebClientResponseException.class, ex -> {
                    subscriberMap.remove(context.getRequestParam().getSessionId());
                    log.error("请求大模型接口异常", ex);
                    return Flux.just(JsonUtils.toJson(buildAnswer(CommonError.LLM_ERROR.getMsg())));
                })
                .onErrorResume(Throwable.class, ex -> {
                    subscriberMap.remove(context.getRequestParam().getSessionId());
                    log.error("系统异常", ex);
                    return Flux.just(JsonUtils.toJson(buildAnswer(CommonError.GLOBAL_ERROR.getMsg())));
                });
    }

    /**
     * 前置逻辑处理
     */
    protected abstract void doPreRequest(FlowChatContext context);

    /**
     * 构建大模型请求参数
     */
    protected abstract FLowChatRequest buildRequest(FlowChatContext context);
}
