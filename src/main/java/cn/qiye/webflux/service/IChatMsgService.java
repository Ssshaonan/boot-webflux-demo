package cn.qiye.webflux.service;

import cn.qiye.webflux.dto.ChatBaseParam;
import cn.qiye.webflux.dto.CreateSessionParam;
import cn.qiye.webflux.dto.SessionBaseParam;
import cn.qiye.webflux.dto.SessionVO;
import reactor.core.publisher.Flux;

/***
 * @ClassName IChatMsgService
 * @Description TODO
 * @Author qiye
 * @Date 2024/11/29 16:08
 * @Version 1.0
 */
public interface IChatMsgService {

    SessionVO createSession(CreateSessionParam param);

    Flux<String> baseChat(ChatBaseParam param);

    void stopChat(SessionBaseParam param);
}
