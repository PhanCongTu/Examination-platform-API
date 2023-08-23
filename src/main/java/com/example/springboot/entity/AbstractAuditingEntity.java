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


    private static final String IS_ENABLE = "is_enable";
    private static final String CREATED_BY = "created_by";
    private static final String CREATED_DATE = "created_date";
    private static final String UPDATE_BY = "update_by";
    private static final String UPDATE_DATE = "update_date";

    @Column(name = IS_ENABLE)
    private Boolean isEnable = Boolean.TRUE;

    @CreatedBy
    @Column(name = CREATED_BY,nullable = false, length = 50, updatable = false)
    @JsonIgnore
    private String createdBy = Constants.ANONYMOUS_USER;

    @CreatedDate
    @Column(name = CREATED_DATE, updatable = false)
    @JsonIgnore
    private Instant createdDate = Instant.now();

    @LastModifiedBy
    @Column(name = UPDATE_BY, length = 50)
    @JsonIgnore
    private String updateBy = Constants.ANONYMOUS_USER;

    @LastModifiedDate
    @Column(name = UPDATE_DATE)
    @JsonIgnore
    private Instant updateDate = Instant.now();
}
