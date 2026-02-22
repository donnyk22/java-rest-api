package com.github.donnyk22.models.entities;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class BaseTimestampCreateUpdate extends BaseTimestampCreate {

    @Column(name = "updated_at", updatable = false)
    private OffsetDateTime updatedAt;

    @Override
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        this.updatedAt = OffsetDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }
}
