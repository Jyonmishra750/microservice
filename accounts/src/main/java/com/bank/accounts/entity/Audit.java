package com.bank.accounts.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@ToString
public class Audit {

    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", updatable = false, nullable = false, length = 20)
    private String createdBy;

    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt;

    @Column(name = "updated_by", insertable = false, length = 20)
    private String updatedBy;
}
