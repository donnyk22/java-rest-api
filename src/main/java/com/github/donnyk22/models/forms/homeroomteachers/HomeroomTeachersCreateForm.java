package com.github.donnyk22.models.forms.homeroomteachers;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class HomeroomTeachersCreateForm {
    @NotNull(message = "Class ID is required")
    private Integer classId;
    @NotNull(message = "Teacher ID is required")
    private Integer teacherId;
}
