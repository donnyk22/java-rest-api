package com.github.donnyk22.models.forms;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class SearchForm {
    private String keyword = "";
    private Integer page = 0;
    private Integer size = 10;
}
