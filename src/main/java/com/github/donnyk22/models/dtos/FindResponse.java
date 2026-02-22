package com.github.donnyk22.models.dtos;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class FindResponse<T> {
    private List<T> records;
    private Integer totalPage;
    private Integer totalItem;
    private Boolean hasNext;
    private Boolean hasPrev;
}