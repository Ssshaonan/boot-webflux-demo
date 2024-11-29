package cn.qiye.webflux.factory;

import cn.qiye.webflux.dto.ChatBaseParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/***
 * @ClassName FlowChatContext
 * @Description 流式问答上下文
 * @Author qiye
 * @Date 2024/11/29 16:26
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlowChatContext {

    /**
     * 前端请求参数
     */
    private ChatBaseParam requestParam;

    /**
     * 问答id
     */
    private Long msgId;

    /**
     * 对话回答
     */
    private StringBuilder answer;
    /**
     * 对话对应索引
     */
    private StringBuilder docs;

    public FlowChatContext(ChatBaseParam requestParam) {
        this.requestParam = requestParam;
    }
}
