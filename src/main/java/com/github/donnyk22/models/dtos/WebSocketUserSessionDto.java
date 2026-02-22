package com.github.donnyk22.models.dtos;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class WebSocketUserSessionDto {
    private Integer count;
    private List<WebSocketUserSessionDetailDto> detail;
}
