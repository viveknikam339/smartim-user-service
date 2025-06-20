package com.smartim.userservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter @Setter @ToString
public class BaseEntity {

    @Column(updatable = false, nullable = false)
    private LocalDateTime createdOn;

    @Column(updatable = false, nullable = false)
    private String createdBy;

    @Column(insertable  = false)
    private LocalDateTime updatedOn;

    @Column(insertable  = false)
    private String updatedBy;
}
