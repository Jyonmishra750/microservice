package com.jyon.accounts.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@MappedSuperclass
@Getter
@Setter
@ToString
public class AuditEntity {

    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDate createdAt;

    @Column(name = "created_by", updatable = false, nullable = false, length = 20)
    private String createdBy;

    @Column(name = "updated_at", insertable = false)
    private LocalDate updatedAt;

    @Column(name = "updated_by", insertable = false, length = 20)
    private String updatedBy;
}
