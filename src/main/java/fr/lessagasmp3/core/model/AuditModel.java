package fr.lessagasmp3.core.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public
class AuditModel<U> extends IdentityModel {

    @CreatedDate
    @Column(name = "created_at", updatable = false, columnDefinition = "timestamp default now()")
    protected Date createdAt;

    @CreatedBy
    @Column(name = "created_by", updatable = false, columnDefinition = "varchar(255) default 'System'")
    protected U createdBy;

    @LastModifiedDate
    @Column(name = "updated_at", columnDefinition = "timestamp default now()")
    protected Date updatedAt;

    @LastModifiedBy
    @Column(name = "updated_by", columnDefinition = "varchar(255) default 'System'")
    protected U updatedBy;

}

