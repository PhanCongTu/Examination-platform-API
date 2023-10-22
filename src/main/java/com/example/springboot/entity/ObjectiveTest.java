package com.example.springboot.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "objective_test")
public class ObjectiveTest extends AbstractAuditingEntity {
    private static final long serialVersionUID = 1L;
    private static final String OBJECTIVE_TEST_NAME = "objective_test_name";
    private static final String START_DATE = "start_date";
    private static final String END_DATE = "end_date";
    private static final String TESTING_TIME = "testing_time";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = OBJECTIVE_TEST_NAME)
    private String objectiveTestName;

    @Column(name = START_DATE)
    private LocalDateTime  startDate;

    @Column(name = END_DATE)
    private LocalDateTime endDate;

    @Column(name = TESTING_TIME)
    private Integer testing_time;

//    @ManyToOne(fetch = FetchType.LAZY)
//    private ClassRoom classRoom;
}
