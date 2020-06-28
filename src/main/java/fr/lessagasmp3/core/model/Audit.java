package fr.lessagasmp3.core.model;

import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@ToString
class Audit<U> {

    @CreatedDate
    @Column(name = "created_at", updatable = false, columnDefinition = "timestamp default now()")
    private Date createdAt;

    @CreatedBy
    @Column(name = "created_by", updatable = false, columnDefinition = "varchar(255) default 'System'")
    private U createdBy;

    @LastModifiedDate
    @Column(name = "updated_at", columnDefinition = "timestamp default now()")
    private Date updatedAt;

    @LastModifiedBy
    @Column(name = "updated_by", columnDefinition = "varchar(255) default 'System'")
    private U updatedBy;

}

