package com.example.springboot.entity;

import com.example.springboot.constant.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.Instant;

@Setter
@Getter
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @CreatedBy
    @Column(name = Constants.CREATED_BY,nullable = false, length = 50, updatable = false)
    @JsonIgnore
    private String createdBy = Constants.ANONYMOUS_USER;

    @CreatedDate
    @Column(name = Constants.CREATED_DATE, updatable = false)
    @JsonIgnore
    private Instant createdDate = Instant.now();

    @LastModifiedBy
    @Column(name = Constants.UPDATE_BY, length = 50)
    @JsonIgnore
    private String updateBy = Constants.ANONYMOUS_USER;

    @LastModifiedDate
    @Column(name = Constants.UPDATE_DATE)
    @JsonIgnore
    private Instant updateDate = Instant.now();
}
