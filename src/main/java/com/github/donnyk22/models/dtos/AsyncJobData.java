package com.github.donnyk22.models.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class AsyncJobData {
    private String jobId;
    private String email;
}
