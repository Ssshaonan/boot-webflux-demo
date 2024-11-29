package cn.qiye.webflux.controller;

import cn.qiye.webflux.common.Result;
import cn.qiye.webflux.dto.ChatBaseParam;
import cn.qiye.webflux.dto.CreateSessionParam;
import cn.qiye.webflux.dto.SessionBaseParam;
import cn.qiye.webflux.dto.SessionVO;
import cn.qiye.webflux.service.IChatMsgService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/chat_msg")
public class ChatMsgController {

    @Resource
    private IChatMsgService chatMsgService;

    /**
     * 创建会话
     */
    @PostMapping("/create_session")
    public Result<SessionVO> createSession(@Valid @RequestBody CreateSessionParam param) {
        return Result.ok(chatMsgService.createSession(param));
    }

    /**
     * 流式问答
     */
    @PostMapping(value = "/base_chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> baseChat(@Valid @RequestBody ChatBaseParam param) {
        return chatMsgService.baseChat(param);
    }

    /**
     * 停止流式问答
     */
    @PostMapping("/stop_chat")
    public Result<?> gptStopChat(@Valid @RequestBody SessionBaseParam param) {
        chatMsgService.stopChat(param);
        return Result.ok();
    }
}

