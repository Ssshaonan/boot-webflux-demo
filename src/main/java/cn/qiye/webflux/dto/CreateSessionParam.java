package cn.qiye.webflux.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateSessionParam {

    @NotBlank(message = "问题query不能为空")
    private String query;
}
