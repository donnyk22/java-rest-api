package com.github.donnyk22.models.forms;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class SearchForm {
    @Schema(example = "", defaultValue = "")
    private String keyword = "";
    @Schema(example = "0", defaultValue = "0")
    private Integer page = 0;
    @Schema(example = "10", defaultValue = "10")
    private Integer size = 10;
}
