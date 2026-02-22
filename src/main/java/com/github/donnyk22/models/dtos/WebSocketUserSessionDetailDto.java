package com.github.donnyk22.models.dtos;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketUserSessionDetailDto {
    private String userId;
    private Set<String> sessions;
}
