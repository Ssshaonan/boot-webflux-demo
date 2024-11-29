package cn.qiye.webflux.factory;

import cn.qiye.webflux.enums.FlowChatSceneEnum;
import reactor.core.publisher.Flux;

public interface IFlowChat {

    FlowChatSceneEnum getFlowChatEnum();

    Flux<String> request(FlowChatContext flowChatContext);

    void stop(Long sessionId);
}
