package com.example.springboot.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "examination")
public class Examination extends AbstractAuditingEntity {
    private static final long serialVersionUID = 1L;
    private static final String EXAM_NAME = "exam_name";
    private static final String START_DATE = "start_date";
    private static final String END_DATE = "end_date";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = EXAM_NAME)
    private String examName;

    @Column(name = START_DATE)
    private LocalDateTime  startDate;

    @Column(name = END_DATE)
    private LocalDateTime endDate;
}
