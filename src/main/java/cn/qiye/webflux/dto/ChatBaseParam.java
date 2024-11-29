package cn.qiye.webflux.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ChatBaseParam {

    @NotBlank(message = "问题query不能为空")
    private String query;

    @NotNull(message = "对话id不能为空")
    private Long sessionId;
}
