package cn.qiye.webflux.service;

import cn.qiye.webflux.dto.ChatBaseParam;
import cn.qiye.webflux.dto.CreateSessionParam;
import cn.qiye.webflux.dto.SessionBaseParam;
import cn.qiye.webflux.dto.SessionVO;
import cn.qiye.webflux.enums.FlowChatSceneEnum;
import cn.qiye.webflux.factory.FlowChatContext;
import cn.qiye.webflux.factory.FlowChatFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;

/***
 * @ClassName ChatMsgService
 * @Description TODO
 * @Author qiye
 * @Date 2024/11/29 16:17
 * @Version 1.0
 */
@Slf4j
@Service
public class ChatMsgService implements IChatMsgService {

    @Resource
    private FlowChatFactory chatFactory;

    @Override
    public SessionVO createSession(CreateSessionParam param) {
        // 创建会话...
        return new SessionVO();
    }

    @Override
    public Flux<String> baseChat(ChatBaseParam param) {
        return chatFactory.getInstance(FlowChatSceneEnum.BASE_CHAT)
                .request(new FlowChatContext(param));
    }

    @Override
    public void stopChat(SessionBaseParam param) {
        chatFactory.getInstance(FlowChatSceneEnum.BASE_CHAT)
                .stop(param.getSessionId());
    }
}
